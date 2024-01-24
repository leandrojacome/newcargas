import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEndereco, NewEndereco } from '../endereco.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: NonNullable<T['id']> };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEndereco for edit and NewEnderecoFormGroupInput for create.
 */
type EnderecoFormGroupInput = IEndereco | PartialWithRequiredId<NewEndereco>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEndereco | NewEndereco> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type EnderecoFormRawValue = FormValueOf<IEndereco>;

type NewEnderecoFormRawValue = FormValueOf<NewEndereco> & { id: number };

type EnderecoFormDefaults = Pick<NewEndereco, 'id' | 'createdDate' | 'lastModifiedDate'>;

type PartialWithRequiredId<T extends { id?: unknown }> = Partial<Omit<T, 'id'>> & { id: NonNullable<T['id']> | null };

type EnderecoFormGroupContent = {
  id: FormControl<EnderecoFormRawValue['id'] | NewEndereco['id']>;
  tipo: FormControl<EnderecoFormRawValue['tipo']>;
  cep: FormControl<EnderecoFormRawValue['cep']>;
  endereco: FormControl<EnderecoFormRawValue['endereco']>;
  numero: FormControl<EnderecoFormRawValue['numero']>;
  complemento: FormControl<EnderecoFormRawValue['complemento']>;
  bairro: FormControl<EnderecoFormRawValue['bairro']>;
  createdBy: FormControl<EnderecoFormRawValue['createdBy']>;
  createdDate: FormControl<EnderecoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<EnderecoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<EnderecoFormRawValue['lastModifiedDate']>;
  estado: FormControl<EnderecoFormRawValue['estado']>;
  cidade: FormControl<EnderecoFormRawValue['cidade']>;
  embarcador: FormControl<EnderecoFormRawValue['embarcador']>;
  transportadora: FormControl<EnderecoFormRawValue['transportadora']>;
  notaFiscalColetaOrigem: FormControl<EnderecoFormRawValue['notaFiscalColetaOrigem']>;
  notaFiscalColetaDestino: FormControl<EnderecoFormRawValue['notaFiscalColetaDestino']>;
  solicitacaoColetaOrigem: FormControl<EnderecoFormRawValue['solicitacaoColetaOrigem']>;
  solicitacaoColetaDestino: FormControl<EnderecoFormRawValue['solicitacaoColetaDestino']>;
};

export type EnderecoFormGroup = FormGroup<EnderecoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnderecoFormService {
  createEnderecoFormGroup(endereco: EnderecoFormGroupInput = { id: null }): EnderecoFormGroup {
    const enderecoRawValue = this.convertEnderecoToEnderecoRawValue({
      ...this.getFormDefaults(),
      ...endereco,
    });
    return new FormGroup<EnderecoFormGroupContent>({
      id: new FormControl(
        { value: enderecoRawValue.id ?? 0, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipo: new FormControl(enderecoRawValue.tipo, {
        validators: [Validators.required],
      }),
      cep: new FormControl(enderecoRawValue.cep, {
        validators: [Validators.required, Validators.minLength(8), Validators.maxLength(8)],
      }),
      endereco: new FormControl(enderecoRawValue.endereco, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      numero: new FormControl(enderecoRawValue.numero, {
        validators: [Validators.minLength(1), Validators.maxLength(10)],
      }),
      complemento: new FormControl(enderecoRawValue.complemento, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      bairro: new FormControl(enderecoRawValue.bairro, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      createdBy: new FormControl(enderecoRawValue.createdBy),
      createdDate: new FormControl(enderecoRawValue.createdDate),
      lastModifiedBy: new FormControl(enderecoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(enderecoRawValue.lastModifiedDate),
      estado: new FormControl(enderecoRawValue.estado),
      cidade: new FormControl(enderecoRawValue.cidade),
      embarcador: new FormControl(enderecoRawValue.embarcador),
      transportadora: new FormControl(enderecoRawValue.transportadora),
      notaFiscalColetaOrigem: new FormControl(enderecoRawValue.notaFiscalColetaOrigem),
      notaFiscalColetaDestino: new FormControl(enderecoRawValue.notaFiscalColetaDestino),
      solicitacaoColetaOrigem: new FormControl(enderecoRawValue.solicitacaoColetaOrigem),
      solicitacaoColetaDestino: new FormControl(enderecoRawValue.solicitacaoColetaDestino),
    });
  }

  getEndereco(form: EnderecoFormGroup): IEndereco | NewEndereco {
    return this.convertEnderecoRawValueToEndereco(form.getRawValue() as EnderecoFormRawValue | NewEnderecoFormRawValue);
  }

  resetForm(form: EnderecoFormGroup, endereco: EnderecoFormGroupInput): void {
    const enderecoRawValue = this.convertEnderecoToEnderecoRawValue({ ...this.getFormDefaults(), ...endereco });
    form.reset(
      {
        ...enderecoRawValue,
        id: { value: enderecoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnderecoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertEnderecoRawValueToEndereco(rawEndereco: EnderecoFormRawValue | NewEnderecoFormRawValue): IEndereco | NewEndereco {
    return {
      ...rawEndereco,
      createdDate: dayjs(rawEndereco.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawEndereco.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertEnderecoToEnderecoRawValue(
    endereco: IEndereco | (Partial<NewEndereco> & EnderecoFormDefaults),
  ): EnderecoFormRawValue | PartialWithRequiredKeyOf<NewEnderecoFormRawValue> {
    return {
      ...endereco,
      createdDate: endereco.createdDate ? endereco.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: endereco.lastModifiedDate ? endereco.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
      id: endereco.id ?? 0, // Fornecendo um valor padrão para id
    };
  }
}
