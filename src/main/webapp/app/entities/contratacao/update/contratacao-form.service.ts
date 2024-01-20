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
type FormValueOf<T extends IContratacao | NewContratacao> = Omit<
  T,
  'dataCadastro' | 'dataAtualizacao' | 'dataCancelamento' | 'dataRemocao'
> & {
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
  dataCancelamento?: string | null;
  dataRemocao?: string | null;
};

type ContratacaoFormRawValue = FormValueOf<IContratacao>;

type NewContratacaoFormRawValue = FormValueOf<NewContratacao>;

type ContratacaoFormDefaults = Pick<
  NewContratacao,
  'id' | 'dataCadastro' | 'dataAtualizacao' | 'cancelado' | 'dataCancelamento' | 'removido' | 'dataRemocao'
>;

type ContratacaoFormGroupContent = {
  id: FormControl<ContratacaoFormRawValue['id'] | NewContratacao['id']>;
  valorTotal: FormControl<ContratacaoFormRawValue['valorTotal']>;
  validadeEmDias: FormControl<ContratacaoFormRawValue['validadeEmDias']>;
  dataValidade: FormControl<ContratacaoFormRawValue['dataValidade']>;
  observacao: FormControl<ContratacaoFormRawValue['observacao']>;
  dataCadastro: FormControl<ContratacaoFormRawValue['dataCadastro']>;
  usuarioCadastro: FormControl<ContratacaoFormRawValue['usuarioCadastro']>;
  dataAtualizacao: FormControl<ContratacaoFormRawValue['dataAtualizacao']>;
  usuarioAtualizacao: FormControl<ContratacaoFormRawValue['usuarioAtualizacao']>;
  cancelado: FormControl<ContratacaoFormRawValue['cancelado']>;
  dataCancelamento: FormControl<ContratacaoFormRawValue['dataCancelamento']>;
  usuarioCancelamento: FormControl<ContratacaoFormRawValue['usuarioCancelamento']>;
  removido: FormControl<ContratacaoFormRawValue['removido']>;
  dataRemocao: FormControl<ContratacaoFormRawValue['dataRemocao']>;
  usuarioRemocao: FormControl<ContratacaoFormRawValue['usuarioRemocao']>;
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
      dataCadastro: new FormControl(contratacaoRawValue.dataCadastro, {
        validators: [Validators.required],
      }),
      usuarioCadastro: new FormControl(contratacaoRawValue.usuarioCadastro, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      dataAtualizacao: new FormControl(contratacaoRawValue.dataAtualizacao),
      usuarioAtualizacao: new FormControl(contratacaoRawValue.usuarioAtualizacao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      cancelado: new FormControl(contratacaoRawValue.cancelado),
      dataCancelamento: new FormControl(contratacaoRawValue.dataCancelamento),
      usuarioCancelamento: new FormControl(contratacaoRawValue.usuarioCancelamento, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      removido: new FormControl(contratacaoRawValue.removido),
      dataRemocao: new FormControl(contratacaoRawValue.dataRemocao),
      usuarioRemocao: new FormControl(contratacaoRawValue.usuarioRemocao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
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
      dataCadastro: currentTime,
      dataAtualizacao: currentTime,
      cancelado: false,
      dataCancelamento: currentTime,
      removido: false,
      dataRemocao: currentTime,
    };
  }

  private convertContratacaoRawValueToContratacao(
    rawContratacao: ContratacaoFormRawValue | NewContratacaoFormRawValue,
  ): IContratacao | NewContratacao {
    return {
      ...rawContratacao,
      dataCadastro: dayjs(rawContratacao.dataCadastro, DATE_TIME_FORMAT),
      dataAtualizacao: dayjs(rawContratacao.dataAtualizacao, DATE_TIME_FORMAT),
      dataCancelamento: dayjs(rawContratacao.dataCancelamento, DATE_TIME_FORMAT),
      dataRemocao: dayjs(rawContratacao.dataRemocao, DATE_TIME_FORMAT),
    };
  }

  private convertContratacaoToContratacaoRawValue(
    contratacao: IContratacao | (Partial<NewContratacao> & ContratacaoFormDefaults),
  ): ContratacaoFormRawValue | PartialWithRequiredKeyOf<NewContratacaoFormRawValue> {
    return {
      ...contratacao,
      dataCadastro: contratacao.dataCadastro ? contratacao.dataCadastro.format(DATE_TIME_FORMAT) : undefined,
      dataAtualizacao: contratacao.dataAtualizacao ? contratacao.dataAtualizacao.format(DATE_TIME_FORMAT) : undefined,
      dataCancelamento: contratacao.dataCancelamento ? contratacao.dataCancelamento.format(DATE_TIME_FORMAT) : undefined,
      dataRemocao: contratacao.dataRemocao ? contratacao.dataRemocao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
