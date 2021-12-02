import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Commit, Feature} from '../../../models/models';
import {AppState} from '../../../store';
import {select, Store} from '@ngrx/store';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {selectedFeature} from '../../../store/feature/feature.selectors';
import {first} from 'rxjs/operators';
import {addCommitsToFeature, removeCommitsFromFeature} from '../../../store/feature/feature.actions';

@Component({
  selector: 'sl-fdv-commit-detail',
  templateUrl: './commit-detail.component.html',
  styleUrls: ['./commit-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CommitDetailComponent implements OnInit {

  @Input() commitSelection: { commit: Commit, features: Feature[] };
  @Output() added = new EventEmitter<Commit>();
  @Output() removed = new EventEmitter<Commit>();

  constructor(public ref: DynamicDialogRef,
              public config: DynamicDialogConfig,
              private store: Store<AppState>) {
  }

  ngOnInit(): void {
    if (!this.commitSelection) {
      this.commitSelection = this.config.data.commitSelection;
    }
  }

  addCommitToFeature(commit: Commit) {
    this.store.pipe(select(selectedFeature), first()).subscribe(feature => {
      this.store.dispatch(addCommitsToFeature({commits: [commit], feature}));
      this.ref.close();
    });
  }

  removeCommitFromFeature(commit: Commit) {
    this.store.pipe(select(selectedFeature), first()).subscribe(feature => {
      this.store.dispatch(removeCommitsFromFeature({commits: [commit], feature}));
      this.ref.close();
    });
  }
}
