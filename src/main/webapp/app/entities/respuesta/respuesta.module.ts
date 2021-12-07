import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RespuestaComponent } from './list/respuesta.component';
import { RespuestaDetailComponent } from './detail/respuesta-detail.component';
import { RespuestaUpdateComponent } from './update/respuesta-update.component';
import { RespuestaDeleteDialogComponent } from './delete/respuesta-delete-dialog.component';
import { RespuestaRoutingModule } from './route/respuesta-routing.module';

@NgModule({
  imports: [SharedModule, RespuestaRoutingModule],
  declarations: [RespuestaComponent, RespuestaDetailComponent, RespuestaUpdateComponent, RespuestaDeleteDialogComponent],
  entryComponents: [RespuestaDeleteDialogComponent],
})
export class RespuestaModule {}
