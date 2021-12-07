import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRespuesta, Respuesta } from '../respuesta.model';
import { RespuestaService } from '../service/respuesta.service';
import { IMarcaPc } from 'app/entities/marca-pc/marca-pc.model';
import { MarcaPcService } from 'app/entities/marca-pc/service/marca-pc.service';

@Component({
  selector: 'jhi-respuesta-update',
  templateUrl: './respuesta-update.component.html',
})
export class RespuestaUpdateComponent implements OnInit {
  isSaving = false;

  marcaPcsSharedCollection: IMarcaPc[] = [];

  editForm = this.fb.group({
    id: [],
    numeroIdentificacion: ['', Validators.required],
    email: ['', Validators.required],
    comentarios: ['', Validators.required],
    fechaHora: ['', Validators.required],
    marcaPc: ['', Validators.required],
  });

  constructor(
    protected respuestaService: RespuestaService,
    protected marcaPcService: MarcaPcService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected router: Router
  ) {}

  ngOnInit(): void {
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    now.toISOString().slice(0, 16);

    this.editForm.get('fechaHora')?.setValue(now.toISOString().slice(0, 16));
    this.editForm.get('fechaHora')?.disable();

    this.loadRelationshipsOptions();
  }

  previousState(): void {
    // window.history.back();
    this.router.navigate([''], { queryParams: { creado: 'exitoso' } });
  }

  save(): void {
    this.isSaving = true;
    const respuesta = this.createFromForm();
    this.subscribeToSaveResponse(this.respuestaService.create(respuesta));
  }

  trackMarcaPcById(index: number, item: IMarcaPc): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRespuesta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected loadRelationshipsOptions(): void {
    this.marcaPcService
      .query()
      .pipe(map((res: HttpResponse<IMarcaPc[]>) => res.body ?? []))
      .pipe(
        map((marcaPcs: IMarcaPc[]) => this.marcaPcService.addMarcaPcToCollectionIfMissing(marcaPcs, this.editForm.get('marcaPc')!.value))
      )
      .subscribe((marcaPcs: IMarcaPc[]) => (this.marcaPcsSharedCollection = marcaPcs));
  }

  protected createFromForm(): IRespuesta {
    return {
      ...new Respuesta(),
      id: this.editForm.get(['id'])!.value,
      numeroIdentificacion: this.editForm.get(['numeroIdentificacion'])!.value,
      email: this.editForm.get(['email'])!.value,
      comentarios: this.editForm.get(['comentarios'])!.value,
      fechaHora: this.editForm.get(['fechaHora'])!.value ? dayjs(this.editForm.get(['fechaHora'])!.value, DATE_TIME_FORMAT) : undefined,
      marcaPc: this.editForm.get(['marcaPc'])!.value,
    };
  }
}
