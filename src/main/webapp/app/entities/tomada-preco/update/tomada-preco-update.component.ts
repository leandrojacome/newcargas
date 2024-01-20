import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContratacao } from 'app/entities/contratacao/contratacao.model';
import { ContratacaoService } from 'app/entities/contratacao/service/contratacao.service';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';
import { RoteirizacaoService } from 'app/entities/roteirizacao/service/roteirizacao.service';
import { TomadaPrecoService } from '../service/tomada-preco.service';
import { ITomadaPreco } from '../tomada-preco.model';
import { TomadaPrecoFormService, TomadaPrecoFormGroup } from './tomada-preco-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tomada-preco-update',
  templateUrl: './tomada-preco-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TomadaPrecoUpdateComponent implements OnInit {
  isSaving = false;
  tomadaPreco: ITomadaPreco | null = null;

  contratacaosCollection: IContratacao[] = [];
  transportadorasSharedCollection: ITransportadora[] = [];
  roteirizacaosSharedCollection: IRoteirizacao[] = [];

  editForm: TomadaPrecoFormGroup = this.tomadaPrecoFormService.createTomadaPrecoFormGroup();

  constructor(
    protected tomadaPrecoService: TomadaPrecoService,
    protected tomadaPrecoFormService: TomadaPrecoFormService,
    protected contratacaoService: ContratacaoService,
    protected transportadoraService: TransportadoraService,
    protected roteirizacaoService: RoteirizacaoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareContratacao = (o1: IContratacao | null, o2: IContratacao | null): boolean => this.contratacaoService.compareContratacao(o1, o2);

  compareTransportadora = (o1: ITransportadora | null, o2: ITransportadora | null): boolean =>
    this.transportadoraService.compareTransportadora(o1, o2);

  compareRoteirizacao = (o1: IRoteirizacao | null, o2: IRoteirizacao | null): boolean =>
    this.roteirizacaoService.compareRoteirizacao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tomadaPreco }) => {
      this.tomadaPreco = tomadaPreco;
      if (tomadaPreco) {
        this.updateForm(tomadaPreco);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tomadaPreco = this.tomadaPrecoFormService.getTomadaPreco(this.editForm);
    if (tomadaPreco.id !== null) {
      this.subscribeToSaveResponse(this.tomadaPrecoService.update(tomadaPreco));
    } else {
      this.subscribeToSaveResponse(this.tomadaPrecoService.create(tomadaPreco));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITomadaPreco>>): void {
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

  protected updateForm(tomadaPreco: ITomadaPreco): void {
    this.tomadaPreco = tomadaPreco;
    this.tomadaPrecoFormService.resetForm(this.editForm, tomadaPreco);

    this.contratacaosCollection = this.contratacaoService.addContratacaoToCollectionIfMissing<IContratacao>(
      this.contratacaosCollection,
      tomadaPreco.contratacao,
    );
    this.transportadorasSharedCollection = this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
      this.transportadorasSharedCollection,
      tomadaPreco.transportadora,
    );
    this.roteirizacaosSharedCollection = this.roteirizacaoService.addRoteirizacaoToCollectionIfMissing<IRoteirizacao>(
      this.roteirizacaosSharedCollection,
      tomadaPreco.roteirizacao,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contratacaoService
      .query({ filter: 'solicitacaocoleta-is-null' })
      .pipe(map((res: HttpResponse<IContratacao[]>) => res.body ?? []))
      .pipe(
        map((contratacaos: IContratacao[]) =>
          this.contratacaoService.addContratacaoToCollectionIfMissing<IContratacao>(contratacaos, this.tomadaPreco?.contratacao),
        ),
      )
      .subscribe((contratacaos: IContratacao[]) => (this.contratacaosCollection = contratacaos));

    this.transportadoraService
      .query()
      .pipe(map((res: HttpResponse<ITransportadora[]>) => res.body ?? []))
      .pipe(
        map((transportadoras: ITransportadora[]) =>
          this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
            transportadoras,
            this.tomadaPreco?.transportadora,
          ),
        ),
      )
      .subscribe((transportadoras: ITransportadora[]) => (this.transportadorasSharedCollection = transportadoras));

    this.roteirizacaoService
      .query()
      .pipe(map((res: HttpResponse<IRoteirizacao[]>) => res.body ?? []))
      .pipe(
        map((roteirizacaos: IRoteirizacao[]) =>
          this.roteirizacaoService.addRoteirizacaoToCollectionIfMissing<IRoteirizacao>(roteirizacaos, this.tomadaPreco?.roteirizacao),
        ),
      )
      .subscribe((roteirizacaos: IRoteirizacao[]) => (this.roteirizacaosSharedCollection = roteirizacaos));
  }
}
