import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {Commit} from '../../../models/models';

@Component({
  selector: 'sl-fdv-feature-commit-list',
  templateUrl: './feature-commit-list.component.html',
  styleUrls: ['./feature-commit-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FeatureCommitListComponent {

  @Input() featureCommits: Commit[];
  @Output() added = new EventEmitter<Commit>();
  @Output() showed = new EventEmitter<Commit>();
  @Output() removed = new EventEmitter<Commit>();

  onAdd(commit: Commit) {
    this.added.emit(commit);
  }

  onShow(commit: Commit) {
    this.showed.emit(commit);
  }

  onRemove(commit: Commit) {
    this.removed.emit(commit);
  }
}
