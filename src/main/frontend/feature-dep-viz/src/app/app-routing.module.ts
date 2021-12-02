import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CommitListComponent} from './components/commits/commit-list/commit-list.component';
import {IssueListComponent} from './components/issues/issue-list/issue-list.component';
import {FeatureListComponent} from './components/features/feature-list/feature-list.component';
import {FeatureSidebarComponent} from './components/features/feature-sidebar/feature-sidebar.component';
import {CommitNavigatorComponent} from './components/commits/commit-navigator/commit-navigator.component';
import {VisualizationWrapperComponent} from './components/visualization/visualization-wrapper/visualization-wrapper.component';
import {AdminComponent} from './components/admin/admin.component';


const routes: Routes = [
  {path: 'commits', component: CommitListComponent},
  {path: 'issues', component: IssueListComponent},
  {path: 'features', component: FeatureListComponent},
  {path: 'visualization', component: VisualizationWrapperComponent},
  {path: 'feature-sidebar', component: FeatureSidebarComponent, outlet: 'aside'},
  {path: 'commit-navigator', component: CommitNavigatorComponent, outlet: 'aside'},
  {path: 'admin', component: AdminComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
