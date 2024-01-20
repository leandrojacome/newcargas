import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmbarcador } from '../embarcador.model';
import { EmbarcadorService } from '../service/embarcador.service';
import { EmbarcadorFormService, EmbarcadorFormGroup } from './embarcador-form.service';

@Component({
  standalone: true,
  selector: 'jhi-embarcador-update',
  templateUrl: './embarcador-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmbarcadorUpdateComponent implements OnInit {
  isSaving = false;
  embarcador: IEmbarcador | null = null;

  editForm: EmbarcadorFormGroup = this.embarcadorFormService.createEmbarcadorFormGroup();

  constructor(
    protected embarcadorService: EmbarcadorService,
    protected embarcadorFormService: EmbarcadorFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ embarcador }) => {
      this.embarcador = embarcador;
      if (embarcador) {
        this.updateForm(embarcador);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const embarcador = this.embarcadorFormService.getEmbarcador(this.editForm);
    if (embarcador.id !== null) {
      this.subscribeToSaveResponse(this.embarcadorService.update(embarcador));
    } else {
      this.subscribeToSaveResponse(this.embarcadorService.create(embarcador));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmbarcador>>): void {
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

  protected updateForm(embarcador: IEmbarcador): void {
    this.embarcador = embarcador;
    this.embarcadorFormService.resetForm(this.editForm, embarcador);
  }
}
