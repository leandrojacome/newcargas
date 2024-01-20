import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRegiao, NewRegiao } from '../regiao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRegiao for edit and NewRegiaoFormGroupInput for create.
 */
type RegiaoFormGroupInput = IRegiao | PartialWithRequiredKeyOf<NewRegiao>;

type RegiaoFormDefaults = Pick<NewRegiao, 'id'>;

type RegiaoFormGroupContent = {
  id: FormControl<IRegiao['id'] | NewRegiao['id']>;
  nome: FormControl<IRegiao['nome']>;
  sigla: FormControl<IRegiao['sigla']>;
  descricao: FormControl<IRegiao['descricao']>;
};

export type RegiaoFormGroup = FormGroup<RegiaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RegiaoFormService {
  createRegiaoFormGroup(regiao: RegiaoFormGroupInput = { id: null }): RegiaoFormGroup {
    const regiaoRawValue = {
      ...this.getFormDefaults(),
      ...regiao,
    };
    return new FormGroup<RegiaoFormGroupContent>({
      id: new FormControl(
        { value: regiaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(regiaoRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      sigla: new FormControl(regiaoRawValue.sigla, {
        validators: [Validators.minLength(2), Validators.maxLength(5)],
      }),
      descricao: new FormControl(regiaoRawValue.descricao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
    });
  }

  getRegiao(form: RegiaoFormGroup): IRegiao | NewRegiao {
    return form.getRawValue() as IRegiao | NewRegiao;
  }

  resetForm(form: RegiaoFormGroup, regiao: RegiaoFormGroupInput): void {
    const regiaoRawValue = { ...this.getFormDefaults(), ...regiao };
    form.reset(
      {
        ...regiaoRawValue,
        id: { value: regiaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RegiaoFormDefaults {
    return {
      id: null,
    };
  }
}
