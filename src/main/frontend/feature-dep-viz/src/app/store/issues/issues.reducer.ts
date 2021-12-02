import {createReducer, on} from '@ngrx/store';
import {IssueFilterOptions} from '../../components/issues/issue-filter/issue-filter-options';
import {Pageable} from '../../models/pageable';
import {Issue} from '../../models/models';
import * as _ from 'lodash-es';
import {IssueActionTypes} from './issue-action-types';

export const issueFeatureKey = 'issue';

export interface IssueState {
  issueListFilter: IssueFilterOptions;
  issueListPage: Pageable<Issue>;
  isLoading: boolean;
}

export const initialState: IssueState = {
  issueListFilter: {page: 0, pageSize: 12},
  issueListPage: null,
  isLoading: false
};

export const issueReducer = createReducer(
  initialState,

  on(IssueActionTypes.changeIssueFilter, (state, {filter}) => {
    const newState = _.cloneDeep(state);
    newState.isLoading = true;
    newState.issueListFilter = filter;
    return newState;
  }),

  on(IssueActionTypes.issuesLoaded, (state, {page}) => {
    const newState = _.cloneDeep(state);
    newState.issueListPage = page;
    newState.isLoading = false;
    newState.issueListFilter.page = page.number;
    newState.issueListFilter.pageSize = page.size;
    return newState;
  }),

  on(IssueActionTypes.resetIssueFilter, (state) => {
    const newState = _.cloneDeep(state);
    return {...newState, issueListFilter: {page: 0, pageSize: 12}, issueListPage: null};
  })
);
