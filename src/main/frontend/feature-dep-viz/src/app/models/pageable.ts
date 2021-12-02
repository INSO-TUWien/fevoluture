
export class Pageable<T> {

  content: T[];
  empty: boolean;
  first: true;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
  sort: {sorted: boolean, unsorted: boolean, empty: boolean};
}
