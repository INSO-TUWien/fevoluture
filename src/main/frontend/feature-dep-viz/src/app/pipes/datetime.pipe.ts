import { Pipe, PipeTransform } from '@angular/core';
import {DatePipe} from '@angular/common';

@Pipe({
  name: 'datetime'
})
export class DatetimePipe implements PipeTransform {

  transform(value: any, args: string): any {
    if (!value.millis) {
      throw Error('Not a valid date time');
    }
    const datePipe = new DatePipe('en-US');
    const date = new Date(value.millis);
    return datePipe.transform(date, args);
  }
}
