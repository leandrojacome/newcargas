import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITipoFrete } from '../tipo-frete.model';
import { TipoFreteService } from '../service/tipo-frete.service';
import { TipoFreteFormService, TipoFreteFormGroup } from './tipo-frete-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tipo-frete-update',
  templateUrl: './tipo-frete-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TipoFreteUpdateComponent implements OnInit {
  isSaving = false;
  tipoFrete: ITipoFrete | null = null;

  editForm: TipoFreteFormGroup = this.tipoFreteFormService.createTipoFreteFormGroup();

  constructor(
    protected tipoFreteService: TipoFreteService,
    protected tipoFreteFormService: TipoFreteFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoFrete }) => {
      this.tipoFrete = tipoFrete;
      if (tipoFrete) {
        this.updateForm(tipoFrete);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoFrete = this.tipoFreteFormService.getTipoFrete(this.editForm);
    if (tipoFrete.id !== null) {
      this.subscribeToSaveResponse(this.tipoFreteService.update(tipoFrete));
    } else {
      this.subscribeToSaveResponse(this.tipoFreteService.create(tipoFrete));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoFrete>>): void {
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

  protected updateForm(tipoFrete: ITipoFrete): void {
    this.tipoFrete = tipoFrete;
    this.tipoFreteFormService.resetForm(this.editForm, tipoFrete);
  }
}
