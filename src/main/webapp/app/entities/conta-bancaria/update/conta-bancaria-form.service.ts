import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContaBancaria, NewContaBancaria } from '../conta-bancaria.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContaBancaria for edit and NewContaBancariaFormGroupInput for create.
 */
type ContaBancariaFormGroupInput = IContaBancaria | PartialWithRequiredKeyOf<NewContaBancaria>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContaBancaria | NewContaBancaria> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ContaBancariaFormRawValue = FormValueOf<IContaBancaria>;

type NewContaBancariaFormRawValue = FormValueOf<NewContaBancaria>;

type ContaBancariaFormDefaults = Pick<NewContaBancaria, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ContaBancariaFormGroupContent = {
  id: FormControl<ContaBancariaFormRawValue['id'] | NewContaBancaria['id']>;
  agencia: FormControl<ContaBancariaFormRawValue['agencia']>;
  conta: FormControl<ContaBancariaFormRawValue['conta']>;
  observacao: FormControl<ContaBancariaFormRawValue['observacao']>;
  tipo: FormControl<ContaBancariaFormRawValue['tipo']>;
  pix: FormControl<ContaBancariaFormRawValue['pix']>;
  titular: FormControl<ContaBancariaFormRawValue['titular']>;
  createdBy: FormControl<ContaBancariaFormRawValue['createdBy']>;
  createdDate: FormControl<ContaBancariaFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ContaBancariaFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ContaBancariaFormRawValue['lastModifiedDate']>;
  banco: FormControl<ContaBancariaFormRawValue['banco']>;
  embarcador: FormControl<ContaBancariaFormRawValue['embarcador']>;
  transportadora: FormControl<ContaBancariaFormRawValue['transportadora']>;
};

export type ContaBancariaFormGroup = FormGroup<ContaBancariaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContaBancariaFormService {
  createContaBancariaFormGroup(contaBancaria: ContaBancariaFormGroupInput = { id: null }): ContaBancariaFormGroup {
    const contaBancariaRawValue = this.convertContaBancariaToContaBancariaRawValue({
      ...this.getFormDefaults(),
      ...contaBancaria,
    });
    return new FormGroup<ContaBancariaFormGroupContent>({
      id: new FormControl(
        { value: contaBancariaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      agencia: new FormControl(contaBancariaRawValue.agencia, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(10)],
      }),
      conta: new FormControl(contaBancariaRawValue.conta, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(10)],
      }),
      observacao: new FormControl(contaBancariaRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      tipo: new FormControl(contaBancariaRawValue.tipo, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      pix: new FormControl(contaBancariaRawValue.pix, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      titular: new FormControl(contaBancariaRawValue.titular, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      createdBy: new FormControl(contaBancariaRawValue.createdBy),
      createdDate: new FormControl(contaBancariaRawValue.createdDate),
      lastModifiedBy: new FormControl(contaBancariaRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(contaBancariaRawValue.lastModifiedDate),
      banco: new FormControl(contaBancariaRawValue.banco),
      embarcador: new FormControl(contaBancariaRawValue.embarcador),
      transportadora: new FormControl(contaBancariaRawValue.transportadora),
    });
  }

  getContaBancaria(form: ContaBancariaFormGroup): IContaBancaria | NewContaBancaria {
    return this.convertContaBancariaRawValueToContaBancaria(form.getRawValue() as ContaBancariaFormRawValue | NewContaBancariaFormRawValue);
  }

  resetForm(form: ContaBancariaFormGroup, contaBancaria: ContaBancariaFormGroupInput): void {
    const contaBancariaRawValue = this.convertContaBancariaToContaBancariaRawValue({ ...this.getFormDefaults(), ...contaBancaria });
    form.reset(
      {
        ...contaBancariaRawValue,
        id: { value: contaBancariaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContaBancariaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertContaBancariaRawValueToContaBancaria(
    rawContaBancaria: ContaBancariaFormRawValue | NewContaBancariaFormRawValue,
  ): IContaBancaria | NewContaBancaria {
    return {
      ...rawContaBancaria,
      createdDate: dayjs(rawContaBancaria.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawContaBancaria.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertContaBancariaToContaBancariaRawValue(
    contaBancaria: IContaBancaria | (Partial<NewContaBancaria> & ContaBancariaFormDefaults),
  ): ContaBancariaFormRawValue | PartialWithRequiredKeyOf<NewContaBancariaFormRawValue> {
    return {
      ...contaBancaria,
      createdDate: contaBancaria.createdDate ? contaBancaria.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: contaBancaria.lastModifiedDate ? contaBancaria.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
