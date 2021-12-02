import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Commit, Issue} from '../models/models';
import {HttpClient, HttpParams} from '@angular/common/http';
import {CommitFilterOptions} from '../components/commits/commit-filter/commit-filter-options';
import {Pageable} from '../models/pageable';
import {createHTTPParams} from '../utils/utils';
import {IssueFilterOptions} from '../components/issues/issue-filter/issue-filter-options';

@Injectable({
  providedIn: 'root'
})
export class IssueService {

  constructor(private http: HttpClient) { }

  getCommitsOfIssue(issueId: string): Observable<Commit[]> {
    return this.http.get<Commit[]>(`/api/issues/${issueId}/commits`);
  }

  loadIssues(options: IssueFilterOptions): Observable<Pageable<Issue>> {
    const httpParams = createHTTPParams(options);
    return this.http.get<Pageable<Issue>>('/api/issues', {params: httpParams});
  }

  getAllIssueAuthorsIncluding(partialAuthorName: string): Observable<string[]> {
    let httpParams = new HttpParams();
    httpParams = httpParams.append('partialAuthorName', partialAuthorName || '');
    return this.http.get<string[]>('/api/issues/authors', {params: httpParams});
  }

  getAllIssueTrackers(): Observable<string[]> {
    return this.http.get<string[]>('/api/issues/trackers');
  }

  getAllIssueStatus(): Observable<string[]> {
    return this.http.get<string[]>('/api/issues/status');
  }

  getRelatedIssues(issueId: string, depth: number): Observable<Issue[]> {
    let httpParams = new HttpParams();
    httpParams = httpParams.append('depth', depth.toString() || '1');
    return this.http.get<Issue[]>(`/api/issues/${issueId}/related`, {params: httpParams});
  }
}
