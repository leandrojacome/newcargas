import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { IContratacao } from '../contratacao.model';
import { ContratacaoService } from '../service/contratacao.service';
import { ContratacaoFormService, ContratacaoFormGroup } from './contratacao-form.service';

@Component({
  standalone: true,
  selector: 'jhi-contratacao-update',
  templateUrl: './contratacao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContratacaoUpdateComponent implements OnInit {
  isSaving = false;
  contratacao: IContratacao | null = null;

  transportadorasSharedCollection: ITransportadora[] = [];

  editForm: ContratacaoFormGroup = this.contratacaoFormService.createContratacaoFormGroup();

  constructor(
    protected contratacaoService: ContratacaoService,
    protected contratacaoFormService: ContratacaoFormService,
    protected transportadoraService: TransportadoraService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTransportadora = (o1: ITransportadora | null, o2: ITransportadora | null): boolean =>
    this.transportadoraService.compareTransportadora(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contratacao }) => {
      this.contratacao = contratacao;
      if (contratacao) {
        this.updateForm(contratacao);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contratacao = this.contratacaoFormService.getContratacao(this.editForm);
    if (contratacao.id !== null) {
      this.subscribeToSaveResponse(this.contratacaoService.update(contratacao));
    } else {
      this.subscribeToSaveResponse(this.contratacaoService.create(contratacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContratacao>>): void {
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

  protected updateForm(contratacao: IContratacao): void {
    this.contratacao = contratacao;
    this.contratacaoFormService.resetForm(this.editForm, contratacao);

    this.transportadorasSharedCollection = this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
      this.transportadorasSharedCollection,
      contratacao.transportadora,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transportadoraService
      .query()
      .pipe(map((res: HttpResponse<ITransportadora[]>) => res.body ?? []))
      .pipe(
        map((transportadoras: ITransportadora[]) =>
          this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
            transportadoras,
            this.contratacao?.transportadora,
          ),
        ),
      )
      .subscribe((transportadoras: ITransportadora[]) => (this.transportadorasSharedCollection = transportadoras));
  }
}
