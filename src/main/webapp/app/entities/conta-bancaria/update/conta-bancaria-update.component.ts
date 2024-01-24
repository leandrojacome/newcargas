import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBanco } from 'app/entities/banco/banco.model';
import { BancoService } from 'app/entities/banco/service/banco.service';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { EmbarcadorService } from 'app/entities/embarcador/service/embarcador.service';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { ContaBancariaService } from '../service/conta-bancaria.service';
import { IContaBancaria } from '../conta-bancaria.model';
import { ContaBancariaFormService, ContaBancariaFormGroup } from './conta-bancaria-form.service';

@Component({
  standalone: true,
  selector: 'jhi-conta-bancaria-update',
  templateUrl: './conta-bancaria-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContaBancariaUpdateComponent implements OnInit {
  isSaving = false;
  contaBancaria: IContaBancaria | null = null;

  contasBancarias: IContaBancaria[] = [];

  bancosSharedCollection: IBanco[] = [];

  editContaBancariaForm: ContaBancariaFormGroup = this.contaBancariaFormService.createContaBancariaFormGroup();

  constructor(
    protected contaBancariaService: ContaBancariaService,
    protected contaBancariaFormService: ContaBancariaFormService,
    protected bancoService: BancoService,
    protected embarcadorService: EmbarcadorService,
    protected transportadoraService: TransportadoraService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareBanco = (o1: IBanco | null, o2: IBanco | null): boolean => this.bancoService.compareBanco(o1, o2);

  compareEmbarcador = (o1: IEmbarcador | null, o2: IEmbarcador | null): boolean => this.embarcadorService.compareEmbarcador(o1, o2);

  compareTransportadora = (o1: ITransportadora | null, o2: ITransportadora | null): boolean =>
    this.transportadoraService.compareTransportadora(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contaBancaria }) => {
      this.contaBancaria = contaBancaria;
      if (contaBancaria) {
        this.updateForm(contaBancaria);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contaBancaria = this.contaBancariaFormService.getContaBancaria(this.editContaBancariaForm);
    if (contaBancaria.id !== null) {
      this.subscribeToSaveResponse(this.contaBancariaService.update(contaBancaria));
    } else {
      this.subscribeToSaveResponse(this.contaBancariaService.create(contaBancaria));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContaBancaria>>): void {
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

  protected updateForm(contaBancaria: IContaBancaria): void {
    this.contaBancaria = contaBancaria;
    this.contaBancariaFormService.resetForm(this.editContaBancariaForm, contaBancaria);

    this.bancosSharedCollection = this.bancoService.addBancoToCollectionIfMissing<IBanco>(this.bancosSharedCollection, contaBancaria.banco);
  }

  protected loadRelationshipsOptions(): void {
    this.bancoService
      .query()
      .pipe(map((res: HttpResponse<IBanco[]>) => res.body ?? []))
      .pipe(map((bancos: IBanco[]) => this.bancoService.addBancoToCollectionIfMissing<IBanco>(bancos, this.contaBancaria?.banco)))
      .subscribe((bancos: IBanco[]) => (this.bancosSharedCollection = bancos));
  }

  addContaBancaria() {}

  edit(contaBancaria: IContaBancaria) {
    this.updateForm(contaBancaria);
  }

  delete(contaBancaria: IContaBancaria) {
    this.contasBancarias.slice(this.contasBancarias.indexOf(contaBancaria), 1);
  }
}
