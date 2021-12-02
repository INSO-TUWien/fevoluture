export interface CommitFilterOptions {
  author?: string;
  fromDate?: number;
  toDate?: number;
  msg?: string;
  hash?: string;
  feature?: string;
  fileName?: string;
  page: number;
  pageSize: number;
}
