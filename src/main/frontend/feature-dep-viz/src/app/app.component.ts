import {Component, OnDestroy} from '@angular/core';
import {Store} from '@ngrx/store';
import {AppState} from './store';
import {FeatureActions} from './store/feature/feature-action-types';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'sl-fdv-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy {
  title = 'feature-depedency-visualization-frontend';
  isUser: boolean;
  private subscription: Subscription;

  constructor(private store: Store<AppState>, private route: ActivatedRoute) {
    this.store.dispatch(FeatureActions.loadFeatures());
    this.subscription = this.route.queryParamMap.subscribe(paramMap => {
      this.isUser = !!this.route.snapshot.queryParamMap.get('user');
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
