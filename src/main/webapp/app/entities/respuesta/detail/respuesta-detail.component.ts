import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRespuesta } from '../respuesta.model';

@Component({
  selector: 'jhi-respuesta-detail',
  templateUrl: './respuesta-detail.component.html',
})
export class RespuestaDetailComponent implements OnInit {
  respuesta: IRespuesta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ respuesta }) => {
      this.respuesta = respuesta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
