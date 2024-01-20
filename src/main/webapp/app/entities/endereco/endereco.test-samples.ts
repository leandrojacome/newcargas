import { IEndereco, NewEndereco } from './endereco.model';

export const sampleWithRequiredData: IEndereco = {
  id: 19718,
  tipo: 'EMBARCADOR',
  cep: 'barricad',
  endereco: 'including',
  bairro: 'about mmm',
};

export const sampleWithPartialData: IEndereco = {
  id: 14819,
  tipo: 'SOLICITACAO_COLETA',
  cep: 'yippeeXX',
  endereco: 'improbable',
  numero: 'miniature',
  bairro: 'unlike',
};

export const sampleWithFullData: IEndereco = {
  id: 18614,
  tipo: 'SOLICITACAO_COLETA',
  cep: 'from erX',
  endereco: 'ugh',
  numero: 'known',
  complemento: 'incidentally tidy',
  bairro: 'shoes',
};

export const sampleWithNewData: NewEndereco = {
  tipo: 'NOTA_FISCAL_COLETA',
  cep: 'searchin',
  endereco: 'kendo',
  bairro: 'catalysis',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
