import {Component, Input, OnChanges} from '@angular/core';
import {compFiles, compMethod, Feature, File, Method} from '../../../models/models';
import * as _ from 'lodash-es';

@Component({
  selector: 'sl-fdv-source-tree',
  templateUrl: './source-tree.component.html',
  styleUrls: ['./source-tree.component.scss']
})
export class SourceTreeComponent implements OnChanges {

  @Input() feature: Feature;
  files: File[];
  methods: Method[];

  tree;

  constructor() {
  }

  ngOnChanges(): void {

    if (!this.feature) {
      return;
    }

    this.files = _.uniqWith(
      _.flatten(this.feature.commits.map(commit => commit.changedFiles)),
      compFiles);
    this.methods = _.uniqWith(
      _.flatten(this.feature.commits.map(commit => commit.changedMethods)),
      compMethod);
    this.buildTree();
  }

  buildTree(): void {
    this.tree = [];
    this.files?.forEach(file => {
      this.addFileToTree(file.path.split('/'), this.tree, file);
    });
  }

  private addFileToTree(filePath: string[], searchLevel: any[], file: File): void {
    let folder = searchLevel.find(level => level.data === filePath[0]);
    const isLeaf = filePath.length === 1;

    if (!folder) {
      folder = {
        label: filePath[0],
        data: filePath[0],
        expandedIcon: isLeaf ? 'pi pi-file' : 'pi pi-folder-open',
        collapsedIcon: isLeaf ? 'pi pi-file' : 'pi pi-folder',
        expanded: filePath.length > 1,
        children: []
      };
      searchLevel.push(folder);
    }

    if (isLeaf) {
      const fileMethods = this.methods?.filter(m => m.file.id === file.id)
        .map(m => {
          return {
            label: m.name,
            data: m.name,
            expandedIcon: 'pi pi-dollar',
            collapsedIcon: 'pi pi-dollar',
            expanded: false,
            children: []
          };
        });
      folder.children = fileMethods || [];
    }


    if (!isLeaf) {
      this.addFileToTree(filePath.slice(1), folder.children, file);
    }
  }

}
