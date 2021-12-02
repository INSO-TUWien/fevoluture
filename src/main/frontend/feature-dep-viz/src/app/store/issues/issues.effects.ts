import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';

import {concatMap, map} from 'rxjs/operators';
import {EMPTY} from 'rxjs';

import * as IssuesActions from './issues.actions';
import {IssueActionTypes} from '../issues/issue-action-types';
import {Store} from '@ngrx/store';
import {AppState} from '../index';
import {IssueService} from '../../services/issue.service';


@Injectable()
export class IssuesEffects {

  loadIssues$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(IssueActionTypes.changeIssueFilter),
      concatMap(filter => this.issueService.loadIssues(filter.filter)
        .pipe(
          map(page => IssueActionTypes.issuesLoaded({page}))
        ))
    );
  });

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private issueService: IssueService) {
  }


}
