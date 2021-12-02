import {Pipe, PipeTransform} from '@angular/core';
import {Color} from '../models/color';

@Pipe({
  name: 'color'
})
export class ColorPipe implements PipeTransform {

  transform(color: Color, ...args: any[]): any {
    if (!color) {
      return 'inherit';
    }
    return `rgba(${color.r || 0},${color.g || 0},${color.b || 0},${color.a || 1})`;
  }

}
