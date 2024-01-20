import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEndereco, NewEndereco } from '../endereco.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEndereco for edit and NewEnderecoFormGroupInput for create.
 */
type EnderecoFormGroupInput = IEndereco | PartialWithRequiredKeyOf<NewEndereco>;

type EnderecoFormDefaults = Pick<NewEndereco, 'id'>;

type EnderecoFormGroupContent = {
  id: FormControl<IEndereco['id'] | NewEndereco['id']>;
  tipo: FormControl<IEndereco['tipo']>;
  cep: FormControl<IEndereco['cep']>;
  endereco: FormControl<IEndereco['endereco']>;
  numero: FormControl<IEndereco['numero']>;
  complemento: FormControl<IEndereco['complemento']>;
  bairro: FormControl<IEndereco['bairro']>;
  cidade: FormControl<IEndereco['cidade']>;
  embarcador: FormControl<IEndereco['embarcador']>;
  transportadora: FormControl<IEndereco['transportadora']>;
  notaFiscalColetaOrigem: FormControl<IEndereco['notaFiscalColetaOrigem']>;
  notaFiscalColetaDestino: FormControl<IEndereco['notaFiscalColetaDestino']>;
  solicitacaoColetaOrigem: FormControl<IEndereco['solicitacaoColetaOrigem']>;
  solicitacaoColetaDestino: FormControl<IEndereco['solicitacaoColetaDestino']>;
};

export type EnderecoFormGroup = FormGroup<EnderecoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnderecoFormService {
  createEnderecoFormGroup(endereco: EnderecoFormGroupInput = { id: null }): EnderecoFormGroup {
    const enderecoRawValue = {
      ...this.getFormDefaults(),
      ...endereco,
    };
    return new FormGroup<EnderecoFormGroupContent>({
      id: new FormControl(
        { value: enderecoRawValue.id, disabled: true },
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
    return form.getRawValue() as IEndereco | NewEndereco;
  }

  resetForm(form: EnderecoFormGroup, endereco: EnderecoFormGroupInput): void {
    const enderecoRawValue = { ...this.getFormDefaults(), ...endereco };
    form.reset(
      {
        ...enderecoRawValue,
        id: { value: enderecoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnderecoFormDefaults {
    return {
      id: null,
    };
  }
}
