import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHistoricoStatusColeta, NewHistoricoStatusColeta } from '../historico-status-coleta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHistoricoStatusColeta for edit and NewHistoricoStatusColetaFormGroupInput for create.
 */
type HistoricoStatusColetaFormGroupInput = IHistoricoStatusColeta | PartialWithRequiredKeyOf<NewHistoricoStatusColeta>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHistoricoStatusColeta | NewHistoricoStatusColeta> = Omit<T, 'dataCriacao'> & {
  dataCriacao?: string | null;
};

type HistoricoStatusColetaFormRawValue = FormValueOf<IHistoricoStatusColeta>;

type NewHistoricoStatusColetaFormRawValue = FormValueOf<NewHistoricoStatusColeta>;

type HistoricoStatusColetaFormDefaults = Pick<NewHistoricoStatusColeta, 'id' | 'dataCriacao'>;

type HistoricoStatusColetaFormGroupContent = {
  id: FormControl<HistoricoStatusColetaFormRawValue['id'] | NewHistoricoStatusColeta['id']>;
  dataCriacao: FormControl<HistoricoStatusColetaFormRawValue['dataCriacao']>;
  observacao: FormControl<HistoricoStatusColetaFormRawValue['observacao']>;
  solicitacaoColeta: FormControl<HistoricoStatusColetaFormRawValue['solicitacaoColeta']>;
  roteirizacao: FormControl<HistoricoStatusColetaFormRawValue['roteirizacao']>;
  statusColetaOrigem: FormControl<HistoricoStatusColetaFormRawValue['statusColetaOrigem']>;
  statusColetaDestino: FormControl<HistoricoStatusColetaFormRawValue['statusColetaDestino']>;
};

export type HistoricoStatusColetaFormGroup = FormGroup<HistoricoStatusColetaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HistoricoStatusColetaFormService {
  createHistoricoStatusColetaFormGroup(
    historicoStatusColeta: HistoricoStatusColetaFormGroupInput = { id: null },
  ): HistoricoStatusColetaFormGroup {
    const historicoStatusColetaRawValue = this.convertHistoricoStatusColetaToHistoricoStatusColetaRawValue({
      ...this.getFormDefaults(),
      ...historicoStatusColeta,
    });
    return new FormGroup<HistoricoStatusColetaFormGroupContent>({
      id: new FormControl(
        { value: historicoStatusColetaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataCriacao: new FormControl(historicoStatusColetaRawValue.dataCriacao, {
        validators: [Validators.required],
      }),
      observacao: new FormControl(historicoStatusColetaRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      solicitacaoColeta: new FormControl(historicoStatusColetaRawValue.solicitacaoColeta),
      roteirizacao: new FormControl(historicoStatusColetaRawValue.roteirizacao),
      statusColetaOrigem: new FormControl(historicoStatusColetaRawValue.statusColetaOrigem),
      statusColetaDestino: new FormControl(historicoStatusColetaRawValue.statusColetaDestino),
    });
  }

  getHistoricoStatusColeta(form: HistoricoStatusColetaFormGroup): IHistoricoStatusColeta | NewHistoricoStatusColeta {
    return this.convertHistoricoStatusColetaRawValueToHistoricoStatusColeta(
      form.getRawValue() as HistoricoStatusColetaFormRawValue | NewHistoricoStatusColetaFormRawValue,
    );
  }

  resetForm(form: HistoricoStatusColetaFormGroup, historicoStatusColeta: HistoricoStatusColetaFormGroupInput): void {
    const historicoStatusColetaRawValue = this.convertHistoricoStatusColetaToHistoricoStatusColetaRawValue({
      ...this.getFormDefaults(),
      ...historicoStatusColeta,
    });
    form.reset(
      {
        ...historicoStatusColetaRawValue,
        id: { value: historicoStatusColetaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HistoricoStatusColetaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataCriacao: currentTime,
    };
  }

  private convertHistoricoStatusColetaRawValueToHistoricoStatusColeta(
    rawHistoricoStatusColeta: HistoricoStatusColetaFormRawValue | NewHistoricoStatusColetaFormRawValue,
  ): IHistoricoStatusColeta | NewHistoricoStatusColeta {
    return {
      ...rawHistoricoStatusColeta,
      dataCriacao: dayjs(rawHistoricoStatusColeta.dataCriacao, DATE_TIME_FORMAT),
    };
  }

  private convertHistoricoStatusColetaToHistoricoStatusColetaRawValue(
    historicoStatusColeta: IHistoricoStatusColeta | (Partial<NewHistoricoStatusColeta> & HistoricoStatusColetaFormDefaults),
  ): HistoricoStatusColetaFormRawValue | PartialWithRequiredKeyOf<NewHistoricoStatusColetaFormRawValue> {
    return {
      ...historicoStatusColeta,
      dataCriacao: historicoStatusColeta.dataCriacao ? historicoStatusColeta.dataCriacao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
