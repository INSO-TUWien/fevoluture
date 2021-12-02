import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {select, Store} from '@ngrx/store';
import {AppState} from '../index';
import {VisualizationActionTypes} from './visualization-action-types';
import {distinctUntilChanged, filter, map, mergeMap, withLatestFrom} from 'rxjs/operators';
import {
  currentCommit,
  currentFeatureMap,
  filteredCommitsOfVisualizedFeatures,
  selectVisualization
} from './visualization.selectors';
import * as _ from 'lodash-es';
import {compCommits, getAbstractionLevelName} from '../../models/models';
import {LogicalCouplingService} from '../../services/logical-coupling.service';

@Injectable()
export class VisualizationEffects {

  wasVisualizationFilterUpdated = false;

  selectNextCommit$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(VisualizationActionTypes.selectNextCommit),
      withLatestFrom(this.store.pipe(select(currentCommit)), this.store.pipe(select(filteredCommitsOfVisualizedFeatures))),
      map(values => {
        const index = _.findIndex(values[2], c => compCommits(c, values[1]));
        let nextCommit;
        if (index < 0) {
          nextCommit = values[2][0];
        } else {
          nextCommit = values[2][Math.min(index + 1, values[2].length - 1)];
        }

        return VisualizationActionTypes.currentCommitChanged({commit: nextCommit});
      })
    );
  });

  selectPreviousCommit$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(VisualizationActionTypes.selectPreviousCommit),
      withLatestFrom(this.store.pipe(select(currentCommit)), this.store.pipe(select(filteredCommitsOfVisualizedFeatures))),
      map(values => {
        const index = _.findIndex(values[2], c => compCommits(c, values[1]));
        const prevCommit = values[2][Math.max(index - 1, 0)];
        return VisualizationActionTypes.currentCommitChanged({commit: prevCommit});
      })
    );
  });

  selectCurrentCommit$ = createEffect(() => {
    return this.actions$.pipe(ofType(VisualizationActionTypes.currentCommitChanged),
      map(() => VisualizationActionTypes.loadLogicalCoupling()
      ));
  });

  changeAbstractionLevel$ = createEffect(() => {
    return this.actions$.pipe(ofType(VisualizationActionTypes.abstractionLevelChanged),
      map(() => VisualizationActionTypes.loadLogicalCoupling()));
  });

  filterIncludeOrExcludeChanged$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(VisualizationActionTypes.visualizationFilterChanged),
      distinctUntilChanged((old, current) => {
        return old.visualizationFilter.excludeText === current.visualizationFilter.excludeText &&
          old.visualizationFilter.sourceText === current.visualizationFilter.sourceText &&
          old.visualizationFilter.minCount === current.visualizationFilter.minCount &&
          old.visualizationFilter.minLC === current.visualizationFilter.minLC;
      }),
      filter(visFilter => {
        const defaultFilter = {minCount: 2, minLC: 0.66, highlight: '', sourceText: '.java', excludeText: 'Test'};
        if (visFilter.visualizationFilter.minCount === defaultFilter.minCount &&
          visFilter.visualizationFilter.minLC === defaultFilter.minLC &&
          visFilter.visualizationFilter.sourceText === defaultFilter.sourceText &&
          visFilter.visualizationFilter.excludeText === defaultFilter.excludeText &&
          !this.wasVisualizationFilterUpdated) {
          // Hack - to prevent calling loadLogicalCoupling() when the filter changed the first time,
          // where distinctUntilChanged is not called (due there is no old filter).
          // So it would also reload the coupling if just the highlighting changed, which should not happen
          // We check for the default config - and if it changed the first time
          // If it does not match the default config or the filter() was already called, then pass it through
          this.wasVisualizationFilterUpdated = true;
          return false;
        }
        return true;
      }),
      map(() => {
        return VisualizationActionTypes.loadLogicalCoupling();
      }));
  });

  $loadLogicalCoupling = createEffect(() => {
    return this.actions$.pipe(
      ofType(VisualizationActionTypes.loadLogicalCoupling),
      withLatestFrom(
        this.store.select(selectVisualization),
        this.store.select(currentFeatureMap),
        this.store.select(currentCommit)),
      map(values => {
        const featureMap = values[2];
        const value = {
          abstractionLevel: values[1].abstractionLevel,
          visualizationFilterOptions: values[1].visualizationFilterOptions,
          currentCommit: values[3],
          entityIds: []
        };

        value.entityIds = Array.from(featureMap[getAbstractionLevelName(value.abstractionLevel) + 's'].keys());
        return value;
      }),
      mergeMap(value => this.logicalCouplingService.fetchLogicalCoupling(
        value.abstractionLevel,
        value.visualizationFilterOptions,
        value.currentCommit,
        value.entityIds
      ).pipe(
        map(logicalCouplings => VisualizationActionTypes.logicalCouplingLoaded({logicalCouplings}))
      ))
    );
  });

  constructor(private actions$: Actions,
              private logicalCouplingService: LogicalCouplingService,
              private store: Store<AppState>) {
  }


}
