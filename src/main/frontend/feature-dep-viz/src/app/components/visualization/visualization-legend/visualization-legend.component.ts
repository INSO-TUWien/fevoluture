import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppState} from '../../../store';
import {select, Store} from '@ngrx/store';
import {visualizedFeatures} from '../../../store/feature/feature.selectors';
import {Color} from '../../../models/color';
import {Feature} from '../../../models/models';
import {map, skipWhile, switchMap, tap} from 'rxjs/operators';
import {currentCommit} from '../../../store/visualization/visualization.selectors';
import {Unsubscriber} from '../../../utils/unsubscriber';
import {FeaturesOfPipe} from '../../../pipes/features-of.pipe';

@Component({
  selector: 'sl-fdv-visualization-legend',
  templateUrl: './visualization-legend.component.html',
  styleUrls: ['./visualization-legend.component.scss']
})
export class VisualizationLegendComponent extends Unsubscriber implements OnInit, OnDestroy {

  dummy = [
    {
      name: 'Highlighted',
      color: Color.HIGHLIGHT()
    }
    , {
      name: 'Logical-coupled',
      color: Color.LOGICAL_COUPLING()
    }, {
      name: 'Feature-coupled',
      color: Color.RED()
    }
  ] as Feature[];

  larger = {
    name: 'Size = Importance',
    color: Color.RED()
  } as Feature;

  border = {
    name: 'Changed in current commit',
    color: Color.RED()
  } as Feature;


  features$ = this.store.pipe(
    select(visualizedFeatures),
    map(features => [...this.dummy, ...features])
  );

  constructor(private store: Store<AppState>) {
    super();
    this.registerSubscription(this.store.pipe(
      select(currentCommit),
      skipWhile(commit => !commit),
      switchMap(commit => new FeaturesOfPipe(this.store).transform(commit).pipe(
        tap(features => {
          this.border = {...this.border, color: features[0]?.color} as Feature;
          this.larger = {...this.larger, color: features[0]?.color} as Feature;
        })))
    ).subscribe());
  }

  ngOnDestroy(): void {
    this.unsubscribeAll();
  }

  ngOnInit(): void {
  }

}
