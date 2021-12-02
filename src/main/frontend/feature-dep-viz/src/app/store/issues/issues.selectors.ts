import {createFeatureSelector, createSelector} from '@ngrx/store';
import {AppState} from '../index';
import {issueFeatureKey, IssueState} from './issues.reducer';

export const selectIssue = createFeatureSelector<AppState, IssueState>(issueFeatureKey);

export const issueFilter = createSelector(
  selectIssue,
  (state: IssueState) => state.issueListFilter
);

export const isLoadingIssues = createSelector(
  selectIssue,
  (state: IssueState) => state.isLoading
);

export const issueListPage = createSelector(
  selectIssue,
  (state: IssueState) => state.issueListPage
);
