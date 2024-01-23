import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITabelaFrete, NewTabelaFrete } from '../tabela-frete.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITabelaFrete for edit and NewTabelaFreteFormGroupInput for create.
 */
type TabelaFreteFormGroupInput = ITabelaFrete | PartialWithRequiredKeyOf<NewTabelaFrete>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITabelaFrete | NewTabelaFrete> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type TabelaFreteFormRawValue = FormValueOf<ITabelaFrete>;

type NewTabelaFreteFormRawValue = FormValueOf<NewTabelaFrete>;

type TabelaFreteFormDefaults = Pick<NewTabelaFrete, 'id' | 'createdDate' | 'lastModifiedDate'>;

type TabelaFreteFormGroupContent = {
  id: FormControl<TabelaFreteFormRawValue['id'] | NewTabelaFrete['id']>;
  tipo: FormControl<TabelaFreteFormRawValue['tipo']>;
  nome: FormControl<TabelaFreteFormRawValue['nome']>;
  descricao: FormControl<TabelaFreteFormRawValue['descricao']>;
  leadTime: FormControl<TabelaFreteFormRawValue['leadTime']>;
  freteMinimo: FormControl<TabelaFreteFormRawValue['freteMinimo']>;
  valorTonelada: FormControl<TabelaFreteFormRawValue['valorTonelada']>;
  valorMetroCubico: FormControl<TabelaFreteFormRawValue['valorMetroCubico']>;
  valorUnidade: FormControl<TabelaFreteFormRawValue['valorUnidade']>;
  valorKm: FormControl<TabelaFreteFormRawValue['valorKm']>;
  valorAdicional: FormControl<TabelaFreteFormRawValue['valorAdicional']>;
  valorColeta: FormControl<TabelaFreteFormRawValue['valorColeta']>;
  valorEntrega: FormControl<TabelaFreteFormRawValue['valorEntrega']>;
  valorTotal: FormControl<TabelaFreteFormRawValue['valorTotal']>;
  valorKmAdicional: FormControl<TabelaFreteFormRawValue['valorKmAdicional']>;
  createdBy: FormControl<TabelaFreteFormRawValue['createdBy']>;
  createdDate: FormControl<TabelaFreteFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<TabelaFreteFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<TabelaFreteFormRawValue['lastModifiedDate']>;
  embarcador: FormControl<TabelaFreteFormRawValue['embarcador']>;
  transportadora: FormControl<TabelaFreteFormRawValue['transportadora']>;
  tipoCarga: FormControl<TabelaFreteFormRawValue['tipoCarga']>;
  tipoFrete: FormControl<TabelaFreteFormRawValue['tipoFrete']>;
  formaCobranca: FormControl<TabelaFreteFormRawValue['formaCobranca']>;
  regiaoOrigem: FormControl<TabelaFreteFormRawValue['regiaoOrigem']>;
  regiaoDestino: FormControl<TabelaFreteFormRawValue['regiaoDestino']>;
};

export type TabelaFreteFormGroup = FormGroup<TabelaFreteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TabelaFreteFormService {
  createTabelaFreteFormGroup(tabelaFrete: TabelaFreteFormGroupInput = { id: null }): TabelaFreteFormGroup {
    const tabelaFreteRawValue = this.convertTabelaFreteToTabelaFreteRawValue({
      ...this.getFormDefaults(),
      ...tabelaFrete,
    });
    return new FormGroup<TabelaFreteFormGroupContent>({
      id: new FormControl(
        { value: tabelaFreteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipo: new FormControl(tabelaFreteRawValue.tipo, {
        validators: [Validators.required],
      }),
      nome: new FormControl(tabelaFreteRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      descricao: new FormControl(tabelaFreteRawValue.descricao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      leadTime: new FormControl(tabelaFreteRawValue.leadTime, {
        validators: [Validators.min(1), Validators.max(4)],
      }),
      freteMinimo: new FormControl(tabelaFreteRawValue.freteMinimo, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      valorTonelada: new FormControl(tabelaFreteRawValue.valorTonelada, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      valorMetroCubico: new FormControl(tabelaFreteRawValue.valorMetroCubico, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      valorUnidade: new FormControl(tabelaFreteRawValue.valorUnidade, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      valorKm: new FormControl(tabelaFreteRawValue.valorKm, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      valorAdicional: new FormControl(tabelaFreteRawValue.valorAdicional, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      valorColeta: new FormControl(tabelaFreteRawValue.valorColeta, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      valorEntrega: new FormControl(tabelaFreteRawValue.valorEntrega, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      valorTotal: new FormControl(tabelaFreteRawValue.valorTotal, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      valorKmAdicional: new FormControl(tabelaFreteRawValue.valorKmAdicional, {
        validators: [Validators.min(1), Validators.max(10)],
      }),
      createdBy: new FormControl(tabelaFreteRawValue.createdBy),
      createdDate: new FormControl(tabelaFreteRawValue.createdDate),
      lastModifiedBy: new FormControl(tabelaFreteRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(tabelaFreteRawValue.lastModifiedDate),
      embarcador: new FormControl(tabelaFreteRawValue.embarcador),
      transportadora: new FormControl(tabelaFreteRawValue.transportadora),
      tipoCarga: new FormControl(tabelaFreteRawValue.tipoCarga),
      tipoFrete: new FormControl(tabelaFreteRawValue.tipoFrete),
      formaCobranca: new FormControl(tabelaFreteRawValue.formaCobranca),
      regiaoOrigem: new FormControl(tabelaFreteRawValue.regiaoOrigem),
      regiaoDestino: new FormControl(tabelaFreteRawValue.regiaoDestino),
    });
  }

  getTabelaFrete(form: TabelaFreteFormGroup): ITabelaFrete | NewTabelaFrete {
    return this.convertTabelaFreteRawValueToTabelaFrete(form.getRawValue() as TabelaFreteFormRawValue | NewTabelaFreteFormRawValue);
  }

  resetForm(form: TabelaFreteFormGroup, tabelaFrete: TabelaFreteFormGroupInput): void {
    const tabelaFreteRawValue = this.convertTabelaFreteToTabelaFreteRawValue({ ...this.getFormDefaults(), ...tabelaFrete });
    form.reset(
      {
        ...tabelaFreteRawValue,
        id: { value: tabelaFreteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TabelaFreteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertTabelaFreteRawValueToTabelaFrete(
    rawTabelaFrete: TabelaFreteFormRawValue | NewTabelaFreteFormRawValue,
  ): ITabelaFrete | NewTabelaFrete {
    return {
      ...rawTabelaFrete,
      createdDate: dayjs(rawTabelaFrete.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawTabelaFrete.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTabelaFreteToTabelaFreteRawValue(
    tabelaFrete: ITabelaFrete | (Partial<NewTabelaFrete> & TabelaFreteFormDefaults),
  ): TabelaFreteFormRawValue | PartialWithRequiredKeyOf<NewTabelaFreteFormRawValue> {
    return {
      ...tabelaFrete,
      createdDate: tabelaFrete.createdDate ? tabelaFrete.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: tabelaFrete.lastModifiedDate ? tabelaFrete.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
