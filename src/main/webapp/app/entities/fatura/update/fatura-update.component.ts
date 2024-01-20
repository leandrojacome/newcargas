import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { EmbarcadorService } from 'app/entities/embarcador/service/embarcador.service';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { IContratacao } from 'app/entities/contratacao/contratacao.model';
import { ContratacaoService } from 'app/entities/contratacao/service/contratacao.service';
import { IFormaCobranca } from 'app/entities/forma-cobranca/forma-cobranca.model';
import { FormaCobrancaService } from 'app/entities/forma-cobranca/service/forma-cobranca.service';
import { TipoFatura } from 'app/entities/enumerations/tipo-fatura.model';
import { FaturaService } from '../service/fatura.service';
import { IFatura } from '../fatura.model';
import { FaturaFormService, FaturaFormGroup } from './fatura-form.service';

@Component({
  standalone: true,
  selector: 'jhi-fatura-update',
  templateUrl: './fatura-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FaturaUpdateComponent implements OnInit {
  isSaving = false;
  fatura: IFatura | null = null;
  tipoFaturaValues = Object.keys(TipoFatura);

  embarcadorsSharedCollection: IEmbarcador[] = [];
  transportadorasSharedCollection: ITransportadora[] = [];
  contratacaosSharedCollection: IContratacao[] = [];
  formaCobrancasSharedCollection: IFormaCobranca[] = [];

  editForm: FaturaFormGroup = this.faturaFormService.createFaturaFormGroup();

  constructor(
    protected faturaService: FaturaService,
    protected faturaFormService: FaturaFormService,
    protected embarcadorService: EmbarcadorService,
    protected transportadoraService: TransportadoraService,
    protected contratacaoService: ContratacaoService,
    protected formaCobrancaService: FormaCobrancaService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEmbarcador = (o1: IEmbarcador | null, o2: IEmbarcador | null): boolean => this.embarcadorService.compareEmbarcador(o1, o2);

  compareTransportadora = (o1: ITransportadora | null, o2: ITransportadora | null): boolean =>
    this.transportadoraService.compareTransportadora(o1, o2);

  compareContratacao = (o1: IContratacao | null, o2: IContratacao | null): boolean => this.contratacaoService.compareContratacao(o1, o2);

  compareFormaCobranca = (o1: IFormaCobranca | null, o2: IFormaCobranca | null): boolean =>
    this.formaCobrancaService.compareFormaCobranca(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fatura }) => {
      this.fatura = fatura;
      if (fatura) {
        this.updateForm(fatura);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fatura = this.faturaFormService.getFatura(this.editForm);
    if (fatura.id !== null) {
      this.subscribeToSaveResponse(this.faturaService.update(fatura));
    } else {
      this.subscribeToSaveResponse(this.faturaService.create(fatura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFatura>>): void {
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

  protected updateForm(fatura: IFatura): void {
    this.fatura = fatura;
    this.faturaFormService.resetForm(this.editForm, fatura);

    this.embarcadorsSharedCollection = this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(
      this.embarcadorsSharedCollection,
      fatura.embarcador,
    );
    this.transportadorasSharedCollection = this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
      this.transportadorasSharedCollection,
      fatura.transportadora,
    );
    this.contratacaosSharedCollection = this.contratacaoService.addContratacaoToCollectionIfMissing<IContratacao>(
      this.contratacaosSharedCollection,
      fatura.contratacao,
    );
    this.formaCobrancasSharedCollection = this.formaCobrancaService.addFormaCobrancaToCollectionIfMissing<IFormaCobranca>(
      this.formaCobrancasSharedCollection,
      fatura.formaCobranca,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.embarcadorService
      .query()
      .pipe(map((res: HttpResponse<IEmbarcador[]>) => res.body ?? []))
      .pipe(
        map((embarcadors: IEmbarcador[]) =>
          this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(embarcadors, this.fatura?.embarcador),
        ),
      )
      .subscribe((embarcadors: IEmbarcador[]) => (this.embarcadorsSharedCollection = embarcadors));

    this.transportadoraService
      .query()
      .pipe(map((res: HttpResponse<ITransportadora[]>) => res.body ?? []))
      .pipe(
        map((transportadoras: ITransportadora[]) =>
          this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(transportadoras, this.fatura?.transportadora),
        ),
      )
      .subscribe((transportadoras: ITransportadora[]) => (this.transportadorasSharedCollection = transportadoras));

    this.contratacaoService
      .query()
      .pipe(map((res: HttpResponse<IContratacao[]>) => res.body ?? []))
      .pipe(
        map((contratacaos: IContratacao[]) =>
          this.contratacaoService.addContratacaoToCollectionIfMissing<IContratacao>(contratacaos, this.fatura?.contratacao),
        ),
      )
      .subscribe((contratacaos: IContratacao[]) => (this.contratacaosSharedCollection = contratacaos));

    this.formaCobrancaService
      .query()
      .pipe(map((res: HttpResponse<IFormaCobranca[]>) => res.body ?? []))
      .pipe(
        map((formaCobrancas: IFormaCobranca[]) =>
          this.formaCobrancaService.addFormaCobrancaToCollectionIfMissing<IFormaCobranca>(formaCobrancas, this.fatura?.formaCobranca),
        ),
      )
      .subscribe((formaCobrancas: IFormaCobranca[]) => (this.formaCobrancasSharedCollection = formaCobrancas));
  }
}
