import {createReducer, on} from '@ngrx/store';
import {Commit} from '../../models/models';
import {CommitFilterOptions} from '../../components/commits/commit-filter/commit-filter-options';
import {CommitActionTypes} from './commit-action-types';
import * as _ from 'lodash-es';
import {Pageable} from '../../models/pageable';


export const commitFeatureKey = 'commit';

export interface CommitState {
  commitListFilter: CommitFilterOptions;
  commitListPage: Pageable<Commit>;
  isLoading: boolean;
  diffs: Map<string, string>;
}

export const initialState: CommitState = {
  commitListFilter: {page: 0, pageSize: 12},
  commitListPage: null,
  isLoading: false,
  diffs: new Map<string, string>()
};

export const commitReducer = createReducer(
  initialState,

  on(CommitActionTypes.changeCommitFilter, (state, {filter}) => {
    const newState = _.cloneDeep(state);
    newState.isLoading = true;
    newState.commitListFilter = filter;
    return newState;
  }),

  on(CommitActionTypes.commitsLoaded, (state, {page}) => {
    const newState = _.cloneDeep(state);
    newState.commitListPage = page;
    newState.isLoading = false;
    newState.commitListFilter.page = page.number;
    newState.commitListFilter.pageSize = page.size;
    return newState;
  }),

  on(CommitActionTypes.addDiff, (state, {commitHash, diff}) => {
    const newState = _.cloneDeep(state);
    if (!newState.diffs.has(commitHash)) {
      newState.diffs.set(commitHash, diff);
    }

    return newState;
  }),

  on(CommitActionTypes.resetCommitFilter, (state) => {
    const newState = _.cloneDeep(state);
    return {...newState,   commitListFilter: {page: 0, pageSize: 12}, commitListPage: null};
  })
);

