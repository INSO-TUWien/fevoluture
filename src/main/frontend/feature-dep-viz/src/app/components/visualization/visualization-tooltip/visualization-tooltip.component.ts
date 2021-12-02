import {Component, Input, OnChanges, OnDestroy, OnInit, Output} from '@angular/core';
import {AbstractionLevel} from '../../../models/models';
import {AppState} from '../../../store';
import {Store} from '@ngrx/store';
import {currentAbstractionLevel} from '../../../store/visualization/visualization.selectors';
import {tap} from 'rxjs/operators';
import {Unsubscriber} from '../../../utils/unsubscriber';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'sl-fdv-visualization-tooltip',
  templateUrl: './visualization-tooltip.component.html',
  styleUrls: ['./visualization-tooltip.component.scss']
})
export class VisualizationTooltipComponent extends Unsubscriber implements OnInit, OnDestroy, OnChanges {

  @Input() data: any;
  @Input() position: { x: number, y: number };
  @Output('close') closeEventEmitter = new EventEmitter<void>();
  currentAbstractionLevel: AbstractionLevel;
  AbstractionLevel = AbstractionLevel;
  isEdge: boolean;

  constructor(private store: Store<AppState>) {
    super();
  }

  ngOnInit(): void {
    this.registerSubscription(this.store.select(currentAbstractionLevel).pipe(
      tap(abstractionLevel => this.currentAbstractionLevel = abstractionLevel)
    ).subscribe());
  }

  ngOnDestroy(): void {
    this.unsubscribeAll();
  }

  ngOnChanges(): void {
    this.isEdge = this.data.source && this.data.target;
  }

  close() {
    this.data = null;
    this.closeEventEmitter.emit();
  }
}
