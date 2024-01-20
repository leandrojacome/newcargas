import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { SolicitacaoColetaService } from 'app/entities/solicitacao-coleta/service/solicitacao-coleta.service';
import { INotaFiscalColeta } from '../nota-fiscal-coleta.model';
import { NotaFiscalColetaService } from '../service/nota-fiscal-coleta.service';
import { NotaFiscalColetaFormService, NotaFiscalColetaFormGroup } from './nota-fiscal-coleta-form.service';

@Component({
  standalone: true,
  selector: 'jhi-nota-fiscal-coleta-update',
  templateUrl: './nota-fiscal-coleta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NotaFiscalColetaUpdateComponent implements OnInit {
  isSaving = false;
  notaFiscalColeta: INotaFiscalColeta | null = null;

  solicitacaoColetasSharedCollection: ISolicitacaoColeta[] = [];

  editForm: NotaFiscalColetaFormGroup = this.notaFiscalColetaFormService.createNotaFiscalColetaFormGroup();

  constructor(
    protected notaFiscalColetaService: NotaFiscalColetaService,
    protected notaFiscalColetaFormService: NotaFiscalColetaFormService,
    protected solicitacaoColetaService: SolicitacaoColetaService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSolicitacaoColeta = (o1: ISolicitacaoColeta | null, o2: ISolicitacaoColeta | null): boolean =>
    this.solicitacaoColetaService.compareSolicitacaoColeta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notaFiscalColeta }) => {
      this.notaFiscalColeta = notaFiscalColeta;
      if (notaFiscalColeta) {
        this.updateForm(notaFiscalColeta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notaFiscalColeta = this.notaFiscalColetaFormService.getNotaFiscalColeta(this.editForm);
    if (notaFiscalColeta.id !== null) {
      this.subscribeToSaveResponse(this.notaFiscalColetaService.update(notaFiscalColeta));
    } else {
      this.subscribeToSaveResponse(this.notaFiscalColetaService.create(notaFiscalColeta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotaFiscalColeta>>): void {
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

  protected updateForm(notaFiscalColeta: INotaFiscalColeta): void {
    this.notaFiscalColeta = notaFiscalColeta;
    this.notaFiscalColetaFormService.resetForm(this.editForm, notaFiscalColeta);

    this.solicitacaoColetasSharedCollection = this.solicitacaoColetaService.addSolicitacaoColetaToCollectionIfMissing<ISolicitacaoColeta>(
      this.solicitacaoColetasSharedCollection,
      notaFiscalColeta.solicitacaoColeta,
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
            this.notaFiscalColeta?.solicitacaoColeta,
          ),
        ),
      )
      .subscribe((solicitacaoColetas: ISolicitacaoColeta[]) => (this.solicitacaoColetasSharedCollection = solicitacaoColetas));
  }
}
