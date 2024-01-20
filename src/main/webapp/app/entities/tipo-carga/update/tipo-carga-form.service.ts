import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITipoCarga, NewTipoCarga } from '../tipo-carga.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoCarga for edit and NewTipoCargaFormGroupInput for create.
 */
type TipoCargaFormGroupInput = ITipoCarga | PartialWithRequiredKeyOf<NewTipoCarga>;

type TipoCargaFormDefaults = Pick<NewTipoCarga, 'id'>;

type TipoCargaFormGroupContent = {
  id: FormControl<ITipoCarga['id'] | NewTipoCarga['id']>;
  nome: FormControl<ITipoCarga['nome']>;
  descricao: FormControl<ITipoCarga['descricao']>;
};

export type TipoCargaFormGroup = FormGroup<TipoCargaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoCargaFormService {
  createTipoCargaFormGroup(tipoCarga: TipoCargaFormGroupInput = { id: null }): TipoCargaFormGroup {
    const tipoCargaRawValue = {
      ...this.getFormDefaults(),
      ...tipoCarga,
    };
    return new FormGroup<TipoCargaFormGroupContent>({
      id: new FormControl(
        { value: tipoCargaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(tipoCargaRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      descricao: new FormControl(tipoCargaRawValue.descricao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
    });
  }

  getTipoCarga(form: TipoCargaFormGroup): ITipoCarga | NewTipoCarga {
    return form.getRawValue() as ITipoCarga | NewTipoCarga;
  }

  resetForm(form: TipoCargaFormGroup, tipoCarga: TipoCargaFormGroupInput): void {
    const tipoCargaRawValue = { ...this.getFormDefaults(), ...tipoCarga };
    form.reset(
      {
        ...tipoCargaRawValue,
        id: { value: tipoCargaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoCargaFormDefaults {
    return {
      id: null,
    };
  }
}
