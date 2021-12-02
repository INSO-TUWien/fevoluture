import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {Feature} from '../../../models/models';
import * as copy from 'copy-to-clipboard';

@Component({
  selector: 'sl-fdv-feature-icons',
  templateUrl: './feature-icons.component.html',
  styleUrls: ['./feature-icons.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FeatureIconsComponent {
  @Input() features: Partial<Feature>[];
  @Input() hasBorder: boolean;
  @Input() isLarger: boolean;

  copyFeatureName(feature: Partial<Feature>): void {
    copy(feature.name);
  }
}
