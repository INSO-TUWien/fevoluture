import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {AbstractionLevel, Commit, getAbstractionLevelName, LogicalCoupling} from '../models/models';
import {VisualizationFilterOptions} from '../components/visualization/visualization-filter-options/visualization-filter-options';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LogicalCouplingService {

  constructor(private httpClient: HttpClient) {
  }

  fetchLogicalCoupling(
    abstractionLevel: AbstractionLevel,
    filter: VisualizationFilterOptions,
    commit: Commit,
    ids: string[]): Observable<LogicalCoupling[]> {

    const entityKey = getAbstractionLevelName(abstractionLevel) + 's';
    const url = `api/lcs/${entityKey}`;
    const params = new HttpParams()
      .append('dateTime', commit.commitTime.millis);
    const payLoad = {
      minLCFilter: filter.minLC,
      minCountFilter: filter.minCount,
      sourceEntityIds: ids
    };

    return this.httpClient.post<LogicalCoupling[]>(url, payLoad, {params});
  }

  fetchCommitsForRelation(abstractionLevel: 'file' | 'method' | 'package', id1: string, id2: string): Observable<Commit[]> {
    const httpParams = new HttpParams()
      .append(abstractionLevel + 'Id1', id1)
      .append(abstractionLevel + 'Id2', id2);
    return this.httpClient.get<Commit[]>('api/relations/' + abstractionLevel + 's',
      {params: httpParams});
  }
}
