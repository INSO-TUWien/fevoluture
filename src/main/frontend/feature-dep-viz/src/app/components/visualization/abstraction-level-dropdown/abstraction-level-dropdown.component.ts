import {Component} from '@angular/core';
import {AppState} from '../../../store';
import {select, Store} from '@ngrx/store';
import {AbstractionLevel} from '../../../models/models';
import {MenuItem} from 'primeng-lts/api';
import {currentAbstractionLevel} from '../../../store/visualization/visualization.selectors';
import {VisualizationActionTypes} from '../../../store/visualization/visualization-action-types';

@Component({
  selector: 'sl-fdv-abstraction-level-dropdown',
  templateUrl: './abstraction-level-dropdown.component.html',
  styleUrls: ['./abstraction-level-dropdown.component.scss']
})
export class AbstractionLevelDropdownComponent {

  AbstractionLevel = AbstractionLevel;
  currentAbstractionLevel$ = this.store.pipe(select(currentAbstractionLevel));

  items: MenuItem[] = Object.keys(AbstractionLevel)
    .filter(key => !isNaN(Number(AbstractionLevel[key])))
    .map(key => {
      return {
        label: key.charAt(0) + key.substr(1).toLowerCase(),
        command: () => this.onMenuItemClicked(AbstractionLevel[key])
      };
    });

  constructor(private store: Store<AppState>) {
  }


  onMenuItemClicked(value: AbstractionLevel) {
    this.store.dispatch(VisualizationActionTypes.abstractionLevelChanged({abstractionLevel: value}));

  }
}
