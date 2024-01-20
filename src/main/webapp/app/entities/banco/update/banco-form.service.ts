import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBanco, NewBanco } from '../banco.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBanco for edit and NewBancoFormGroupInput for create.
 */
type BancoFormGroupInput = IBanco | PartialWithRequiredKeyOf<NewBanco>;

type BancoFormDefaults = Pick<NewBanco, 'id'>;

type BancoFormGroupContent = {
  id: FormControl<IBanco['id'] | NewBanco['id']>;
  nome: FormControl<IBanco['nome']>;
  codigo: FormControl<IBanco['codigo']>;
};

export type BancoFormGroup = FormGroup<BancoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BancoFormService {
  createBancoFormGroup(banco: BancoFormGroupInput = { id: null }): BancoFormGroup {
    const bancoRawValue = {
      ...this.getFormDefaults(),
      ...banco,
    };
    return new FormGroup<BancoFormGroupContent>({
      id: new FormControl(
        { value: bancoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(bancoRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      codigo: new FormControl(bancoRawValue.codigo, {
        validators: [Validators.minLength(3), Validators.maxLength(3)],
      }),
    });
  }

  getBanco(form: BancoFormGroup): IBanco | NewBanco {
    return form.getRawValue() as IBanco | NewBanco;
  }

  resetForm(form: BancoFormGroup, banco: BancoFormGroupInput): void {
    const bancoRawValue = { ...this.getFormDefaults(), ...banco };
    form.reset(
      {
        ...bancoRawValue,
        id: { value: bancoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BancoFormDefaults {
    return {
      id: null,
    };
  }
}
