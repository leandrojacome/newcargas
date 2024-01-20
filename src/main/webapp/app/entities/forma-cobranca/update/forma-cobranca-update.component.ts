import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFormaCobranca } from '../forma-cobranca.model';
import { FormaCobrancaService } from '../service/forma-cobranca.service';
import { FormaCobrancaFormService, FormaCobrancaFormGroup } from './forma-cobranca-form.service';

@Component({
  standalone: true,
  selector: 'jhi-forma-cobranca-update',
  templateUrl: './forma-cobranca-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FormaCobrancaUpdateComponent implements OnInit {
  isSaving = false;
  formaCobranca: IFormaCobranca | null = null;

  editForm: FormaCobrancaFormGroup = this.formaCobrancaFormService.createFormaCobrancaFormGroup();

  constructor(
    protected formaCobrancaService: FormaCobrancaService,
    protected formaCobrancaFormService: FormaCobrancaFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formaCobranca }) => {
      this.formaCobranca = formaCobranca;
      if (formaCobranca) {
        this.updateForm(formaCobranca);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formaCobranca = this.formaCobrancaFormService.getFormaCobranca(this.editForm);
    if (formaCobranca.id !== null) {
      this.subscribeToSaveResponse(this.formaCobrancaService.update(formaCobranca));
    } else {
      this.subscribeToSaveResponse(this.formaCobrancaService.create(formaCobranca));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormaCobranca>>): void {
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

  protected updateForm(formaCobranca: IFormaCobranca): void {
    this.formaCobranca = formaCobranca;
    this.formaCobrancaFormService.resetForm(this.editForm, formaCobranca);
  }
}
