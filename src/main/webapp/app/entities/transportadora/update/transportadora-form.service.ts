import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransportadora, NewTransportadora } from '../transportadora.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransportadora for edit and NewTransportadoraFormGroupInput for create.
 */
type TransportadoraFormGroupInput = ITransportadora | PartialWithRequiredKeyOf<NewTransportadora>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransportadora | NewTransportadora> = Omit<T, 'dataCadastro' | 'dataAtualizacao'> & {
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
};

type TransportadoraFormRawValue = FormValueOf<ITransportadora>;

type NewTransportadoraFormRawValue = FormValueOf<NewTransportadora>;

type TransportadoraFormDefaults = Pick<NewTransportadora, 'id' | 'dataCadastro' | 'dataAtualizacao'>;

type TransportadoraFormGroupContent = {
  id: FormControl<TransportadoraFormRawValue['id'] | NewTransportadora['id']>;
  nome: FormControl<TransportadoraFormRawValue['nome']>;
  cnpj: FormControl<TransportadoraFormRawValue['cnpj']>;
  razaoSocial: FormControl<TransportadoraFormRawValue['razaoSocial']>;
  inscricaoEstadual: FormControl<TransportadoraFormRawValue['inscricaoEstadual']>;
  inscricaoMunicipal: FormControl<TransportadoraFormRawValue['inscricaoMunicipal']>;
  responsavel: FormControl<TransportadoraFormRawValue['responsavel']>;
  cep: FormControl<TransportadoraFormRawValue['cep']>;
  endereco: FormControl<TransportadoraFormRawValue['endereco']>;
  numero: FormControl<TransportadoraFormRawValue['numero']>;
  complemento: FormControl<TransportadoraFormRawValue['complemento']>;
  bairro: FormControl<TransportadoraFormRawValue['bairro']>;
  telefone: FormControl<TransportadoraFormRawValue['telefone']>;
  email: FormControl<TransportadoraFormRawValue['email']>;
  observacao: FormControl<TransportadoraFormRawValue['observacao']>;
  dataCadastro: FormControl<TransportadoraFormRawValue['dataCadastro']>;
  usuarioCadastro: FormControl<TransportadoraFormRawValue['usuarioCadastro']>;
  dataAtualizacao: FormControl<TransportadoraFormRawValue['dataAtualizacao']>;
  usuarioAtualizacao: FormControl<TransportadoraFormRawValue['usuarioAtualizacao']>;
};

export type TransportadoraFormGroup = FormGroup<TransportadoraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransportadoraFormService {
  createTransportadoraFormGroup(transportadora: TransportadoraFormGroupInput = { id: null }): TransportadoraFormGroup {
    const transportadoraRawValue = this.convertTransportadoraToTransportadoraRawValue({
      ...this.getFormDefaults(),
      ...transportadora,
    });
    return new FormGroup<TransportadoraFormGroupContent>({
      id: new FormControl(
        { value: transportadoraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(transportadoraRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      cnpj: new FormControl(transportadoraRawValue.cnpj, {
        validators: [Validators.required, Validators.minLength(14), Validators.maxLength(14)],
      }),
      razaoSocial: new FormControl(transportadoraRawValue.razaoSocial, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      inscricaoEstadual: new FormControl(transportadoraRawValue.inscricaoEstadual, {
        validators: [Validators.minLength(2), Validators.maxLength(20)],
      }),
      inscricaoMunicipal: new FormControl(transportadoraRawValue.inscricaoMunicipal, {
        validators: [Validators.minLength(2), Validators.maxLength(20)],
      }),
      responsavel: new FormControl(transportadoraRawValue.responsavel, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      cep: new FormControl(transportadoraRawValue.cep, {
        validators: [Validators.minLength(8), Validators.maxLength(8)],
      }),
      endereco: new FormControl(transportadoraRawValue.endereco, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      numero: new FormControl(transportadoraRawValue.numero, {
        validators: [Validators.minLength(1), Validators.maxLength(10)],
      }),
      complemento: new FormControl(transportadoraRawValue.complemento, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      bairro: new FormControl(transportadoraRawValue.bairro, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      telefone: new FormControl(transportadoraRawValue.telefone, {
        validators: [Validators.minLength(10), Validators.maxLength(11)],
      }),
      email: new FormControl(transportadoraRawValue.email, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      observacao: new FormControl(transportadoraRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      dataCadastro: new FormControl(transportadoraRawValue.dataCadastro, {
        validators: [Validators.required],
      }),
      usuarioCadastro: new FormControl(transportadoraRawValue.usuarioCadastro, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      dataAtualizacao: new FormControl(transportadoraRawValue.dataAtualizacao),
      usuarioAtualizacao: new FormControl(transportadoraRawValue.usuarioAtualizacao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
    });
  }

  getTransportadora(form: TransportadoraFormGroup): ITransportadora | NewTransportadora {
    return this.convertTransportadoraRawValueToTransportadora(
      form.getRawValue() as TransportadoraFormRawValue | NewTransportadoraFormRawValue,
    );
  }

  resetForm(form: TransportadoraFormGroup, transportadora: TransportadoraFormGroupInput): void {
    const transportadoraRawValue = this.convertTransportadoraToTransportadoraRawValue({ ...this.getFormDefaults(), ...transportadora });
    form.reset(
      {
        ...transportadoraRawValue,
        id: { value: transportadoraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TransportadoraFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataCadastro: currentTime,
      dataAtualizacao: currentTime,
    };
  }

  private convertTransportadoraRawValueToTransportadora(
    rawTransportadora: TransportadoraFormRawValue | NewTransportadoraFormRawValue,
  ): ITransportadora | NewTransportadora {
    return {
      ...rawTransportadora,
      dataCadastro: dayjs(rawTransportadora.dataCadastro, DATE_TIME_FORMAT),
      dataAtualizacao: dayjs(rawTransportadora.dataAtualizacao, DATE_TIME_FORMAT),
    };
  }

  private convertTransportadoraToTransportadoraRawValue(
    transportadora: ITransportadora | (Partial<NewTransportadora> & TransportadoraFormDefaults),
  ): TransportadoraFormRawValue | PartialWithRequiredKeyOf<NewTransportadoraFormRawValue> {
    return {
      ...transportadora,
      dataCadastro: transportadora.dataCadastro ? transportadora.dataCadastro.format(DATE_TIME_FORMAT) : undefined,
      dataAtualizacao: transportadora.dataAtualizacao ? transportadora.dataAtualizacao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
