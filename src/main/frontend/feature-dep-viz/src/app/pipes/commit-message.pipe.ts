import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'commitMessage'
})
export class CommitMessagePipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    return value.replace('https://issues.apache.org/jira/browse/', '');
  }
}
