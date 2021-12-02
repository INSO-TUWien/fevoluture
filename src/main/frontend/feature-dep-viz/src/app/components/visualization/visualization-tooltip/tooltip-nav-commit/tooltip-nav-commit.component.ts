import {Component, Input, OnInit} from '@angular/core';
import {Commit} from '../../../../models/models';
import {AppState} from '../../../../store';
import {Store} from '@ngrx/store';
import {currentCommitChanged} from '../../../../store/visualization/visualization.actions';
import {DialogService} from 'primeng-lts/dynamicdialog';
import {CommitDetailComponent} from '../../../commits/commit-detail/commit-detail.component';
import {VisualizationActionTypes} from '../../../../store/visualization/visualization-action-types';
import {CommitService} from '../../../../services/commit.service';

@Component({
  selector: 'sl-fdv-tooltip-nav-commit',
  templateUrl: './tooltip-nav-commit.component.html',
  styleUrls: ['./tooltip-nav-commit.component.scss']
})
export class TooltipNavCommitComponent implements OnInit {

  @Input() commit: Partial<Commit>;
  @Input() current: Partial<Commit>;
  @Input() active: boolean;
  @Input() hideFeatureIcons: boolean;

  constructor(private store: Store<AppState>, private dialogService: DialogService, private commitService: CommitService) {
  }

  ngOnInit(): void {
  }

  goToCommit(commit: Partial<Commit>) {
    this.commitService.loadCommits({hash: commit.hash, page: 0, pageSize: 1}).subscribe(page => {
      const fullCommit = page.content[0];
      this.store.dispatch(VisualizationActionTypes.currentCommitChanged({commit: fullCommit}));
    });
  }

  show(commit: Partial<Commit>) {
    this.dialogService.open(CommitDetailComponent, {
      header: commit.hash,
      width: '90%',
      data: {
        commitSelection: {
          commit,
          features: []
        },
        commit,
        features: []
      }
    });
  }
}
