import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRespuesta } from '../respuesta.model';
import { RespuestaService } from '../service/respuesta.service';
import { RespuestaDeleteDialogComponent } from '../delete/respuesta-delete-dialog.component';

@Component({
  selector: 'jhi-respuesta',
  templateUrl: './respuesta.component.html',
})
export class RespuestaComponent implements OnInit {
  respuestas?: IRespuesta[];
  isLoading = false;

  constructor(protected respuestaService: RespuestaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.respuestaService.query().subscribe(
      (res: HttpResponse<IRespuesta[]>) => {
        this.isLoading = false;
        this.respuestas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IRespuesta): number {
    return item.id!;
  }

  delete(respuesta: IRespuesta): void {
    const modalRef = this.modalService.open(RespuestaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.respuesta = respuesta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
