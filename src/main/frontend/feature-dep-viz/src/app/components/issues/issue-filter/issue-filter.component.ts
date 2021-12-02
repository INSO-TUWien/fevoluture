import {Component, EventEmitter, OnDestroy, Output} from '@angular/core';
import {IssueFilterOptions} from './issue-filter-options';
import {castToDropdownValueArray} from '../../../utils/utils';
import {debounceTime, first, map, tap} from 'rxjs/operators';
import {FormBuilder} from '@angular/forms';
import {IssueService} from '../../../services/issue.service';
import {select, Store} from '@ngrx/store';
import {AppState} from '../../../store';
import {issueFilter} from '../../../store/issues/issues.selectors';
import {Observable} from 'rxjs';

@Component({
  selector: 'sl-fdv-issue-filter',
  templateUrl: './issue-filter.component.html',
  styleUrls: ['./issue-filter.component.scss']
})
export class IssueFilterComponent implements OnDestroy {

  @Output() filterChanged = new EventEmitter<IssueFilterOptions>();


  authors$: Observable<{ label: string, value: string }[]>;
  trackers$: Observable<{ label: string, value: string }[]>;
  status$: Observable<{ label: string, value: string }[]>;

  issueFilterForm = this.formBuilder.group({
    issueId: [],
    author: [],
    text: [],
    status: [],
    tracker: [],
    fromDate: [],
    toDate: [],
  });

  private subscription = this.issueFilterForm
    .valueChanges
    .pipe(
      debounceTime(1000),
      map((filter: IssueFilterOptions) => this.parseFormToIssueFilterOptions(filter)),
      tap(value => this.filterChanged.emit(value)))
    .subscribe();

  constructor(
    private formBuilder: FormBuilder,
    private issueService: IssueService,
    private store: Store<AppState>) {
    this.fillForm();
    this.fetchDropdownData();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  fillForm() {
    this.store.pipe(select(issueFilter))
      .pipe(
        tap(filter => this.issueFilterForm.patchValue(filter, {emitEvent: false})),
        first()
      ).subscribe();
  }

  parseFormToIssueFilterOptions(data: Partial<IssueFilterOptions>): IssueFilterOptions {
    if (data?.fromDate) {
      data.fromDate = new Date(data.fromDate).getTime();
    }

    if (data?.toDate) {
      data.toDate = new Date(data.toDate).getTime();
    }

    return data as IssueFilterOptions;
  }

  private fetchDropdownData(): void {
    this.authors$ = castToDropdownValueArray(this.issueService.getAllIssueAuthorsIncluding(''));
    this.trackers$ = castToDropdownValueArray(this.issueService.getAllIssueTrackers());
    this.status$ = castToDropdownValueArray(this.issueService.getAllIssueStatus());
  }
}
