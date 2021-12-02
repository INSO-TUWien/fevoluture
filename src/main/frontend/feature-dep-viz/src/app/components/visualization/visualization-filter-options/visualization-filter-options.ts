export interface VisualizationFilterOptions {
  sourceText?: string;
  excludeText?: string;
  minCount: number;
  minLC: number;
  highlight: string;
}

/**
 * Returns true if the visualization node name matches the query.
 * @param queryText: the query to search in the form of searchTerm1;searchTerm2;
 * @param node: The node, which is either a file, method or package.
 */
export function doesNodeMatchQuery(queryText: string, node: unknown): boolean {
  return queryText?.length > 0 && queryText?.toLowerCase().split(';')
    .filter(val => val.length)
    .map(value => getNodeName(node).toLowerCase().includes(value))
    .reduce((prev, curr) => prev || curr);
}

export function getNodeName(node): string {

  let name = '';

  if (!node) {
    return '';
  }

  if (node.file) {
    name = name + node.file.name + '.';
  }

  if (node.name) {
    name = name + node.name;
  }

  if (node.path) {
    name = node.path;
  }

  return name;
}
