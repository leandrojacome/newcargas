import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEstado, NewEstado } from '../estado.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEstado for edit and NewEstadoFormGroupInput for create.
 */
type EstadoFormGroupInput = IEstado | PartialWithRequiredKeyOf<NewEstado>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEstado | NewEstado> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type EstadoFormRawValue = FormValueOf<IEstado>;

type NewEstadoFormRawValue = FormValueOf<NewEstado>;

type EstadoFormDefaults = Pick<NewEstado, 'id' | 'createdDate' | 'lastModifiedDate'>;

type EstadoFormGroupContent = {
  id: FormControl<EstadoFormRawValue['id'] | NewEstado['id']>;
  nome: FormControl<EstadoFormRawValue['nome']>;
  sigla: FormControl<EstadoFormRawValue['sigla']>;
  codigoIbge: FormControl<EstadoFormRawValue['codigoIbge']>;
  createdBy: FormControl<EstadoFormRawValue['createdBy']>;
  createdDate: FormControl<EstadoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<EstadoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<EstadoFormRawValue['lastModifiedDate']>;
};

export type EstadoFormGroup = FormGroup<EstadoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EstadoFormService {
  createEstadoFormGroup(estado: EstadoFormGroupInput = { id: null }): EstadoFormGroup {
    const estadoRawValue = this.convertEstadoToEstadoRawValue({
      ...this.getFormDefaults(),
      ...estado,
    });
    return new FormGroup<EstadoFormGroupContent>({
      id: new FormControl(
        { value: estadoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(estadoRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      sigla: new FormControl(estadoRawValue.sigla, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(2)],
      }),
      codigoIbge: new FormControl(estadoRawValue.codigoIbge, {
        validators: [Validators.min(7), Validators.max(7)],
      }),
      createdBy: new FormControl(estadoRawValue.createdBy),
      createdDate: new FormControl(estadoRawValue.createdDate),
      lastModifiedBy: new FormControl(estadoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(estadoRawValue.lastModifiedDate),
    });
  }

  getEstado(form: EstadoFormGroup): IEstado | NewEstado {
    return this.convertEstadoRawValueToEstado(form.getRawValue() as EstadoFormRawValue | NewEstadoFormRawValue);
  }

  resetForm(form: EstadoFormGroup, estado: EstadoFormGroupInput): void {
    const estadoRawValue = this.convertEstadoToEstadoRawValue({ ...this.getFormDefaults(), ...estado });
    form.reset(
      {
        ...estadoRawValue,
        id: { value: estadoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EstadoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertEstadoRawValueToEstado(rawEstado: EstadoFormRawValue | NewEstadoFormRawValue): IEstado | NewEstado {
    return {
      ...rawEstado,
      createdDate: dayjs(rawEstado.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawEstado.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertEstadoToEstadoRawValue(
    estado: IEstado | (Partial<NewEstado> & EstadoFormDefaults),
  ): EstadoFormRawValue | PartialWithRequiredKeyOf<NewEstadoFormRawValue> {
    return {
      ...estado,
      createdDate: estado.createdDate ? estado.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: estado.lastModifiedDate ? estado.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
