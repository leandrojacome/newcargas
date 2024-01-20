import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type EstadoFormDefaults = Pick<NewEstado, 'id'>;

type EstadoFormGroupContent = {
  id: FormControl<IEstado['id'] | NewEstado['id']>;
  nome: FormControl<IEstado['nome']>;
  sigla: FormControl<IEstado['sigla']>;
  codigoIbge: FormControl<IEstado['codigoIbge']>;
};

export type EstadoFormGroup = FormGroup<EstadoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EstadoFormService {
  createEstadoFormGroup(estado: EstadoFormGroupInput = { id: null }): EstadoFormGroup {
    const estadoRawValue = {
      ...this.getFormDefaults(),
      ...estado,
    };
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
    });
  }

  getEstado(form: EstadoFormGroup): IEstado | NewEstado {
    return form.getRawValue() as IEstado | NewEstado;
  }

  resetForm(form: EstadoFormGroup, estado: EstadoFormGroupInput): void {
    const estadoRawValue = { ...this.getFormDefaults(), ...estado };
    form.reset(
      {
        ...estadoRawValue,
        id: { value: estadoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EstadoFormDefaults {
    return {
      id: null,
    };
  }
}
