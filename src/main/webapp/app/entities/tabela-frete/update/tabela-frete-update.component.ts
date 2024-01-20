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
import { ITipoCarga } from 'app/entities/tipo-carga/tipo-carga.model';
import { TipoCargaService } from 'app/entities/tipo-carga/service/tipo-carga.service';
import { ITipoFrete } from 'app/entities/tipo-frete/tipo-frete.model';
import { TipoFreteService } from 'app/entities/tipo-frete/service/tipo-frete.service';
import { IFormaCobranca } from 'app/entities/forma-cobranca/forma-cobranca.model';
import { FormaCobrancaService } from 'app/entities/forma-cobranca/service/forma-cobranca.service';
import { IRegiao } from 'app/entities/regiao/regiao.model';
import { RegiaoService } from 'app/entities/regiao/service/regiao.service';
import { TipoTabelaFrete } from 'app/entities/enumerations/tipo-tabela-frete.model';
import { TabelaFreteService } from '../service/tabela-frete.service';
import { ITabelaFrete } from '../tabela-frete.model';
import { TabelaFreteFormService, TabelaFreteFormGroup } from './tabela-frete-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tabela-frete-update',
  templateUrl: './tabela-frete-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TabelaFreteUpdateComponent implements OnInit {
  isSaving = false;
  tabelaFrete: ITabelaFrete | null = null;
  tipoTabelaFreteValues = Object.keys(TipoTabelaFrete);

  embarcadorsSharedCollection: IEmbarcador[] = [];
  transportadorasSharedCollection: ITransportadora[] = [];
  tipoCargasSharedCollection: ITipoCarga[] = [];
  tipoFretesSharedCollection: ITipoFrete[] = [];
  formaCobrancasSharedCollection: IFormaCobranca[] = [];
  regiaosSharedCollection: IRegiao[] = [];

  editForm: TabelaFreteFormGroup = this.tabelaFreteFormService.createTabelaFreteFormGroup();

  constructor(
    protected tabelaFreteService: TabelaFreteService,
    protected tabelaFreteFormService: TabelaFreteFormService,
    protected embarcadorService: EmbarcadorService,
    protected transportadoraService: TransportadoraService,
    protected tipoCargaService: TipoCargaService,
    protected tipoFreteService: TipoFreteService,
    protected formaCobrancaService: FormaCobrancaService,
    protected regiaoService: RegiaoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEmbarcador = (o1: IEmbarcador | null, o2: IEmbarcador | null): boolean => this.embarcadorService.compareEmbarcador(o1, o2);

  compareTransportadora = (o1: ITransportadora | null, o2: ITransportadora | null): boolean =>
    this.transportadoraService.compareTransportadora(o1, o2);

  compareTipoCarga = (o1: ITipoCarga | null, o2: ITipoCarga | null): boolean => this.tipoCargaService.compareTipoCarga(o1, o2);

  compareTipoFrete = (o1: ITipoFrete | null, o2: ITipoFrete | null): boolean => this.tipoFreteService.compareTipoFrete(o1, o2);

  compareFormaCobranca = (o1: IFormaCobranca | null, o2: IFormaCobranca | null): boolean =>
    this.formaCobrancaService.compareFormaCobranca(o1, o2);

  compareRegiao = (o1: IRegiao | null, o2: IRegiao | null): boolean => this.regiaoService.compareRegiao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tabelaFrete }) => {
      this.tabelaFrete = tabelaFrete;
      if (tabelaFrete) {
        this.updateForm(tabelaFrete);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tabelaFrete = this.tabelaFreteFormService.getTabelaFrete(this.editForm);
    if (tabelaFrete.id !== null) {
      this.subscribeToSaveResponse(this.tabelaFreteService.update(tabelaFrete));
    } else {
      this.subscribeToSaveResponse(this.tabelaFreteService.create(tabelaFrete));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITabelaFrete>>): void {
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

  protected updateForm(tabelaFrete: ITabelaFrete): void {
    this.tabelaFrete = tabelaFrete;
    this.tabelaFreteFormService.resetForm(this.editForm, tabelaFrete);

    this.embarcadorsSharedCollection = this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(
      this.embarcadorsSharedCollection,
      tabelaFrete.embarcador,
    );
    this.transportadorasSharedCollection = this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
      this.transportadorasSharedCollection,
      tabelaFrete.transportadora,
    );
    this.tipoCargasSharedCollection = this.tipoCargaService.addTipoCargaToCollectionIfMissing<ITipoCarga>(
      this.tipoCargasSharedCollection,
      tabelaFrete.tipoCarga,
    );
    this.tipoFretesSharedCollection = this.tipoFreteService.addTipoFreteToCollectionIfMissing<ITipoFrete>(
      this.tipoFretesSharedCollection,
      tabelaFrete.tipoFrete,
    );
    this.formaCobrancasSharedCollection = this.formaCobrancaService.addFormaCobrancaToCollectionIfMissing<IFormaCobranca>(
      this.formaCobrancasSharedCollection,
      tabelaFrete.formaCobranca,
    );
    this.regiaosSharedCollection = this.regiaoService.addRegiaoToCollectionIfMissing<IRegiao>(
      this.regiaosSharedCollection,
      tabelaFrete.regiaoOrigem,
      tabelaFrete.regiaoDestino,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.embarcadorService
      .query()
      .pipe(map((res: HttpResponse<IEmbarcador[]>) => res.body ?? []))
      .pipe(
        map((embarcadors: IEmbarcador[]) =>
          this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(embarcadors, this.tabelaFrete?.embarcador),
        ),
      )
      .subscribe((embarcadors: IEmbarcador[]) => (this.embarcadorsSharedCollection = embarcadors));

    this.transportadoraService
      .query()
      .pipe(map((res: HttpResponse<ITransportadora[]>) => res.body ?? []))
      .pipe(
        map((transportadoras: ITransportadora[]) =>
          this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
            transportadoras,
            this.tabelaFrete?.transportadora,
          ),
        ),
      )
      .subscribe((transportadoras: ITransportadora[]) => (this.transportadorasSharedCollection = transportadoras));

    this.tipoCargaService
      .query()
      .pipe(map((res: HttpResponse<ITipoCarga[]>) => res.body ?? []))
      .pipe(
        map((tipoCargas: ITipoCarga[]) =>
          this.tipoCargaService.addTipoCargaToCollectionIfMissing<ITipoCarga>(tipoCargas, this.tabelaFrete?.tipoCarga),
        ),
      )
      .subscribe((tipoCargas: ITipoCarga[]) => (this.tipoCargasSharedCollection = tipoCargas));

    this.tipoFreteService
      .query()
      .pipe(map((res: HttpResponse<ITipoFrete[]>) => res.body ?? []))
      .pipe(
        map((tipoFretes: ITipoFrete[]) =>
          this.tipoFreteService.addTipoFreteToCollectionIfMissing<ITipoFrete>(tipoFretes, this.tabelaFrete?.tipoFrete),
        ),
      )
      .subscribe((tipoFretes: ITipoFrete[]) => (this.tipoFretesSharedCollection = tipoFretes));

    this.formaCobrancaService
      .query()
      .pipe(map((res: HttpResponse<IFormaCobranca[]>) => res.body ?? []))
      .pipe(
        map((formaCobrancas: IFormaCobranca[]) =>
          this.formaCobrancaService.addFormaCobrancaToCollectionIfMissing<IFormaCobranca>(formaCobrancas, this.tabelaFrete?.formaCobranca),
        ),
      )
      .subscribe((formaCobrancas: IFormaCobranca[]) => (this.formaCobrancasSharedCollection = formaCobrancas));

    this.regiaoService
      .query()
      .pipe(map((res: HttpResponse<IRegiao[]>) => res.body ?? []))
      .pipe(
        map((regiaos: IRegiao[]) =>
          this.regiaoService.addRegiaoToCollectionIfMissing<IRegiao>(
            regiaos,
            this.tabelaFrete?.regiaoOrigem,
            this.tabelaFrete?.regiaoDestino,
          ),
        ),
      )
      .subscribe((regiaos: IRegiao[]) => (this.regiaosSharedCollection = regiaos));
  }
}
