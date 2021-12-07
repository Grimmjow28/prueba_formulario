import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MarcaPcComponent } from '../list/marca-pc.component';
import { MarcaPcDetailComponent } from '../detail/marca-pc-detail.component';
import { MarcaPcUpdateComponent } from '../update/marca-pc-update.component';
import { MarcaPcRoutingResolveService } from './marca-pc-routing-resolve.service';

const marcaPcRoute: Routes = [
  {
    path: '',
    component: MarcaPcComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MarcaPcDetailComponent,
    resolve: {
      marcaPc: MarcaPcRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MarcaPcUpdateComponent,
    resolve: {
      marcaPc: MarcaPcRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MarcaPcUpdateComponent,
    resolve: {
      marcaPc: MarcaPcRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(marcaPcRoute)],
  exports: [RouterModule],
})
export class MarcaPcRoutingModule {}
