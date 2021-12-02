import {Injectable, OnDestroy} from '@angular/core';
import {AbstractionLevelEntity, Commit} from '../models/models';
import {AppState} from '../store';
import {select, Store} from '@ngrx/store';
import {
  abstractionLevelEntityToCurrentCommitMap,
  abstractionLevelEntityToGlobalCommitMap,
  currentCommitEntityIds,
  visualizationFilter,
  visualizedCommits
} from '../store/visualization/visualization.selectors';
import {tap} from 'rxjs/operators';
import {Unsubscriber} from '../utils/unsubscriber';
import {VisualizationFilterOptions} from '../components/visualization/visualization-filter-options/visualization-filter-options';

@Injectable({
  providedIn: 'root'
})
export class VisualizationService extends Unsubscriber implements OnDestroy{

  entityToGlobalCommitMap = new Map<string, Partial<Commit>[]>();
  entityToCurrentCommitMap = new Map<string, Partial<Commit>[]>();
  amountVisualizedCommits = 1;
  currentCommit: Commit;
  currentEntityIds: Set<string> = new Set<string>();
  visualizationFilterOptions: VisualizationFilterOptions;

  constructor(private store: Store<AppState>) {
    super();
    this.initSubscriptions();
  }

  ngOnDestroy(): void {
    this.unsubscribeAll();
  }

  getAmountOfVisualizedCommits(): number {
    return this.amountVisualizedCommits;
  }

  isChangedInCurrentCommit(abstractionLevelEntity: AbstractionLevelEntity): boolean {
    return this.currentEntityIds.has(abstractionLevelEntity.id);
  }

  isHighlighted(abstractionLevelEntity: AbstractionLevelEntity): boolean {
    // TODO: Implement
    return false;
  }

  getCommitsOfEntityId(id: string): Partial<Commit>[] {
    return this.entityToGlobalCommitMap.get(id) || [];
  }

  getCommitsOfEntity(abstractionLevelEntity: AbstractionLevelEntity): Partial<Commit>[] {
    return this.getCommitsOfEntityId(abstractionLevelEntity.id);
  }

  getCommitsOfEntityIdUntilCurrentCommit(id: string): Partial<Commit>[] {
    return this.entityToCurrentCommitMap.get(id) || [];
  }

  getCommitsOfEntityUntilCurrentCommit(abstractionLevelEntity: AbstractionLevelEntity): Partial<Commit>[] {
    return this.getCommitsOfEntityIdUntilCurrentCommit(abstractionLevelEntity.id);
  }

  private initSubscriptions() {
    this.registerSubscription(this.store.pipe(
      select(visualizedCommits),
      tap(commits => this.amountVisualizedCommits = commits.length))
      .subscribe());

    this.registerSubscription(this.store.pipe(
      select(abstractionLevelEntityToGlobalCommitMap),
      tap(map => this.entityToGlobalCommitMap = map))
      .subscribe());

    this.registerSubscription(this.store.pipe(
      select(abstractionLevelEntityToCurrentCommitMap),
      tap(map => this.entityToCurrentCommitMap = map))
      .subscribe());

    this.registerSubscription(this.store.pipe(
      select(currentCommitEntityIds),
      tap(set => this.currentEntityIds = set))
      .subscribe());

    this.registerSubscription(this.store.pipe(
      select(visualizationFilter),
      tap(filter => this.visualizationFilterOptions = filter))
      .subscribe());
  }
}
