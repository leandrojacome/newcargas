import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEstado } from 'app/entities/estado/estado.model';
import { EstadoService } from 'app/entities/estado/service/estado.service';
import { ICidade } from '../cidade.model';
import { CidadeService } from '../service/cidade.service';
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

  editForm: CidadeFormGroup = this.cidadeFormService.createCidadeFormGroup();

  constructor(
    protected cidadeService: CidadeService,
    protected cidadeFormService: CidadeFormService,
    protected estadoService: EstadoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEstado = (o1: IEstado | null, o2: IEstado | null): boolean => this.estadoService.compareEstado(o1, o2);

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
  }

  protected loadRelationshipsOptions(): void {
    this.estadoService
      .query()
      .pipe(map((res: HttpResponse<IEstado[]>) => res.body ?? []))
      .pipe(map((estados: IEstado[]) => this.estadoService.addEstadoToCollectionIfMissing<IEstado>(estados, this.cidade?.estado)))
      .subscribe((estados: IEstado[]) => (this.estadosSharedCollection = estados));
  }
}
