import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {AbstractionLevel, Commit} from 'src/app/models/models';
import {Store} from '@ngrx/store';
import {AppState} from '../../../../store';
import {DialogService} from 'primeng/dynamicdialog';
import {combineLatest, Observable} from 'rxjs';
import {CommitDetailComponent} from '../../../commits/commit-detail/commit-detail.component';
import {map, shareReplay} from 'rxjs/operators';
import {CommitService} from '../../../../services/commit.service';
import {currentCommit} from '../../../../store/visualization/visualization.selectors';
import {FeatureService} from '../../../../services/feature.service';
import {VisualizationActionTypes} from '../../../../store/visualization/visualization-action-types';

@Component({
  selector: 'sl-fdv-tooltip-edge-content',
  templateUrl: './tooltip-edge-content.component.html',
  styleUrls: ['./tooltip-edge-content.component.scss']
})
export class TooltipEdgeContentComponent implements OnInit, OnChanges {

  @Input() data;
  @Input() currentAbstractionLevel;
  AbstractionLevel = AbstractionLevel;
  reverseConfidence: number;
  countBoth: number;
  commits$: Observable<Commit[]>;

  currentCommit$: Observable<Commit>;
  commitsBeforeCurrent$: Observable<Partial<Commit>[]>;
  commitsAfterCurrent$: Observable<Partial<Commit>[]>;
  hideFeatureIconsForCurrentCommit$: Observable<boolean>;

  constructor(
    private store: Store<AppState>,
    private featureService: FeatureService,
    private dialogService: DialogService,
    private commitService: CommitService) {
  }

  ngOnChanges(): void {
    this.init();
    this.calculateLCValues();
  }

  private calculateLCValues(): void {
    this.countBoth = this.data.countRoot + this.data.countEntity - this.data.countChangedTogether;
    this.reverseConfidence = this.data.countChangedTogether / this.data.countEntity;
  }

  goToCommit(commit: Commit): void {
    this.store.dispatch(VisualizationActionTypes.currentCommitChanged({commit}));
  }

  show(commit: Commit) {
    this.dialogService.open(CommitDetailComponent, {
      header: commit.hash,
      width: '90%',
      data: {
        commitSelection: {
          commit,
          features: []
        },
        commit,
        features: []
      }
    });
  }

  ngOnInit(): void {
    this.init();
  }

  private init(): void {
    this.currentCommit$ = this.store.select(currentCommit);
    this.commits$ = this.commitService.getCommitsForRelation(
      this.data.source.id,
      this.data.target.id).pipe(
      map(commits => {
          // commits.forEach(commit => commit.features = this.featureService.getFeaturesContainingCommit(commit));
          return commits;
        }
      ), shareReplay());

    this.commitsBeforeCurrent$ = combineLatest([this.commits$, this.currentCommit$])
      .pipe(
        map(values => {
          const allCommits = values[0] || [];
          const current = values[1];
          return allCommits.filter(c => c.commitTime.millis < current.commitTime.millis);
        }));

    this.commitsAfterCurrent$ = combineLatest([this.commits$, this.currentCommit$])
      .pipe(
        map(values => {
          const allCommits = values[0] || [];
          const current = values[1];
          return allCommits.filter(c => c.commitTime.millis > current.commitTime.millis);
        }));

    this.hideFeatureIconsForCurrentCommit$ = combineLatest([this.commits$, this.currentCommit$]).pipe(
      map(values => {
        const commitsHashes = values[0].map(c => c.hash);
        const currentHash = values[1].hash;
        return !commitsHashes.includes(currentHash);
      })
    );
  }
}
