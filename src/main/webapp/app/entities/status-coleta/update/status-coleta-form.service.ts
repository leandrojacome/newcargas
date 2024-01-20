import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IStatusColeta, NewStatusColeta } from '../status-coleta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStatusColeta for edit and NewStatusColetaFormGroupInput for create.
 */
type StatusColetaFormGroupInput = IStatusColeta | PartialWithRequiredKeyOf<NewStatusColeta>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IStatusColeta | NewStatusColeta> = Omit<T, 'dataCadastro' | 'dataAtualizacao' | 'dataRemocao'> & {
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
  dataRemocao?: string | null;
};

type StatusColetaFormRawValue = FormValueOf<IStatusColeta>;

type NewStatusColetaFormRawValue = FormValueOf<NewStatusColeta>;

type StatusColetaFormDefaults = Pick<
  NewStatusColeta,
  | 'id'
  | 'estadoInicial'
  | 'estadoFinal'
  | 'permiteCancelar'
  | 'permiteEditar'
  | 'permiteExcluir'
  | 'dataCadastro'
  | 'dataAtualizacao'
  | 'ativo'
  | 'removido'
  | 'dataRemocao'
  | 'statusColetaOrigems'
>;

type StatusColetaFormGroupContent = {
  id: FormControl<StatusColetaFormRawValue['id'] | NewStatusColeta['id']>;
  nome: FormControl<StatusColetaFormRawValue['nome']>;
  cor: FormControl<StatusColetaFormRawValue['cor']>;
  ordem: FormControl<StatusColetaFormRawValue['ordem']>;
  estadoInicial: FormControl<StatusColetaFormRawValue['estadoInicial']>;
  estadoFinal: FormControl<StatusColetaFormRawValue['estadoFinal']>;
  permiteCancelar: FormControl<StatusColetaFormRawValue['permiteCancelar']>;
  permiteEditar: FormControl<StatusColetaFormRawValue['permiteEditar']>;
  permiteExcluir: FormControl<StatusColetaFormRawValue['permiteExcluir']>;
  descricao: FormControl<StatusColetaFormRawValue['descricao']>;
  dataCadastro: FormControl<StatusColetaFormRawValue['dataCadastro']>;
  usuarioCadastro: FormControl<StatusColetaFormRawValue['usuarioCadastro']>;
  dataAtualizacao: FormControl<StatusColetaFormRawValue['dataAtualizacao']>;
  usuarioAtualizacao: FormControl<StatusColetaFormRawValue['usuarioAtualizacao']>;
  ativo: FormControl<StatusColetaFormRawValue['ativo']>;
  removido: FormControl<StatusColetaFormRawValue['removido']>;
  dataRemocao: FormControl<StatusColetaFormRawValue['dataRemocao']>;
  usuarioRemocao: FormControl<StatusColetaFormRawValue['usuarioRemocao']>;
  statusColetaOrigems: FormControl<StatusColetaFormRawValue['statusColetaOrigems']>;
};

export type StatusColetaFormGroup = FormGroup<StatusColetaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StatusColetaFormService {
  createStatusColetaFormGroup(statusColeta: StatusColetaFormGroupInput = { id: null }): StatusColetaFormGroup {
    const statusColetaRawValue = this.convertStatusColetaToStatusColetaRawValue({
      ...this.getFormDefaults(),
      ...statusColeta,
    });
    return new FormGroup<StatusColetaFormGroupContent>({
      id: new FormControl(
        { value: statusColetaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(statusColetaRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      cor: new FormControl(statusColetaRawValue.cor, {
        validators: [Validators.minLength(2), Validators.maxLength(8)],
      }),
      ordem: new FormControl(statusColetaRawValue.ordem, {
        validators: [Validators.min(1), Validators.max(4)],
      }),
      estadoInicial: new FormControl(statusColetaRawValue.estadoInicial),
      estadoFinal: new FormControl(statusColetaRawValue.estadoFinal),
      permiteCancelar: new FormControl(statusColetaRawValue.permiteCancelar),
      permiteEditar: new FormControl(statusColetaRawValue.permiteEditar),
      permiteExcluir: new FormControl(statusColetaRawValue.permiteExcluir),
      descricao: new FormControl(statusColetaRawValue.descricao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      dataCadastro: new FormControl(statusColetaRawValue.dataCadastro, {
        validators: [Validators.required],
      }),
      usuarioCadastro: new FormControl(statusColetaRawValue.usuarioCadastro, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      dataAtualizacao: new FormControl(statusColetaRawValue.dataAtualizacao),
      usuarioAtualizacao: new FormControl(statusColetaRawValue.usuarioAtualizacao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      ativo: new FormControl(statusColetaRawValue.ativo),
      removido: new FormControl(statusColetaRawValue.removido),
      dataRemocao: new FormControl(statusColetaRawValue.dataRemocao),
      usuarioRemocao: new FormControl(statusColetaRawValue.usuarioRemocao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      statusColetaOrigems: new FormControl(statusColetaRawValue.statusColetaOrigems ?? []),
    });
  }

  getStatusColeta(form: StatusColetaFormGroup): IStatusColeta | NewStatusColeta {
    return this.convertStatusColetaRawValueToStatusColeta(form.getRawValue() as StatusColetaFormRawValue | NewStatusColetaFormRawValue);
  }

  resetForm(form: StatusColetaFormGroup, statusColeta: StatusColetaFormGroupInput): void {
    const statusColetaRawValue = this.convertStatusColetaToStatusColetaRawValue({ ...this.getFormDefaults(), ...statusColeta });
    form.reset(
      {
        ...statusColetaRawValue,
        id: { value: statusColetaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StatusColetaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      estadoInicial: false,
      estadoFinal: false,
      permiteCancelar: false,
      permiteEditar: false,
      permiteExcluir: false,
      dataCadastro: currentTime,
      dataAtualizacao: currentTime,
      ativo: false,
      removido: false,
      dataRemocao: currentTime,
      statusColetaOrigems: [],
    };
  }

  private convertStatusColetaRawValueToStatusColeta(
    rawStatusColeta: StatusColetaFormRawValue | NewStatusColetaFormRawValue,
  ): IStatusColeta | NewStatusColeta {
    return {
      ...rawStatusColeta,
      dataCadastro: dayjs(rawStatusColeta.dataCadastro, DATE_TIME_FORMAT),
      dataAtualizacao: dayjs(rawStatusColeta.dataAtualizacao, DATE_TIME_FORMAT),
      dataRemocao: dayjs(rawStatusColeta.dataRemocao, DATE_TIME_FORMAT),
    };
  }

  private convertStatusColetaToStatusColetaRawValue(
    statusColeta: IStatusColeta | (Partial<NewStatusColeta> & StatusColetaFormDefaults),
  ): StatusColetaFormRawValue | PartialWithRequiredKeyOf<NewStatusColetaFormRawValue> {
    return {
      ...statusColeta,
      dataCadastro: statusColeta.dataCadastro ? statusColeta.dataCadastro.format(DATE_TIME_FORMAT) : undefined,
      dataAtualizacao: statusColeta.dataAtualizacao ? statusColeta.dataAtualizacao.format(DATE_TIME_FORMAT) : undefined,
      dataRemocao: statusColeta.dataRemocao ? statusColeta.dataRemocao.format(DATE_TIME_FORMAT) : undefined,
      statusColetaOrigems: statusColeta.statusColetaOrigems ?? [],
    };
  }
}
