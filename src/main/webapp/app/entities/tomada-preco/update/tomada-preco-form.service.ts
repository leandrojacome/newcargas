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
type FormValueOf<T extends ITomadaPreco | NewTomadaPreco> = Omit<
  T,
  'dataHoraEnvio' | 'dataCadastro' | 'dataAtualizacao' | 'dataAprovacao' | 'dataCancelamento' | 'dataRemocao'
> & {
  dataHoraEnvio?: string | null;
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
  dataAprovacao?: string | null;
  dataCancelamento?: string | null;
  dataRemocao?: string | null;
};

type TomadaPrecoFormRawValue = FormValueOf<ITomadaPreco>;

type NewTomadaPrecoFormRawValue = FormValueOf<NewTomadaPreco>;

type TomadaPrecoFormDefaults = Pick<
  NewTomadaPreco,
  | 'id'
  | 'dataHoraEnvio'
  | 'dataCadastro'
  | 'dataAtualizacao'
  | 'aprovado'
  | 'dataAprovacao'
  | 'cancelado'
  | 'dataCancelamento'
  | 'removido'
  | 'dataRemocao'
>;

type TomadaPrecoFormGroupContent = {
  id: FormControl<TomadaPrecoFormRawValue['id'] | NewTomadaPreco['id']>;
  dataHoraEnvio: FormControl<TomadaPrecoFormRawValue['dataHoraEnvio']>;
  prazoResposta: FormControl<TomadaPrecoFormRawValue['prazoResposta']>;
  valorTotal: FormControl<TomadaPrecoFormRawValue['valorTotal']>;
  observacao: FormControl<TomadaPrecoFormRawValue['observacao']>;
  dataCadastro: FormControl<TomadaPrecoFormRawValue['dataCadastro']>;
  usuarioCadastro: FormControl<TomadaPrecoFormRawValue['usuarioCadastro']>;
  dataAtualizacao: FormControl<TomadaPrecoFormRawValue['dataAtualizacao']>;
  usuarioAtualizacao: FormControl<TomadaPrecoFormRawValue['usuarioAtualizacao']>;
  aprovado: FormControl<TomadaPrecoFormRawValue['aprovado']>;
  dataAprovacao: FormControl<TomadaPrecoFormRawValue['dataAprovacao']>;
  usuarioAprovacao: FormControl<TomadaPrecoFormRawValue['usuarioAprovacao']>;
  cancelado: FormControl<TomadaPrecoFormRawValue['cancelado']>;
  dataCancelamento: FormControl<TomadaPrecoFormRawValue['dataCancelamento']>;
  usuarioCancelamento: FormControl<TomadaPrecoFormRawValue['usuarioCancelamento']>;
  removido: FormControl<TomadaPrecoFormRawValue['removido']>;
  dataRemocao: FormControl<TomadaPrecoFormRawValue['dataRemocao']>;
  usuarioRemocao: FormControl<TomadaPrecoFormRawValue['usuarioRemocao']>;
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
      dataCadastro: new FormControl(tomadaPrecoRawValue.dataCadastro, {
        validators: [Validators.required],
      }),
      usuarioCadastro: new FormControl(tomadaPrecoRawValue.usuarioCadastro, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      dataAtualizacao: new FormControl(tomadaPrecoRawValue.dataAtualizacao),
      usuarioAtualizacao: new FormControl(tomadaPrecoRawValue.usuarioAtualizacao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      aprovado: new FormControl(tomadaPrecoRawValue.aprovado),
      dataAprovacao: new FormControl(tomadaPrecoRawValue.dataAprovacao),
      usuarioAprovacao: new FormControl(tomadaPrecoRawValue.usuarioAprovacao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      cancelado: new FormControl(tomadaPrecoRawValue.cancelado),
      dataCancelamento: new FormControl(tomadaPrecoRawValue.dataCancelamento),
      usuarioCancelamento: new FormControl(tomadaPrecoRawValue.usuarioCancelamento, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      removido: new FormControl(tomadaPrecoRawValue.removido),
      dataRemocao: new FormControl(tomadaPrecoRawValue.dataRemocao),
      usuarioRemocao: new FormControl(tomadaPrecoRawValue.usuarioRemocao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
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
      dataCadastro: currentTime,
      dataAtualizacao: currentTime,
      aprovado: false,
      dataAprovacao: currentTime,
      cancelado: false,
      dataCancelamento: currentTime,
      removido: false,
      dataRemocao: currentTime,
    };
  }

  private convertTomadaPrecoRawValueToTomadaPreco(
    rawTomadaPreco: TomadaPrecoFormRawValue | NewTomadaPrecoFormRawValue,
  ): ITomadaPreco | NewTomadaPreco {
    return {
      ...rawTomadaPreco,
      dataHoraEnvio: dayjs(rawTomadaPreco.dataHoraEnvio, DATE_TIME_FORMAT),
      dataCadastro: dayjs(rawTomadaPreco.dataCadastro, DATE_TIME_FORMAT),
      dataAtualizacao: dayjs(rawTomadaPreco.dataAtualizacao, DATE_TIME_FORMAT),
      dataAprovacao: dayjs(rawTomadaPreco.dataAprovacao, DATE_TIME_FORMAT),
      dataCancelamento: dayjs(rawTomadaPreco.dataCancelamento, DATE_TIME_FORMAT),
      dataRemocao: dayjs(rawTomadaPreco.dataRemocao, DATE_TIME_FORMAT),
    };
  }

  private convertTomadaPrecoToTomadaPrecoRawValue(
    tomadaPreco: ITomadaPreco | (Partial<NewTomadaPreco> & TomadaPrecoFormDefaults),
  ): TomadaPrecoFormRawValue | PartialWithRequiredKeyOf<NewTomadaPrecoFormRawValue> {
    return {
      ...tomadaPreco,
      dataHoraEnvio: tomadaPreco.dataHoraEnvio ? tomadaPreco.dataHoraEnvio.format(DATE_TIME_FORMAT) : undefined,
      dataCadastro: tomadaPreco.dataCadastro ? tomadaPreco.dataCadastro.format(DATE_TIME_FORMAT) : undefined,
      dataAtualizacao: tomadaPreco.dataAtualizacao ? tomadaPreco.dataAtualizacao.format(DATE_TIME_FORMAT) : undefined,
      dataAprovacao: tomadaPreco.dataAprovacao ? tomadaPreco.dataAprovacao.format(DATE_TIME_FORMAT) : undefined,
      dataCancelamento: tomadaPreco.dataCancelamento ? tomadaPreco.dataCancelamento.format(DATE_TIME_FORMAT) : undefined,
      dataRemocao: tomadaPreco.dataRemocao ? tomadaPreco.dataRemocao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
