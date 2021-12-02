import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppState} from '../../../store';
import {select, Store} from '@ngrx/store';
import {
  currentCommit,
  filteredCommitsOfVisualizedFeatures,
  visualizedCommitFilterOptions
} from '../../../store/visualization/visualization.selectors';
import {VisualizationActionTypes} from '../../../store/visualization/visualization-action-types';
import {CommitFilterOptions} from '../commit-filter/commit-filter-options';
import {delay, distinctUntilChanged, first, map, tap} from 'rxjs/operators';
import * as $ from 'jquery';

@Component({
  selector: 'sl-fdv-commit-navigator',
  templateUrl: './commit-navigator.component.html',
  styleUrls: ['./commit-navigator.component.scss']
})
export class CommitNavigatorComponent implements OnInit, OnDestroy {
  visualizedCommitFilter$ = this.store.pipe(
    select(visualizedCommitFilterOptions),
    first());

  commits$ = this.store.pipe(
    select(filteredCommitsOfVisualizedFeatures)
  );

  currentCommit$ = this.store.pipe(
    select(currentCommit));

  subscription = this.currentCommit$.pipe(
    map(commit => commit?.hash),
    distinctUntilChanged(),
    delay(100),
    tap(() => {
      const $scrollingContainer = $('.navigation-commits');
      const link = $('.nav-commit-ele.active').first();
      const position = link.position();
      if (position) {
        $scrollingContainer.animate({
          scrollTop: position.top
        }, 500);
      }
    })).subscribe();

  constructor(private store: Store<AppState>) {
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
  }

  setActiveCommit(commit: any) {
    this.store.dispatch(VisualizationActionTypes.currentCommitChanged({commit}));

  }

  setPreviousCommitActive() {
    this.store.dispatch(VisualizationActionTypes.selectPreviousCommit());
  }

  setNextCommitActive() {
    this.store.dispatch(VisualizationActionTypes.selectNextCommit());
  }

  onFilterChanged(commitFilterOptions: CommitFilterOptions) {
    this.store.dispatch(VisualizationActionTypes.filterVisualizationCommits({filter: commitFilterOptions}));
  }
}
