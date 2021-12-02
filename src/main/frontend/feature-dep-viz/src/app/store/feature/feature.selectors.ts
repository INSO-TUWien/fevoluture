import { createFeatureSelector, createSelector } from '@ngrx/store';
import {featureFeatureKey, FeatureState} from './feature.reducer';
import {AppState} from '../index';
import {Commit, compCommits, Feature, orderCommits} from '../../models/models';
import * as _ from 'lodash-es';

export const selectFeature = createFeatureSelector<AppState, FeatureState>(featureFeatureKey);

export const allFeatures = createSelector(
  selectFeature,
  (state: FeatureState) => state.features
);

export const selectedFeature = createSelector(
  selectFeature,
  (state: FeatureState) => state.selectedFeature
);

export const areFeaturesLoaded = createSelector(
  selectFeature,
  (state: FeatureState) => state.areFeaturesLoaded
);

export const visualizedFeatures = createSelector(
  selectFeature,
  (state: FeatureState) => state.features.filter(f => f.visualized));

export const commitsOfVisualizedFeatures = createSelector(
  visualizedFeatures,
  (features: Feature[]) => {
    const allCommits = features.map(f => f.commits).reduce((prev, curr) => [...prev, ...curr], []);
    const uniqueCommits = _.uniqWith(allCommits, compCommits);
    const uniqueAndSortedCommits = _.orderBy(uniqueCommits, orderCommits);
    return uniqueAndSortedCommits as Commit[];
  }
);
