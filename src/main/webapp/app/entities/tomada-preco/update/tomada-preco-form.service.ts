import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITomadaPreco, NewTomadaPreco } from '../tomada-preco.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITomadaPreco for edit and NewTomadaPrecoFormGroupInput for create.
 */
type TomadaPrecoFormGroupInput = ITomadaPreco | PartialWithRequiredKeyOf<NewTomadaPreco>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITomadaPreco | NewTomadaPreco> = Omit<T, 'dataHoraEnvio' | 'createdDate' | 'lastModifiedDate'> & {
  dataHoraEnvio?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type TomadaPrecoFormRawValue = FormValueOf<ITomadaPreco>;

type NewTomadaPrecoFormRawValue = FormValueOf<NewTomadaPreco>;

type TomadaPrecoFormDefaults = Pick<
  NewTomadaPreco,
  'id' | 'dataHoraEnvio' | 'aprovado' | 'cancelado' | 'removido' | 'createdDate' | 'lastModifiedDate'
>;

type TomadaPrecoFormGroupContent = {
  id: FormControl<TomadaPrecoFormRawValue['id'] | NewTomadaPreco['id']>;
  dataHoraEnvio: FormControl<TomadaPrecoFormRawValue['dataHoraEnvio']>;
  prazoResposta: FormControl<TomadaPrecoFormRawValue['prazoResposta']>;
  valorTotal: FormControl<TomadaPrecoFormRawValue['valorTotal']>;
  observacao: FormControl<TomadaPrecoFormRawValue['observacao']>;
  aprovado: FormControl<TomadaPrecoFormRawValue['aprovado']>;
  cancelado: FormControl<TomadaPrecoFormRawValue['cancelado']>;
  removido: FormControl<TomadaPrecoFormRawValue['removido']>;
  createdBy: FormControl<TomadaPrecoFormRawValue['createdBy']>;
  createdDate: FormControl<TomadaPrecoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<TomadaPrecoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<TomadaPrecoFormRawValue['lastModifiedDate']>;
  contratacao: FormControl<TomadaPrecoFormRawValue['contratacao']>;
  transportadora: FormControl<TomadaPrecoFormRawValue['transportadora']>;
  roteirizacao: FormControl<TomadaPrecoFormRawValue['roteirizacao']>;
};

export type TomadaPrecoFormGroup = FormGroup<TomadaPrecoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TomadaPrecoFormService {
  createTomadaPrecoFormGroup(tomadaPreco: TomadaPrecoFormGroupInput = { id: null }): TomadaPrecoFormGroup {
    const tomadaPrecoRawValue = this.convertTomadaPrecoToTomadaPrecoRawValue({
      ...this.getFormDefaults(),
      ...tomadaPreco,
    });
    return new FormGroup<TomadaPrecoFormGroupContent>({
      id: new FormControl(
        { value: tomadaPrecoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataHoraEnvio: new FormControl(tomadaPrecoRawValue.dataHoraEnvio, {
        validators: [Validators.required],
      }),
      prazoResposta: new FormControl(tomadaPrecoRawValue.prazoResposta, {
        validators: [Validators.min(1), Validators.max(4)],
      }),
      valorTotal: new FormControl(tomadaPrecoRawValue.valorTotal, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      observacao: new FormControl(tomadaPrecoRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      aprovado: new FormControl(tomadaPrecoRawValue.aprovado),
      cancelado: new FormControl(tomadaPrecoRawValue.cancelado),
      removido: new FormControl(tomadaPrecoRawValue.removido),
      createdBy: new FormControl(tomadaPrecoRawValue.createdBy),
      createdDate: new FormControl(tomadaPrecoRawValue.createdDate),
      lastModifiedBy: new FormControl(tomadaPrecoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(tomadaPrecoRawValue.lastModifiedDate),
      contratacao: new FormControl(tomadaPrecoRawValue.contratacao),
      transportadora: new FormControl(tomadaPrecoRawValue.transportadora),
      roteirizacao: new FormControl(tomadaPrecoRawValue.roteirizacao),
    });
  }

  getTomadaPreco(form: TomadaPrecoFormGroup): ITomadaPreco | NewTomadaPreco {
    return this.convertTomadaPrecoRawValueToTomadaPreco(form.getRawValue() as TomadaPrecoFormRawValue | NewTomadaPrecoFormRawValue);
  }

  resetForm(form: TomadaPrecoFormGroup, tomadaPreco: TomadaPrecoFormGroupInput): void {
    const tomadaPrecoRawValue = this.convertTomadaPrecoToTomadaPrecoRawValue({ ...this.getFormDefaults(), ...tomadaPreco });
    form.reset(
      {
        ...tomadaPrecoRawValue,
        id: { value: tomadaPrecoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TomadaPrecoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataHoraEnvio: currentTime,
      aprovado: false,
      cancelado: false,
      removido: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertTomadaPrecoRawValueToTomadaPreco(
    rawTomadaPreco: TomadaPrecoFormRawValue | NewTomadaPrecoFormRawValue,
  ): ITomadaPreco | NewTomadaPreco {
    return {
      ...rawTomadaPreco,
      dataHoraEnvio: dayjs(rawTomadaPreco.dataHoraEnvio, DATE_TIME_FORMAT),
      createdDate: dayjs(rawTomadaPreco.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawTomadaPreco.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTomadaPrecoToTomadaPrecoRawValue(
    tomadaPreco: ITomadaPreco | (Partial<NewTomadaPreco> & TomadaPrecoFormDefaults),
  ): TomadaPrecoFormRawValue | PartialWithRequiredKeyOf<NewTomadaPrecoFormRawValue> {
    return {
      ...tomadaPreco,
      dataHoraEnvio: tomadaPreco.dataHoraEnvio ? tomadaPreco.dataHoraEnvio.format(DATE_TIME_FORMAT) : undefined,
      createdDate: tomadaPreco.createdDate ? tomadaPreco.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: tomadaPreco.lastModifiedDate ? tomadaPreco.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
