import {createAction, props} from '@ngrx/store';
import {Commit, Feature, Issue} from '../../models/models';

export const loadFeatures = createAction(
  '[Feature List] Load Features'
);

export const loadFeaturesSuccess = createAction(
  '[Feature Effects] Load Features Success',
  props<{ features: Feature[] }>()
);

export const loadFeaturesError = createAction(
  '[Feature List] Load Features Error'
);

export const addFeature = createAction(
  '[Feature List] Add Feature',
  props<{ feature: Feature }>());

export const featureAdded = createAction(
  '[Feature Effects] Feature Added',
  props<{ feature: Feature }>());

export const removeFeature = createAction(
  '[Feature List] Remove Feature',
  props<{ feature: Feature }>());

export const updateFeature = createAction(
  '[Feature Form] Update Feature',
  props<{ feature: Feature }>());

export const setSelectedFeature = createAction(
  '[Feature List] Select Feature',
  props<{ feature: Feature }>());

export const addCommitsToFeature = createAction(
  '[Feature Issue List] Add commits to feature',
  props<{ feature: Feature, commits: Commit[] }>());

export const removeCommitsFromFeature = createAction(
  '[Feature Issue List] Remove commits from feature',
  props<{ feature: Feature, commits: Commit[] }>());

export const addIssuesToFeature = createAction(
  '[Issue List] Add issues to feature',
  props<{ feature: Feature, issues: Issue[] }>());

export const removeIssuesFromFeature = createAction(
  '[Feature Issue List] Remove issues from feature',
  props<{ feature: Feature, issues: Issue[] }>());
