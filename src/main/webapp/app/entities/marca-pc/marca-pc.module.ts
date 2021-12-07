import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MarcaPcComponent } from './list/marca-pc.component';
import { MarcaPcDetailComponent } from './detail/marca-pc-detail.component';
import { MarcaPcUpdateComponent } from './update/marca-pc-update.component';
import { MarcaPcDeleteDialogComponent } from './delete/marca-pc-delete-dialog.component';
import { MarcaPcRoutingModule } from './route/marca-pc-routing.module';

@NgModule({
  imports: [SharedModule, MarcaPcRoutingModule],
  declarations: [MarcaPcComponent, MarcaPcDetailComponent, MarcaPcUpdateComponent, MarcaPcDeleteDialogComponent],
  entryComponents: [MarcaPcDeleteDialogComponent],
})
export class MarcaPcModule {}
