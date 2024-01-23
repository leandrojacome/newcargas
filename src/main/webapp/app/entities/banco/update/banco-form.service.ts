import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBanco, NewBanco } from '../banco.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBanco for edit and NewBancoFormGroupInput for create.
 */
type BancoFormGroupInput = IBanco | PartialWithRequiredKeyOf<NewBanco>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBanco | NewBanco> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BancoFormRawValue = FormValueOf<IBanco>;

type NewBancoFormRawValue = FormValueOf<NewBanco>;

type BancoFormDefaults = Pick<NewBanco, 'id' | 'createdDate' | 'lastModifiedDate'>;

type BancoFormGroupContent = {
  id: FormControl<BancoFormRawValue['id'] | NewBanco['id']>;
  nome: FormControl<BancoFormRawValue['nome']>;
  codigo: FormControl<BancoFormRawValue['codigo']>;
  createdBy: FormControl<BancoFormRawValue['createdBy']>;
  createdDate: FormControl<BancoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BancoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BancoFormRawValue['lastModifiedDate']>;
};

export type BancoFormGroup = FormGroup<BancoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BancoFormService {
  createBancoFormGroup(banco: BancoFormGroupInput = { id: null }): BancoFormGroup {
    const bancoRawValue = this.convertBancoToBancoRawValue({
      ...this.getFormDefaults(),
      ...banco,
    });
    return new FormGroup<BancoFormGroupContent>({
      id: new FormControl(
        { value: bancoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(bancoRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      codigo: new FormControl(bancoRawValue.codigo, {
        validators: [Validators.minLength(3), Validators.maxLength(3)],
      }),
      createdBy: new FormControl(bancoRawValue.createdBy),
      createdDate: new FormControl(bancoRawValue.createdDate),
      lastModifiedBy: new FormControl(bancoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(bancoRawValue.lastModifiedDate),
    });
  }

  getBanco(form: BancoFormGroup): IBanco | NewBanco {
    return this.convertBancoRawValueToBanco(form.getRawValue() as BancoFormRawValue | NewBancoFormRawValue);
  }

  resetForm(form: BancoFormGroup, banco: BancoFormGroupInput): void {
    const bancoRawValue = this.convertBancoToBancoRawValue({ ...this.getFormDefaults(), ...banco });
    form.reset(
      {
        ...bancoRawValue,
        id: { value: bancoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BancoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertBancoRawValueToBanco(rawBanco: BancoFormRawValue | NewBancoFormRawValue): IBanco | NewBanco {
    return {
      ...rawBanco,
      createdDate: dayjs(rawBanco.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBanco.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBancoToBancoRawValue(
    banco: IBanco | (Partial<NewBanco> & BancoFormDefaults),
  ): BancoFormRawValue | PartialWithRequiredKeyOf<NewBancoFormRawValue> {
    return {
      ...banco,
      createdDate: banco.createdDate ? banco.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: banco.lastModifiedDate ? banco.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
