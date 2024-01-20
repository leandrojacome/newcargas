import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoVeiculo } from '../tipo-veiculo.model';
import { TipoVeiculoService } from '../service/tipo-veiculo.service';
import { TipoVeiculoFormService, TipoVeiculoFormGroup } from './tipo-veiculo-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tipo-veiculo-update',
  templateUrl: './tipo-veiculo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoVeiculoUpdateComponent implements OnInit {
  isSaving = false;
  tipoVeiculo: ITipoVeiculo | null = null;

  editForm: TipoVeiculoFormGroup = this.tipoVeiculoFormService.createTipoVeiculoFormGroup();

  constructor(
    protected tipoVeiculoService: TipoVeiculoService,
    protected tipoVeiculoFormService: TipoVeiculoFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoVeiculo }) => {
      this.tipoVeiculo = tipoVeiculo;
      if (tipoVeiculo) {
        this.updateForm(tipoVeiculo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoVeiculo = this.tipoVeiculoFormService.getTipoVeiculo(this.editForm);
    if (tipoVeiculo.id !== null) {
      this.subscribeToSaveResponse(this.tipoVeiculoService.update(tipoVeiculo));
    } else {
      this.subscribeToSaveResponse(this.tipoVeiculoService.create(tipoVeiculo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoVeiculo>>): void {
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

  protected updateForm(tipoVeiculo: ITipoVeiculo): void {
    this.tipoVeiculo = tipoVeiculo;
    this.tipoVeiculoFormService.resetForm(this.editForm, tipoVeiculo);
  }
}
