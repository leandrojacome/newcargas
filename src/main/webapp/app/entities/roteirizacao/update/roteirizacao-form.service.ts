import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRoteirizacao, NewRoteirizacao } from '../roteirizacao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoteirizacao for edit and NewRoteirizacaoFormGroupInput for create.
 */
type RoteirizacaoFormGroupInput = IRoteirizacao | PartialWithRequiredKeyOf<NewRoteirizacao>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRoteirizacao | NewRoteirizacao> = Omit<
  T,
  | 'dataHoraPrimeiraColeta'
  | 'dataHoraUltimaColeta'
  | 'dataHoraPrimeiraEntrega'
  | 'dataHoraUltimaEntrega'
  | 'dataCadastro'
  | 'dataAtualizacao'
  | 'dataCancelamento'
  | 'dataRemocao'
> & {
  dataHoraPrimeiraColeta?: string | null;
  dataHoraUltimaColeta?: string | null;
  dataHoraPrimeiraEntrega?: string | null;
  dataHoraUltimaEntrega?: string | null;
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
  dataCancelamento?: string | null;
  dataRemocao?: string | null;
};

type RoteirizacaoFormRawValue = FormValueOf<IRoteirizacao>;

type NewRoteirizacaoFormRawValue = FormValueOf<NewRoteirizacao>;

type RoteirizacaoFormDefaults = Pick<
  NewRoteirizacao,
  | 'id'
  | 'dataHoraPrimeiraColeta'
  | 'dataHoraUltimaColeta'
  | 'dataHoraPrimeiraEntrega'
  | 'dataHoraUltimaEntrega'
  | 'dataCadastro'
  | 'dataAtualizacao'
  | 'cancelado'
  | 'dataCancelamento'
  | 'removido'
  | 'dataRemocao'
>;

type RoteirizacaoFormGroupContent = {
  id: FormControl<RoteirizacaoFormRawValue['id'] | NewRoteirizacao['id']>;
  dataHoraPrimeiraColeta: FormControl<RoteirizacaoFormRawValue['dataHoraPrimeiraColeta']>;
  dataHoraUltimaColeta: FormControl<RoteirizacaoFormRawValue['dataHoraUltimaColeta']>;
  dataHoraPrimeiraEntrega: FormControl<RoteirizacaoFormRawValue['dataHoraPrimeiraEntrega']>;
  dataHoraUltimaEntrega: FormControl<RoteirizacaoFormRawValue['dataHoraUltimaEntrega']>;
  valorTotal: FormControl<RoteirizacaoFormRawValue['valorTotal']>;
  observacao: FormControl<RoteirizacaoFormRawValue['observacao']>;
  dataCadastro: FormControl<RoteirizacaoFormRawValue['dataCadastro']>;
  usuarioCadastro: FormControl<RoteirizacaoFormRawValue['usuarioCadastro']>;
  dataAtualizacao: FormControl<RoteirizacaoFormRawValue['dataAtualizacao']>;
  usuarioAtualizacao: FormControl<RoteirizacaoFormRawValue['usuarioAtualizacao']>;
  cancelado: FormControl<RoteirizacaoFormRawValue['cancelado']>;
  dataCancelamento: FormControl<RoteirizacaoFormRawValue['dataCancelamento']>;
  usuarioCancelamento: FormControl<RoteirizacaoFormRawValue['usuarioCancelamento']>;
  removido: FormControl<RoteirizacaoFormRawValue['removido']>;
  dataRemocao: FormControl<RoteirizacaoFormRawValue['dataRemocao']>;
  usuarioRemocao: FormControl<RoteirizacaoFormRawValue['usuarioRemocao']>;
  statusColeta: FormControl<RoteirizacaoFormRawValue['statusColeta']>;
};

export type RoteirizacaoFormGroup = FormGroup<RoteirizacaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoteirizacaoFormService {
  createRoteirizacaoFormGroup(roteirizacao: RoteirizacaoFormGroupInput = { id: null }): RoteirizacaoFormGroup {
    const roteirizacaoRawValue = this.convertRoteirizacaoToRoteirizacaoRawValue({
      ...this.getFormDefaults(),
      ...roteirizacao,
    });
    return new FormGroup<RoteirizacaoFormGroupContent>({
      id: new FormControl(
        { value: roteirizacaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataHoraPrimeiraColeta: new FormControl(roteirizacaoRawValue.dataHoraPrimeiraColeta, {
        validators: [Validators.required],
      }),
      dataHoraUltimaColeta: new FormControl(roteirizacaoRawValue.dataHoraUltimaColeta),
      dataHoraPrimeiraEntrega: new FormControl(roteirizacaoRawValue.dataHoraPrimeiraEntrega),
      dataHoraUltimaEntrega: new FormControl(roteirizacaoRawValue.dataHoraUltimaEntrega),
      valorTotal: new FormControl(roteirizacaoRawValue.valorTotal, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      observacao: new FormControl(roteirizacaoRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      dataCadastro: new FormControl(roteirizacaoRawValue.dataCadastro, {
        validators: [Validators.required],
      }),
      usuarioCadastro: new FormControl(roteirizacaoRawValue.usuarioCadastro, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      dataAtualizacao: new FormControl(roteirizacaoRawValue.dataAtualizacao),
      usuarioAtualizacao: new FormControl(roteirizacaoRawValue.usuarioAtualizacao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      cancelado: new FormControl(roteirizacaoRawValue.cancelado),
      dataCancelamento: new FormControl(roteirizacaoRawValue.dataCancelamento),
      usuarioCancelamento: new FormControl(roteirizacaoRawValue.usuarioCancelamento, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      removido: new FormControl(roteirizacaoRawValue.removido),
      dataRemocao: new FormControl(roteirizacaoRawValue.dataRemocao),
      usuarioRemocao: new FormControl(roteirizacaoRawValue.usuarioRemocao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      statusColeta: new FormControl(roteirizacaoRawValue.statusColeta),
    });
  }

  getRoteirizacao(form: RoteirizacaoFormGroup): IRoteirizacao | NewRoteirizacao {
    return this.convertRoteirizacaoRawValueToRoteirizacao(form.getRawValue() as RoteirizacaoFormRawValue | NewRoteirizacaoFormRawValue);
  }

  resetForm(form: RoteirizacaoFormGroup, roteirizacao: RoteirizacaoFormGroupInput): void {
    const roteirizacaoRawValue = this.convertRoteirizacaoToRoteirizacaoRawValue({ ...this.getFormDefaults(), ...roteirizacao });
    form.reset(
      {
        ...roteirizacaoRawValue,
        id: { value: roteirizacaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RoteirizacaoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataHoraPrimeiraColeta: currentTime,
      dataHoraUltimaColeta: currentTime,
      dataHoraPrimeiraEntrega: currentTime,
      dataHoraUltimaEntrega: currentTime,
      dataCadastro: currentTime,
      dataAtualizacao: currentTime,
      cancelado: false,
      dataCancelamento: currentTime,
      removido: false,
      dataRemocao: currentTime,
    };
  }

  private convertRoteirizacaoRawValueToRoteirizacao(
    rawRoteirizacao: RoteirizacaoFormRawValue | NewRoteirizacaoFormRawValue,
  ): IRoteirizacao | NewRoteirizacao {
    return {
      ...rawRoteirizacao,
      dataHoraPrimeiraColeta: dayjs(rawRoteirizacao.dataHoraPrimeiraColeta, DATE_TIME_FORMAT),
      dataHoraUltimaColeta: dayjs(rawRoteirizacao.dataHoraUltimaColeta, DATE_TIME_FORMAT),
      dataHoraPrimeiraEntrega: dayjs(rawRoteirizacao.dataHoraPrimeiraEntrega, DATE_TIME_FORMAT),
      dataHoraUltimaEntrega: dayjs(rawRoteirizacao.dataHoraUltimaEntrega, DATE_TIME_FORMAT),
      dataCadastro: dayjs(rawRoteirizacao.dataCadastro, DATE_TIME_FORMAT),
      dataAtualizacao: dayjs(rawRoteirizacao.dataAtualizacao, DATE_TIME_FORMAT),
      dataCancelamento: dayjs(rawRoteirizacao.dataCancelamento, DATE_TIME_FORMAT),
      dataRemocao: dayjs(rawRoteirizacao.dataRemocao, DATE_TIME_FORMAT),
    };
  }

  private convertRoteirizacaoToRoteirizacaoRawValue(
    roteirizacao: IRoteirizacao | (Partial<NewRoteirizacao> & RoteirizacaoFormDefaults),
  ): RoteirizacaoFormRawValue | PartialWithRequiredKeyOf<NewRoteirizacaoFormRawValue> {
    return {
      ...roteirizacao,
      dataHoraPrimeiraColeta: roteirizacao.dataHoraPrimeiraColeta
        ? roteirizacao.dataHoraPrimeiraColeta.format(DATE_TIME_FORMAT)
        : undefined,
      dataHoraUltimaColeta: roteirizacao.dataHoraUltimaColeta ? roteirizacao.dataHoraUltimaColeta.format(DATE_TIME_FORMAT) : undefined,
      dataHoraPrimeiraEntrega: roteirizacao.dataHoraPrimeiraEntrega
        ? roteirizacao.dataHoraPrimeiraEntrega.format(DATE_TIME_FORMAT)
        : undefined,
      dataHoraUltimaEntrega: roteirizacao.dataHoraUltimaEntrega ? roteirizacao.dataHoraUltimaEntrega.format(DATE_TIME_FORMAT) : undefined,
      dataCadastro: roteirizacao.dataCadastro ? roteirizacao.dataCadastro.format(DATE_TIME_FORMAT) : undefined,
      dataAtualizacao: roteirizacao.dataAtualizacao ? roteirizacao.dataAtualizacao.format(DATE_TIME_FORMAT) : undefined,
      dataCancelamento: roteirizacao.dataCancelamento ? roteirizacao.dataCancelamento.format(DATE_TIME_FORMAT) : undefined,
      dataRemocao: roteirizacao.dataRemocao ? roteirizacao.dataRemocao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
