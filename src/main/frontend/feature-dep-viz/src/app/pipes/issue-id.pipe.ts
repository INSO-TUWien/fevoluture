import { Pipe, PipeTransform } from '@angular/core';
import * as _ from 'lodash-es';

@Pipe({
  name: 'issueId'
})
export class IssueIdPipe implements PipeTransform {

  projectRegExs = [
    /SERVER-\d+/gi,
    /WT-\d+/gi,
    /COMPASS-\d+/gi,
    /DRIVERS-\d+/gi,
    /EVG-\d+/gi,
    /CDRIVER-\d+/gi,
    /CXX-\d+/gi,
    /DOCS-\d+/gi,
    /SWIFT-\d+/gi,
    /TOOLS-\d+/gi,
    /AMQ-\d+/gi,
    /MATH-\d+/gi,
    /IO-\d+/gi
  ];

  transform(value: string): string {

    const matches = [];

    this.projectRegExs.forEach(regex => {
      const subMatches = value.match(regex);
      if (subMatches?.length > 0) {
        matches.push(...subMatches);
      }
    });

    return _.uniqWith(matches).join(',');
  }
}
