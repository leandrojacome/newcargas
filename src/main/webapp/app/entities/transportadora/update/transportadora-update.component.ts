import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITransportadora } from '../transportadora.model';
import { TransportadoraService } from '../service/transportadora.service';
import { TransportadoraFormService, TransportadoraFormGroup } from './transportadora-form.service';

@Component({
  standalone: true,
  selector: 'jhi-transportadora-update',
  templateUrl: './transportadora-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransportadoraUpdateComponent implements OnInit {
  isSaving = false;
  transportadora: ITransportadora | null = null;

  editForm: TransportadoraFormGroup = this.transportadoraFormService.createTransportadoraFormGroup();

  constructor(
    protected transportadoraService: TransportadoraService,
    protected transportadoraFormService: TransportadoraFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transportadora }) => {
      this.transportadora = transportadora;
      if (transportadora) {
        this.updateForm(transportadora);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transportadora = this.transportadoraFormService.getTransportadora(this.editForm);
    if (transportadora.id !== null) {
      this.subscribeToSaveResponse(this.transportadoraService.update(transportadora));
    } else {
      this.subscribeToSaveResponse(this.transportadoraService.create(transportadora));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransportadora>>): void {
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

  protected updateForm(transportadora: ITransportadora): void {
    this.transportadora = transportadora;
    this.transportadoraFormService.resetForm(this.editForm, transportadora);
  }
}
