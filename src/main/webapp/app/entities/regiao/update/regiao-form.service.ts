import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRegiao | NewRegiao> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type RegiaoFormRawValue = FormValueOf<IRegiao>;

type NewRegiaoFormRawValue = FormValueOf<NewRegiao>;

type RegiaoFormDefaults = Pick<NewRegiao, 'id' | 'createdDate' | 'lastModifiedDate'>;

type RegiaoFormGroupContent = {
  id: FormControl<RegiaoFormRawValue['id'] | NewRegiao['id']>;
  nome: FormControl<RegiaoFormRawValue['nome']>;
  sigla: FormControl<RegiaoFormRawValue['sigla']>;
  descricao: FormControl<RegiaoFormRawValue['descricao']>;
  createdBy: FormControl<RegiaoFormRawValue['createdBy']>;
  createdDate: FormControl<RegiaoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<RegiaoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<RegiaoFormRawValue['lastModifiedDate']>;
};

export type RegiaoFormGroup = FormGroup<RegiaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RegiaoFormService {
  createRegiaoFormGroup(regiao: RegiaoFormGroupInput = { id: null }): RegiaoFormGroup {
    const regiaoRawValue = this.convertRegiaoToRegiaoRawValue({
      ...this.getFormDefaults(),
      ...regiao,
    });
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
      createdBy: new FormControl(regiaoRawValue.createdBy),
      createdDate: new FormControl(regiaoRawValue.createdDate),
      lastModifiedBy: new FormControl(regiaoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(regiaoRawValue.lastModifiedDate),
    });
  }

  getRegiao(form: RegiaoFormGroup): IRegiao | NewRegiao {
    return this.convertRegiaoRawValueToRegiao(form.getRawValue() as RegiaoFormRawValue | NewRegiaoFormRawValue);
  }

  resetForm(form: RegiaoFormGroup, regiao: RegiaoFormGroupInput): void {
    const regiaoRawValue = this.convertRegiaoToRegiaoRawValue({ ...this.getFormDefaults(), ...regiao });
    form.reset(
      {
        ...regiaoRawValue,
        id: { value: regiaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RegiaoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertRegiaoRawValueToRegiao(rawRegiao: RegiaoFormRawValue | NewRegiaoFormRawValue): IRegiao | NewRegiao {
    return {
      ...rawRegiao,
      createdDate: dayjs(rawRegiao.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawRegiao.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertRegiaoToRegiaoRawValue(
    regiao: IRegiao | (Partial<NewRegiao> & RegiaoFormDefaults),
  ): RegiaoFormRawValue | PartialWithRequiredKeyOf<NewRegiaoFormRawValue> {
    return {
      ...regiao,
      createdDate: regiao.createdDate ? regiao.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: regiao.lastModifiedDate ? regiao.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
