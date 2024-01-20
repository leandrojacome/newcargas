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
import { TipoNotificacao } from 'app/entities/enumerations/tipo-notificacao.model';
import { NotificacaoService } from '../service/notificacao.service';
import { INotificacao } from '../notificacao.model';
import { NotificacaoFormService, NotificacaoFormGroup } from './notificacao-form.service';

@Component({
  standalone: true,
  selector: 'jhi-notificacao-update',
  templateUrl: './notificacao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NotificacaoUpdateComponent implements OnInit {
  isSaving = false;
  notificacao: INotificacao | null = null;
  tipoNotificacaoValues = Object.keys(TipoNotificacao);

  embarcadorsSharedCollection: IEmbarcador[] = [];
  transportadorasSharedCollection: ITransportadora[] = [];

  editForm: NotificacaoFormGroup = this.notificacaoFormService.createNotificacaoFormGroup();

  constructor(
    protected notificacaoService: NotificacaoService,
    protected notificacaoFormService: NotificacaoFormService,
    protected embarcadorService: EmbarcadorService,
    protected transportadoraService: TransportadoraService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEmbarcador = (o1: IEmbarcador | null, o2: IEmbarcador | null): boolean => this.embarcadorService.compareEmbarcador(o1, o2);

  compareTransportadora = (o1: ITransportadora | null, o2: ITransportadora | null): boolean =>
    this.transportadoraService.compareTransportadora(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificacao }) => {
      this.notificacao = notificacao;
      if (notificacao) {
        this.updateForm(notificacao);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notificacao = this.notificacaoFormService.getNotificacao(this.editForm);
    if (notificacao.id !== null) {
      this.subscribeToSaveResponse(this.notificacaoService.update(notificacao));
    } else {
      this.subscribeToSaveResponse(this.notificacaoService.create(notificacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificacao>>): void {
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

  protected updateForm(notificacao: INotificacao): void {
    this.notificacao = notificacao;
    this.notificacaoFormService.resetForm(this.editForm, notificacao);

    this.embarcadorsSharedCollection = this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(
      this.embarcadorsSharedCollection,
      notificacao.embarcador,
    );
    this.transportadorasSharedCollection = this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
      this.transportadorasSharedCollection,
      notificacao.transportadora,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.embarcadorService
      .query()
      .pipe(map((res: HttpResponse<IEmbarcador[]>) => res.body ?? []))
      .pipe(
        map((embarcadors: IEmbarcador[]) =>
          this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(embarcadors, this.notificacao?.embarcador),
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
            this.notificacao?.transportadora,
          ),
        ),
      )
      .subscribe((transportadoras: ITransportadora[]) => (this.transportadorasSharedCollection = transportadoras));
  }
}
