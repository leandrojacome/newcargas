import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContratacao, NewContratacao } from '../contratacao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContratacao for edit and NewContratacaoFormGroupInput for create.
 */
type ContratacaoFormGroupInput = IContratacao | PartialWithRequiredKeyOf<NewContratacao>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContratacao | NewContratacao> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ContratacaoFormRawValue = FormValueOf<IContratacao>;

type NewContratacaoFormRawValue = FormValueOf<NewContratacao>;

type ContratacaoFormDefaults = Pick<NewContratacao, 'id' | 'cancelado' | 'removido' | 'createdDate' | 'lastModifiedDate'>;

type ContratacaoFormGroupContent = {
  id: FormControl<ContratacaoFormRawValue['id'] | NewContratacao['id']>;
  valorTotal: FormControl<ContratacaoFormRawValue['valorTotal']>;
  validadeEmDias: FormControl<ContratacaoFormRawValue['validadeEmDias']>;
  dataValidade: FormControl<ContratacaoFormRawValue['dataValidade']>;
  observacao: FormControl<ContratacaoFormRawValue['observacao']>;
  cancelado: FormControl<ContratacaoFormRawValue['cancelado']>;
  removido: FormControl<ContratacaoFormRawValue['removido']>;
  createdBy: FormControl<ContratacaoFormRawValue['createdBy']>;
  createdDate: FormControl<ContratacaoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ContratacaoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ContratacaoFormRawValue['lastModifiedDate']>;
  transportadora: FormControl<ContratacaoFormRawValue['transportadora']>;
};

export type ContratacaoFormGroup = FormGroup<ContratacaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContratacaoFormService {
  createContratacaoFormGroup(contratacao: ContratacaoFormGroupInput = { id: null }): ContratacaoFormGroup {
    const contratacaoRawValue = this.convertContratacaoToContratacaoRawValue({
      ...this.getFormDefaults(),
      ...contratacao,
    });
    return new FormGroup<ContratacaoFormGroupContent>({
      id: new FormControl(
        { value: contratacaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      valorTotal: new FormControl(contratacaoRawValue.valorTotal, {
        validators: [Validators.required, Validators.min(1), Validators.max(10)],
      }),
      validadeEmDias: new FormControl(contratacaoRawValue.validadeEmDias, {
        validators: [Validators.required, Validators.min(1), Validators.max(4)],
      }),
      dataValidade: new FormControl(contratacaoRawValue.dataValidade, {
        validators: [Validators.required],
      }),
      observacao: new FormControl(contratacaoRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      cancelado: new FormControl(contratacaoRawValue.cancelado),
      removido: new FormControl(contratacaoRawValue.removido),
      createdBy: new FormControl(contratacaoRawValue.createdBy),
      createdDate: new FormControl(contratacaoRawValue.createdDate),
      lastModifiedBy: new FormControl(contratacaoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(contratacaoRawValue.lastModifiedDate),
      transportadora: new FormControl(contratacaoRawValue.transportadora),
    });
  }

  getContratacao(form: ContratacaoFormGroup): IContratacao | NewContratacao {
    return this.convertContratacaoRawValueToContratacao(form.getRawValue() as ContratacaoFormRawValue | NewContratacaoFormRawValue);
  }

  resetForm(form: ContratacaoFormGroup, contratacao: ContratacaoFormGroupInput): void {
    const contratacaoRawValue = this.convertContratacaoToContratacaoRawValue({ ...this.getFormDefaults(), ...contratacao });
    form.reset(
      {
        ...contratacaoRawValue,
        id: { value: contratacaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContratacaoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      cancelado: false,
      removido: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertContratacaoRawValueToContratacao(
    rawContratacao: ContratacaoFormRawValue | NewContratacaoFormRawValue,
  ): IContratacao | NewContratacao {
    return {
      ...rawContratacao,
      createdDate: dayjs(rawContratacao.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawContratacao.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertContratacaoToContratacaoRawValue(
    contratacao: IContratacao | (Partial<NewContratacao> & ContratacaoFormDefaults),
  ): ContratacaoFormRawValue | PartialWithRequiredKeyOf<NewContratacaoFormRawValue> {
    return {
      ...contratacao,
      createdDate: contratacao.createdDate ? contratacao.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: contratacao.lastModifiedDate ? contratacao.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
