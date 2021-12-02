import { Pipe, PipeTransform } from '@angular/core';
import {Issue} from '../models/models';

@Pipe({
  name: 'jiraLink'
})
export class JiraLinkPipe implements PipeTransform {

  transform(issue: Issue | string): string {
    const url = 'https://issues.apache.org/jira/browse/';
    if (typeof issue === 'string') {
      return url + issue;
    }

    return url + issue.key;
  }
}
