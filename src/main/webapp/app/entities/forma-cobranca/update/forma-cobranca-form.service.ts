import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFormaCobranca | NewFormaCobranca> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type FormaCobrancaFormRawValue = FormValueOf<IFormaCobranca>;

type NewFormaCobrancaFormRawValue = FormValueOf<NewFormaCobranca>;

type FormaCobrancaFormDefaults = Pick<NewFormaCobranca, 'id' | 'createdDate' | 'lastModifiedDate'>;

type FormaCobrancaFormGroupContent = {
  id: FormControl<FormaCobrancaFormRawValue['id'] | NewFormaCobranca['id']>;
  nome: FormControl<FormaCobrancaFormRawValue['nome']>;
  descricao: FormControl<FormaCobrancaFormRawValue['descricao']>;
  createdBy: FormControl<FormaCobrancaFormRawValue['createdBy']>;
  createdDate: FormControl<FormaCobrancaFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<FormaCobrancaFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<FormaCobrancaFormRawValue['lastModifiedDate']>;
};

export type FormaCobrancaFormGroup = FormGroup<FormaCobrancaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormaCobrancaFormService {
  createFormaCobrancaFormGroup(formaCobranca: FormaCobrancaFormGroupInput = { id: null }): FormaCobrancaFormGroup {
    const formaCobrancaRawValue = this.convertFormaCobrancaToFormaCobrancaRawValue({
      ...this.getFormDefaults(),
      ...formaCobranca,
    });
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
      createdBy: new FormControl(formaCobrancaRawValue.createdBy),
      createdDate: new FormControl(formaCobrancaRawValue.createdDate),
      lastModifiedBy: new FormControl(formaCobrancaRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(formaCobrancaRawValue.lastModifiedDate),
    });
  }

  getFormaCobranca(form: FormaCobrancaFormGroup): IFormaCobranca | NewFormaCobranca {
    return this.convertFormaCobrancaRawValueToFormaCobranca(form.getRawValue() as FormaCobrancaFormRawValue | NewFormaCobrancaFormRawValue);
  }

  resetForm(form: FormaCobrancaFormGroup, formaCobranca: FormaCobrancaFormGroupInput): void {
    const formaCobrancaRawValue = this.convertFormaCobrancaToFormaCobrancaRawValue({ ...this.getFormDefaults(), ...formaCobranca });
    form.reset(
      {
        ...formaCobrancaRawValue,
        id: { value: formaCobrancaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FormaCobrancaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertFormaCobrancaRawValueToFormaCobranca(
    rawFormaCobranca: FormaCobrancaFormRawValue | NewFormaCobrancaFormRawValue,
  ): IFormaCobranca | NewFormaCobranca {
    return {
      ...rawFormaCobranca,
      createdDate: dayjs(rawFormaCobranca.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawFormaCobranca.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertFormaCobrancaToFormaCobrancaRawValue(
    formaCobranca: IFormaCobranca | (Partial<NewFormaCobranca> & FormaCobrancaFormDefaults),
  ): FormaCobrancaFormRawValue | PartialWithRequiredKeyOf<NewFormaCobrancaFormRawValue> {
    return {
      ...formaCobranca,
      createdDate: formaCobranca.createdDate ? formaCobranca.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: formaCobranca.lastModifiedDate ? formaCobranca.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
