import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { SolicitacaoColetaService } from 'app/entities/solicitacao-coleta/service/solicitacao-coleta.service';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';
import { RoteirizacaoService } from 'app/entities/roteirizacao/service/roteirizacao.service';
import { IStatusColeta } from 'app/entities/status-coleta/status-coleta.model';
import { StatusColetaService } from 'app/entities/status-coleta/service/status-coleta.service';
import { HistoricoStatusColetaService } from '../service/historico-status-coleta.service';
import { IHistoricoStatusColeta } from '../historico-status-coleta.model';
import { HistoricoStatusColetaFormService, HistoricoStatusColetaFormGroup } from './historico-status-coleta-form.service';

@Component({
  standalone: true,
  selector: 'jhi-historico-status-coleta-update',
  templateUrl: './historico-status-coleta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HistoricoStatusColetaUpdateComponent implements OnInit {
  isSaving = false;
  historicoStatusColeta: IHistoricoStatusColeta | null = null;

  solicitacaoColetasSharedCollection: ISolicitacaoColeta[] = [];
  roteirizacaosSharedCollection: IRoteirizacao[] = [];
  statusColetasSharedCollection: IStatusColeta[] = [];

  editForm: HistoricoStatusColetaFormGroup = this.historicoStatusColetaFormService.createHistoricoStatusColetaFormGroup();

  constructor(
    protected historicoStatusColetaService: HistoricoStatusColetaService,
    protected historicoStatusColetaFormService: HistoricoStatusColetaFormService,
    protected solicitacaoColetaService: SolicitacaoColetaService,
    protected roteirizacaoService: RoteirizacaoService,
    protected statusColetaService: StatusColetaService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSolicitacaoColeta = (o1: ISolicitacaoColeta | null, o2: ISolicitacaoColeta | null): boolean =>
    this.solicitacaoColetaService.compareSolicitacaoColeta(o1, o2);

  compareRoteirizacao = (o1: IRoteirizacao | null, o2: IRoteirizacao | null): boolean =>
    this.roteirizacaoService.compareRoteirizacao(o1, o2);

  compareStatusColeta = (o1: IStatusColeta | null, o2: IStatusColeta | null): boolean =>
    this.statusColetaService.compareStatusColeta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historicoStatusColeta }) => {
      this.historicoStatusColeta = historicoStatusColeta;
      if (historicoStatusColeta) {
        this.updateForm(historicoStatusColeta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const historicoStatusColeta = this.historicoStatusColetaFormService.getHistoricoStatusColeta(this.editForm);
    if (historicoStatusColeta.id !== null) {
      this.subscribeToSaveResponse(this.historicoStatusColetaService.update(historicoStatusColeta));
    } else {
      this.subscribeToSaveResponse(this.historicoStatusColetaService.create(historicoStatusColeta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistoricoStatusColeta>>): void {
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

  protected updateForm(historicoStatusColeta: IHistoricoStatusColeta): void {
    this.historicoStatusColeta = historicoStatusColeta;
    this.historicoStatusColetaFormService.resetForm(this.editForm, historicoStatusColeta);

    this.solicitacaoColetasSharedCollection = this.solicitacaoColetaService.addSolicitacaoColetaToCollectionIfMissing<ISolicitacaoColeta>(
      this.solicitacaoColetasSharedCollection,
      historicoStatusColeta.solicitacaoColeta,
    );
    this.roteirizacaosSharedCollection = this.roteirizacaoService.addRoteirizacaoToCollectionIfMissing<IRoteirizacao>(
      this.roteirizacaosSharedCollection,
      historicoStatusColeta.roteirizacao,
    );
    this.statusColetasSharedCollection = this.statusColetaService.addStatusColetaToCollectionIfMissing<IStatusColeta>(
      this.statusColetasSharedCollection,
      historicoStatusColeta.statusColetaOrigem,
      historicoStatusColeta.statusColetaDestino,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.solicitacaoColetaService
      .query()
      .pipe(map((res: HttpResponse<ISolicitacaoColeta[]>) => res.body ?? []))
      .pipe(
        map((solicitacaoColetas: ISolicitacaoColeta[]) =>
          this.solicitacaoColetaService.addSolicitacaoColetaToCollectionIfMissing<ISolicitacaoColeta>(
            solicitacaoColetas,
            this.historicoStatusColeta?.solicitacaoColeta,
          ),
        ),
      )
      .subscribe((solicitacaoColetas: ISolicitacaoColeta[]) => (this.solicitacaoColetasSharedCollection = solicitacaoColetas));

    this.roteirizacaoService
      .query()
      .pipe(map((res: HttpResponse<IRoteirizacao[]>) => res.body ?? []))
      .pipe(
        map((roteirizacaos: IRoteirizacao[]) =>
          this.roteirizacaoService.addRoteirizacaoToCollectionIfMissing<IRoteirizacao>(
            roteirizacaos,
            this.historicoStatusColeta?.roteirizacao,
          ),
        ),
      )
      .subscribe((roteirizacaos: IRoteirizacao[]) => (this.roteirizacaosSharedCollection = roteirizacaos));

    this.statusColetaService
      .query()
      .pipe(map((res: HttpResponse<IStatusColeta[]>) => res.body ?? []))
      .pipe(
        map((statusColetas: IStatusColeta[]) =>
          this.statusColetaService.addStatusColetaToCollectionIfMissing<IStatusColeta>(
            statusColetas,
            this.historicoStatusColeta?.statusColetaOrigem,
            this.historicoStatusColeta?.statusColetaDestino,
          ),
        ),
      )
      .subscribe((statusColetas: IStatusColeta[]) => (this.statusColetasSharedCollection = statusColetas));
  }
}
