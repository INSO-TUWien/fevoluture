import {HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

export const createHTTPParams = (options: unknown) => {
  let httpParams = new HttpParams();
  for (const [key, value] of Object.entries(options)) {
    if (value === null || value === undefined || key === 'feature') {
      continue;
    }
    httpParams = httpParams.append(key, value.toString());
  }
  return httpParams;
};

export function castToDropdownValueArray(observable: Observable<string[]>): Observable<{ label: string, value: string }[]> {
  return observable.pipe(
    map(response => [{label: '-- Empty --', value: ''}, ...response.map(ele => {
      return {label: ele, value: ele};
    })]));
}
