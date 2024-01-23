import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICidade, NewCidade } from '../cidade.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICidade for edit and NewCidadeFormGroupInput for create.
 */
type CidadeFormGroupInput = ICidade | PartialWithRequiredKeyOf<NewCidade>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICidade | NewCidade> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type CidadeFormRawValue = FormValueOf<ICidade>;

type NewCidadeFormRawValue = FormValueOf<NewCidade>;

type CidadeFormDefaults = Pick<NewCidade, 'id' | 'createdDate' | 'lastModifiedDate'>;

type CidadeFormGroupContent = {
  id: FormControl<CidadeFormRawValue['id'] | NewCidade['id']>;
  nome: FormControl<CidadeFormRawValue['nome']>;
  codigoIbge: FormControl<CidadeFormRawValue['codigoIbge']>;
  createdBy: FormControl<CidadeFormRawValue['createdBy']>;
  createdDate: FormControl<CidadeFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<CidadeFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<CidadeFormRawValue['lastModifiedDate']>;
  estado: FormControl<CidadeFormRawValue['estado']>;
};

export type CidadeFormGroup = FormGroup<CidadeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CidadeFormService {
  createCidadeFormGroup(cidade: CidadeFormGroupInput = { id: null }): CidadeFormGroup {
    const cidadeRawValue = this.convertCidadeToCidadeRawValue({
      ...this.getFormDefaults(),
      ...cidade,
    });
    return new FormGroup<CidadeFormGroupContent>({
      id: new FormControl(
        { value: cidadeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(cidadeRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      codigoIbge: new FormControl(cidadeRawValue.codigoIbge, {
        validators: [Validators.min(7), Validators.max(7)],
      }),
      createdBy: new FormControl(cidadeRawValue.createdBy),
      createdDate: new FormControl(cidadeRawValue.createdDate),
      lastModifiedBy: new FormControl(cidadeRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(cidadeRawValue.lastModifiedDate),
      estado: new FormControl(cidadeRawValue.estado),
    });
  }

  getCidade(form: CidadeFormGroup): ICidade | NewCidade {
    return this.convertCidadeRawValueToCidade(form.getRawValue() as CidadeFormRawValue | NewCidadeFormRawValue);
  }

  resetForm(form: CidadeFormGroup, cidade: CidadeFormGroupInput): void {
    const cidadeRawValue = this.convertCidadeToCidadeRawValue({ ...this.getFormDefaults(), ...cidade });
    form.reset(
      {
        ...cidadeRawValue,
        id: { value: cidadeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CidadeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertCidadeRawValueToCidade(rawCidade: CidadeFormRawValue | NewCidadeFormRawValue): ICidade | NewCidade {
    return {
      ...rawCidade,
      createdDate: dayjs(rawCidade.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawCidade.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertCidadeToCidadeRawValue(
    cidade: ICidade | (Partial<NewCidade> & CidadeFormDefaults),
  ): CidadeFormRawValue | PartialWithRequiredKeyOf<NewCidadeFormRawValue> {
    return {
      ...cidade,
      createdDate: cidade.createdDate ? cidade.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: cidade.lastModifiedDate ? cidade.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
