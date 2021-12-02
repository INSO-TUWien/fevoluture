import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {html} from 'diff2html';
import * as $ from 'jquery';
import {CommitService} from '../../../services/commit.service';
import {DomSanitizer, SafeHtml} from '@angular/platform-browser';
import {Commit} from '../../../models/models';
import {Observable} from 'rxjs';
import {map, tap} from 'rxjs/operators';

@Component({
  selector: 'sl-fdv-commit-diff',
  templateUrl: './commit-diff.component.html',
  styleUrls: ['./commit-diff.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CommitDiffComponent implements OnChanges {

  @Input() commit: Commit;
  diffHTML$: Observable<SafeHtml>;

  constructor(private commitService: CommitService,
              private domSanitizer: DomSanitizer) {
  }

  ngOnChanges(changes: SimpleChanges) {
    this.getDiff();
  }

  getDiff() {

    this.diffHTML$ = this.commitService.getDiffOfCommit(this.commit).pipe(
      map(diff => this.domSanitizer.bypassSecurityTrustHtml(html(diff, {matching: 'words'}))),
      tap(() => {
        setTimeout(() => {

          const className = 'a.d2h-file-name';

          $(className).click(function($event) {
            const file = $(this).text();
            $event.preventDefault();
            $event.stopPropagation();

            const $scrollingContainer = $('.diff-container');
            const scrollTop = $scrollingContainer.scrollTop();
            const link = $(`.diff.dialog-diff .d2h-wrapper .d2h-file-name:contains("${file}")`).first();
            const position = link.position();
            if (position) {
              $scrollingContainer.animate({
                scrollTop: position.top + scrollTop
              }, 500);
            }
          });

        }, 100);
      })
    );
  }
}
