import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Pageable} from '../models/pageable';
import {Commit, Feature, File, Issue} from '../models/models';
import {debounceTime, map} from 'rxjs/operators';
import {Color} from '../models/color';
import {Observable} from 'rxjs';
import * as _ from 'lodash-es';

@Injectable({
  providedIn: 'root'
})
export class FeatureService {

  constructor(private http: HttpClient) {
  }

  loadFeatures(httpParams?: HttpParams): Observable<Feature[]> {
    return this.http.get<Pageable<Feature>>('api/features', {params: httpParams})
      .pipe(debounceTime(400),
        map(pageable => pageable.content),
        map(features => features.map(feature => {
          feature = this.trimResponse(feature);
          feature.color = Color.toColor(feature.color);
          return feature;
        })),
      );
  }

  saveFeature(feature: Feature): Observable<Feature> {
    const clone = _.clone(feature);
    clone.issues = clone.issues.map(i => {
      return {
        id: i.id
      } as Issue;
    });
    clone.commits = clone.commits.map(c => {
      return {
        hash: c.hash
      } as Commit;
    });

    return this.http.post<Feature>('/api/features', clone).pipe(map(storedFeature => {
      storedFeature.color = Color.toColor(storedFeature.color);
      return storedFeature;
    }));
  }

  removeFeature(feature): Observable<void> {
    return this.http.delete<void>('api/features/' + feature.id);
  }


  /**
   * Removes unnecessary data from the response, because the data is returned duplicated.
   * @param feature
   * @private
   */
  private trimResponse(feature: Feature): Feature {

    feature.commits.forEach(c => {

      delete (c as any).entity;
      delete (c as any).refId;
      c.changedPackages = c.changedPackages.map(p => {
        return {id: p.id, name: p.name};
      });

      c.changedFiles = c.changedFiles.map(f => {
        const file = {
          id: f.id,
          name: f.name,
          path: f.path,
        } as File;

        if (f.aPackage) {
          file.aPackage = {name: f.aPackage.name};
        }

        return file;
      });

      c.changedMethods = c.changedMethods.map(m => {
        return {
          id: m.id,
          name: m.name,
          file: {name: m.file.name}
        };
      });

    });

    return feature;
  }
}
