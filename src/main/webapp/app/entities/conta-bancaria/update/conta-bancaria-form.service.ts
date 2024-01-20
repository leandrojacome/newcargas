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
type FormValueOf<T extends IContaBancaria | NewContaBancaria> = Omit<T, 'dataCadastro' | 'dataAtualizacao'> & {
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
};

type ContaBancariaFormRawValue = FormValueOf<IContaBancaria>;

type NewContaBancariaFormRawValue = FormValueOf<NewContaBancaria>;

type ContaBancariaFormDefaults = Pick<NewContaBancaria, 'id' | 'dataCadastro' | 'dataAtualizacao'>;

type ContaBancariaFormGroupContent = {
  id: FormControl<ContaBancariaFormRawValue['id'] | NewContaBancaria['id']>;
  agencia: FormControl<ContaBancariaFormRawValue['agencia']>;
  conta: FormControl<ContaBancariaFormRawValue['conta']>;
  observacao: FormControl<ContaBancariaFormRawValue['observacao']>;
  tipo: FormControl<ContaBancariaFormRawValue['tipo']>;
  pix: FormControl<ContaBancariaFormRawValue['pix']>;
  titular: FormControl<ContaBancariaFormRawValue['titular']>;
  dataCadastro: FormControl<ContaBancariaFormRawValue['dataCadastro']>;
  dataAtualizacao: FormControl<ContaBancariaFormRawValue['dataAtualizacao']>;
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
      dataCadastro: new FormControl(contaBancariaRawValue.dataCadastro, {
        validators: [Validators.required],
      }),
      dataAtualizacao: new FormControl(contaBancariaRawValue.dataAtualizacao),
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
      dataCadastro: currentTime,
      dataAtualizacao: currentTime,
    };
  }

  private convertContaBancariaRawValueToContaBancaria(
    rawContaBancaria: ContaBancariaFormRawValue | NewContaBancariaFormRawValue,
  ): IContaBancaria | NewContaBancaria {
    return {
      ...rawContaBancaria,
      dataCadastro: dayjs(rawContaBancaria.dataCadastro, DATE_TIME_FORMAT),
      dataAtualizacao: dayjs(rawContaBancaria.dataAtualizacao, DATE_TIME_FORMAT),
    };
  }

  private convertContaBancariaToContaBancariaRawValue(
    contaBancaria: IContaBancaria | (Partial<NewContaBancaria> & ContaBancariaFormDefaults),
  ): ContaBancariaFormRawValue | PartialWithRequiredKeyOf<NewContaBancariaFormRawValue> {
    return {
      ...contaBancaria,
      dataCadastro: contaBancaria.dataCadastro ? contaBancaria.dataCadastro.format(DATE_TIME_FORMAT) : undefined,
      dataAtualizacao: contaBancaria.dataAtualizacao ? contaBancaria.dataAtualizacao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
