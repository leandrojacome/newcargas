import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITipoVeiculo, NewTipoVeiculo } from '../tipo-veiculo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITipoVeiculo for edit and NewTipoVeiculoFormGroupInput for create.
 */
type TipoVeiculoFormGroupInput = ITipoVeiculo | PartialWithRequiredKeyOf<NewTipoVeiculo>;

type TipoVeiculoFormDefaults = Pick<NewTipoVeiculo, 'id'>;

type TipoVeiculoFormGroupContent = {
  id: FormControl<ITipoVeiculo['id'] | NewTipoVeiculo['id']>;
  nome: FormControl<ITipoVeiculo['nome']>;
  descricao: FormControl<ITipoVeiculo['descricao']>;
};

export type TipoVeiculoFormGroup = FormGroup<TipoVeiculoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoVeiculoFormService {
  createTipoVeiculoFormGroup(tipoVeiculo: TipoVeiculoFormGroupInput = { id: null }): TipoVeiculoFormGroup {
    const tipoVeiculoRawValue = {
      ...this.getFormDefaults(),
      ...tipoVeiculo,
    };
    return new FormGroup<TipoVeiculoFormGroupContent>({
      id: new FormControl(
        { value: tipoVeiculoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(tipoVeiculoRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      descricao: new FormControl(tipoVeiculoRawValue.descricao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
    });
  }

  getTipoVeiculo(form: TipoVeiculoFormGroup): ITipoVeiculo | NewTipoVeiculo {
    return form.getRawValue() as ITipoVeiculo | NewTipoVeiculo;
  }

  resetForm(form: TipoVeiculoFormGroup, tipoVeiculo: TipoVeiculoFormGroupInput): void {
    const tipoVeiculoRawValue = { ...this.getFormDefaults(), ...tipoVeiculo };
    form.reset(
      {
        ...tipoVeiculoRawValue,
        id: { value: tipoVeiculoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoVeiculoFormDefaults {
    return {
      id: null,
    };
  }
}
