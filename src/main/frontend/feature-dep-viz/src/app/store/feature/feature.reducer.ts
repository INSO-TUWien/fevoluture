import {createReducer, on} from '@ngrx/store';
import {
  Commit,
  compCommits,
  compFeature,
  compIssues,
  Feature,
  filterCommits,
  filterFeature, filterIssues,
  Issue,
  orderCommits,
  orderIssues
} from '../../models/models';
import * as _ from 'lodash-es';
import {FeatureActions} from './feature-action-types';

export const featureFeatureKey = 'feature';

export interface FeatureState {
  features: Feature[];
  selectedFeature: Feature;
  isLoading: boolean;
  areFeaturesLoaded: boolean;
}

export const initialState: FeatureState = {
  features: [],
  selectedFeature: null,
  isLoading: false,
  areFeaturesLoaded: false,
};


export const featureReducer = createReducer(
  initialState,

  on(FeatureActions.featureAdded, (state, {feature}) => {
    const features = [...state.features, feature];
    const selectedFeature = feature;
    return {
      ...state,
      features,
      selectedFeature,
    };
  }),

  on(FeatureActions.removeFeature, (state, {feature}) => {

    const features = state.features.filter(f => filterFeature(f, feature));

    let selectedFeature = state.selectedFeature;
    if (selectedFeature.id === feature.id) {
      selectedFeature = features[0];
    }
    return _.cloneDeep({
      ...state,
      features,
      selectedFeature
    });
  }),

  on(FeatureActions.updateFeature, (state, {feature}) => {
    const index = getFeatureIndex(state, feature);
    const newState = _.cloneDeep(state);
    newState.features[index] = feature;
    return newState;
  }),

  on(FeatureActions.loadFeaturesSuccess, (state, {features}) => {
    return {
      ...state,
      features,
      selectedFeature: features[0],
      areFeaturesLoaded: true
    };
  }),

  on(FeatureActions.setSelectedFeature, (state, {feature}) => {
    return {
      ...state,
      selectedFeature: feature
    };
  }),

  on(FeatureActions.addCommitsToFeature, (state, {feature, commits}) => {
    const index = getFeatureIndex(state, feature);
    const newFeatureCommits = _.orderBy<Commit>(
      _.uniqWith([...feature.commits, ...commits], compCommits),
      orderCommits,
      'asc');

    const newState = _.cloneDeep(state);
    newState.features[index].commits = newFeatureCommits;

    return {...newState};
  }),

  on(FeatureActions.removeCommitsFromFeature, (state, {feature, commits}) => {
    const index = getFeatureIndex(state, feature);
    const filteredCommits = feature.commits
      .filter(commit => commits.find(c => filterCommits(commit, c)));

    const newState = _.cloneDeep(state);
    newState.features[index].commits = filteredCommits;

    return {...newState};
  }),

  on(FeatureActions.addIssuesToFeature, (state, {feature, issues}) => {
    const index = getFeatureIndex(state, feature);
    const newFeatureIssues = _.orderBy<Issue>(
      _.uniqWith([...feature.issues, ...issues], compIssues),
      orderIssues,
      'asc');

    const newState = _.cloneDeep(state);
    newState.features[index].issues = newFeatureIssues;

    return {...newState};
  }),

  on(FeatureActions.removeIssuesFromFeature, (state, {feature, issues}) => {
    const index = getFeatureIndex(state, feature);
    const filteredIssues = feature.issues
      .filter(issue => issues.find(c => filterIssues(issue, c)));

    const newState = _.cloneDeep(state);
    newState.features[index].issues = filteredIssues;

    return {...newState};
  }),
);

const getFeatureIndex = (state: FeatureState, feature: Feature): number => {
  return state.features.findIndex(f => compFeature(f, feature));
};


