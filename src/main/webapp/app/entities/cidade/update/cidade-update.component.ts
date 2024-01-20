import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEstado } from 'app/entities/estado/estado.model';
import { EstadoService } from 'app/entities/estado/service/estado.service';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { EmbarcadorService } from 'app/entities/embarcador/service/embarcador.service';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { CidadeService } from '../service/cidade.service';
import { ICidade } from '../cidade.model';
import { CidadeFormService, CidadeFormGroup } from './cidade-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cidade-update',
  templateUrl: './cidade-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CidadeUpdateComponent implements OnInit {
  isSaving = false;
  cidade: ICidade | null = null;

  estadosSharedCollection: IEstado[] = [];
  embarcadorsSharedCollection: IEmbarcador[] = [];
  transportadorasSharedCollection: ITransportadora[] = [];

  editForm: CidadeFormGroup = this.cidadeFormService.createCidadeFormGroup();

  constructor(
    protected cidadeService: CidadeService,
    protected cidadeFormService: CidadeFormService,
    protected estadoService: EstadoService,
    protected embarcadorService: EmbarcadorService,
    protected transportadoraService: TransportadoraService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEstado = (o1: IEstado | null, o2: IEstado | null): boolean => this.estadoService.compareEstado(o1, o2);

  compareEmbarcador = (o1: IEmbarcador | null, o2: IEmbarcador | null): boolean => this.embarcadorService.compareEmbarcador(o1, o2);

  compareTransportadora = (o1: ITransportadora | null, o2: ITransportadora | null): boolean =>
    this.transportadoraService.compareTransportadora(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cidade }) => {
      this.cidade = cidade;
      if (cidade) {
        this.updateForm(cidade);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cidade = this.cidadeFormService.getCidade(this.editForm);
    if (cidade.id !== null) {
      this.subscribeToSaveResponse(this.cidadeService.update(cidade));
    } else {
      this.subscribeToSaveResponse(this.cidadeService.create(cidade));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICidade>>): void {
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

  protected updateForm(cidade: ICidade): void {
    this.cidade = cidade;
    this.cidadeFormService.resetForm(this.editForm, cidade);

    this.estadosSharedCollection = this.estadoService.addEstadoToCollectionIfMissing<IEstado>(this.estadosSharedCollection, cidade.estado);
    this.embarcadorsSharedCollection = this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(
      this.embarcadorsSharedCollection,
      cidade.embarcador,
    );
    this.transportadorasSharedCollection = this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(
      this.transportadorasSharedCollection,
      cidade.transportadora,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.estadoService
      .query()
      .pipe(map((res: HttpResponse<IEstado[]>) => res.body ?? []))
      .pipe(map((estados: IEstado[]) => this.estadoService.addEstadoToCollectionIfMissing<IEstado>(estados, this.cidade?.estado)))
      .subscribe((estados: IEstado[]) => (this.estadosSharedCollection = estados));

    this.embarcadorService
      .query()
      .pipe(map((res: HttpResponse<IEmbarcador[]>) => res.body ?? []))
      .pipe(
        map((embarcadors: IEmbarcador[]) =>
          this.embarcadorService.addEmbarcadorToCollectionIfMissing<IEmbarcador>(embarcadors, this.cidade?.embarcador),
        ),
      )
      .subscribe((embarcadors: IEmbarcador[]) => (this.embarcadorsSharedCollection = embarcadors));

    this.transportadoraService
      .query()
      .pipe(map((res: HttpResponse<ITransportadora[]>) => res.body ?? []))
      .pipe(
        map((transportadoras: ITransportadora[]) =>
          this.transportadoraService.addTransportadoraToCollectionIfMissing<ITransportadora>(transportadoras, this.cidade?.transportadora),
        ),
      )
      .subscribe((transportadoras: ITransportadora[]) => (this.transportadorasSharedCollection = transportadoras));
  }
}
