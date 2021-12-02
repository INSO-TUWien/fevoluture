import {createFeatureSelector, createSelector} from '@ngrx/store';
import {commitFeatureKey, CommitState} from './commit.reducer';
import {AppState} from '../index';

export const selectCommit = createFeatureSelector<AppState, CommitState>(commitFeatureKey);

export const commitFilter = createSelector(
  selectCommit,
  (state: CommitState) => state.commitListFilter
);

export const isLoadingCommits = createSelector(
  selectCommit,
  (state: CommitState) => state.isLoading
);

export const commitListPage = createSelector(
  selectCommit,
  (state: CommitState) => state.commitListPage);

export const getDiff = createSelector(
  selectCommit,
  (state: CommitState, hash: string) => state.diffs.get(hash));

export const hasDiff = createSelector(
  selectCommit,
  (state: CommitState, hash: string) => state.diffs.has(hash));

