import dayjs from 'dayjs/esm';

import { IEmbarcador, NewEmbarcador } from './embarcador.model';

export const sampleWithRequiredData: IEmbarcador = {
  id: 12710,
  nome: 'eek',
  cnpj: 'succeed smug m',
};

export const sampleWithPartialData: IEmbarcador = {
  id: 18602,
  nome: 'unbearably',
  cnpj: 'team cornyXXXX',
  inscricaoMunicipal: 'whoever wonderfully',
  cep: 'worth ha',
  telefone: 'user repent',
  createdDate: dayjs('2024-01-20T14:54'),
  lastModifiedBy: 'than swathe kiss',
};

export const sampleWithFullData: IEmbarcador = {
  id: 12741,
  nome: 'log victoriously',
  cnpj: 'tangible helme',
  razaoSocial: 'incidentally furthermore',
  inscricaoEstadual: 'phooey closed',
  inscricaoMunicipal: 'urge table loosely',
  responsavel: 'before generously furnace',
  cep: 'gosh jum',
  endereco: 'hence',
  numero: 'versify af',
  complemento: 'limo honestly provided',
  bairro: 'sedately',
  telefone: 'whether fro',
  email: 'Talita.Barros88@live.com',
  observacao: 'fooey often whereas',
  createdBy: 'gosh yawning',
  createdDate: dayjs('2024-01-20T05:31'),
  lastModifiedBy: 'onto past',
  lastModifiedDate: dayjs('2024-01-20T15:06'),
};

export const sampleWithNewData: NewEmbarcador = {
  nome: 'messy however',
  cnpj: 'instead furthe',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
