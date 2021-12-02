import {createFeatureSelector, createSelector} from '@ngrx/store';
import {AppState} from '../index';
import {FeatureMap, visualizationFeatureKey, VisualizationState} from './visualization.reducer';
import {commitsOfVisualizedFeatures, visualizedFeatures} from '../feature/feature.selectors';
import {
  AbstractionLevel,
  AbstractionLevelEntity,
  Commit,
  compFeature,
  Feature,
  getAbstractionLevelName,
  LogicalCouplingEntry
} from '../../models/models';
import * as _ from 'lodash-es';
import {CommitFilterOptions} from '../../components/commits/commit-filter/commit-filter-options';
import {Color} from '../../models/color';
import {VisualizationFilterOptions} from '../../components/visualization/visualization-filter-options/visualization-filter-options';

export const selectVisualization = createFeatureSelector<AppState, VisualizationState>(visualizationFeatureKey);

export const visualizationFilter = createSelector(
  selectVisualization,
  (state: VisualizationState) => state.visualizationFilterOptions);

export const currentAbstractionLevel = createSelector(
  selectVisualization,
  (state: VisualizationState) => state.abstractionLevel);

export const visualizedCommitFilterOptions = createSelector(
  selectVisualization,
  (state: VisualizationState) => {
    return state.visualizedCommitFilterOptions;
  });

export const filteredCommitsOfVisualizedFeatures = createSelector(
  selectVisualization,
  commitsOfVisualizedFeatures,
  visualizedCommitFilterOptions,
  (state, commits, filter: CommitFilterOptions) => {
    return commits.filter(c => matchesFilter(c, filter));
  });

export const currentCommit = createSelector(
  selectVisualization,
  filteredCommitsOfVisualizedFeatures,
  (state: VisualizationState, commits: Commit[]) => {

    if (state.currentCommit) {
      return state.currentCommit;
    } else {
      return commits[0];
    }
  });

export const visualizedCommits = createSelector(
  selectVisualization,

  // can also use commitsOfVisualizedFeatures instead
  filteredCommitsOfVisualizedFeatures,
  currentCommit,
  (state: VisualizationState, filteredCommits: Commit[], lastCommit: Commit) => {
    return filteredCommits.filter(c => c.commitTime.millis <= lastCommit.commitTime.millis);
  });

/**
 * Returns a map, which saves for the ids of an entity (package, file, method) the visualized commits which include the entity.
 */
export const abstractionLevelEntityToGlobalCommitMap = createSelector(
  selectVisualization,
  commitsOfVisualizedFeatures,
  currentAbstractionLevel,
  (state: VisualizationState, commits, currAbstractionLevel: AbstractionLevel) => {

    const abstractionLevelName = getCommitAbstractionLevelFieldNameByAbstractionLevel(currAbstractionLevel);
    const map = new Map<string, Partial<Commit>[]>();
    commits.forEach(commit => {
      commit[abstractionLevelName].forEach(abstractionLevelEntity => {
        if (map.has(abstractionLevelEntity.id)) {
          map.get(abstractionLevelEntity.id).push({hash: commit.hash, commitTime: commit.commitTime});
        } else {
          map.set(abstractionLevelEntity.id, [{hash: commit.hash, commitTime: commit.commitTime}]);
        }
      });
    });
    return map;
  });

export const abstractionLevelEntityToCurrentCommitMap = createSelector(
  abstractionLevelEntityToGlobalCommitMap,
  currentCommit,
  (commitMap, currCommit) => {
    const newMap = new Map<string, Partial<Commit>[]>();
    commitMap.forEach((value, key) => {
      newMap.set(key, value.filter(c => c.commitTime.millis <= currCommit.commitTime.millis));
    });
    return newMap;
  });

export const globalFeatureMap = createSelector(
  selectVisualization,
  commitsOfVisualizedFeatures,
  visualizedFeatures,
  (state: VisualizationState, commits: Commit[], features: Feature[]) => {
    return buildFeatureMap(commits, features);
  });

export const currentFeatureMap = createSelector(
  selectVisualization,
  visualizedCommits,
  visualizedFeatures,
  (state: VisualizationState, commits: Commit[], features: Feature[]) => {
    return buildFeatureMap(commits, features);
  });

export const currentCommitEntityIds = createSelector(
  currentCommit,
  currentAbstractionLevel,
  (currCommit, currAbstractionLevel) => {
    const abstractionLevelName = getCommitAbstractionLevelFieldNameByAbstractionLevel(currAbstractionLevel);
    const idSet = new Set<string>();
    if (currCommit) {
      currCommit[abstractionLevelName]?.forEach(entity => idSet.add(entity.id));
    }
    return idSet;
  });

export const isLoadingVisualization = createSelector(
  selectVisualization,
  (state) => {
    return state.isVisualizationLoading;
  });

export const visualizationData = createSelector(
  selectVisualization,
  currentFeatureMap,
  visualizationFilter,
  (state: VisualizationState, featureMap: FeatureMap, filter: VisualizationFilterOptions) => {


    const abstractionLevel = state.abstractionLevel;
    const logicalCouplings = state.logicalCoupling;
    const abstractionLevelEntityMap: Map<string, Partial<Feature[]>> = featureMap[getAbstractionLevelName(abstractionLevel) + 's'];

    const nodes = [];
    const edges = [];

    const nodeHashSet = new Set<string>();
    const edgeHashSet = new Set<string>();

    function addNode(abstractionLevelEntity: AbstractionLevelEntity): boolean {

      if (!doesNodeMatchFilter(abstractionLevelEntity, filter)) {
        return false;
      }

      if (!nodeHashSet.has(abstractionLevelEntity.id)) {

        nodeHashSet.add(abstractionLevelEntity.id);
        abstractionLevelEntity.features = abstractionLevelEntityMap.get(abstractionLevelEntity.id) || [];
        abstractionLevelEntity.color = getNodeColor(abstractionLevelEntity);
        nodes.push(abstractionLevelEntity);
      }

      return true;
    }

    function getNodeColor(abstractionLevelEntity: AbstractionLevelEntity): Color {
      if (abstractionLevelEntity.features.length === 1) {
        return Color.toColor(abstractionLevelEntity.features[0].color);
      }

      if (abstractionLevelEntity.features.length > 1) {
        return Color.RED();
      }

      return Color.LOGICAL_COUPLING();
    }

    function getEdgeColor(
      source: AbstractionLevelEntity,
      target: AbstractionLevelEntity,
      couplingInfo: {
        maxConfidence: number,
        support: number,
        numberChangedTogether: number
      }
    ): Color {
      const sourceFeatures = abstractionLevelEntityMap.get(source.id) || [];
      const targetFeatures = abstractionLevelEntityMap.get(target.id) || [];

      // Its a logical coupled file
      if (targetFeatures.length === 0) {
        // return Color.EDGE();
        return couplingInfo.support >= state.visualizationFilterOptions.minLC  &&
        couplingInfo.numberChangedTogether >= state.visualizationFilterOptions.minCount ? Color.RED() : Color.EDGE();
      }

      const sourceFeatureSet = new Set();
      sourceFeatures.forEach(ele => sourceFeatureSet.add(ele));
      const targetFeatureSet = new Set();
      targetFeatures.forEach(ele => targetFeatureSet.add(ele));

      return !_.isEqual(sourceFeatureSet, targetFeatureSet) &&
      couplingInfo.numberChangedTogether >= state.visualizationFilterOptions.minCount &&
      couplingInfo.maxConfidence >= state.visualizationFilterOptions.minLC ? Color.RED() : Color.EDGE();
    }

    function addEdge(source: AbstractionLevelEntity, couplingEntry: LogicalCouplingEntry) {
      const coupledEntity = couplingEntry[getAbstractionLevelName(abstractionLevel)];
      const edgeHash = [source.id, coupledEntity.id]
        .sort()
        .join('-');

      if (!edgeHashSet.has(edgeHash) && doesNodeMatchFilter(source, filter) && doesNodeMatchFilter(coupledEntity, filter)) {
        const countEntity = couplingEntry.countPackage || couplingEntry.countFile || couplingEntry.countMethod;
        const maxConfidence = Math.max(couplingEntry.countChangedTogether / countEntity, couplingEntry.confidence);
        edgeHashSet.add(edgeHash);
        const edge = {
          source: source.id,
          target: coupledEntity.id,
          support: couplingEntry.support,
          confidence: couplingEntry.confidence,
          reverseConfidence: couplingEntry.countChangedTogether / countEntity,
          maxConfidence,
          countRoot: couplingEntry.countRoot,
          countEntity,
          countChangedTogether: couplingEntry.countChangedTogether,
          color: getEdgeColor(source, coupledEntity, {
            maxConfidence,
            support: couplingEntry.confidence,
            numberChangedTogether: couplingEntry.countChangedTogether
          })
        };
        edges.push(edge);
      }
    }

    try {
      logicalCouplings.forEach(coupling => {
        const addedRoot = addNode(_.cloneDeep(coupling.root));
        if (addedRoot) {
          coupling.couplings.forEach(coupledEntity => {
            const coupledAbstractionLevelEntity: AbstractionLevelEntity = coupledEntity[getAbstractionLevelName(abstractionLevel)];
            const addedCoupled = addNode(_.cloneDeep(coupledAbstractionLevelEntity));
            if (addedRoot && addedCoupled) {
              addEdge(coupling.root, coupledEntity);
            }
          });
        }
      });
      return {nodes, edges};
    } catch (err) {
      console.error(err);
      return {
        nodes: [], edges: []
      };
    }
  });


/*********************
 * Helper functions *
 *********************/

/**
 * Build the feature Map to easily fetch the features of an entity.
 * @param commits: Commits to be considered in the map
 * @param features The features to add as values
 */
function buildFeatureMap(commits: Commit[], features: Feature[]): FeatureMap {
  const featureMap = {
    commits: new Map<string, Partial<Feature>[]>(),
    packages: new Map<string, Partial<Feature>[]>(),
    files: new Map<string, Partial<Feature>[]>(),
    methods: new Map<string, Partial<Feature>[]>()
  };

  commits.forEach(filteredCommit => {

    const featuresOfFilteredCommit = features
      .filter(f => f.commits.some(featureCommit => featureCommit.hash === filteredCommit.hash))
      .map(f => ({id: f.id, name: f.name, color: f.color}));
    featureMap.commits.set(filteredCommit.hash, featuresOfFilteredCommit);

    filteredCommit.changedPackages.forEach(changedPackage => {
      if (featureMap.packages.has(changedPackage.id)) {
        const mergedFeatures = _.uniqWith([
          ...featureMap.packages.get(changedPackage.id),
          ...featuresOfFilteredCommit
        ], compFeature);
        featureMap.packages.set(changedPackage.id, mergedFeatures);
      } else {
        featureMap.packages.set(changedPackage.id, featuresOfFilteredCommit);
      }
    });

    filteredCommit.changedFiles.forEach(changedFile => {
      if (featureMap.files.has(changedFile.id)) {
        const mergedFeatures = _.uniqWith([
          ...featureMap.files.get(changedFile.id),
          ...featuresOfFilteredCommit
        ], compFeature);
        featureMap.files.set(changedFile.id, mergedFeatures);
      } else {
        featureMap.files.set(changedFile.id, featuresOfFilteredCommit);
      }
    });

    filteredCommit.changedMethods.forEach(changedMethod => {
      if (featureMap.methods.has(changedMethod.id)) {
        const mergedFeatures = _.uniqWith([
          ...featureMap.methods.get(changedMethod.id),
          ...featuresOfFilteredCommit
        ], compFeature);
        featureMap.methods.set(changedMethod.id, mergedFeatures);
      } else {
        featureMap.methods.set(changedMethod.id, featuresOfFilteredCommit);
      }
    });

  });

  return featureMap;
}

function matchesFilter(commit: Commit, filter: CommitFilterOptions): boolean {

  if (!filter) {
    return true;
  }

  let fromDateCheck = true;
  if (filter.fromDate) {
    fromDateCheck = commit.commitTime.millis >= filter.fromDate;
  }

  let toDateCheck = true;
  if (filter.toDate) {
    toDateCheck = commit.commitTime.millis <= filter.toDate;
  }
  const authorCheck = commit.authorName.includes(filter?.author || '');
  const hashCheck = commit.hash.includes(filter?.hash || '');
  const msgCheck = commit.message.includes(filter?.msg || '');

  let featureCheck = true;
  if (filter.feature) {
    console.log(filter);
    featureCheck = commit.features.filter(feature => feature.name === filter.feature).length > 0;
  }

  let fileCheck = true;
  if (filter.fileName) {
    fileCheck = commit.changedFiles.filter(file => file.path.includes(filter.fileName)).length > 0;
  }

  return authorCheck &&
    hashCheck &&
    msgCheck &&
    fromDateCheck &&
    toDateCheck &&
    fileCheck &&
    featureCheck;
}

export function getCommitAbstractionLevelFieldNameByAbstractionLevel(abstractionLevel: AbstractionLevel): string {
  let abstractionLevelName = getAbstractionLevelName(abstractionLevel) + 's';
  abstractionLevelName = abstractionLevelName.charAt(0).toUpperCase() + abstractionLevelName.substr(1);
  abstractionLevelName = 'changed' + abstractionLevelName;
  return abstractionLevelName;
}

export function doesNodeMatchFilter(abstractionLevelEntity: AbstractionLevelEntity, filter: VisualizationFilterOptions): boolean {
  const nodeName = getNodeName(abstractionLevelEntity).toLowerCase();
  const includeText = (filter?.sourceText || '').toLowerCase();
  return nodeName.includes(includeText) &&
    (filter.excludeText?.length < 3 ||
      !filter.excludeText?.toLowerCase().split(';')
        .filter(val => val.length)
        .map(value => nodeName.toLowerCase().includes(value))
        .reduce((prev, curr) => prev || curr));
}

export function getNodeName(abstractionLevelEntity): string {
  // It is a file
  if (abstractionLevelEntity.path) {
    return abstractionLevelEntity.path;
  }

  // it is a method
  if (abstractionLevelEntity.file && abstractionLevelEntity.name) {
    return abstractionLevelEntity.file.path + '.' + abstractionLevelEntity.name;
  }

  // It is a package
  return abstractionLevelEntity.name;
}
