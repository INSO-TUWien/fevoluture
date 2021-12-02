/* tslint:disable:max-line-length */
// Angular related stuff
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import {environment} from '../environments/environment';

// Store related stuff
import {StoreModule} from '@ngrx/store';
import {EffectsModule} from '@ngrx/effects';
import {AppEffects} from './app.effects';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {metaReducers, reducers} from './store';

// PrimeNg related stuff
import {InputTextModule} from 'primeng-lts/inputtext';
import {ColorPickerModule} from 'primeng-lts/colorpicker';
import {ButtonModule} from 'primeng-lts/button';
import {ProgressSpinnerModule} from 'primeng-lts/progressspinner';
import {TooltipModule} from 'primeng-lts/tooltip';
import {TableModule} from 'primeng-lts/table';
import {SharedModule} from 'primeng-lts/api';
import {CheckboxModule} from 'primeng-lts/checkbox';
import {DropdownModule} from 'primeng-lts/dropdown';
import {TreeModule} from 'primeng-lts/tree';
import {CalendarModule} from 'primeng-lts/calendar';
import {AccordionModule} from 'primeng-lts/accordion';
import {PaginatorModule} from 'primeng-lts/paginator';
import {DialogModule} from 'primeng-lts/dialog';
import {DialogService, DynamicDialogModule} from 'primeng-lts/dynamicdialog';
import {MenuModule} from 'primeng-lts/menu';
import {MessageModule} from 'primeng-lts/message';

// Commit related stuff
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CommitListComponent} from './components/commits/commit-list/commit-list.component';
import {CommitDetailComponent} from './components/commits/commit-detail/commit-detail.component';
import {CommitNavigatorComponent} from './components/commits/commit-navigator/commit-navigator.component';
import {CommitFilterComponent} from './components/commits/commit-filter/commit-filter.component';
import {CommitEffects} from './store/commits/commit.effects';
import {CommitDiffComponent} from './components/commits/commit-diff/commit-diff.component';

// Issue related stuff
import {IssueListComponent} from './components/issues/issue-list/issue-list.component';
import {IssueDetailComponent} from './components/issues/issue-detail/issue-detail.component';
import {IssueFilterComponent} from './components/issues/issue-filter/issue-filter.component';
import {IssuesEffects} from './store/issues/issues.effects';

// Feature related stuff
import {FeatureListComponent} from './components/features/feature-list/feature-list.component';
import {FeatureDetailComponent} from './components/features/feature-detail/feature-detail.component';
import {FeatureSidebarComponent} from './components/features/feature-sidebar/feature-sidebar.component';
import {FeatureFormComponent} from './components/features/feature-form/feature-form.component';
import {FeatureIconsComponent} from './components/features/feature-icons/feature-icons.component';
import {FeatureEffects} from './store/feature/feature.effects';
import {FeatureDropdownComponent} from './components/features/feature-dropdown/feature-dropdown.component';
import {FeatureIssueListComponent} from './components/features/feature-issue-list/feature-issue-list.component';
import {FeatureCommitListComponent} from './components/features/feature-commit-list/feature-commit-list.component';
import {FeatureSourceCodeTreeComponent} from './components/features/feature-source-code-tree/feature-source-code-tree.component';
import {FeaturesOfPipe} from './pipes/features-of.pipe';


// Visualization related stuff
import {VisualizationWrapperComponent} from './components/visualization/visualization-wrapper/visualization-wrapper.component';
import {VisualizationFilterOptionsComponent} from './components/visualization/visualization-filter-options/visualization-filter-options.component';
import {VisualizationLegendComponent} from './components/visualization/visualization-legend/visualization-legend.component';
import {AbstractionLevelDropdownComponent} from './components/visualization/abstraction-level-dropdown/abstraction-level-dropdown.component';
import {VisualizationEffects} from './store/visualization/visualization.effects';


// Color related stuff
import {ColorPipe} from './pipes/color.pipe';
import {ColorDirective} from './directives/color.directive';
import {ColorBackgroundDirective} from './directives/color-background.directive';

// Pipes
import {IssueIdPipe} from './pipes/issue-id.pipe';
import {DatetimePipe} from './pipes/datetime.pipe';
import {CommitMessagePipe} from './pipes/commit-message.pipe';
import {JiraLinkPipe} from './pipes/jira-link.pipe';
import {EllipsisPipe} from './pipes/ellipsis.pipe';

// Source code related stuff
import {SourceTreeComponent} from './components/files/source-tree/source-tree.component';
import { VisualizationTooltipComponent } from './components/visualization/visualization-tooltip/visualization-tooltip.component';
import { TooltipEdgeContentComponent } from './components/visualization/visualization-tooltip/tooltip-edge-content/tooltip-edge-content.component';
import { TooltipNodeContentComponent } from './components/visualization/visualization-tooltip/tooltip-node-content/tooltip-node-content.component';
import { TooltipNavCommitComponent } from './components/visualization/visualization-tooltip/tooltip-nav-commit/tooltip-nav-commit.component';
import { EntityTooltipNamePipe } from './components/visualization/visualization-tooltip/entity-tooltip-name.pipe';
import { AdminComponent } from './components/admin/admin.component';


@NgModule({
  declarations: [
    AppComponent,
    CommitListComponent,
    CommitDetailComponent,
    IssueListComponent,
    IssueDetailComponent,
    FeatureListComponent,
    FeatureDetailComponent,
    FeatureSidebarComponent,
    CommitNavigatorComponent,
    CommitFilterComponent,
    IssueFilterComponent,
    VisualizationWrapperComponent,
    FeatureFormComponent,
    ColorPipe,
    FeatureIconsComponent,
    ColorDirective,
    ColorBackgroundDirective,
    FeatureDropdownComponent,
    FeatureIssueListComponent,
    FeatureCommitListComponent,
    FeatureSourceCodeTreeComponent,
    IssueIdPipe,
    DatetimePipe,
    CommitMessagePipe,
    JiraLinkPipe,
    EllipsisPipe,
    SourceTreeComponent,
    CommitDiffComponent,
    VisualizationFilterOptionsComponent,
    VisualizationLegendComponent,
    AbstractionLevelDropdownComponent,
    FeaturesOfPipe,
    VisualizationTooltipComponent,
    TooltipEdgeContentComponent,
    TooltipNodeContentComponent,
    TooltipNavCommitComponent,
    EntityTooltipNamePipe,
    AdminComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ButtonModule,
    InputTextModule,
    FormsModule,
    ReactiveFormsModule,
    ColorPickerModule,
    InputTextModule,
    ColorPickerModule,
    ButtonModule,
    BrowserAnimationsModule,
    HttpClientModule,
    TooltipModule,
    TableModule,
    CheckboxModule,
    TreeModule,
    CalendarModule,
    AccordionModule,
    DialogModule,
    DynamicDialogModule,
    MenuModule,
    StoreModule.forRoot(reducers, {metaReducers}),
    !environment.production ? StoreDevtoolsModule.instrument() : [],
    EffectsModule.forRoot([AppEffects]),
    EffectsModule.forFeature([FeatureEffects, CommitEffects, IssuesEffects, VisualizationEffects]),
    ProgressSpinnerModule,
    SharedModule,
    ReactiveFormsModule,
    DropdownModule,
    PaginatorModule,
    MessageModule
  ],
  providers: [DialogService],
  bootstrap: [AppComponent],
  entryComponents: [
    CommitDetailComponent,
    IssueDetailComponent
    // FileLevelVisualizationComponent,
    // MethodLevelVisualizationComponent,
    // PackageLevelVisualizationComponent
  ]
})
export class AppModule {
}
