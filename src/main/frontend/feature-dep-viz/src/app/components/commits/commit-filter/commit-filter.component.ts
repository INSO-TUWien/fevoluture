import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {castToDropdownValueArray} from '../../../utils/utils';
import {CommitService} from '../../../services/commit.service';
import {CommitFilterOptions} from './commit-filter-options';
import {debounceTime, map, tap} from 'rxjs/operators';
import {select, Store} from '@ngrx/store';
import {allFeatures} from '../../../store/feature/feature.selectors';

@Component({
  selector: 'sl-fdv-commit-filter',
  templateUrl: './commit-filter.component.html',
  styleUrls: ['./commit-filter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CommitFilterComponent implements OnDestroy, OnInit {

  @Input() initValue: CommitFilterOptions;
  @Output() filterChanged = new EventEmitter<CommitFilterOptions>();

  commitFilterForm = this.formBuilder.group({
    author: [],
    fromDate: [],
    toDate: [],
    msg: [],
    hash: [],
    fileName: [],
    feature: []
  });

  authors$ = castToDropdownValueArray(this.commitService.getAllCommitAuthors());
  features$ = castToDropdownValueArray(this.store.pipe(select(allFeatures)).pipe(map(f => f.map(f0 => f0.name))));

  private subscription = this.commitFilterForm
    .valueChanges
    .pipe(
      debounceTime(1000),
      map((filter: CommitFilterOptions) => this.parseFormToCommitFilterOptions(filter)),
      tap(value => this.filterChanged.emit(value)))
    .subscribe();

  constructor(
    private formBuilder: FormBuilder,
    private commitService: CommitService,
    private store: Store) {
  }

  ngOnInit(): void {
    if (this.initValue) {
      this.fillForm(this.initValue);
    }
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  fillForm(values: CommitFilterOptions) {
    this.commitFilterForm.patchValue(values, {emitEvent: false});
  }

  parseFormToCommitFilterOptions(data: Partial<CommitFilterOptions>): CommitFilterOptions {
    if (data?.fromDate) {
      data.fromDate = new Date(data.fromDate).getTime();
    }

    if (data?.toDate) {
      data.toDate = new Date(data.toDate).getTime();
    }

    return data as CommitFilterOptions;
  }
}
