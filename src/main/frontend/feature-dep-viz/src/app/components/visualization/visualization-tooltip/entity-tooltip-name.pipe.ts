import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'entityTooltipName'
})
export class EntityTooltipNamePipe implements PipeTransform {

  transform(value: any): string {

    if (value.path) {
      const seperated = value.path.split('/');
      return seperated[seperated.length - 1];
    }

    if (value.file && value.name) {
      const splitName = value.file.name.split('.');
      const fileName = splitName[splitName.length - 1];
      return fileName + '.' + value.name;
    }

    return value.name;
  }
}
