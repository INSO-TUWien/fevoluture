import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {select, Store} from '@ngrx/store';
import {debounceTime, first, tap} from 'rxjs/operators';
import {AppState} from '../../../store';
import {visualizationFilter} from '../../../store/visualization/visualization.selectors';
import {VisualizationActionTypes} from '../../../store/visualization/visualization-action-types';
import {VisualizationFilterOptions} from './visualization-filter-options';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'sl-fdv-visualization-filter-options',
  templateUrl: './visualization-filter-options.component.html',
  styleUrls: ['./visualization-filter-options.component.scss']
})
export class VisualizationFilterOptionsComponent implements OnInit, OnDestroy {

  visualizationFilterForm = this.formBuilder.group({
    sourceText: [],
    excludeText: [],
    minCount: [],
    minLC: [],
    highlight: []
  });

  private subscription = this.visualizationFilterForm
    .valueChanges
    .pipe(
      debounceTime(500),
      tap(value => this.updateFilter(value)))
    .subscribe();
  isUser: boolean;
  private routeSubscription: Subscription;

  constructor(private formBuilder: FormBuilder, private store: Store<AppState>, private route: ActivatedRoute) {
    this.fillForm();
    this.routeSubscription = this.route.queryParamMap.subscribe(paramMap => {
      this.isUser = !!this.route.snapshot.queryParamMap.get('user');
    });
  }

  ngOnInit(): void {
  }

  fillForm() {
    this.store.pipe(select(visualizationFilter))
      .pipe(
        tap(filter => this.visualizationFilterForm.patchValue(filter, {emitEvent: false})),
        first()
      ).subscribe();
  }

  updateFilter(filter: VisualizationFilterOptions) {
    this.store.dispatch(VisualizationActionTypes.visualizationFilterChanged({visualizationFilter: filter}));
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.routeSubscription.unsubscribe();
  }
}
