import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {select, Store} from '@ngrx/store';
import {selectedFeature} from '../../../store/feature/feature.selectors';
import {distinctUntilChanged} from 'rxjs/operators';
import {AppState} from '../../../store';

@Component({
  selector: 'sl-fdv-feature-sidebar',
  templateUrl: './feature-sidebar.component.html',
  styleUrls: ['./feature-sidebar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FeatureSidebarComponent implements OnInit {

  selectedFeature$ = this.store.pipe(
    select(selectedFeature),
    distinctUntilChanged()
  );

  constructor(private store: Store<AppState>) { }

  ngOnInit(): void {
  }

}
