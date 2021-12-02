
export interface IssueFilterOptions {
  id?: string;
  title?: string;
  author?: string;
  description?: string;
  tracker?: string;
  fromDate?: number;
  toDate?: number;
  page: number;
  pageSize: number;
}
