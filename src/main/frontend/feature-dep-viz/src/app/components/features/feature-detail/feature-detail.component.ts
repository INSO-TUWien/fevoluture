import {Component, Input, OnInit} from '@angular/core';
import {Commit, Feature, Issue} from '../../../models/models';
import {IssueService} from '../../../services/issue.service';
import {AppState} from '../../../store';
import {Store} from '@ngrx/store';
import {FeatureActions} from '../../../store/feature/feature-action-types';
import {CommitDetailComponent} from '../../commits/commit-detail/commit-detail.component';
import {DialogService} from 'primeng/dynamicdialog';
import {IssueDetailComponent} from '../../issues/issue-detail/issue-detail.component';

@Component({
  selector: 'sl-fdv-feature-detail',
  templateUrl: './feature-detail.component.html',
  styleUrls: ['./feature-detail.component.scss']
})
export class FeatureDetailComponent {

  @Input() feature: Feature;

  constructor(private issueService: IssueService,
              private dialogService: DialogService,
              private store: Store<AppState>) {
  }

  addIssueCommitsToFeature(issue: Issue) {
    this.issueService.getCommitsOfIssue(issue.id)
      .subscribe(commits => {
        this.store.dispatch(FeatureActions.addCommitsToFeature(
          {feature: this.feature, commits}));
      });

  }

  showIssueDetail(issue: Issue) {
    this.dialogService.open(IssueDetailComponent, {
      header: issue.key + ' - ' + issue.title,
      width: '90%',
      data: {
        issue
      }
    });
  }

  removeIssueFromFeature(issue: Issue) {
    this.store.dispatch(FeatureActions.removeIssuesFromFeature({
      feature: this.feature,
      issues: [issue]
    }));
  }

  showCommitDetail(commit: Commit) {
    this.dialogService.open(CommitDetailComponent, {
      header: commit.hash,
      width: '90%',
      data: {
        commitSelection: {
          commit,
          features: []
        }
      }
    });
  }

  removeCommitFromFeature(commit: Commit) {
    this.store.dispatch(FeatureActions.removeCommitsFromFeature({
      feature: this.feature,
      commits: [commit]
    }));
  }
}
