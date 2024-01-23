import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITipoFrete, NewTipoFrete } from '../tipo-frete.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoFrete for edit and NewTipoFreteFormGroupInput for create.
 */
type TipoFreteFormGroupInput = ITipoFrete | PartialWithRequiredKeyOf<NewTipoFrete>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITipoFrete | NewTipoFrete> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type TipoFreteFormRawValue = FormValueOf<ITipoFrete>;

type NewTipoFreteFormRawValue = FormValueOf<NewTipoFrete>;

type TipoFreteFormDefaults = Pick<NewTipoFrete, 'id' | 'createdDate' | 'lastModifiedDate'>;

type TipoFreteFormGroupContent = {
  id: FormControl<TipoFreteFormRawValue['id'] | NewTipoFrete['id']>;
  nome: FormControl<TipoFreteFormRawValue['nome']>;
  descricao: FormControl<TipoFreteFormRawValue['descricao']>;
  createdBy: FormControl<TipoFreteFormRawValue['createdBy']>;
  createdDate: FormControl<TipoFreteFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<TipoFreteFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<TipoFreteFormRawValue['lastModifiedDate']>;
};

export type TipoFreteFormGroup = FormGroup<TipoFreteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoFreteFormService {
  createTipoFreteFormGroup(tipoFrete: TipoFreteFormGroupInput = { id: null }): TipoFreteFormGroup {
    const tipoFreteRawValue = this.convertTipoFreteToTipoFreteRawValue({
      ...this.getFormDefaults(),
      ...tipoFrete,
    });
    return new FormGroup<TipoFreteFormGroupContent>({
      id: new FormControl(
        { value: tipoFreteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(tipoFreteRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      descricao: new FormControl(tipoFreteRawValue.descricao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      createdBy: new FormControl(tipoFreteRawValue.createdBy),
      createdDate: new FormControl(tipoFreteRawValue.createdDate),
      lastModifiedBy: new FormControl(tipoFreteRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(tipoFreteRawValue.lastModifiedDate),
    });
  }

  getTipoFrete(form: TipoFreteFormGroup): ITipoFrete | NewTipoFrete {
    return this.convertTipoFreteRawValueToTipoFrete(form.getRawValue() as TipoFreteFormRawValue | NewTipoFreteFormRawValue);
  }

  resetForm(form: TipoFreteFormGroup, tipoFrete: TipoFreteFormGroupInput): void {
    const tipoFreteRawValue = this.convertTipoFreteToTipoFreteRawValue({ ...this.getFormDefaults(), ...tipoFrete });
    form.reset(
      {
        ...tipoFreteRawValue,
        id: { value: tipoFreteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoFreteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertTipoFreteRawValueToTipoFrete(rawTipoFrete: TipoFreteFormRawValue | NewTipoFreteFormRawValue): ITipoFrete | NewTipoFrete {
    return {
      ...rawTipoFrete,
      createdDate: dayjs(rawTipoFrete.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawTipoFrete.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertTipoFreteToTipoFreteRawValue(
    tipoFrete: ITipoFrete | (Partial<NewTipoFrete> & TipoFreteFormDefaults),
  ): TipoFreteFormRawValue | PartialWithRequiredKeyOf<NewTipoFreteFormRawValue> {
    return {
      ...tipoFrete,
      createdDate: tipoFrete.createdDate ? tipoFrete.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: tipoFrete.lastModifiedDate ? tipoFrete.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
