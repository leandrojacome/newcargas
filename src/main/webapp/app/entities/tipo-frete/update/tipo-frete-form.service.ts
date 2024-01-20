import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type TipoFreteFormDefaults = Pick<NewTipoFrete, 'id'>;

type TipoFreteFormGroupContent = {
  id: FormControl<ITipoFrete['id'] | NewTipoFrete['id']>;
  nome: FormControl<ITipoFrete['nome']>;
  descricao: FormControl<ITipoFrete['descricao']>;
};

export type TipoFreteFormGroup = FormGroup<TipoFreteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TipoFreteFormService {
  createTipoFreteFormGroup(tipoFrete: TipoFreteFormGroupInput = { id: null }): TipoFreteFormGroup {
    const tipoFreteRawValue = {
      ...this.getFormDefaults(),
      ...tipoFrete,
    };
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
    });
  }

  getTipoFrete(form: TipoFreteFormGroup): ITipoFrete | NewTipoFrete {
    return form.getRawValue() as ITipoFrete | NewTipoFrete;
  }

  resetForm(form: TipoFreteFormGroup, tipoFrete: TipoFreteFormGroupInput): void {
    const tipoFreteRawValue = { ...this.getFormDefaults(), ...tipoFrete };
    form.reset(
      {
        ...tipoFreteRawValue,
        id: { value: tipoFreteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TipoFreteFormDefaults {
    return {
      id: null,
    };
  }
}
