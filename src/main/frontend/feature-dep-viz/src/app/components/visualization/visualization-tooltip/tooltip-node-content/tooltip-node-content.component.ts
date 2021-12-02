import {Component, Input, OnChanges, OnDestroy, OnInit} from '@angular/core';
import {AbstractionLevel, Commit} from '../../../../models/models';
import {AppState} from '../../../../store';
import {Store} from '@ngrx/store';
import {Unsubscriber} from '../../../../utils/unsubscriber';
import {abstractionLevelEntityToGlobalCommitMap, currentCommit} from '../../../../store/visualization/visualization.selectors';
import {map, shareReplay} from 'rxjs/operators';
import {BehaviorSubject, combineLatest, Observable} from 'rxjs';

@Component({
  selector: 'sl-fdv-tooltip-node-content',
  templateUrl: './tooltip-node-content.component.html',
  styleUrls: ['./tooltip-node-content.component.scss']
})
export class TooltipNodeContentComponent extends Unsubscriber implements OnInit, OnDestroy, OnChanges {

  private currentElementIdSubject = new BehaviorSubject<string>('');
  @Input() data;
  @Input() currentAbstractionLevel;
  AbstractionLevel = AbstractionLevel;
  commits$: Observable<Partial<Commit>[]>;
  currentCommit$: Observable<Commit>;

  commitsBeforeCurrent$: Observable<Partial<Commit>[]>;
  commitsAfterCurrent$: Observable<Partial<Commit>[]>;
  hideFeatureIconsForCurrentCommit$: Observable<boolean>;

  constructor(private store: Store<AppState>) {
    super();
    this.init();
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.unsubscribeAll();
  }

  ngOnChanges(): void {
    this.currentElementIdSubject.next(this.data.id);
  }

  init() {
    this.currentCommit$ = this.store.select(currentCommit)
      .pipe(shareReplay());
    const currentElementId$ = this.currentElementIdSubject.asObservable().pipe();
    const entitiesToCommitsMap$ = this.store.select(abstractionLevelEntityToGlobalCommitMap);

    this.commits$ = combineLatest([currentElementId$, entitiesToCommitsMap$])
      .pipe(map(values => {
        const entitiesToCommitMap = values[1];
        const elementId = values[0];
        return entitiesToCommitMap.get(elementId);
      }), shareReplay());

    this.commitsBeforeCurrent$ = combineLatest([this.commits$, this.currentCommit$])
      .pipe(
        map(values => {
          const allCommits = values[0] || [];
          const current = values[1];
          return allCommits.filter(c => c.commitTime.millis < current.commitTime.millis);
        }));

    this.commitsAfterCurrent$ = combineLatest([this.commits$, this.currentCommit$])
      .pipe(
        map(values => {
          const allCommits = values[0] || [];
          const current = values[1];
          return allCommits.filter(c => c.commitTime.millis > current.commitTime.millis);
        }));

    this.hideFeatureIconsForCurrentCommit$ = combineLatest([this.commits$, this.currentCommit$]).pipe(
      map(values => {
        const commitsHashes = values[0].map(c => c.hash);
        const currentHash = values[1].hash;
        return !commitsHashes.includes(currentHash);
      })
    );
  }
}
