import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFatura, NewFatura } from '../fatura.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFatura for edit and NewFaturaFormGroupInput for create.
 */
type FaturaFormGroupInput = IFatura | PartialWithRequiredKeyOf<NewFatura>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFatura | NewFatura> = Omit<
  T,
  'dataFatura' | 'dataVencimento' | 'dataPagamento' | 'createdDate' | 'lastModifiedDate'
> & {
  dataFatura?: string | null;
  dataVencimento?: string | null;
  dataPagamento?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type FaturaFormRawValue = FormValueOf<IFatura>;

type NewFaturaFormRawValue = FormValueOf<NewFatura>;

type FaturaFormDefaults = Pick<
  NewFatura,
  'id' | 'dataFatura' | 'dataVencimento' | 'dataPagamento' | 'cancelado' | 'removido' | 'createdDate' | 'lastModifiedDate'
>;

type FaturaFormGroupContent = {
  id: FormControl<FaturaFormRawValue['id'] | NewFatura['id']>;
  tipo: FormControl<FaturaFormRawValue['tipo']>;
  dataFatura: FormControl<FaturaFormRawValue['dataFatura']>;
  dataVencimento: FormControl<FaturaFormRawValue['dataVencimento']>;
  dataPagamento: FormControl<FaturaFormRawValue['dataPagamento']>;
  numeroParcela: FormControl<FaturaFormRawValue['numeroParcela']>;
  valorTotal: FormControl<FaturaFormRawValue['valorTotal']>;
  observacao: FormControl<FaturaFormRawValue['observacao']>;
  cancelado: FormControl<FaturaFormRawValue['cancelado']>;
  removido: FormControl<FaturaFormRawValue['removido']>;
  createdBy: FormControl<FaturaFormRawValue['createdBy']>;
  createdDate: FormControl<FaturaFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<FaturaFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<FaturaFormRawValue['lastModifiedDate']>;
  embarcador: FormControl<FaturaFormRawValue['embarcador']>;
  transportadora: FormControl<FaturaFormRawValue['transportadora']>;
  contratacao: FormControl<FaturaFormRawValue['contratacao']>;
  formaCobranca: FormControl<FaturaFormRawValue['formaCobranca']>;
};

export type FaturaFormGroup = FormGroup<FaturaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FaturaFormService {
  createFaturaFormGroup(fatura: FaturaFormGroupInput = { id: null }): FaturaFormGroup {
    const faturaRawValue = this.convertFaturaToFaturaRawValue({
      ...this.getFormDefaults(),
      ...fatura,
    });
    return new FormGroup<FaturaFormGroupContent>({
      id: new FormControl(
        { value: faturaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipo: new FormControl(faturaRawValue.tipo, {
        validators: [Validators.required],
      }),
      dataFatura: new FormControl(faturaRawValue.dataFatura, {
        validators: [Validators.required],
      }),
      dataVencimento: new FormControl(faturaRawValue.dataVencimento, {
        validators: [Validators.required],
      }),
      dataPagamento: new FormControl(faturaRawValue.dataPagamento),
      numeroParcela: new FormControl(faturaRawValue.numeroParcela, {
        validators: [Validators.min(1), Validators.max(4)],
      }),
      valorTotal: new FormControl(faturaRawValue.valorTotal, {
        validators: [Validators.required, Validators.min(1), Validators.max(10)],
      }),
      observacao: new FormControl(faturaRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      cancelado: new FormControl(faturaRawValue.cancelado),
      removido: new FormControl(faturaRawValue.removido),
      createdBy: new FormControl(faturaRawValue.createdBy),
      createdDate: new FormControl(faturaRawValue.createdDate),
      lastModifiedBy: new FormControl(faturaRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(faturaRawValue.lastModifiedDate),
      embarcador: new FormControl(faturaRawValue.embarcador),
      transportadora: new FormControl(faturaRawValue.transportadora),
      contratacao: new FormControl(faturaRawValue.contratacao),
      formaCobranca: new FormControl(faturaRawValue.formaCobranca),
    });
  }

  getFatura(form: FaturaFormGroup): IFatura | NewFatura {
    return this.convertFaturaRawValueToFatura(form.getRawValue() as FaturaFormRawValue | NewFaturaFormRawValue);
  }

  resetForm(form: FaturaFormGroup, fatura: FaturaFormGroupInput): void {
    const faturaRawValue = this.convertFaturaToFaturaRawValue({ ...this.getFormDefaults(), ...fatura });
    form.reset(
      {
        ...faturaRawValue,
        id: { value: faturaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FaturaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataFatura: currentTime,
      dataVencimento: currentTime,
      dataPagamento: currentTime,
      cancelado: false,
      removido: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertFaturaRawValueToFatura(rawFatura: FaturaFormRawValue | NewFaturaFormRawValue): IFatura | NewFatura {
    return {
      ...rawFatura,
      dataFatura: dayjs(rawFatura.dataFatura, DATE_TIME_FORMAT),
      dataVencimento: dayjs(rawFatura.dataVencimento, DATE_TIME_FORMAT),
      dataPagamento: dayjs(rawFatura.dataPagamento, DATE_TIME_FORMAT),
      createdDate: dayjs(rawFatura.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawFatura.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertFaturaToFaturaRawValue(
    fatura: IFatura | (Partial<NewFatura> & FaturaFormDefaults),
  ): FaturaFormRawValue | PartialWithRequiredKeyOf<NewFaturaFormRawValue> {
    return {
      ...fatura,
      dataFatura: fatura.dataFatura ? fatura.dataFatura.format(DATE_TIME_FORMAT) : undefined,
      dataVencimento: fatura.dataVencimento ? fatura.dataVencimento.format(DATE_TIME_FORMAT) : undefined,
      dataPagamento: fatura.dataPagamento ? fatura.dataPagamento.format(DATE_TIME_FORMAT) : undefined,
      createdDate: fatura.createdDate ? fatura.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: fatura.lastModifiedDate ? fatura.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
