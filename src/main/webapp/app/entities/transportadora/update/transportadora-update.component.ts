import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICidade } from 'app/entities/cidade/cidade.model';
import { CidadeService } from 'app/entities/cidade/service/cidade.service';
import { ITransportadora } from '../transportadora.model';
import { TransportadoraService } from '../service/transportadora.service';
import { TransportadoraFormGroup, TransportadoraFormService } from './transportadora-form.service';
import { EstadoService } from '../../estado/service/estado.service';
import { IEstado } from '../../estado/estado.model';
import { EnderecoUpdateComponent } from '../../endereco/update/endereco-update.component';
import { EnderecoComponent } from '../../endereco/list/endereco.component';
import { TipoEndereco } from '../../enumerations/tipo-endereco.model';
import { ContaBancariaUpdateComponent } from '../../conta-bancaria/update/conta-bancaria-update.component';
import { TabelaFreteUpdateComponent } from '../../tabela-frete/update/tabela-frete-update.component';
import { IEndereco } from '../../endereco/endereco.model';
import { TransportadoraCommunicationService } from '../service/transportadora-communication-service';

@Component({
  standalone: true,
  selector: 'jhi-transportadora-update',
  templateUrl: './transportadora-update.component.html',
  styleUrl: './transportadora-update.component.scss',
  imports: [
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    EnderecoUpdateComponent,
    EnderecoComponent,
    ContaBancariaUpdateComponent,
    TabelaFreteUpdateComponent,
  ],
})
export class TransportadoraUpdateComponent implements OnInit, OnDestroy {
  isSaving = false;
  transportadora: ITransportadora | null = null;
  enderecos: IEndereco[] = [];

  cidadesSharedCollection: ICidade[] = [];
  estadosSharedCollection: IEstado[] = [];

  editTransportadoraForm: TransportadoraFormGroup = this.transportadoraFormService.createTransportadoraFormGroup();

  private enderecoSubscription?: Subscription;

  constructor(
    protected transportadoraService: TransportadoraService,
    protected transportadoraFormService: TransportadoraFormService,
    protected transportadoraCommunicationService: TransportadoraCommunicationService,
    protected estadoService: EstadoService,
    protected cidadeService: CidadeService,
    protected activatedRoute: ActivatedRoute,
  ) {
    this.enderecoSubscription = this.transportadoraCommunicationService.enderecosUpdated$.subscribe((enderecos: IEndereco[]) => {
      if (enderecos.length > 0) {
        this.enderecos = enderecos;
      }
    });
  }

  compareCidade = (o1: ICidade | null, o2: ICidade | null): boolean => this.cidadeService.compareCidade(o1, o2);

  compareEstado = (o1: IEstado | null, o2: IEstado | null): boolean => this.estadoService.compareEstado(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transportadora }) => {
      this.transportadora = transportadora;
      if (transportadora) {
        this.updateForm(transportadora);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    let transportadora = this.transportadoraFormService.getTransportadora(this.editTransportadoraForm);
    transportadora.enderecos = this.enderecos;
    console.log(transportadora);
    // if (transportadora.id !== null) {
    //   this.subscribeToSaveResponse(this.transportadoraService.update(transportadora));
    // } else {
    //   this.subscribeToSaveResponse(this.transportadoraService.create(transportadora));
    // }
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
    this.transportadoraFormService.resetForm(this.editTransportadoraForm, transportadora);

    this.cidadesSharedCollection = this.cidadeService.addCidadeToCollectionIfMissing<ICidade>(
      this.cidadesSharedCollection,
      transportadora.cidade,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.estadoService
      .query()
      .pipe(map((res: HttpResponse<IEstado[]>) => res.body ?? []))
      .pipe(map((estados: IEstado[]) => this.estadoService.addEstadoToCollectionIfMissing<IEstado>(estados, this.transportadora?.estado)))
      .subscribe((estados: IEstado[]) => (this.estadosSharedCollection = estados));
  }

  buscarCep() {}

  loadCidades() {
    this.cidadeService
      .query({ 'estadoId.equals': this.editTransportadoraForm.get('estado')?.value ?? 0 })
      .pipe(map((res: HttpResponse<ICidade[]>) => res.body ?? []))
      .pipe(map((cidades: ICidade[]) => this.cidadeService.addCidadeToCollectionIfMissing<ICidade>(cidades, this.transportadora?.cidade)))
      .subscribe((cidades: ICidade[]) => (this.cidadesSharedCollection = cidades));
  }

  protected readonly TipoEndereco = TipoEndereco;

  ngOnDestroy(): void {
    if (this.enderecoSubscription) {
      this.enderecoSubscription.unsubscribe();
    }
  }
}
