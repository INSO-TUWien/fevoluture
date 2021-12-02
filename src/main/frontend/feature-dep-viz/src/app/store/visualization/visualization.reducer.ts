import {VisualizationFilterOptions} from '../../components/visualization/visualization-filter-options/visualization-filter-options';
import {createReducer, on} from '@ngrx/store';
import {AbstractionLevel, Commit, Feature, LogicalCoupling} from '../../models/models';
import {VisualizationActionTypes} from './visualization-action-types';
import * as _ from 'lodash-es';
import {CommitFilterOptions} from '../../components/commits/commit-filter/commit-filter-options';

export type FeatureMap = {
  commits: Map<string, Partial<Feature>[]>;
  packages: Map<string, Partial<Feature>[]>;
  files: Map<string, Partial<Feature>[]>;
  methods: Map<string, Partial<Feature>[]>;
};

export const visualizationFeatureKey = 'visualization';

export interface VisualizationState {
  visualizationFilterOptions: VisualizationFilterOptions;
  currentCommit: Commit;
  abstractionLevel: AbstractionLevel;
  visualizedCommitFilterOptions: CommitFilterOptions;
  isVisualizationLoading: boolean;
  logicalCoupling: LogicalCoupling[];
}

export const initialState: VisualizationState = {
  visualizationFilterOptions: {minCount: 2, minLC: 0.66, highlight: '', sourceText: '.java', excludeText: 'Test'},
  visualizedCommitFilterOptions: {page: 0, pageSize: 0},
  currentCommit: null,
  abstractionLevel: AbstractionLevel.FILE,
  isVisualizationLoading: false,
  logicalCoupling: []
};

export const visualizationReducer = createReducer(
  initialState,

  on(VisualizationActionTypes.abstractionLevelChanged, (state, {abstractionLevel}) => {
    const newState = _.cloneDeep(state);
    newState.abstractionLevel = abstractionLevel;
    return newState;
  }),

  on(VisualizationActionTypes.visualizationFilterChanged, (state, {visualizationFilter}) => {
    const newState = _.cloneDeep(state);
    newState.visualizationFilterOptions = visualizationFilter;
    return newState;
  }),

  on(VisualizationActionTypes.currentCommitChanged, (state, {commit}) => {
    const newState = _.cloneDeep(state);
    newState.currentCommit = commit;
    return newState;
  }),

  on(VisualizationActionTypes.loadLogicalCoupling, (state) => {
    const newState = _.cloneDeep(state);
    newState.isVisualizationLoading = true;
    return newState;
  }),

  on(VisualizationActionTypes.logicalCouplingLoaded, (state, {logicalCouplings}) => {
    const newState = _.cloneDeep(state);
    newState.isVisualizationLoading = false;
    newState.logicalCoupling = logicalCouplings;
    return newState;
  }),

  on(VisualizationActionTypes.filterVisualizationCommits, (state, {filter}) => {
    const newState = _.cloneDeep(state);
    newState.visualizedCommitFilterOptions = filter;
    return newState;
  }),

  on(VisualizationActionTypes.defaultSettings, () => {
    return {
      visualizationFilterOptions: {minCount: 2, minLC: 0.66, highlight: '', sourceText: '.java', excludeText: 'Test'},
      visualizedCommitFilterOptions: {page: 0, pageSize: 0},
      currentCommit: null,
      abstractionLevel: AbstractionLevel.FILE,
      isVisualizationLoading: false,
      logicalCoupling: []
    };
  })
);
