import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IStatusColeta } from '../status-coleta.model';
import { StatusColetaService } from '../service/status-coleta.service';
import { StatusColetaFormService, StatusColetaFormGroup } from './status-coleta-form.service';

@Component({
  standalone: true,
  selector: 'jhi-status-coleta-update',
  templateUrl: './status-coleta-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StatusColetaUpdateComponent implements OnInit {
  isSaving = false;
  statusColeta: IStatusColeta | null = null;

  statusColetasSharedCollection: IStatusColeta[] = [];

  editForm: StatusColetaFormGroup = this.statusColetaFormService.createStatusColetaFormGroup();

  constructor(
    protected statusColetaService: StatusColetaService,
    protected statusColetaFormService: StatusColetaFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareStatusColeta = (o1: IStatusColeta | null, o2: IStatusColeta | null): boolean =>
    this.statusColetaService.compareStatusColeta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statusColeta }) => {
      this.statusColeta = statusColeta;
      if (statusColeta) {
        this.updateForm(statusColeta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const statusColeta = this.statusColetaFormService.getStatusColeta(this.editForm);
    if (statusColeta.id !== null) {
      this.subscribeToSaveResponse(this.statusColetaService.update(statusColeta));
    } else {
      this.subscribeToSaveResponse(this.statusColetaService.create(statusColeta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStatusColeta>>): void {
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

  protected updateForm(statusColeta: IStatusColeta): void {
    this.statusColeta = statusColeta;
    this.statusColetaFormService.resetForm(this.editForm, statusColeta);

    this.statusColetasSharedCollection = this.statusColetaService.addStatusColetaToCollectionIfMissing<IStatusColeta>(
      this.statusColetasSharedCollection,
      ...(statusColeta.statusColetaOrigems ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.statusColetaService
      .query()
      .pipe(map((res: HttpResponse<IStatusColeta[]>) => res.body ?? []))
      .pipe(
        map((statusColetas: IStatusColeta[]) =>
          this.statusColetaService.addStatusColetaToCollectionIfMissing<IStatusColeta>(
            statusColetas,
            ...(this.statusColeta?.statusColetaOrigems ?? []),
          ),
        ),
      )
      .subscribe((statusColetas: IStatusColeta[]) => (this.statusColetasSharedCollection = statusColetas));
  }
}
