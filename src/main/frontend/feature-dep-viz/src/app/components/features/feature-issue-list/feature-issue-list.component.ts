import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {Issue} from '../../../models/models';

@Component({
  selector: 'sl-fdv-feature-issue-list',
  templateUrl: './feature-issue-list.component.html',
  styleUrls: ['./feature-issue-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FeatureIssueListComponent {

  @Input() featureIssues: Issue[];
  @Output() added = new EventEmitter<Issue>();
  @Output() showed = new EventEmitter<Issue>();
  @Output() removed = new EventEmitter<Issue>();

  onAdd(issue: Issue) {
    this.added.emit(issue);
  }

  onShow(issue: Issue) {
    this.showed.emit(issue);
  }

  onRemove(issue: Issue) {
    this.removed.emit(issue);
  }
}
