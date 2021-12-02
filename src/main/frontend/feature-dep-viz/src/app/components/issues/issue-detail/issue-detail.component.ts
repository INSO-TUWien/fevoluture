import {ChangeDetectionStrategy, Component, Input, OnInit} from '@angular/core';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng-lts/dynamicdialog';
import * as copy from 'copy-to-clipboard';
import {Commit, Issue} from '../../../models/models';
import {DialogService} from 'primeng/dynamicdialog';
import {Observable} from 'rxjs';
import {IssueService} from '../../../services/issue.service';
import {AppState} from '../../../store';
import {select, Store} from '@ngrx/store';
import {addIssuesToFeature} from '../../../store/feature/feature.actions';
import {selectedFeature} from '../../../store/feature/feature.selectors';
import {first} from 'rxjs/operators';

@Component({
  selector: 'sl-fdv-issue-detail',
  templateUrl: './issue-detail.component.html',
  styleUrls: ['./issue-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class IssueDetailComponent implements OnInit {

  constructor(
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private dialogService: DialogService,
    private issueService: IssueService,
    private store: Store<AppState>) { }

  @Input() issue: Issue;
  relatedIssues: Issue[] = [];
  loadingRelatedIssues: boolean;
  commits$: Observable<Commit[]>;

  ngOnInit() {
    if (!this.issue) {
      this.issue = this.config.data.issue;
    }
    this.fetchRelatedIssues();
    this.fetchRelatedCommits();
  }

  copy() {
    copy('issues/' + this.issue.id);
  }

  fetchRelatedIssues(): void {
    this.loadingRelatedIssues = true;
    this.issueService.getRelatedIssues(this.issue.id, 3).subscribe(issues => {
      this.relatedIssues = issues;
      this.loadingRelatedIssues = false;
    });
  }

  addIssueToCurrentFeature(issue: Issue) {
    this.store.pipe(select(selectedFeature), first())
      .subscribe(feature => {
        this.store.dispatch(addIssuesToFeature({issues: [issue], feature}));
      });
  }

  show(issue: Issue, replace?: boolean) {

    if (replace && this.ref) {
      this.ref.close();
    }

    setTimeout(() => {
      const ref = this.dialogService.open(IssueDetailComponent, {
        header: issue.key + ' - ' + issue.title,
        width: '90%',
        data: {
          issue
        }
      });
    }, 500);
  }

  addCommitToCurrentFeature(commit: Commit): void {

  }

  private fetchRelatedCommits() {
    this.commits$ = this.issueService.getCommitsOfIssue(this.issue.id);
  }
}
