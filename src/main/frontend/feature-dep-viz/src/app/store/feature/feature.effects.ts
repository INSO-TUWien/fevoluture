import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {FeatureService} from '../../services/feature.service';
import {catchError, concatMap, map, mergeMap, tap} from 'rxjs/operators';
import {of} from 'rxjs';
import {createHTTPParams} from '../../utils/utils';
import {FeatureActions} from './feature-action-types';
import {AppState} from '../index';
import {Store} from '@ngrx/store';


@Injectable()
export class FeatureEffects {

  loadFeatures$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(FeatureActions.loadFeatures),
      mergeMap(() => this.featureService.loadFeatures(createHTTPParams({page: 0, pageSize: 20})).pipe(
        map(features => FeatureActions.loadFeaturesSuccess({features})),
        catchError(() => of(FeatureActions.loadFeaturesError))
      ))
    );
  });

  addNewFeature$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(FeatureActions.addFeature),
      concatMap((action) => this.featureService.saveFeature(action.feature)),
      map(feature => FeatureActions.featureAdded({feature})));
  });

  updateFeature$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(FeatureActions.updateFeature),
      tap(action => this.store.dispatch(FeatureActions.setSelectedFeature({feature: action.feature}))),
      concatMap((action) => this.featureService.saveFeature(action.feature)));
  }, {dispatch: false});

  removeFeature$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(FeatureActions.removeFeature),
      concatMap(action => this.featureService.removeFeature(action.feature))
    );
  }, {dispatch: false});

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private featureService: FeatureService) {
  }
}
