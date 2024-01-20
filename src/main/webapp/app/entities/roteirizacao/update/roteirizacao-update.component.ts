import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStatusColeta } from 'app/entities/status-coleta/status-coleta.model';
import { StatusColetaService } from 'app/entities/status-coleta/service/status-coleta.service';
import { IRoteirizacao } from '../roteirizacao.model';
import { RoteirizacaoService } from '../service/roteirizacao.service';
import { RoteirizacaoFormService, RoteirizacaoFormGroup } from './roteirizacao-form.service';

@Component({
  standalone: true,
  selector: 'jhi-roteirizacao-update',
  templateUrl: './roteirizacao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RoteirizacaoUpdateComponent implements OnInit {
  isSaving = false;
  roteirizacao: IRoteirizacao | null = null;

  statusColetasSharedCollection: IStatusColeta[] = [];

  editForm: RoteirizacaoFormGroup = this.roteirizacaoFormService.createRoteirizacaoFormGroup();

  constructor(
    protected roteirizacaoService: RoteirizacaoService,
    protected roteirizacaoFormService: RoteirizacaoFormService,
    protected statusColetaService: StatusColetaService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareStatusColeta = (o1: IStatusColeta | null, o2: IStatusColeta | null): boolean =>
    this.statusColetaService.compareStatusColeta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roteirizacao }) => {
      this.roteirizacao = roteirizacao;
      if (roteirizacao) {
        this.updateForm(roteirizacao);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roteirizacao = this.roteirizacaoFormService.getRoteirizacao(this.editForm);
    if (roteirizacao.id !== null) {
      this.subscribeToSaveResponse(this.roteirizacaoService.update(roteirizacao));
    } else {
      this.subscribeToSaveResponse(this.roteirizacaoService.create(roteirizacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoteirizacao>>): void {
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

  protected updateForm(roteirizacao: IRoteirizacao): void {
    this.roteirizacao = roteirizacao;
    this.roteirizacaoFormService.resetForm(this.editForm, roteirizacao);

    this.statusColetasSharedCollection = this.statusColetaService.addStatusColetaToCollectionIfMissing<IStatusColeta>(
      this.statusColetasSharedCollection,
      roteirizacao.statusColeta,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.statusColetaService
      .query()
      .pipe(map((res: HttpResponse<IStatusColeta[]>) => res.body ?? []))
      .pipe(
        map((statusColetas: IStatusColeta[]) =>
          this.statusColetaService.addStatusColetaToCollectionIfMissing<IStatusColeta>(statusColetas, this.roteirizacao?.statusColeta),
        ),
      )
      .subscribe((statusColetas: IStatusColeta[]) => (this.statusColetasSharedCollection = statusColetas));
  }
}
