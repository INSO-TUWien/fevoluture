import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {CommitFilterOptions} from '../commit-filter/commit-filter-options';
import {AppState} from '../../../store';
import {select, Store} from '@ngrx/store';
import {commitFilter, commitListPage, isLoadingCommits} from '../../../store/commits/commit.selectors';
import {CommitActionTypes} from '../../../store/commits/commit-action-types';
import {first, tap} from 'rxjs/operators';
import {Commit, Feature} from '../../../models/models';
import {selectedFeature} from '../../../store/feature/feature.selectors';
import {FeatureActions} from '../../../store/feature/feature-action-types';
import {DialogService} from 'primeng/dynamicdialog';
import {CommitDetailComponent} from '../commit-detail/commit-detail.component';

@Component({
  selector: 'sl-fdv-commit-list',
  templateUrl: './commit-list.component.html',
  styleUrls: ['./commit-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CommitListComponent implements OnDestroy {

  isLoading$ = this.store.pipe(select(isLoadingCommits));

  initValue$ = this.store.pipe(select(commitFilter))
    .pipe(first());

  page$ = this.store.pipe(
    select(commitListPage),
    tap(page => {
      if (page === null) {
        this.store.dispatch(CommitActionTypes.changeCommitFilter({filter: this.commitFilterOptions}));
      }
    }));

  private commitFilterOptions: CommitFilterOptions;
  private commitFilterSubscription = this.store.pipe(select(commitFilter))
    .subscribe(commitFilterOptions => this.commitFilterOptions = commitFilterOptions);

  private selectedFeature: Feature;
  private selectedFeatureSubscription = this.store.pipe(select(selectedFeature))
    .subscribe(feature => this.selectedFeature = feature);

  constructor(private store: Store<AppState>, private dialogService: DialogService) {
  }

  ngOnDestroy() {
    this.commitFilterSubscription.unsubscribe();
    this.selectedFeatureSubscription.unsubscribe();
  }

  onFilterChanged(filter: CommitFilterOptions) {
    filter.page = 0;
    filter.pageSize = this.commitFilterOptions.pageSize;
    this.updateList(filter);
  }

  paginate(event) {
    const filter = {...this.commitFilterOptions, page: event.page, pageSize: event.rows};
    this.updateList(filter);
  }

  updateList(commitFilterOptions: CommitFilterOptions) {
    this.store.dispatch(CommitActionTypes.changeCommitFilter({filter: commitFilterOptions}));
  }

  addCommitToCurrentFeature(commit: Commit) {
    this.store.dispatch(FeatureActions.addCommitsToFeature(
      {commits: [commit], feature: this.selectedFeature}
    ));
  }

  show(commit: Commit) {
    this.dialogService.open(CommitDetailComponent, {
      header: commit.hash,
      width: '90%',
      data: {
        commitSelection: {
          commit,
          features: []
        }
      }
    });
  }

  allAllToCurrentFeature() {
    this.page$.pipe(first()).subscribe(currentPage => {
      if (currentPage) {
        this.store.dispatch(FeatureActions.addCommitsToFeature(
          {commits: currentPage.content, feature: this.selectedFeature}
        ));
      }
    });
  }
}
