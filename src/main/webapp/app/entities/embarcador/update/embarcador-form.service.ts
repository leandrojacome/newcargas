import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmbarcador, NewEmbarcador } from '../embarcador.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmbarcador for edit and NewEmbarcadorFormGroupInput for create.
 */
type EmbarcadorFormGroupInput = IEmbarcador | PartialWithRequiredKeyOf<NewEmbarcador>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmbarcador | NewEmbarcador> = Omit<T, 'dataCadastro' | 'dataAtualizacao'> & {
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
};

type EmbarcadorFormRawValue = FormValueOf<IEmbarcador>;

type NewEmbarcadorFormRawValue = FormValueOf<NewEmbarcador>;

type EmbarcadorFormDefaults = Pick<NewEmbarcador, 'id' | 'dataCadastro' | 'dataAtualizacao'>;

type EmbarcadorFormGroupContent = {
  id: FormControl<EmbarcadorFormRawValue['id'] | NewEmbarcador['id']>;
  nome: FormControl<EmbarcadorFormRawValue['nome']>;
  cnpj: FormControl<EmbarcadorFormRawValue['cnpj']>;
  razaoSocial: FormControl<EmbarcadorFormRawValue['razaoSocial']>;
  inscricaoEstadual: FormControl<EmbarcadorFormRawValue['inscricaoEstadual']>;
  inscricaoMunicipal: FormControl<EmbarcadorFormRawValue['inscricaoMunicipal']>;
  responsavel: FormControl<EmbarcadorFormRawValue['responsavel']>;
  cep: FormControl<EmbarcadorFormRawValue['cep']>;
  endereco: FormControl<EmbarcadorFormRawValue['endereco']>;
  numero: FormControl<EmbarcadorFormRawValue['numero']>;
  complemento: FormControl<EmbarcadorFormRawValue['complemento']>;
  bairro: FormControl<EmbarcadorFormRawValue['bairro']>;
  telefone: FormControl<EmbarcadorFormRawValue['telefone']>;
  email: FormControl<EmbarcadorFormRawValue['email']>;
  observacao: FormControl<EmbarcadorFormRawValue['observacao']>;
  dataCadastro: FormControl<EmbarcadorFormRawValue['dataCadastro']>;
  usuarioCadastro: FormControl<EmbarcadorFormRawValue['usuarioCadastro']>;
  dataAtualizacao: FormControl<EmbarcadorFormRawValue['dataAtualizacao']>;
  usuarioAtualizacao: FormControl<EmbarcadorFormRawValue['usuarioAtualizacao']>;
};

export type EmbarcadorFormGroup = FormGroup<EmbarcadorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmbarcadorFormService {
  createEmbarcadorFormGroup(embarcador: EmbarcadorFormGroupInput = { id: null }): EmbarcadorFormGroup {
    const embarcadorRawValue = this.convertEmbarcadorToEmbarcadorRawValue({
      ...this.getFormDefaults(),
      ...embarcador,
    });
    return new FormGroup<EmbarcadorFormGroupContent>({
      id: new FormControl(
        { value: embarcadorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(embarcadorRawValue.nome, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(150)],
      }),
      cnpj: new FormControl(embarcadorRawValue.cnpj, {
        validators: [Validators.required, Validators.minLength(14), Validators.maxLength(14)],
      }),
      razaoSocial: new FormControl(embarcadorRawValue.razaoSocial, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      inscricaoEstadual: new FormControl(embarcadorRawValue.inscricaoEstadual, {
        validators: [Validators.minLength(2), Validators.maxLength(20)],
      }),
      inscricaoMunicipal: new FormControl(embarcadorRawValue.inscricaoMunicipal, {
        validators: [Validators.minLength(2), Validators.maxLength(20)],
      }),
      responsavel: new FormControl(embarcadorRawValue.responsavel, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      cep: new FormControl(embarcadorRawValue.cep, {
        validators: [Validators.minLength(8), Validators.maxLength(8)],
      }),
      endereco: new FormControl(embarcadorRawValue.endereco, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      numero: new FormControl(embarcadorRawValue.numero, {
        validators: [Validators.minLength(1), Validators.maxLength(10)],
      }),
      complemento: new FormControl(embarcadorRawValue.complemento, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      bairro: new FormControl(embarcadorRawValue.bairro, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      telefone: new FormControl(embarcadorRawValue.telefone, {
        validators: [Validators.minLength(10), Validators.maxLength(11)],
      }),
      email: new FormControl(embarcadorRawValue.email, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      observacao: new FormControl(embarcadorRawValue.observacao, {
        validators: [Validators.minLength(2), Validators.maxLength(500)],
      }),
      dataCadastro: new FormControl(embarcadorRawValue.dataCadastro, {
        validators: [Validators.required],
      }),
      usuarioCadastro: new FormControl(embarcadorRawValue.usuarioCadastro, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
      dataAtualizacao: new FormControl(embarcadorRawValue.dataAtualizacao),
      usuarioAtualizacao: new FormControl(embarcadorRawValue.usuarioAtualizacao, {
        validators: [Validators.minLength(2), Validators.maxLength(150)],
      }),
    });
  }

  getEmbarcador(form: EmbarcadorFormGroup): IEmbarcador | NewEmbarcador {
    return this.convertEmbarcadorRawValueToEmbarcador(form.getRawValue() as EmbarcadorFormRawValue | NewEmbarcadorFormRawValue);
  }

  resetForm(form: EmbarcadorFormGroup, embarcador: EmbarcadorFormGroupInput): void {
    const embarcadorRawValue = this.convertEmbarcadorToEmbarcadorRawValue({ ...this.getFormDefaults(), ...embarcador });
    form.reset(
      {
        ...embarcadorRawValue,
        id: { value: embarcadorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmbarcadorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataCadastro: currentTime,
      dataAtualizacao: currentTime,
    };
  }

  private convertEmbarcadorRawValueToEmbarcador(
    rawEmbarcador: EmbarcadorFormRawValue | NewEmbarcadorFormRawValue,
  ): IEmbarcador | NewEmbarcador {
    return {
      ...rawEmbarcador,
      dataCadastro: dayjs(rawEmbarcador.dataCadastro, DATE_TIME_FORMAT),
      dataAtualizacao: dayjs(rawEmbarcador.dataAtualizacao, DATE_TIME_FORMAT),
    };
  }

  private convertEmbarcadorToEmbarcadorRawValue(
    embarcador: IEmbarcador | (Partial<NewEmbarcador> & EmbarcadorFormDefaults),
  ): EmbarcadorFormRawValue | PartialWithRequiredKeyOf<NewEmbarcadorFormRawValue> {
    return {
      ...embarcador,
      dataCadastro: embarcador.dataCadastro ? embarcador.dataCadastro.format(DATE_TIME_FORMAT) : undefined,
      dataAtualizacao: embarcador.dataAtualizacao ? embarcador.dataAtualizacao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
