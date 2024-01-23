import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotificacao, NewNotificacao } from '../notificacao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotificacao for edit and NewNotificacaoFormGroupInput for create.
 */
type NotificacaoFormGroupInput = INotificacao | PartialWithRequiredKeyOf<NewNotificacao>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotificacao | NewNotificacao> = Omit<
  T,
  'dataHoraEnvio' | 'dataHoraLeitura' | 'dataLeitura' | 'createdDate' | 'lastModifiedDate'
> & {
  dataHoraEnvio?: string | null;
  dataHoraLeitura?: string | null;
  dataLeitura?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type NotificacaoFormRawValue = FormValueOf<INotificacao>;

type NewNotificacaoFormRawValue = FormValueOf<NewNotificacao>;

type NotificacaoFormDefaults = Pick<
  NewNotificacao,
  'id' | 'dataHoraEnvio' | 'dataHoraLeitura' | 'lido' | 'dataLeitura' | 'removido' | 'createdDate' | 'lastModifiedDate'
>;

type NotificacaoFormGroupContent = {
  id: FormControl<NotificacaoFormRawValue['id'] | NewNotificacao['id']>;
  tipo: FormControl<NotificacaoFormRawValue['tipo']>;
  email: FormControl<NotificacaoFormRawValue['email']>;
  telefone: FormControl<NotificacaoFormRawValue['telefone']>;
  assunto: FormControl<NotificacaoFormRawValue['assunto']>;
  mensagem: FormControl<NotificacaoFormRawValue['mensagem']>;
  dataHoraEnvio: FormControl<NotificacaoFormRawValue['dataHoraEnvio']>;
  dataHoraLeitura: FormControl<NotificacaoFormRawValue['dataHoraLeitura']>;
  lido: FormControl<NotificacaoFormRawValue['lido']>;
  dataLeitura: FormControl<NotificacaoFormRawValue['dataLeitura']>;
  removido: FormControl<NotificacaoFormRawValue['removido']>;
  createdBy: FormControl<NotificacaoFormRawValue['createdBy']>;
  createdDate: FormControl<NotificacaoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<NotificacaoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<NotificacaoFormRawValue['lastModifiedDate']>;
  embarcador: FormControl<NotificacaoFormRawValue['embarcador']>;
  transportadora: FormControl<NotificacaoFormRawValue['transportadora']>;
};

export type NotificacaoFormGroup = FormGroup<NotificacaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotificacaoFormService {
  createNotificacaoFormGroup(notificacao: NotificacaoFormGroupInput = { id: null }): NotificacaoFormGroup {
    const notificacaoRawValue = this.convertNotificacaoToNotificacaoRawValue({
      ...this.getFormDefaults(),
      ...notificacao,
    });
    return new FormGroup<NotificacaoFormGroupContent>({
      id: new FormControl(
        { value: notificacaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipo: new FormControl(notificacaoRawValue.tipo, {
        validators: [Validators.required],
      }),
      email: new FormControl(notificacaoRawValue.email, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      telefone: new FormControl(notificacaoRawValue.telefone, {
        validators: [Validators.minLength(10), Validators.maxLength(11)],
      }),
      assunto: new FormControl(notificacaoRawValue.assunto, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      mensagem: new FormControl(notificacaoRawValue.mensagem, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(500)],
      }),
      dataHoraEnvio: new FormControl(notificacaoRawValue.dataHoraEnvio, {
        validators: [Validators.required],
      }),
      dataHoraLeitura: new FormControl(notificacaoRawValue.dataHoraLeitura),
      lido: new FormControl(notificacaoRawValue.lido),
      dataLeitura: new FormControl(notificacaoRawValue.dataLeitura),
      removido: new FormControl(notificacaoRawValue.removido),
      createdBy: new FormControl(notificacaoRawValue.createdBy),
      createdDate: new FormControl(notificacaoRawValue.createdDate),
      lastModifiedBy: new FormControl(notificacaoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(notificacaoRawValue.lastModifiedDate),
      embarcador: new FormControl(notificacaoRawValue.embarcador),
      transportadora: new FormControl(notificacaoRawValue.transportadora),
    });
  }

  getNotificacao(form: NotificacaoFormGroup): INotificacao | NewNotificacao {
    return this.convertNotificacaoRawValueToNotificacao(form.getRawValue() as NotificacaoFormRawValue | NewNotificacaoFormRawValue);
  }

  resetForm(form: NotificacaoFormGroup, notificacao: NotificacaoFormGroupInput): void {
    const notificacaoRawValue = this.convertNotificacaoToNotificacaoRawValue({ ...this.getFormDefaults(), ...notificacao });
    form.reset(
      {
        ...notificacaoRawValue,
        id: { value: notificacaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotificacaoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataHoraEnvio: currentTime,
      dataHoraLeitura: currentTime,
      lido: false,
      dataLeitura: currentTime,
      removido: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertNotificacaoRawValueToNotificacao(
    rawNotificacao: NotificacaoFormRawValue | NewNotificacaoFormRawValue,
  ): INotificacao | NewNotificacao {
    return {
      ...rawNotificacao,
      dataHoraEnvio: dayjs(rawNotificacao.dataHoraEnvio, DATE_TIME_FORMAT),
      dataHoraLeitura: dayjs(rawNotificacao.dataHoraLeitura, DATE_TIME_FORMAT),
      dataLeitura: dayjs(rawNotificacao.dataLeitura, DATE_TIME_FORMAT),
      createdDate: dayjs(rawNotificacao.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawNotificacao.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertNotificacaoToNotificacaoRawValue(
    notificacao: INotificacao | (Partial<NewNotificacao> & NotificacaoFormDefaults),
  ): NotificacaoFormRawValue | PartialWithRequiredKeyOf<NewNotificacaoFormRawValue> {
    return {
      ...notificacao,
      dataHoraEnvio: notificacao.dataHoraEnvio ? notificacao.dataHoraEnvio.format(DATE_TIME_FORMAT) : undefined,
      dataHoraLeitura: notificacao.dataHoraLeitura ? notificacao.dataHoraLeitura.format(DATE_TIME_FORMAT) : undefined,
      dataLeitura: notificacao.dataLeitura ? notificacao.dataLeitura.format(DATE_TIME_FORMAT) : undefined,
      createdDate: notificacao.createdDate ? notificacao.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: notificacao.lastModifiedDate ? notificacao.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
