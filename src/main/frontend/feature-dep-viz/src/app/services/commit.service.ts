import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {shareReplay, switchMap, tap} from 'rxjs/operators';
import {CommitFilterOptions} from '../components/commits/commit-filter/commit-filter-options';
import {createHTTPParams} from '../utils/utils';
import {Pageable} from '../models/pageable';
import {Commit, getAbstractionLevelName} from '../models/models';
import {AppState} from '../store';
import {select, Store} from '@ngrx/store';
import {getDiff, hasDiff} from '../store/commits/commit.selectors';
import {addDiff} from '../store/commits/commit.actions';
import {currentAbstractionLevel} from '../store/visualization/visualization.selectors';

@Injectable({
  providedIn: 'root'
})
export class CommitService {

  commitAuthors$: Observable<string[]>;

  constructor(private http: HttpClient, private store: Store<AppState>) { }

  getAllCommitAuthors(): Observable<string[]> {
    if (this.commitAuthors$) {
      return this.commitAuthors$;
    } else {
      this.commitAuthors$ = this.http.get<string[]>( '/api/commits/authors')
        .pipe(shareReplay({ bufferSize: 1, refCount: true }));
      return this.commitAuthors$;
    }
  }

  loadCommits(options: CommitFilterOptions): Observable<Pageable<Commit>> {
    const httpParams = createHTTPParams(options);
    return this.http.get<Pageable<Commit>>('/api/commits', {params: httpParams});
  }

  getDiffOfCommit(commit: Commit): Observable<string> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'text/html')
      .set('Response-Type', 'text/html');
    const url =  '/api/commits/' + commit.hash + '/diff';

    // @ts-ignore
    return this.store.pipe(select(hasDiff, commit.hash)).pipe(
      switchMap(isCached => {
      if (isCached) {
        return this.store.pipe(select(getDiff, commit.hash));
      } else {
        // @ts-ignore
        return this.http.get<string>(url, {headers, responseType: 'text'})
          .pipe(
            tap(diff => this.store.dispatch(addDiff({diff: diff.toString(), commitHash: commit.hash}))),
            shareReplay());
      }
    }));
  }

  getCommitsForRelation(id1: string, id2: string): Observable<Commit[]> {
    return this.store.select(currentAbstractionLevel)
      .pipe(
        switchMap(level => {
          const abstractionLevel = getAbstractionLevelName(level);
          const httpParams = new HttpParams()
            .append(abstractionLevel + 'Id1', id1)
            .append(abstractionLevel + 'Id2', id2);
          return this.http.get<Commit[]>('/api/relations/' + abstractionLevel + 's',
            {params: httpParams}).pipe(
              shareReplay());
        })
      );
  }
}
