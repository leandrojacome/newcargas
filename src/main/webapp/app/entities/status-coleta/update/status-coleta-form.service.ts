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
type FormValueOf<T extends IStatusColeta | NewStatusColeta> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
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
  | 'ativo'
  | 'removido'
  | 'createdDate'
  | 'lastModifiedDate'
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
  ativo: FormControl<StatusColetaFormRawValue['ativo']>;
  removido: FormControl<StatusColetaFormRawValue['removido']>;
  createdBy: FormControl<StatusColetaFormRawValue['createdBy']>;
  createdDate: FormControl<StatusColetaFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<StatusColetaFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<StatusColetaFormRawValue['lastModifiedDate']>;
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
      ativo: new FormControl(statusColetaRawValue.ativo),
      removido: new FormControl(statusColetaRawValue.removido),
      createdBy: new FormControl(statusColetaRawValue.createdBy),
      createdDate: new FormControl(statusColetaRawValue.createdDate),
      lastModifiedBy: new FormControl(statusColetaRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(statusColetaRawValue.lastModifiedDate),
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
      ativo: false,
      removido: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
      statusColetaOrigems: [],
    };
  }

  private convertStatusColetaRawValueToStatusColeta(
    rawStatusColeta: StatusColetaFormRawValue | NewStatusColetaFormRawValue,
  ): IStatusColeta | NewStatusColeta {
    return {
      ...rawStatusColeta,
      createdDate: dayjs(rawStatusColeta.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawStatusColeta.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertStatusColetaToStatusColetaRawValue(
    statusColeta: IStatusColeta | (Partial<NewStatusColeta> & StatusColetaFormDefaults),
  ): StatusColetaFormRawValue | PartialWithRequiredKeyOf<NewStatusColetaFormRawValue> {
    return {
      ...statusColeta,
      createdDate: statusColeta.createdDate ? statusColeta.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: statusColeta.lastModifiedDate ? statusColeta.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
      statusColetaOrigems: statusColeta.statusColetaOrigems ?? [],
    };
  }
}
