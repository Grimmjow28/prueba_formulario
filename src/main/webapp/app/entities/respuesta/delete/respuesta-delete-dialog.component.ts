import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRespuesta } from '../respuesta.model';
import { RespuestaService } from '../service/respuesta.service';

@Component({
  templateUrl: './respuesta-delete-dialog.component.html',
})
export class RespuestaDeleteDialogComponent {
  respuesta?: IRespuesta;

  constructor(protected respuestaService: RespuestaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.respuestaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
