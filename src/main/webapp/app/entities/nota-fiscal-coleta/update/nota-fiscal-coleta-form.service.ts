import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotaFiscalColeta, NewNotaFiscalColeta } from '../nota-fiscal-coleta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INotaFiscalColeta for edit and NewNotaFiscalColetaFormGroupInput for create.
 */
type NotaFiscalColetaFormGroupInput = INotaFiscalColeta | PartialWithRequiredKeyOf<NewNotaFiscalColeta>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends INotaFiscalColeta | NewNotaFiscalColeta> = Omit<
  T,
  'dataEmissao' | 'dataSaida' | 'createdDate' | 'lastModifiedDate'
> & {
  dataEmissao?: string | null;
  dataSaida?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type NotaFiscalColetaFormRawValue = FormValueOf<INotaFiscalColeta>;

type NewNotaFiscalColetaFormRawValue = FormValueOf<NewNotaFiscalColeta>;

type NotaFiscalColetaFormDefaults = Pick<NewNotaFiscalColeta, 'id' | 'dataEmissao' | 'dataSaida' | 'createdDate' | 'lastModifiedDate'>;

type NotaFiscalColetaFormGroupContent = {
  id: FormControl<NotaFiscalColetaFormRawValue['id'] | NewNotaFiscalColeta['id']>;
  numero: FormControl<NotaFiscalColetaFormRawValue['numero']>;
  serie: FormControl<NotaFiscalColetaFormRawValue['serie']>;
  remetente: FormControl<NotaFiscalColetaFormRawValue['remetente']>;
  destinatario: FormControl<NotaFiscalColetaFormRawValue['destinatario']>;
  metroCubico: FormControl<NotaFiscalColetaFormRawValue['metroCubico']>;
  quantidade: FormControl<NotaFiscalColetaFormRawValue['quantidade']>;
  peso: FormControl<NotaFiscalColetaFormRawValue['peso']>;
  dataEmissao: FormControl<NotaFiscalColetaFormRawValue['dataEmissao']>;
  dataSaida: FormControl<NotaFiscalColetaFormRawValue['dataSaida']>;
  valorTotal: FormControl<NotaFiscalColetaFormRawValue['valorTotal']>;
  pesoTotal: FormControl<NotaFiscalColetaFormRawValue['pesoTotal']>;
  quantidadeTotal: FormControl<NotaFiscalColetaFormRawValue['quantidadeTotal']>;
  observacao: FormControl<NotaFiscalColetaFormRawValue['observacao']>;
  createdBy: FormControl<NotaFiscalColetaFormRawValue['createdBy']>;
  createdDate: FormControl<NotaFiscalColetaFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<NotaFiscalColetaFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<NotaFiscalColetaFormRawValue['lastModifiedDate']>;
  solicitacaoColeta: FormControl<NotaFiscalColetaFormRawValue['solicitacaoColeta']>;
};

export type NotaFiscalColetaFormGroup = FormGroup<NotaFiscalColetaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class NotaFiscalColetaFormService {
  createNotaFiscalColetaFormGroup(notaFiscalColeta: NotaFiscalColetaFormGroupInput = { id: null }): NotaFiscalColetaFormGroup {
    const notaFiscalColetaRawValue = this.convertNotaFiscalColetaToNotaFiscalColetaRawValue({
      ...this.getFormDefaults(),
      ...notaFiscalColeta,
    });
    return new FormGroup<NotaFiscalColetaFormGroupContent>({
      id: new FormControl(
        { value: notaFiscalColetaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numero: new FormControl(notaFiscalColetaRawValue.numero, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(20)],
      }),
      serie: new FormControl(notaFiscalColetaRawValue.serie, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(20)],
      }),
      remetente: new FormControl(notaFiscalColetaRawValue.remetente, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      destinatario: new FormControl(notaFiscalColetaRawValue.destinatario, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      metroCubico: new FormControl(notaFiscalColetaRawValue.metroCubico, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      quantidade: new FormControl(notaFiscalColetaRawValue.quantidade, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      peso: new FormControl(notaFiscalColetaRawValue.peso, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      dataEmissao: new FormControl(notaFiscalColetaRawValue.dataEmissao),
      dataSaida: new FormControl(notaFiscalColetaRawValue.dataSaida),
      valorTotal: new FormControl(notaFiscalColetaRawValue.valorTotal, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      pesoTotal: new FormControl(notaFiscalColetaRawValue.pesoTotal, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      quantidadeTotal: new FormControl(notaFiscalColetaRawValue.quantidadeTotal, {
        validators: [Validators.min(1), Validators.max(4)],
      }),
      observacao: new FormControl(notaFiscalColetaRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      createdBy: new FormControl(notaFiscalColetaRawValue.createdBy),
      createdDate: new FormControl(notaFiscalColetaRawValue.createdDate),
      lastModifiedBy: new FormControl(notaFiscalColetaRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(notaFiscalColetaRawValue.lastModifiedDate),
      solicitacaoColeta: new FormControl(notaFiscalColetaRawValue.solicitacaoColeta),
    });
  }

  getNotaFiscalColeta(form: NotaFiscalColetaFormGroup): INotaFiscalColeta | NewNotaFiscalColeta {
    return this.convertNotaFiscalColetaRawValueToNotaFiscalColeta(
      form.getRawValue() as NotaFiscalColetaFormRawValue | NewNotaFiscalColetaFormRawValue,
    );
  }

  resetForm(form: NotaFiscalColetaFormGroup, notaFiscalColeta: NotaFiscalColetaFormGroupInput): void {
    const notaFiscalColetaRawValue = this.convertNotaFiscalColetaToNotaFiscalColetaRawValue({
      ...this.getFormDefaults(),
      ...notaFiscalColeta,
    });
    form.reset(
      {
        ...notaFiscalColetaRawValue,
        id: { value: notaFiscalColetaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): NotaFiscalColetaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataEmissao: currentTime,
      dataSaida: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertNotaFiscalColetaRawValueToNotaFiscalColeta(
    rawNotaFiscalColeta: NotaFiscalColetaFormRawValue | NewNotaFiscalColetaFormRawValue,
  ): INotaFiscalColeta | NewNotaFiscalColeta {
    return {
      ...rawNotaFiscalColeta,
      dataEmissao: dayjs(rawNotaFiscalColeta.dataEmissao, DATE_TIME_FORMAT),
      dataSaida: dayjs(rawNotaFiscalColeta.dataSaida, DATE_TIME_FORMAT),
      createdDate: dayjs(rawNotaFiscalColeta.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawNotaFiscalColeta.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertNotaFiscalColetaToNotaFiscalColetaRawValue(
    notaFiscalColeta: INotaFiscalColeta | (Partial<NewNotaFiscalColeta> & NotaFiscalColetaFormDefaults),
  ): NotaFiscalColetaFormRawValue | PartialWithRequiredKeyOf<NewNotaFiscalColetaFormRawValue> {
    return {
      ...notaFiscalColeta,
      dataEmissao: notaFiscalColeta.dataEmissao ? notaFiscalColeta.dataEmissao.format(DATE_TIME_FORMAT) : undefined,
      dataSaida: notaFiscalColeta.dataSaida ? notaFiscalColeta.dataSaida.format(DATE_TIME_FORMAT) : undefined,
      createdDate: notaFiscalColeta.createdDate ? notaFiscalColeta.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: notaFiscalColeta.lastModifiedDate ? notaFiscalColeta.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
