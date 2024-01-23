import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITipoVeiculo, NewTipoVeiculo } from '../tipo-veiculo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoVeiculo for edit and NewTipoVeiculoFormGroupInput for create.
 */
type TipoVeiculoFormGroupInput = ITipoVeiculo | PartialWithRequiredKeyOf<NewTipoVeiculo>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITipoVeiculo | NewTipoVeiculo> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type TipoVeiculoFormRawValue = FormValueOf<ITipoVeiculo>;

type NewTipoVeiculoFormRawValue = FormValueOf<NewTipoVeiculo>;

type TipoVeiculoFormDefaults = Pick<NewTipoVeiculo, 'id' | 'createdDate' | 'lastModifiedDate'>;

type TipoVeiculoFormGroupContent = {
  id: FormControl<TipoVeiculoFormRawValue['id'] | NewTipoVeiculo['id']>;
  nome: FormControl<TipoVeiculoFormRawValue['nome']>;
  descricao: FormControl<TipoVeiculoFormRawValue['descricao']>;
  createdBy: FormControl<TipoVeiculoFormRawValue['createdBy']>;
  createdDate: FormControl<TipoVeiculoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<TipoVeiculoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<TipoVeiculoFormRawValue['lastModifiedDate']>;
};

export type TipoVeiculoFormGroup = FormGroup<TipoVeiculoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoVeiculoFormService {
  createTipoVeiculoFormGroup(tipoVeiculo: TipoVeiculoFormGroupInput = { id: null }): TipoVeiculoFormGroup {
    const tipoVeiculoRawValue = this.convertTipoVeiculoToTipoVeiculoRawValue({
      ...this.getFormDefaults(),
      ...tipoVeiculo,
    });
    return new FormGroup<TipoVeiculoFormGroupContent>({
      id: new FormControl(
        { value: tipoVeiculoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(tipoVeiculoRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      descricao: new FormControl(tipoVeiculoRawValue.descricao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      createdBy: new FormControl(tipoVeiculoRawValue.createdBy),
      createdDate: new FormControl(tipoVeiculoRawValue.createdDate),
      lastModifiedBy: new FormControl(tipoVeiculoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(tipoVeiculoRawValue.lastModifiedDate),
    });
  }

  getTipoVeiculo(form: TipoVeiculoFormGroup): ITipoVeiculo | NewTipoVeiculo {
    return this.convertTipoVeiculoRawValueToTipoVeiculo(form.getRawValue() as TipoVeiculoFormRawValue | NewTipoVeiculoFormRawValue);
  }

  resetForm(form: TipoVeiculoFormGroup, tipoVeiculo: TipoVeiculoFormGroupInput): void {
    const tipoVeiculoRawValue = this.convertTipoVeiculoToTipoVeiculoRawValue({ ...this.getFormDefaults(), ...tipoVeiculo });
    form.reset(
      {
        ...tipoVeiculoRawValue,
        id: { value: tipoVeiculoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoVeiculoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertTipoVeiculoRawValueToTipoVeiculo(
    rawTipoVeiculo: TipoVeiculoFormRawValue | NewTipoVeiculoFormRawValue,
  ): ITipoVeiculo | NewTipoVeiculo {
    return {
      ...rawTipoVeiculo,
      createdDate: dayjs(rawTipoVeiculo.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawTipoVeiculo.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTipoVeiculoToTipoVeiculoRawValue(
    tipoVeiculo: ITipoVeiculo | (Partial<NewTipoVeiculo> & TipoVeiculoFormDefaults),
  ): TipoVeiculoFormRawValue | PartialWithRequiredKeyOf<NewTipoVeiculoFormRawValue> {
    return {
      ...tipoVeiculo,
      createdDate: tipoVeiculo.createdDate ? tipoVeiculo.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: tipoVeiculo.lastModifiedDate ? tipoVeiculo.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
