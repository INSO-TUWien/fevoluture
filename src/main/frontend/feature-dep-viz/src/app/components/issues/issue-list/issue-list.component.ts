import {ChangeDetectionStrategy, Component, OnDestroy} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {tap} from 'rxjs/operators';
import {IssueActionTypes} from '../../../store/issues/issue-action-types';
import {IssueFilterOptions} from '../issue-filter/issue-filter-options';
import {Feature, Issue} from '../../../models/models';
import {AppState} from '../../../store';
import {selectedFeature} from 'src/app/store/feature/feature.selectors';
import {isLoadingIssues, issueFilter, issueListPage} from '../../../store/issues/issues.selectors';
import {FeatureActions} from '../../../store/feature/feature-action-types';
import {IssueService} from '../../../services/issue.service';
import {DialogService} from 'primeng-lts/dynamicdialog';
import {IssueDetailComponent} from '../issue-detail/issue-detail.component';

@Component({
  selector: 'sl-fdv-issue-list',
  templateUrl: './issue-list.component.html',
  styleUrls: ['./issue-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IssueListComponent implements OnDestroy {
  isLoading$ = this.store.pipe(select(isLoadingIssues));

  page$ = this.store.pipe(
    select(issueListPage),
    tap(page => {
      if (page === null) {
        this.store.dispatch(IssueActionTypes.changeIssueFilter({filter: this.issueFilterOptions}));
      }
    }));

  private issueFilterOptions: IssueFilterOptions;
  private issueFilterSubscription = this.store.pipe(select(issueFilter))
    .subscribe(issueFilterOptions => this.issueFilterOptions = issueFilterOptions);

  private selectedFeature: Feature;
  private selectedFeatureSubscription = this.store.pipe(select(selectedFeature))
    .subscribe(feature => this.selectedFeature = feature);

  constructor(private store: Store<AppState>,
              public dialogService: DialogService,
              private issueService: IssueService) {
  }

  ngOnDestroy() {
    this.issueFilterSubscription.unsubscribe();
    this.selectedFeatureSubscription.unsubscribe();
  }

  onFilterChanged(filter: IssueFilterOptions) {
    filter.page = 0;
    filter.pageSize = this.issueFilterOptions.pageSize;
    this.updateList(filter);
  }

  paginate(event) {
    const filter = {...this.issueFilterOptions, page: event.page, pageSize: event.rows};
    this.updateList(filter);
  }

  updateList(issueFilterOptions: IssueFilterOptions) {
    this.store.dispatch(IssueActionTypes.changeIssueFilter({filter: issueFilterOptions}));
  }

  show(issue: Issue) {
    this.dialogService.open(IssueDetailComponent, {
      header: issue.key + ' - ' + issue.title,
      width: '90%',
      data: {
        issue
      }
    });
  }

  addRelatedIssuesToFeature(issue: Issue) {
    this.issueService.getRelatedIssues(issue.id, 3)
      .subscribe(issues => {
        issues.forEach(ele => ele.color = issue.color);
        this.store.dispatch(FeatureActions.addIssuesToFeature({issues: [issue, ...issues], feature: this.selectedFeature}));
      });
  }

  addIssueToCurrentFeature(issue: Issue) {
    this.store.dispatch(FeatureActions.addIssuesToFeature({issues: [issue], feature: this.selectedFeature}));
  }
}
