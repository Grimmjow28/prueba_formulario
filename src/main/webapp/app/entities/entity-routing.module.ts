import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'marca-pc',
        data: { pageTitle: 'pruebaFormularioApp.marcaPc.home.title' },
        loadChildren: () => import('./marca-pc/marca-pc.module').then(m => m.MarcaPcModule),
      },
      {
        path: 'respuesta',
        data: { pageTitle: 'pruebaFormularioApp.respuesta.home.title' },
        loadChildren: () => import('./respuesta/respuesta.module').then(m => m.RespuestaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
