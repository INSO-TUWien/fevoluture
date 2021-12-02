import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {AppState} from '../index';
import {CommitService} from '../../services/commit.service';
import {CommitActionTypes} from './commit-action-types';
import {concatMap, map} from 'rxjs/operators';

@Injectable()
export class CommitEffects {

  loadCommits$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(CommitActionTypes.changeCommitFilter),
      concatMap(filter => this.commitService.loadCommits(filter.filter)
        .pipe(
          map(page => CommitActionTypes.commitsLoaded({page}))
        ))
    );
  });

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private commitService: CommitService) {
  }


}
