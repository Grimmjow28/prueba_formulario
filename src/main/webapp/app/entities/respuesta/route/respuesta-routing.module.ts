import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RespuestaComponent } from '../list/respuesta.component';
import { RespuestaDetailComponent } from '../detail/respuesta-detail.component';
import { RespuestaUpdateComponent } from '../update/respuesta-update.component';
import { RespuestaRoutingResolveService } from './respuesta-routing-resolve.service';

const respuestaRoute: Routes = [
  {
    path: '',
    component: RespuestaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RespuestaDetailComponent,
    resolve: {
      respuesta: RespuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RespuestaUpdateComponent,
    resolve: {
      respuesta: RespuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RespuestaUpdateComponent,
    resolve: {
      respuesta: RespuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(respuestaRoute)],
  exports: [RouterModule],
})
export class RespuestaRoutingModule {}
