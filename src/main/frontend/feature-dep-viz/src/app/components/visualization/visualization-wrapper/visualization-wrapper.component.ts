/* tslint:disable:space-before-function-paren */
import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AppState} from '../../../store';
import {select, Store} from '@ngrx/store';
import {VisualizationActionTypes} from '../../../store/visualization/visualization-action-types';
import {
  currentCommit,
  isLoadingVisualization,
  visualizationData,
  visualizationFilter
} from '../../../store/visualization/visualization.selectors';
import {debounceTime, distinctUntilChanged, filter, first, tap, withLatestFrom} from 'rxjs/operators';
import * as d3 from 'd3';
import {AbstractionLevelEntity} from '../../../models/models';
import {Unsubscriber} from '../../../utils/unsubscriber';
import {VisualizationService} from '../../../services/visualization.service';
import {Actions, ofType} from '@ngrx/effects';
import * as _ from 'lodash-es';
import {doesNodeMatchQuery} from '../visualization-filter-options/visualization-filter-options';
import {Color} from '../../../models/color';

@Component({
  selector: 'sl-fdv-visualization-wrapper',
  templateUrl: './visualization-wrapper.component.html',
  styleUrls: ['./visualization-wrapper.component.scss']
})
export class VisualizationWrapperComponent extends Unsubscriber implements OnInit, AfterViewInit, OnDestroy {

  isLoadingVisualization$ = this.store.pipe(select(isLoadingVisualization));

  @ViewChild('visualizationWrapper', {static: true}) visualizationWrapper: ElementRef;

  // Visualization elements
  width: number;
  height: number;
  nodeRadius = 25;
  svg: any;
  zoomGroup: any;
  nodeSelectionGroup: any;
  linkSelectionGroup: any;

  selectedElement = null;
  selectedElementPosition: { x: number, y: number };
  selectedEdgeNodes = [];

  // Visualization methods
  strokeWidthScale = d3.scaleLinear()
    .domain([0, 1])
    .range([3, 7]);

  opacityScale = d3.scaleLinear()
    .domain([0, 1])
    .range([0.3, 1]);
  private lastHightlightText: string;


  constructor(
    private store: Store<AppState>,
    private visualizationService: VisualizationService,
    private actions$: Actions) {
    super();
  }

  ngAfterViewInit(): void {
    this.initSubscriptions();
  }

  ngOnInit() {
    this.initVisualization();
  }

  ngOnDestroy() {
    this.unsubscribeAll();
  }

  loadVisualization() {
    this.store.dispatch(VisualizationActionTypes.loadLogicalCoupling());
  }

  private initVisualization() {
    this.width = this.calcWidth();
    this.height = this.calcHeight();
    this.svg = d3.select('svg');
    this.svg
      .attr('width', this.calcWidth())
      .attr('height', this.calcHeight())
      .call(d3.zoom()
        .scaleExtent([-1, 1])
        .on('zoom', () => this.zoomGroup.attr('transform', d3.event.transform)));

    this.zoomGroup = this.svg
      .append('g')
      .attr('id', 'zoomableGroup');

    this.linkSelectionGroup = this.zoomGroup
      .append('g')
      .attr('class', 'links')
      .selectAll('line');

    this.nodeSelectionGroup = this.zoomGroup
      .append('g')
      .attr('class', 'nodes')
      .selectAll('circle');
  }

  private calcWidth(): number {
    return this.visualizationWrapper.nativeElement.offsetWidth;
  }

  private calcHeight(): number {
    // return window.innerHeight - this.visualizationWrapper.nativeElement.offsetTop;
    return 850;
  }

  private getBorderWidthOfNode(node: AbstractionLevelEntity): string {
    if (this.visualizationService.isChangedInCurrentCommit(node)) {
      return '5px';
    }
    return '1px';
  }

  private getBorderColorOfNode(node: AbstractionLevelEntity): string {
    if (this.visualizationService.isChangedInCurrentCommit(node)) {
      return '#000';
    }
    return '#5a5a5a';
  }

  private render(data: { nodes, edges }) {
    d3.selectAll('circle').remove();
    d3.selectAll('line').remove();
    this.onCloseTooltip();

    const thiz = this;

    const showDetails = function (d) {

      thiz.onCloseTooltip();
      if (thiz.selectedElement === d) {
        thiz.selectedElement = null;
        thiz.selectedElementPosition = null;
        thiz.selectedEdgeNodes = [];
        return;
      }

      const point = d3.mouse(d3.select(this).node() as any);
      const transform = thiz.zoomGroup.attr('transform');
      let scale = 1;
      let translate = [0, 0];
      if (transform) {

        const matches = transform?.match(/^translate\((.+)\) scale\((.+)\)/);
        translate = matches[1].split(',');
        scale = matches[2];
      }

      thiz.selectedElementPosition = {x: (point[0] * +scale + +translate[0]), y: (point[1] * scale + +translate[1])};
      thiz.selectedElement = d;
      d3.select(this)
        .attr('class', 'tooltiped');

      // It is an edge
      if (d.source && d.target) {
        d3.select(this)
          .style('stroke', Color.BLACK().toHex())
          .style('opacity', 1);
        thiz.zoomGroup
          .selectAll('circle')
          .filter(f => f.id === d.source.id || f.id === d.target.id)
          .style('fill', Color.HIGHLIGHT().toHex())
          .style('stroke', Color.BLACK().toHex());
        thiz.selectedEdgeNodes = [d.source, d.target];
      }

    };
    // Handle nodes
    const nodeSelection = this.nodeSelectionGroup
      .data(data.nodes, d => d.id);
    nodeSelection.exit().remove();

    const nodeRadiusScale = d3.scaleLinear()
      .domain([0, this.visualizationService.getAmountOfVisualizedCommits()])
      .range([this.nodeRadius, this.nodeRadius * 3]);


    nodeSelection
      .attr('r', d => nodeRadiusScale(this.visualizationService.getCommitsOfEntityUntilCurrentCommit(d).length))
      .style('fill', d => d.color.toHex())
      .style('stroke-width', d => this.getBorderWidthOfNode(d))
      .style('stroke', d => this.getBorderColorOfNode(d));

    const nodes = nodeSelection
      .enter()
      .append('circle')
      .attr('r', d => nodeRadiusScale(this.visualizationService.getCommitsOfEntityUntilCurrentCommit(d).length))
      .attr('id', d => d.id)
      .attr('name', d => d.name)
      .attr('baseColor', d => d.color.toHex())
      .style('fill', d => d.color.toHex())
      .style('cursor', 'pointer')
      .style('stroke-width', d => this.getBorderWidthOfNode(d))
      .style('stroke', d => this.getBorderColorOfNode(d))
      .classed('fixed', d => d.fx !== undefined)
      .on('click', showDetails);

    // Handle edges
    const linkSelection = this.linkSelectionGroup
      .data(data.edges, edge => `${edge.source}-${edge.target}`);
    linkSelection.exit().remove();

    const links = linkSelection
      .enter()
      .append('line')
      .attr('stroke-width', d => this.strokeWidthScale(d.confidence))
      .attr('opacity', d => this.opacityScale(Math.max(
        d.confidence,
        d.countChangedTogether / d.countEntity)))
      .attr('source', d => d.source)
      .attr('target', d => d.target)
      .style('stroke', d => d.color.toHex())
      .style('cursor', 'pointer')
      .on('click', showDetails);

    const ticked = () => {
      nodes
        .attr('cx', d => d.x)
        .attr('cy', d => d.y);
      links
        .attr('x1', d => d.source.x)
        .attr('y1', d => d.source.y)
        .attr('x2', d => d.target.x)
        .attr('y2', d => d.target.y);
    };

    const simulation = d3.forceSimulation(data.nodes)
      .force('center', d3.forceCenter(this.width / 2, this.height / 2))
      .force('charge', d3.forceManyBody().strength(-20))
      .force('collide', d3.forceCollide(d => {
        return nodeRadiusScale(this.visualizationService.getCommitsOfEntityUntilCurrentCommit((d as AbstractionLevelEntity)).length) + 10;
      }))
      .force('feature', d3.forceLink()
        .id(d => (d as any).id)
        .links(data.edges)
        .distance(value => {
          const edgeData = value as any;
          return (1 - edgeData.maxConfidence) * 100;
        }).strength(link => {
          let min = Math.min(
            this.visualizationService.getCommitsOfEntityIdUntilCurrentCommit((link.source as any).id).length,
            this.visualizationService.getCommitsOfEntityIdUntilCurrentCommit((link.target as any).id).length);

          if (min === 0) {
            min = 1;
          }
          return 1 / min;
        }))
      .on('tick', ticked)
      .on('end', () => {
        this.store.select(visualizationFilter)
          .pipe(
            first(),
            tap(visFilter => this.highlightNodeContainingText(visFilter.highlight)))
          .subscribe();
      });

    const drag = d3.drag()
      .on('drag', function () {
        const point = d3.mouse(d3.select(this).node() as any);
        d3.select(this)
          .attr('cx', point[0])
          .attr('cy', point[1]);
        const allLines = d3.selectAll('line');

        allLines
          .filter((l: any) => l.source.id === this.id)
          .attr('x1', point[0])
          .attr('y1', point[1]);

        allLines
          .filter((l: any) => l.target.id === this.id)
          .attr('x2', point[0])
          .attr('y2', point[1]);
      });
    drag(nodes);
  }

  private initSubscriptions() {

    const afterLogicalCouplingLoaded$ = this.actions$.pipe(ofType(VisualizationActionTypes.logicalCouplingLoaded));
    const visualizationData$ = this.store.pipe(select(visualizationData));
    this.registerSubscription(afterLogicalCouplingLoaded$.pipe(
      withLatestFrom(visualizationData$),
      debounceTime(1000),
      tap(data => this.render(data[1])))
      .subscribe());

    this.registerSubscription(this.store.pipe(
      select(currentCommit),
      filter(commit => !!commit),
      first(),
      tap(() => this.loadVisualization())
    ).subscribe());

    this.registerSubscription(this.store.pipe(
      select(visualizationFilter),
      distinctUntilChanged((oldFilter, newFilter) => _.isEqual(oldFilter, newFilter)),
      tap(currFilter => {

        this.lastHightlightText = currFilter.highlight;
        this.highlightNodeContainingText(currFilter.highlight);
      })
    ).subscribe());
  }

  onCloseTooltip() {
    d3.select('.tooltiped')
      .attr('class', '');
    this.selectedElement = null;
    this.selectedElementPosition = null;
    this.selectedEdgeNodes.forEach(node => {
      this.zoomGroup
        .selectAll('circle')
        .filter(f => f.id === node.id)
        .style('fill', d => d.color.toHex())
        .style('stroke', d => this.getBorderColorOfNode(d));
    });
    this.zoomGroup
      .selectAll('line')
      .filter(l => {
        return this.selectedEdgeNodes.map(n => n.id).includes(l.source.id) &&
          this.selectedEdgeNodes.map(n => n.id).includes(l.target.id);
      })
      .style('opacity', d => this.opacityScale(Math.max(
        d.confidence,
        d.countChangedTogether / d.countEntity)))
      .style('stroke', d => d.color.toHex());
    this.highlightNodeContainingText(this.lastHightlightText);
  }

  private highlightNodeContainingText(queryText: string): void {
    d3
      .selectAll('circle')
      .classed('highlighted', node => {
        return doesNodeMatchQuery(queryText, node);
      });
  }
}
