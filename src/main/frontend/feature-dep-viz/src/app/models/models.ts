import {Color} from './color';
import {extractOriginalSegments} from '@angular/compiler-cli/src/ngtsc/sourcemaps/src/source_file';

export interface Feature {
  id: string;
  name: string;
  issues: Issue[];
  commits: Commit[];
  files: File[];
  methods: Method[];
  packages: Package[];
  color: Color;
  visualized: boolean;
}

export interface Issue {
  id: string;
  key: string;
  title: string;
  description: string;
  status: string;
  authorName: string;
  authorEmail: string;
  tracker: string;
  updatedAt: Date;
  creationDate: Date;
  dueDate: Date;
  link: string;
  subtasks: string[];
  history: any;
  color: string;
}

export interface Commit {
  id: string;
  hash: string;
  authorName: string;
  authorEmail: string;
  message: string;
  commitTime: any;
  issues: Issue[];
  changedFiles: File[];
  changedMethods: Method[];
  changedPackages: Package[];
  features: Feature[];
  color: Color | string;
}

export interface AbstractionLevelEntity {
  id: string;
  features?: Feature[];
  color?: Color;
}

export interface Method extends AbstractionLevelEntity {
  id: string;
  name: string;
  file: Partial<File>;
}

export interface Package extends AbstractionLevelEntity {
  id: string;
  name: string;
}

export interface File extends AbstractionLevelEntity {
  name: string;
  id: string;
  path: string;
  aPackage?: Partial<Package>;
  createdAt?: any;
}

export interface LogicalCoupling {
  root: AbstractionLevelEntity;
  couplings: LogicalCouplingEntry[];
  features: Feature[];
  color: Color;
}

export type LogicalCouplingEntry = {
  method: Method;
  file: File;
  package: Package;
  support: number;
  confidence: number;
  countRoot: number,
  countMethod?: number;
  countFile?: number;
  countPackage?: number;
  countChangedTogether?: number
};

export enum AbstractionLevel {
  FILE, METHOD, PACKAGE
}

export const getAbstractionLevelName = (abstractionLevel: AbstractionLevel) => {
  switch (abstractionLevel) {
    case AbstractionLevel.PACKAGE:
      return 'package';
    case AbstractionLevel.FILE:
      return 'file';
    case  AbstractionLevel.METHOD:
      return 'method';
    default:
      console.error('Invalid abstraction level');
  }
};

export type VisualizationEdge = {
  source: string,
  target: symbol,
  support: number,
  confidence: number,
  reverseConfidence: number,
  maxConfidence: number,
  countRoot: number,
  countEntity: number,
  countChangedTogether: number,
  color: Color
};

export type VisualizationNode = {
  id: string,
  color: Color,
  features: Partial<Feature>[]
};

export const compCommits = (a: Commit, b: Commit) => a.hash === b.hash;
export const filterCommits = (a: Commit, b: Commit) => !compCommits(a, b);
export const orderCommits = (commit) => -commit.commitTime.millis;

export const compIssues = (a: Issue, b: Issue) => a.id === b.id;
export const filterIssues = (a: Issue, b: Issue) => !compIssues(a, b);
export const orderIssues = (issue) => issue.creationDate;

export const compFiles = (a: File, b: File) => a.path === b.path;
export const filterFiles = (a: File, b: File) => !compFiles(a, b);
export const orderFiles = (file) => file.path;

export const compMethod = (a: Method, b: Method) => a.file.path + a.name === b.file.path + b.name;
export const filterMethods = (a: Method, b: Method) => !compMethod(a, b);
export const orderMethods = (method) => method.file.path + method.name;

export const compPackage = (a: Package, b: Package) => a.name === b.name;
export const filterPackages = (a: Package, b: Package) => !compPackage(a, b);
export const orderPackages = (aPackage) => aPackage.name;

export const compFeature = (a: Feature, b: Feature) => {
  if (a.id && b.id) {
    return a.id === b.id;
  } else {
    return a.name === b.name;
  }
};
export const filterFeature = (a: Feature, b: Feature) => !compFeature(a, b);
