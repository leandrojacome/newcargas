import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEstado } from '../estado.model';
import { EstadoService } from '../service/estado.service';
import { EstadoFormService, EstadoFormGroup } from './estado-form.service';

@Component({
  standalone: true,
  selector: 'jhi-estado-update',
  templateUrl: './estado-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EstadoUpdateComponent implements OnInit {
  isSaving = false;
  estado: IEstado | null = null;

  editForm: EstadoFormGroup = this.estadoFormService.createEstadoFormGroup();

  constructor(
    protected estadoService: EstadoService,
    protected estadoFormService: EstadoFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ estado }) => {
      this.estado = estado;
      if (estado) {
        this.updateForm(estado);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const estado = this.estadoFormService.getEstado(this.editForm);
    if (estado.id !== null) {
      this.subscribeToSaveResponse(this.estadoService.update(estado));
    } else {
      this.subscribeToSaveResponse(this.estadoService.create(estado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEstado>>): void {
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

  protected updateForm(estado: IEstado): void {
    this.estado = estado;
    this.estadoFormService.resetForm(this.editForm, estado);
  }
}
