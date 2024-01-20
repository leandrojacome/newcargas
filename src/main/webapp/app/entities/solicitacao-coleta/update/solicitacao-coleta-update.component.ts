import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { EmbarcadorService } from 'app/entities/embarcador/service/embarcador.service';
import { IStatusColeta } from 'app/entities/status-coleta/status-coleta.model';
import { StatusColetaService } from 'app/entities/status-coleta/service/status-coleta.service';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';
import { RoteirizacaoService } from 'app/entities/roteirizacao/service/roteirizacao.service';
import { ITipoVeiculo } from 'app/entities/tipo-veiculo/tipo-veiculo.model';
import { TipoVeiculoService } from 'app/entities/tipo-veiculo/service/tipo-veiculo.service';
import { SolicitacaoColetaService } from '../service/solicitacao-coleta.service';
import { ISolicitacaoColeta } from '../solicitacao-coleta.model';
import { SolicitacaoColetaFormService, SolicitacaoColetaFormGroup } from './solicitacao-coleta-form.service';

@Component({
  standalone: true,
  selector: 'jhi-solicitacao-coleta-update',
  templateUrl: './solicitacao-coleta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SolicitacaoColetaUpdateComponent implements OnInit {
  isSaving = false;
  solicitacaoColeta: ISolicitacaoColeta | null = null;

  embarcadorsSharedCollection: IEmbarcador[] = [];
  statusColetasSharedCollection: IStatusColeta[] = [];
  roteirizacaosSharedCollection: IRoteirizacao[] = [];
  tipoVeiculosSharedCollection: ITipoVeiculo[] = [];

  editForm: SolicitacaoColetaFormGroup = this.solicitacaoColetaFormService.createSolicitacaoColetaFormGroup();

  constructor(
    protected solicitacaoColetaService: SolicitacaoColetaService,
    protected solicitacaoColetaFormService: SolicitacaoColetaFormService,
    protected embarcadorService: EmbarcadorService,
    protected statusColetaService: StatusColetaService,
    protected roteirizacaoService: RoteirizacaoService,
    protected tipoVeiculoService: TipoVeiculoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEmbarcador = (o1: IEmbarcador | null, o2: IEmbarcador | null): boolean => this.embarcadorService.compareEmbarcador(o1, o2);

  compareStatusColeta = (o1: IStatusColeta | null, o2: IStatusColeta | null): boolean =>
    this.statusColetaService.compareStatusColeta(o1, o2);

  compareRoteirizacao = (o1: IRoteirizacao | null, o2: IRoteirizacao | null): boolean =>
    this.roteirizacaoService.compareRoteirizacao(o1, o2);

  compareTipoVeiculo = (o1: ITipoVeiculo | null, o2: ITipoVeiculo | null): boolean => this.tipoVeiculoService.compareTipoVeiculo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ solicitacaoColeta }) => {
      this.solicitacaoColeta = solicitacaoColeta;
      if (solicitacaoColeta) {
        this.updateForm(solicitacaoColeta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const solicitacaoColeta = this.solicitacaoColetaFormService.getSolicitacaoColeta(this.editForm);
    if (solicitacaoColeta.id !== null) {
      this.subscribeToSaveResponse(this.solicitacaoColetaService.update(solicitacaoColeta));
    } else {
      this.subscribeToSaveResponse(this.solicitacaoColetaService.create(solicitacaoColeta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISolicitacaoColeta>>): void {
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

  protected updateForm(solicitacaoColeta: ISolicitacaoColeta): void {
    this.solicitacaoColeta = solicitacaoColeta;
    this.solicitacaoColetaFormService.resetForm(this.editForm, solicitacaoColeta);

    this.embarcadorsSharedCollection = this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(
      this.embarcadorsSharedCollection,
      solicitacaoColeta.embarcador,
    );
    this.statusColetasSharedCollection = this.statusColetaService.addStatusColetaToCollectionIfMissing<IStatusColeta>(
      this.statusColetasSharedCollection,
      solicitacaoColeta.statusColeta,
    );
    this.roteirizacaosSharedCollection = this.roteirizacaoService.addRoteirizacaoToCollectionIfMissing<IRoteirizacao>(
      this.roteirizacaosSharedCollection,
      solicitacaoColeta.roteirizacao,
    );
    this.tipoVeiculosSharedCollection = this.tipoVeiculoService.addTipoVeiculoToCollectionIfMissing<ITipoVeiculo>(
      this.tipoVeiculosSharedCollection,
      solicitacaoColeta.tipoVeiculo,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.embarcadorService
      .query()
      .pipe(map((res: HttpResponse<IEmbarcador[]>) => res.body ?? []))
      .pipe(
        map((embarcadors: IEmbarcador[]) =>
          this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(embarcadors, this.solicitacaoColeta?.embarcador),
        ),
      )
      .subscribe((embarcadors: IEmbarcador[]) => (this.embarcadorsSharedCollection = embarcadors));

    this.statusColetaService
      .query()
      .pipe(map((res: HttpResponse<IStatusColeta[]>) => res.body ?? []))
      .pipe(
        map((statusColetas: IStatusColeta[]) =>
          this.statusColetaService.addStatusColetaToCollectionIfMissing<IStatusColeta>(statusColetas, this.solicitacaoColeta?.statusColeta),
        ),
      )
      .subscribe((statusColetas: IStatusColeta[]) => (this.statusColetasSharedCollection = statusColetas));

    this.roteirizacaoService
      .query()
      .pipe(map((res: HttpResponse<IRoteirizacao[]>) => res.body ?? []))
      .pipe(
        map((roteirizacaos: IRoteirizacao[]) =>
          this.roteirizacaoService.addRoteirizacaoToCollectionIfMissing<IRoteirizacao>(roteirizacaos, this.solicitacaoColeta?.roteirizacao),
        ),
      )
      .subscribe((roteirizacaos: IRoteirizacao[]) => (this.roteirizacaosSharedCollection = roteirizacaos));

    this.tipoVeiculoService
      .query()
      .pipe(map((res: HttpResponse<ITipoVeiculo[]>) => res.body ?? []))
      .pipe(
        map((tipoVeiculos: ITipoVeiculo[]) =>
          this.tipoVeiculoService.addTipoVeiculoToCollectionIfMissing<ITipoVeiculo>(tipoVeiculos, this.solicitacaoColeta?.tipoVeiculo),
        ),
      )
      .subscribe((tipoVeiculos: ITipoVeiculo[]) => (this.tipoVeiculosSharedCollection = tipoVeiculos));
  }
}
