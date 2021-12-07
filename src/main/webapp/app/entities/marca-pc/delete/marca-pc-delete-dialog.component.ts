import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMarcaPc } from '../marca-pc.model';
import { MarcaPcService } from '../service/marca-pc.service';

@Component({
  templateUrl: './marca-pc-delete-dialog.component.html',
})
export class MarcaPcDeleteDialogComponent {
  marcaPc?: IMarcaPc;

  constructor(protected marcaPcService: MarcaPcService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.marcaPcService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
