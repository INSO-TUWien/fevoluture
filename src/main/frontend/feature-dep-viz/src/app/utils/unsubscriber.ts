import {Unsubscribable} from 'rxjs';

export abstract class Unsubscriber {

  private subscriptions: Unsubscribable[] = [];

  protected registerSubscriptions(subscriptions: Unsubscribable[]): void {
    this.subscriptions.concat(...subscriptions);
  }

  protected registerSubscription(subscription: Unsubscribable): void {
    this.subscriptions.push(subscription);
  }

  protected unsubscribeAll(): void {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

}
