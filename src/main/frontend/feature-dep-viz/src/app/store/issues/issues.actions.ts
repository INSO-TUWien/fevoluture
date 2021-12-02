import {createAction, props} from '@ngrx/store';
import {Pageable} from '../../models/pageable';
import {Issue} from '../../models/models';
import {IssueFilterOptions} from '../../components/issues/issue-filter/issue-filter-options';

export const issuesLoaded = createAction(
  '[Issue Effects] Issues loaded',
  props<{ page: Pageable<Issue> }>());

export const changeIssueFilter = createAction(
  '[Issue Filter] Change Issue Filter',
  props<{ filter: IssueFilterOptions }>());

export const resetIssueFilter = createAction(
  '[Admin] Reset issue filter'
)

