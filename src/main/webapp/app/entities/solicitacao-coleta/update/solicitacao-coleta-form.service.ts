import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISolicitacaoColeta, NewSolicitacaoColeta } from '../solicitacao-coleta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISolicitacaoColeta for edit and NewSolicitacaoColetaFormGroupInput for create.
 */
type SolicitacaoColetaFormGroupInput = ISolicitacaoColeta | PartialWithRequiredKeyOf<NewSolicitacaoColeta>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISolicitacaoColeta | NewSolicitacaoColeta> = Omit<
  T,
  'dataHoraColeta' | 'dataHoraEntrega' | 'createdDate' | 'lastModifiedDate'
> & {
  dataHoraColeta?: string | null;
  dataHoraEntrega?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type SolicitacaoColetaFormRawValue = FormValueOf<ISolicitacaoColeta>;

type NewSolicitacaoColetaFormRawValue = FormValueOf<NewSolicitacaoColeta>;

type SolicitacaoColetaFormDefaults = Pick<
  NewSolicitacaoColeta,
  'id' | 'coletado' | 'dataHoraColeta' | 'entregue' | 'dataHoraEntrega' | 'cancelado' | 'removido' | 'createdDate' | 'lastModifiedDate'
>;

type SolicitacaoColetaFormGroupContent = {
  id: FormControl<SolicitacaoColetaFormRawValue['id'] | NewSolicitacaoColeta['id']>;
  coletado: FormControl<SolicitacaoColetaFormRawValue['coletado']>;
  dataHoraColeta: FormControl<SolicitacaoColetaFormRawValue['dataHoraColeta']>;
  entregue: FormControl<SolicitacaoColetaFormRawValue['entregue']>;
  dataHoraEntrega: FormControl<SolicitacaoColetaFormRawValue['dataHoraEntrega']>;
  valorTotal: FormControl<SolicitacaoColetaFormRawValue['valorTotal']>;
  observacao: FormControl<SolicitacaoColetaFormRawValue['observacao']>;
  cancelado: FormControl<SolicitacaoColetaFormRawValue['cancelado']>;
  removido: FormControl<SolicitacaoColetaFormRawValue['removido']>;
  createdBy: FormControl<SolicitacaoColetaFormRawValue['createdBy']>;
  createdDate: FormControl<SolicitacaoColetaFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<SolicitacaoColetaFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<SolicitacaoColetaFormRawValue['lastModifiedDate']>;
  embarcador: FormControl<SolicitacaoColetaFormRawValue['embarcador']>;
  statusColeta: FormControl<SolicitacaoColetaFormRawValue['statusColeta']>;
  roteirizacao: FormControl<SolicitacaoColetaFormRawValue['roteirizacao']>;
  tipoVeiculo: FormControl<SolicitacaoColetaFormRawValue['tipoVeiculo']>;
};

export type SolicitacaoColetaFormGroup = FormGroup<SolicitacaoColetaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SolicitacaoColetaFormService {
  createSolicitacaoColetaFormGroup(solicitacaoColeta: SolicitacaoColetaFormGroupInput = { id: null }): SolicitacaoColetaFormGroup {
    const solicitacaoColetaRawValue = this.convertSolicitacaoColetaToSolicitacaoColetaRawValue({
      ...this.getFormDefaults(),
      ...solicitacaoColeta,
    });
    return new FormGroup<SolicitacaoColetaFormGroupContent>({
      id: new FormControl(
        { value: solicitacaoColetaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      coletado: new FormControl(solicitacaoColetaRawValue.coletado, {
        validators: [Validators.required],
      }),
      dataHoraColeta: new FormControl(solicitacaoColetaRawValue.dataHoraColeta, {
        validators: [Validators.required],
      }),
      entregue: new FormControl(solicitacaoColetaRawValue.entregue, {
        validators: [Validators.required],
      }),
      dataHoraEntrega: new FormControl(solicitacaoColetaRawValue.dataHoraEntrega),
      valorTotal: new FormControl(solicitacaoColetaRawValue.valorTotal, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      observacao: new FormControl(solicitacaoColetaRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      cancelado: new FormControl(solicitacaoColetaRawValue.cancelado),
      removido: new FormControl(solicitacaoColetaRawValue.removido),
      createdBy: new FormControl(solicitacaoColetaRawValue.createdBy),
      createdDate: new FormControl(solicitacaoColetaRawValue.createdDate),
      lastModifiedBy: new FormControl(solicitacaoColetaRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(solicitacaoColetaRawValue.lastModifiedDate),
      embarcador: new FormControl(solicitacaoColetaRawValue.embarcador),
      statusColeta: new FormControl(solicitacaoColetaRawValue.statusColeta),
      roteirizacao: new FormControl(solicitacaoColetaRawValue.roteirizacao),
      tipoVeiculo: new FormControl(solicitacaoColetaRawValue.tipoVeiculo),
    });
  }

  getSolicitacaoColeta(form: SolicitacaoColetaFormGroup): ISolicitacaoColeta | NewSolicitacaoColeta {
    return this.convertSolicitacaoColetaRawValueToSolicitacaoColeta(
      form.getRawValue() as SolicitacaoColetaFormRawValue | NewSolicitacaoColetaFormRawValue,
    );
  }

  resetForm(form: SolicitacaoColetaFormGroup, solicitacaoColeta: SolicitacaoColetaFormGroupInput): void {
    const solicitacaoColetaRawValue = this.convertSolicitacaoColetaToSolicitacaoColetaRawValue({
      ...this.getFormDefaults(),
      ...solicitacaoColeta,
    });
    form.reset(
      {
        ...solicitacaoColetaRawValue,
        id: { value: solicitacaoColetaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SolicitacaoColetaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      coletado: false,
      dataHoraColeta: currentTime,
      entregue: false,
      dataHoraEntrega: currentTime,
      cancelado: false,
      removido: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertSolicitacaoColetaRawValueToSolicitacaoColeta(
    rawSolicitacaoColeta: SolicitacaoColetaFormRawValue | NewSolicitacaoColetaFormRawValue,
  ): ISolicitacaoColeta | NewSolicitacaoColeta {
    return {
      ...rawSolicitacaoColeta,
      dataHoraColeta: dayjs(rawSolicitacaoColeta.dataHoraColeta, DATE_TIME_FORMAT),
      dataHoraEntrega: dayjs(rawSolicitacaoColeta.dataHoraEntrega, DATE_TIME_FORMAT),
      createdDate: dayjs(rawSolicitacaoColeta.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawSolicitacaoColeta.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertSolicitacaoColetaToSolicitacaoColetaRawValue(
    solicitacaoColeta: ISolicitacaoColeta | (Partial<NewSolicitacaoColeta> & SolicitacaoColetaFormDefaults),
  ): SolicitacaoColetaFormRawValue | PartialWithRequiredKeyOf<NewSolicitacaoColetaFormRawValue> {
    return {
      ...solicitacaoColeta,
      dataHoraColeta: solicitacaoColeta.dataHoraColeta ? solicitacaoColeta.dataHoraColeta.format(DATE_TIME_FORMAT) : undefined,
      dataHoraEntrega: solicitacaoColeta.dataHoraEntrega ? solicitacaoColeta.dataHoraEntrega.format(DATE_TIME_FORMAT) : undefined,
      createdDate: solicitacaoColeta.createdDate ? solicitacaoColeta.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: solicitacaoColeta.lastModifiedDate ? solicitacaoColeta.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
