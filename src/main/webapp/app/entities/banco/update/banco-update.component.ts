import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBanco } from '../banco.model';
import { BancoService } from '../service/banco.service';
import { BancoFormService, BancoFormGroup } from './banco-form.service';

@Component({
  standalone: true,
  selector: 'jhi-banco-update',
  templateUrl: './banco-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BancoUpdateComponent implements OnInit {
  isSaving = false;
  banco: IBanco | null = null;

  editForm: BancoFormGroup = this.bancoFormService.createBancoFormGroup();

  constructor(
    protected bancoService: BancoService,
    protected bancoFormService: BancoFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ banco }) => {
      this.banco = banco;
      if (banco) {
        this.updateForm(banco);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const banco = this.bancoFormService.getBanco(this.editForm);
    if (banco.id !== null) {
      this.subscribeToSaveResponse(this.bancoService.update(banco));
    } else {
      this.subscribeToSaveResponse(this.bancoService.create(banco));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBanco>>): void {
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

  protected updateForm(banco: IBanco): void {
    this.banco = banco;
    this.bancoFormService.resetForm(this.editForm, banco);
  }
}
