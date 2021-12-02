import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ellipsis'
})
export class EllipsisPipe implements PipeTransform {

  transform(value: string, args: { maxLength: number, append?: string }): string {

    if (value.length > args.maxLength) {
      return  value.substring(0, args.maxLength) + (args.append || '');
    }

    return value;
  }

}
