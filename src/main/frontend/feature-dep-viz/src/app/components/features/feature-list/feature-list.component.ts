import {Component} from '@angular/core';
import {Feature} from '../../../models/models';
import {select, Store} from '@ngrx/store';
import {distinctUntilChanged, filter, finalize, first, tap} from 'rxjs/operators';
import {addFeature, loadFeatures, removeFeature, setSelectedFeature} from '../../../store/feature/feature.actions';
import {allFeatures, areFeaturesLoaded, selectedFeature} from '../../../store/feature/feature.selectors';
import {Color} from '../../../models/color';
import * as _ from 'lodash-es';

@Component({
  selector: 'sl-fdv-feature-list',
  templateUrl: './feature-list.component.html',
  styleUrls: ['./feature-list.component.scss']
})
export class FeatureListComponent {

  isLoading: boolean;

  features$ = this.store.pipe(select(allFeatures));
  selectedFeature$ = this.store.pipe(
    select(selectedFeature),
    distinctUntilChanged()
  );

  areFeaturesLoaded$ = this.store.pipe(
    select(areFeaturesLoaded),
    tap(areLoaded => {
      if (!areLoaded) {
        this.isLoading = true;
        this.loadFeatures();
      }
    }),
    filter(areLoaded => areLoaded),
    first(),
    finalize(() => {
      this.isLoading = false;
    })
  );

  constructor(private store: Store) {
    this.areFeaturesLoaded$.subscribe();
  }

  setSelectedFeature(feature: Feature) {
    this.store.dispatch(setSelectedFeature({feature}));
  }

  addNewFeature() {
    const newFeature = {
      id: null,
      name: 'Neues Feature',
      issues: [],
      commits: [],
      packages: [],
      files: [],
      methods: [],
      color: Color.BLACK(),
      visualized: true
    };
    this.store.dispatch(addFeature({feature: _.cloneDeep(newFeature)}));
  }

  removeFeature(feature: Feature) {
    this.store.dispatch(removeFeature({feature}));
  }

  private loadFeatures() {
    this.store.dispatch(loadFeatures());
  }


}
