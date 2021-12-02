import { createAction, props } from '@ngrx/store';
import {CommitFilterOptions} from '../../components/commits/commit-filter/commit-filter-options';
import {Commit} from '../../models/models';
import {Pageable} from '../../models/pageable';

export const commitsLoaded = createAction(
  '[Commit Effects] Commits loaded',
  props<{page: Pageable<Commit>}>());

export const changeCommitFilter = createAction(
  '[Commit Filter] Change Commit Filter',
  props<{filter: CommitFilterOptions}>());

export const addDiff = createAction(
  '[Diff Component] Cache diff',
  props<{commitHash: string, diff: string}>());

export const resetCommitFilter = createAction(
  '[Admin] Reset commits'
);
