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
  'dataHoraColeta' | 'dataHoraEntrega' | 'dataCadastro' | 'dataAtualizacao' | 'dataCancelamento' | 'dataRemocao'
> & {
  dataHoraColeta?: string | null;
  dataHoraEntrega?: string | null;
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
  dataCancelamento?: string | null;
  dataRemocao?: string | null;
};

type SolicitacaoColetaFormRawValue = FormValueOf<ISolicitacaoColeta>;

type NewSolicitacaoColetaFormRawValue = FormValueOf<NewSolicitacaoColeta>;

type SolicitacaoColetaFormDefaults = Pick<
  NewSolicitacaoColeta,
  | 'id'
  | 'coletado'
  | 'dataHoraColeta'
  | 'entregue'
  | 'dataHoraEntrega'
  | 'dataCadastro'
  | 'dataAtualizacao'
  | 'cancelado'
  | 'dataCancelamento'
  | 'removido'
  | 'dataRemocao'
>;

type SolicitacaoColetaFormGroupContent = {
  id: FormControl<SolicitacaoColetaFormRawValue['id'] | NewSolicitacaoColeta['id']>;
  coletado: FormControl<SolicitacaoColetaFormRawValue['coletado']>;
  dataHoraColeta: FormControl<SolicitacaoColetaFormRawValue['dataHoraColeta']>;
  entregue: FormControl<SolicitacaoColetaFormRawValue['entregue']>;
  dataHoraEntrega: FormControl<SolicitacaoColetaFormRawValue['dataHoraEntrega']>;
  valorTotal: FormControl<SolicitacaoColetaFormRawValue['valorTotal']>;
  observacao: FormControl<SolicitacaoColetaFormRawValue['observacao']>;
  dataCadastro: FormControl<SolicitacaoColetaFormRawValue['dataCadastro']>;
  dataAtualizacao: FormControl<SolicitacaoColetaFormRawValue['dataAtualizacao']>;
  cancelado: FormControl<SolicitacaoColetaFormRawValue['cancelado']>;
  dataCancelamento: FormControl<SolicitacaoColetaFormRawValue['dataCancelamento']>;
  usuarioCancelamento: FormControl<SolicitacaoColetaFormRawValue['usuarioCancelamento']>;
  removido: FormControl<SolicitacaoColetaFormRawValue['removido']>;
  dataRemocao: FormControl<SolicitacaoColetaFormRawValue['dataRemocao']>;
  usuarioRemocao: FormControl<SolicitacaoColetaFormRawValue['usuarioRemocao']>;
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
      dataCadastro: new FormControl(solicitacaoColetaRawValue.dataCadastro, {
        validators: [Validators.required],
      }),
      dataAtualizacao: new FormControl(solicitacaoColetaRawValue.dataAtualizacao),
      cancelado: new FormControl(solicitacaoColetaRawValue.cancelado),
      dataCancelamento: new FormControl(solicitacaoColetaRawValue.dataCancelamento),
      usuarioCancelamento: new FormControl(solicitacaoColetaRawValue.usuarioCancelamento, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      removido: new FormControl(solicitacaoColetaRawValue.removido),
      dataRemocao: new FormControl(solicitacaoColetaRawValue.dataRemocao),
      usuarioRemocao: new FormControl(solicitacaoColetaRawValue.usuarioRemocao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
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
      dataCadastro: currentTime,
      dataAtualizacao: currentTime,
      cancelado: false,
      dataCancelamento: currentTime,
      removido: false,
      dataRemocao: currentTime,
    };
  }

  private convertSolicitacaoColetaRawValueToSolicitacaoColeta(
    rawSolicitacaoColeta: SolicitacaoColetaFormRawValue | NewSolicitacaoColetaFormRawValue,
  ): ISolicitacaoColeta | NewSolicitacaoColeta {
    return {
      ...rawSolicitacaoColeta,
      dataHoraColeta: dayjs(rawSolicitacaoColeta.dataHoraColeta, DATE_TIME_FORMAT),
      dataHoraEntrega: dayjs(rawSolicitacaoColeta.dataHoraEntrega, DATE_TIME_FORMAT),
      dataCadastro: dayjs(rawSolicitacaoColeta.dataCadastro, DATE_TIME_FORMAT),
      dataAtualizacao: dayjs(rawSolicitacaoColeta.dataAtualizacao, DATE_TIME_FORMAT),
      dataCancelamento: dayjs(rawSolicitacaoColeta.dataCancelamento, DATE_TIME_FORMAT),
      dataRemocao: dayjs(rawSolicitacaoColeta.dataRemocao, DATE_TIME_FORMAT),
    };
  }

  private convertSolicitacaoColetaToSolicitacaoColetaRawValue(
    solicitacaoColeta: ISolicitacaoColeta | (Partial<NewSolicitacaoColeta> & SolicitacaoColetaFormDefaults),
  ): SolicitacaoColetaFormRawValue | PartialWithRequiredKeyOf<NewSolicitacaoColetaFormRawValue> {
    return {
      ...solicitacaoColeta,
      dataHoraColeta: solicitacaoColeta.dataHoraColeta ? solicitacaoColeta.dataHoraColeta.format(DATE_TIME_FORMAT) : undefined,
      dataHoraEntrega: solicitacaoColeta.dataHoraEntrega ? solicitacaoColeta.dataHoraEntrega.format(DATE_TIME_FORMAT) : undefined,
      dataCadastro: solicitacaoColeta.dataCadastro ? solicitacaoColeta.dataCadastro.format(DATE_TIME_FORMAT) : undefined,
      dataAtualizacao: solicitacaoColeta.dataAtualizacao ? solicitacaoColeta.dataAtualizacao.format(DATE_TIME_FORMAT) : undefined,
      dataCancelamento: solicitacaoColeta.dataCancelamento ? solicitacaoColeta.dataCancelamento.format(DATE_TIME_FORMAT) : undefined,
      dataRemocao: solicitacaoColeta.dataRemocao ? solicitacaoColeta.dataRemocao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
