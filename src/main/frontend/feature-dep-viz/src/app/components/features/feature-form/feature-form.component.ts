import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Feature} from '../../../models/models';
import {Color} from '../../../models/color';
import {Store} from '@ngrx/store';
import {addFeature, updateFeature} from '../../../store/feature/feature.actions';
import {FormControl, FormGroup} from '@angular/forms';
import * as _ from 'lodash-es';

@Component({
  selector: 'sl-fdv-feature-form',
  templateUrl: './feature-form.component.html',
  styleUrls: ['./feature-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FeatureFormComponent implements OnChanges {

  @Input() feature: Feature;

  form: FormGroup = new FormGroup({
    name: new FormControl(''),
    color: new FormControl(''),
    visualized: new FormControl(false)
  });

  constructor(private store: Store) {
  }

  ngOnChanges(changes: SimpleChanges) {
    this.patchForm();
  }

  private patchForm() {
    this.form.patchValue({
      ...this.feature,
      color: this.feature.color.toHex()
    });
  }

  save() {
    const feature = _.cloneDeep(this.feature);
    feature.color = Color.hexToColor(this.form.value.color);
    feature.name = this.form.value.name;
    feature.visualized = this.form.value.visualized;

    if (this.feature.id) {
      this.store.dispatch(updateFeature({feature: _.cloneDeep(feature)}));
    } else {
      this.store.dispatch(addFeature({feature: _.cloneDeep(feature)}));
    }
  }
}
