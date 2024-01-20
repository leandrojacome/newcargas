import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRegiao } from '../regiao.model';
import { RegiaoService } from '../service/regiao.service';
import { RegiaoFormService, RegiaoFormGroup } from './regiao-form.service';

@Component({
  standalone: true,
  selector: 'jhi-regiao-update',
  templateUrl: './regiao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RegiaoUpdateComponent implements OnInit {
  isSaving = false;
  regiao: IRegiao | null = null;

  editForm: RegiaoFormGroup = this.regiaoFormService.createRegiaoFormGroup();

  constructor(
    protected regiaoService: RegiaoService,
    protected regiaoFormService: RegiaoFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ regiao }) => {
      this.regiao = regiao;
      if (regiao) {
        this.updateForm(regiao);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const regiao = this.regiaoFormService.getRegiao(this.editForm);
    if (regiao.id !== null) {
      this.subscribeToSaveResponse(this.regiaoService.update(regiao));
    } else {
      this.subscribeToSaveResponse(this.regiaoService.create(regiao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegiao>>): void {
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

  protected updateForm(regiao: IRegiao): void {
    this.regiao = regiao;
    this.regiaoFormService.resetForm(this.editForm, regiao);
  }
}
