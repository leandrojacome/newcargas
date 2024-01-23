import dayjs from 'dayjs/esm';

import { IEndereco, NewEndereco } from './endereco.model';

export const sampleWithRequiredData: IEndereco = {
  id: 19718,
  tipo: 'EMBARCADOR',
  cep: 'barricad',
  endereco: 'including',
  bairro: 'about mmm',
};

export const sampleWithPartialData: IEndereco = {
  id: 11180,
  tipo: 'NOTA_FISCAL_COLETA',
  cep: 'during w',
  endereco: 'absent',
  numero: 'liquid',
  bairro: 'save amazement ugh',
  createdBy: 'known',
  lastModifiedBy: 'incidentally tidy',
  lastModifiedDate: dayjs('2024-01-20T16:23'),
};

export const sampleWithFullData: IEndereco = {
  id: 13822,
  tipo: 'EMBARCADOR',
  cep: 'spongeXX',
  endereco: 'infant although',
  numero: 'inflame um',
  complemento: 'underexpose whether',
  bairro: 'acknowledge sleepily',
  createdBy: 'vital',
  createdDate: dayjs('2024-01-20T07:42'),
  lastModifiedBy: 'relish',
  lastModifiedDate: dayjs('2024-01-19T23:14'),
};

export const sampleWithNewData: NewEndereco = {
  tipo: 'EMBARCADOR',
  cep: 'bowling ',
  endereco: 'calico badly petition',
  bairro: 'mid',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
