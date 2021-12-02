import {Pipe, PipeTransform} from '@angular/core';
import {AppState} from '../store';
import {select, Store} from '@ngrx/store';
import {Commit, Feature} from '../models/models';
import {Observable} from 'rxjs';
import {globalFeatureMap} from '../store/visualization/visualization.selectors';
import {map} from 'rxjs/operators';

@Pipe({
  name: 'featuresOf'
})
export class FeaturesOfPipe implements PipeTransform {

  constructor(private store: Store<AppState>) {
  }

  transform(entity: Partial<Commit>): Observable<Partial<Feature>[]> {
    return this.store.pipe(
      select(globalFeatureMap),
      map(featureMap => {
        return featureMap.commits.get(entity.hash);
      })
    );
  }
}
