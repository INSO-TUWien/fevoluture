import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {defaultSettings, visualizationFilterChanged} from '../../store/visualization/visualization.actions';
import {resetCommitFilter} from '../../store/commits/commit.actions';
import {resetIssueFilter} from '../../store/issues/issues.actions';
import {Feature} from '../../models/models';
import {Unsubscriber} from '../../utils/unsubscriber';
import {allFeatures} from '../../store/feature/feature.selectors';
import {tap} from 'rxjs/operators';
import {updateFeature} from '../../store/feature/feature.actions';

@Component({
  selector: 'sl-fdv-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent extends Unsubscriber implements OnInit {

  features: Feature[];


  constructor(private store: Store) {
    super();
    this.init();
  }

  ngOnInit(): void {
  }

  setUp1() {
    this.resetAllFilters();
    this.deactivateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.activateFeature('AMQP');
    this.activateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.activateFeature('Pending Message Size Metrics');
    this.activateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
  }

  setUp2() {
    this.resetAllFilters();
    this.deactivateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.deactivateFeature('AMQP');
    this.activateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.deactivateFeature('Pending Message Size Metrics');
    this.activateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
  }

  setUp3() {
    this.resetAllFilters();
    this.activateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.activateFeature('AMQP');
    this.deactivateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.deactivateFeature('Pending Message Size Metrics');
    this.deactivateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
  }

  setUp4() {
    this.resetAllFilters();
    this.deactivateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.deactivateFeature('AMQP');
    this.activateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.deactivateFeature('Pending Message Size Metrics');
    this.activateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
  }

  setUp5() {
    this.resetAllFilters();
    this.deactivateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.deactivateFeature('AMQP');
    this.activateFeature('Dynamic network');
    this.activateFeature('JMX Query API');
    this.deactivateFeature('Pending Message Size Metrics');
    this.deactivateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
    this.store.dispatch(visualizationFilterChanged({
      visualizationFilter: {minCount: 2, minLC: 0.66, highlight: '', sourceText: '', excludeText: 'Test;.xml'}
    }));
  }

  setUp6() {
    this.resetAllFilters();
    this.deactivateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.deactivateFeature('AMQP');
    this.activateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.deactivateFeature('Pending Message Size Metrics');
    this.activateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
  }

  setUp7() {
    this.resetAllFilters();
    this.deactivateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.activateFeature('AMQP');
    this.activateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.activateFeature('Pending Message Size Metrics');
    this.activateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
  }

  setUp8() {
    this.resetAllFilters();
    this.deactivateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.activateFeature('AMQP');
    this.activateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.activateFeature('Pending Message Size Metrics');
    this.activateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
  }

  setUp9() {
    this.resetAllFilters();
    this.deactivateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.deactivateFeature('AMQP');
    this.activateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.deactivateFeature('Pending Message Size Metrics');
    this.deactivateFeature('Runtime Configuration');
    this.activateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
  }

  setUp10() {
    this.resetAllFilters();
    this.activateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.activateFeature('AMQP');
    this.activateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.activateFeature('Pending Message Size Metrics');
    this.activateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.deactivateFeature('Demo 1');
    this.deactivateFeature('Demo 2');
    this.store.dispatch(visualizationFilterChanged({
      visualizationFilter: {minCount: 5, minLC: 1, highlight: '', sourceText: '', excludeText: 'Test;.xml'}
    }));
  }

  setUpDemo() {
    this.resetAllFilters();
    this.deactivateFeature('Single port for all wire protocols');
    this.deactivateFeature('AMQ-5757');
    this.deactivateFeature('Debugger');
    this.deactivateFeature('AMQP');
    this.deactivateFeature('Dynamic network');
    this.deactivateFeature('JMX Query API');
    this.deactivateFeature('Pending Message Size Metrics');
    this.deactivateFeature('Runtime Configuration');
    this.deactivateFeature('AMQ-8097');
    this.activateFeature('Demo 1');
    this.activateFeature('Demo 2');
  }

  resetAllFilters() {
    this.setDefaultVisualizationFilter();
    this.setDefaultCommitFilter();
    this.setDefaultIssueFilter();
  }

  setDefaultVisualizationFilter() {
    this.store.dispatch(defaultSettings());
  }

  setDefaultCommitFilter() {
    this.store.dispatch(resetCommitFilter());
  }

  setDefaultIssueFilter() {
    this.store.dispatch(resetIssueFilter());
  }

  init() {
    this.registerSubscription(this.store.select(allFeatures).pipe(
      tap(feature => {
        this.features = feature;
      })).subscribe());
  }

  getFeatureByName(featureName: string): Feature {
    const feature = this.features.find(f => f.name === featureName);
    if (feature) {
      return {...feature};
    }

    return null;
  }

  activateFeature(featureName: string) {
    const feature = this.getFeatureByName(featureName);

    if (feature === null) {
      return;
    }

    feature.visualized = true;
    this.store.dispatch(updateFeature({feature}));
  }

  deactivateFeature(featureName: string) {
    const feature = this.getFeatureByName(featureName);

    if (feature === null) {
      return;
    }

    feature.visualized = false;
    this.store.dispatch(updateFeature({feature}));
  }

}
