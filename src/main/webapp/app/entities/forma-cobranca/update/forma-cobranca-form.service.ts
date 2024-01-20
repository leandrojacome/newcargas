import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFormaCobranca, NewFormaCobranca } from '../forma-cobranca.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFormaCobranca for edit and NewFormaCobrancaFormGroupInput for create.
 */
type FormaCobrancaFormGroupInput = IFormaCobranca | PartialWithRequiredKeyOf<NewFormaCobranca>;

type FormaCobrancaFormDefaults = Pick<NewFormaCobranca, 'id'>;

type FormaCobrancaFormGroupContent = {
  id: FormControl<IFormaCobranca['id'] | NewFormaCobranca['id']>;
  nome: FormControl<IFormaCobranca['nome']>;
  descricao: FormControl<IFormaCobranca['descricao']>;
};

export type FormaCobrancaFormGroup = FormGroup<FormaCobrancaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormaCobrancaFormService {
  createFormaCobrancaFormGroup(formaCobranca: FormaCobrancaFormGroupInput = { id: null }): FormaCobrancaFormGroup {
    const formaCobrancaRawValue = {
      ...this.getFormDefaults(),
      ...formaCobranca,
    };
    return new FormGroup<FormaCobrancaFormGroupContent>({
      id: new FormControl(
        { value: formaCobrancaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(formaCobrancaRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      descricao: new FormControl(formaCobrancaRawValue.descricao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
    });
  }

  getFormaCobranca(form: FormaCobrancaFormGroup): IFormaCobranca | NewFormaCobranca {
    return form.getRawValue() as IFormaCobranca | NewFormaCobranca;
  }

  resetForm(form: FormaCobrancaFormGroup, formaCobranca: FormaCobrancaFormGroupInput): void {
    const formaCobrancaRawValue = { ...this.getFormDefaults(), ...formaCobranca };
    form.reset(
      {
        ...formaCobrancaRawValue,
        id: { value: formaCobrancaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FormaCobrancaFormDefaults {
    return {
      id: null,
    };
  }
}
