import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITipoCarga, NewTipoCarga } from '../tipo-carga.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoCarga for edit and NewTipoCargaFormGroupInput for create.
 */
type TipoCargaFormGroupInput = ITipoCarga | PartialWithRequiredKeyOf<NewTipoCarga>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITipoCarga | NewTipoCarga> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type TipoCargaFormRawValue = FormValueOf<ITipoCarga>;

type NewTipoCargaFormRawValue = FormValueOf<NewTipoCarga>;

type TipoCargaFormDefaults = Pick<NewTipoCarga, 'id' | 'createdDate' | 'lastModifiedDate'>;

type TipoCargaFormGroupContent = {
  id: FormControl<TipoCargaFormRawValue['id'] | NewTipoCarga['id']>;
  nome: FormControl<TipoCargaFormRawValue['nome']>;
  descricao: FormControl<TipoCargaFormRawValue['descricao']>;
  createdBy: FormControl<TipoCargaFormRawValue['createdBy']>;
  createdDate: FormControl<TipoCargaFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<TipoCargaFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<TipoCargaFormRawValue['lastModifiedDate']>;
};

export type TipoCargaFormGroup = FormGroup<TipoCargaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoCargaFormService {
  createTipoCargaFormGroup(tipoCarga: TipoCargaFormGroupInput = { id: null }): TipoCargaFormGroup {
    const tipoCargaRawValue = this.convertTipoCargaToTipoCargaRawValue({
      ...this.getFormDefaults(),
      ...tipoCarga,
    });
    return new FormGroup<TipoCargaFormGroupContent>({
      id: new FormControl(
        { value: tipoCargaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(tipoCargaRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      descricao: new FormControl(tipoCargaRawValue.descricao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      createdBy: new FormControl(tipoCargaRawValue.createdBy),
      createdDate: new FormControl(tipoCargaRawValue.createdDate),
      lastModifiedBy: new FormControl(tipoCargaRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(tipoCargaRawValue.lastModifiedDate),
    });
  }

  getTipoCarga(form: TipoCargaFormGroup): ITipoCarga | NewTipoCarga {
    return this.convertTipoCargaRawValueToTipoCarga(form.getRawValue() as TipoCargaFormRawValue | NewTipoCargaFormRawValue);
  }

  resetForm(form: TipoCargaFormGroup, tipoCarga: TipoCargaFormGroupInput): void {
    const tipoCargaRawValue = this.convertTipoCargaToTipoCargaRawValue({ ...this.getFormDefaults(), ...tipoCarga });
    form.reset(
      {
        ...tipoCargaRawValue,
        id: { value: tipoCargaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoCargaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertTipoCargaRawValueToTipoCarga(rawTipoCarga: TipoCargaFormRawValue | NewTipoCargaFormRawValue): ITipoCarga | NewTipoCarga {
    return {
      ...rawTipoCarga,
      createdDate: dayjs(rawTipoCarga.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawTipoCarga.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTipoCargaToTipoCargaRawValue(
    tipoCarga: ITipoCarga | (Partial<NewTipoCarga> & TipoCargaFormDefaults),
  ): TipoCargaFormRawValue | PartialWithRequiredKeyOf<NewTipoCargaFormRawValue> {
    return {
      ...tipoCarga,
      createdDate: tipoCarga.createdDate ? tipoCarga.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: tipoCarga.lastModifiedDate ? tipoCarga.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
