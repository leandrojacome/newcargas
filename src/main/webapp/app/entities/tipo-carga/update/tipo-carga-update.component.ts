import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoCarga } from '../tipo-carga.model';
import { TipoCargaService } from '../service/tipo-carga.service';
import { TipoCargaFormService, TipoCargaFormGroup } from './tipo-carga-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tipo-carga-update',
  templateUrl: './tipo-carga-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoCargaUpdateComponent implements OnInit {
  isSaving = false;
  tipoCarga: ITipoCarga | null = null;

  editForm: TipoCargaFormGroup = this.tipoCargaFormService.createTipoCargaFormGroup();

  constructor(
    protected tipoCargaService: TipoCargaService,
    protected tipoCargaFormService: TipoCargaFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoCarga }) => {
      this.tipoCarga = tipoCarga;
      if (tipoCarga) {
        this.updateForm(tipoCarga);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoCarga = this.tipoCargaFormService.getTipoCarga(this.editForm);
    if (tipoCarga.id !== null) {
      this.subscribeToSaveResponse(this.tipoCargaService.update(tipoCarga));
    } else {
      this.subscribeToSaveResponse(this.tipoCargaService.create(tipoCarga));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoCarga>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(tipoCarga: ITipoCarga): void {
    this.tipoCarga = tipoCarga;
    this.tipoCargaFormService.resetForm(this.editForm, tipoCarga);
  }
}
