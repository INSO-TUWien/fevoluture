import {
  ActionReducerMap,
  MetaReducer
} from '@ngrx/store';
import { environment } from '../../environments/environment';
import {FeatureState, featureReducer} from './feature/feature.reducer';
import {commitReducer, CommitState} from './commits/commit.reducer';
import {issueReducer, IssueState} from './issues/issues.reducer';
import {visualizationReducer, VisualizationState} from './visualization/visualization.reducer';


export interface AppState {
  feature: FeatureState;
  commit: CommitState;
  issue: IssueState;
  visualization: VisualizationState;
}

export const reducers: ActionReducerMap<AppState> = {
  feature: featureReducer,
  commit: commitReducer,
  issue: issueReducer,
  visualization: visualizationReducer
};


export const metaReducers: MetaReducer<AppState>[] = !environment.production ? [] : [];
