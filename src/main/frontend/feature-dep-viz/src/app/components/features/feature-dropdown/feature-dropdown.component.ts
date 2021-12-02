import {Component, OnDestroy} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {allFeatures, selectedFeature} from '../../../store/feature/feature.selectors';
import {distinctUntilChanged, tap} from 'rxjs/operators';
import {AppState} from '../../../store';
import {Feature} from '../../../models/models';
import {FeatureActions} from '../../../store/feature/feature-action-types';

@Component({
  selector: 'sl-fdv-feature-dropdown',
  templateUrl: './feature-dropdown.component.html',
  styleUrls: ['./feature-dropdown.component.scss'],
})
export class FeatureDropdownComponent implements OnDestroy {

  features$ = this.store.pipe(select(allFeatures));
  selectedFeature: Feature;
  selectedFeature$ = this.store.pipe(
    select(selectedFeature),
    distinctUntilChanged(),
    tap(feature => this.selectedFeature = feature));
  subscription = this.selectedFeature$.subscribe();

  constructor(private store: Store<AppState>) {
  }

  onChange(feature: Feature) {
    this.store.dispatch(FeatureActions.setSelectedFeature({feature}));
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
