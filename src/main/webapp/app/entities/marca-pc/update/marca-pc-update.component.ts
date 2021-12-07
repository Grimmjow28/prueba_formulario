import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMarcaPc, MarcaPc } from '../marca-pc.model';
import { MarcaPcService } from '../service/marca-pc.service';

@Component({
  selector: 'jhi-marca-pc-update',
  templateUrl: './marca-pc-update.component.html',
})
export class MarcaPcUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombreMarca: [],
  });

  constructor(protected marcaPcService: MarcaPcService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marcaPc }) => {
      this.updateForm(marcaPc);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const marcaPc = this.createFromForm();
    if (marcaPc.id !== undefined) {
      this.subscribeToSaveResponse(this.marcaPcService.update(marcaPc));
    } else {
      this.subscribeToSaveResponse(this.marcaPcService.create(marcaPc));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMarcaPc>>): void {
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

  protected updateForm(marcaPc: IMarcaPc): void {
    this.editForm.patchValue({
      id: marcaPc.id,
      nombreMarca: marcaPc.nombreMarca,
    });
  }

  protected createFromForm(): IMarcaPc {
    return {
      ...new MarcaPc(),
      id: this.editForm.get(['id'])!.value,
      nombreMarca: this.editForm.get(['nombreMarca'])!.value,
    };
  }
}
