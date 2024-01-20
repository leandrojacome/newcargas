import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICidade } from 'app/entities/cidade/cidade.model';
import { CidadeService } from 'app/entities/cidade/service/cidade.service';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { EmbarcadorService } from 'app/entities/embarcador/service/embarcador.service';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { INotaFiscalColeta } from 'app/entities/nota-fiscal-coleta/nota-fiscal-coleta.model';
import { NotaFiscalColetaService } from 'app/entities/nota-fiscal-coleta/service/nota-fiscal-coleta.service';
import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { SolicitacaoColetaService } from 'app/entities/solicitacao-coleta/service/solicitacao-coleta.service';
import { TipoEndereco } from 'app/entities/enumerations/tipo-endereco.model';
import { EnderecoService } from '../service/endereco.service';
import { IEndereco } from '../endereco.model';
import { EnderecoFormService, EnderecoFormGroup } from './endereco-form.service';

@Component({
  standalone: true,
  selector: 'jhi-endereco-update',
  templateUrl: './endereco-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnderecoUpdateComponent implements OnInit {
  isSaving = false;
  endereco: IEndereco | null = null;
  tipoEnderecoValues = Object.keys(TipoEndereco);

  cidadesSharedCollection: ICidade[] = [];
  embarcadorsSharedCollection: IEmbarcador[] = [];
  transportadorasSharedCollection: ITransportadora[] = [];
  notaFiscalColetasSharedCollection: INotaFiscalColeta[] = [];
  solicitacaoColetasSharedCollection: ISolicitacaoColeta[] = [];

  editForm: EnderecoFormGroup = this.enderecoFormService.createEnderecoFormGroup();

  constructor(
    protected enderecoService: EnderecoService,
    protected enderecoFormService: EnderecoFormService,
    protected cidadeService: CidadeService,
    protected embarcadorService: EmbarcadorService,
    protected transportadoraService: TransportadoraService,
    protected notaFiscalColetaService: NotaFiscalColetaService,
    protected solicitacaoColetaService: SolicitacaoColetaService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCidade = (o1: ICidade | null, o2: ICidade | null): boolean => this.cidadeService.compareCidade(o1, o2);

  compareEmbarcador = (o1: IEmbarcador | null, o2: IEmbarcador | null): boolean => this.embarcadorService.compareEmbarcador(o1, o2);

  compareTransportadora = (o1: ITransportadora | null, o2: ITransportadora | null): boolean =>
    this.transportadoraService.compareTransportadora(o1, o2);

  compareNotaFiscalColeta = (o1: INotaFiscalColeta | null, o2: INotaFiscalColeta | null): boolean =>
    this.notaFiscalColetaService.compareNotaFiscalColeta(o1, o2);

  compareSolicitacaoColeta = (o1: ISolicitacaoColeta | null, o2: ISolicitacaoColeta | null): boolean =>
    this.solicitacaoColetaService.compareSolicitacaoColeta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ endereco }) => {
      this.endereco = endereco;
      if (endereco) {
        this.updateForm(endereco);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const endereco = this.enderecoFormService.getEndereco(this.editForm);
    if (endereco.id !== null) {
      this.subscribeToSaveResponse(this.enderecoService.update(endereco));
    } else {
      this.subscribeToSaveResponse(this.enderecoService.create(endereco));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEndereco>>): void {
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

  protected updateForm(endereco: IEndereco): void {
    this.endereco = endereco;
    this.enderecoFormService.resetForm(this.editForm, endereco);

    this.cidadesSharedCollection = this.cidadeService.addCidadeToCollectionIfMissing<ICidade>(
      this.cidadesSharedCollection,
      endereco.cidade,
    );
    this.embarcadorsSharedCollection = this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(
      this.embarcadorsSharedCollection,
      endereco.embarcador,
    );
    this.transportadorasSharedCollection = this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
      this.transportadorasSharedCollection,
      endereco.transportadora,
    );
    this.notaFiscalColetasSharedCollection = this.notaFiscalColetaService.addNotaFiscalColetaToCollectionIfMissing<INotaFiscalColeta>(
      this.notaFiscalColetasSharedCollection,
      endereco.notaFiscalColetaOrigem,
      endereco.notaFiscalColetaDestino,
    );
    this.solicitacaoColetasSharedCollection = this.solicitacaoColetaService.addSolicitacaoColetaToCollectionIfMissing<ISolicitacaoColeta>(
      this.solicitacaoColetasSharedCollection,
      endereco.solicitacaoColetaOrigem,
      endereco.solicitacaoColetaDestino,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cidadeService
      .query()
      .pipe(map((res: HttpResponse<ICidade[]>) => res.body ?? []))
      .pipe(map((cidades: ICidade[]) => this.cidadeService.addCidadeToCollectionIfMissing<ICidade>(cidades, this.endereco?.cidade)))
      .subscribe((cidades: ICidade[]) => (this.cidadesSharedCollection = cidades));

    this.embarcadorService
      .query()
      .pipe(map((res: HttpResponse<IEmbarcador[]>) => res.body ?? []))
      .pipe(
        map((embarcadors: IEmbarcador[]) =>
          this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(embarcadors, this.endereco?.embarcador),
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
            this.endereco?.transportadora,
          ),
        ),
      )
      .subscribe((transportadoras: ITransportadora[]) => (this.transportadorasSharedCollection = transportadoras));

    this.notaFiscalColetaService
      .query()
      .pipe(map((res: HttpResponse<INotaFiscalColeta[]>) => res.body ?? []))
      .pipe(
        map((notaFiscalColetas: INotaFiscalColeta[]) =>
          this.notaFiscalColetaService.addNotaFiscalColetaToCollectionIfMissing<INotaFiscalColeta>(
            notaFiscalColetas,
            this.endereco?.notaFiscalColetaOrigem,
            this.endereco?.notaFiscalColetaDestino,
          ),
        ),
      )
      .subscribe((notaFiscalColetas: INotaFiscalColeta[]) => (this.notaFiscalColetasSharedCollection = notaFiscalColetas));

    this.solicitacaoColetaService
      .query()
      .pipe(map((res: HttpResponse<ISolicitacaoColeta[]>) => res.body ?? []))
      .pipe(
        map((solicitacaoColetas: ISolicitacaoColeta[]) =>
          this.solicitacaoColetaService.addSolicitacaoColetaToCollectionIfMissing<ISolicitacaoColeta>(
            solicitacaoColetas,
            this.endereco?.solicitacaoColetaOrigem,
            this.endereco?.solicitacaoColetaDestino,
          ),
        ),
      )
      .subscribe((solicitacaoColetas: ISolicitacaoColeta[]) => (this.solicitacaoColetasSharedCollection = solicitacaoColetas));
  }
}
