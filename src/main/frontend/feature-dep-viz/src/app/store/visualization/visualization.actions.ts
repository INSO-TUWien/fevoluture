import {createAction, props} from '@ngrx/store';
import {AbstractionLevel, Commit, LogicalCoupling} from '../../models/models';
import {VisualizationFilterOptions} from '../../components/visualization/visualization-filter-options/visualization-filter-options';
import {CommitFilterOptions} from '../../components/commits/commit-filter/commit-filter-options';

export const currentCommitChanged = createAction(
  '[Commit Navigator] Set selected commit',
  props<{ commit: Commit }>());

export const visualizationFilterChanged = createAction(
  '[Visualization Filter Form] Visualization filter changed',
  props<{ visualizationFilter: VisualizationFilterOptions }>());

export const selectNextCommit = createAction(
  '[Commit Navigator] Select next commit'
);

export const selectPreviousCommit = createAction(
  '[Commit Navigator] Select previous commit'
);

export const abstractionLevelChanged = createAction(
  '[Abstraction Level Dropdown] Abstraction Level changed',
  props<{ abstractionLevel: AbstractionLevel }>());

export const loadLogicalCoupling = createAction(
  '[Visualization] Load visualization',
);

export const logicalCouplingLoaded = createAction(
  '[Visualization Effects] Loading visualization successfull',
  props<{ logicalCouplings: LogicalCoupling[] }>());

export const filterVisualizationCommits = createAction(
  '[Commit Navigator] Filter feature commits',
  props<{ filter: CommitFilterOptions }>());

export const defaultSettings = createAction(
  '[Admin] Reset visualization state',
);

