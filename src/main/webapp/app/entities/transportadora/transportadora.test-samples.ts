import dayjs from 'dayjs/esm';

import { ITransportadora, NewTransportadora } from './transportadora.model';

export const sampleWithRequiredData: ITransportadora = {
  id: 27424,
  nome: 'although',
  cnpj: 'dratXXXXXXXXXX',
};

export const sampleWithPartialData: ITransportadora = {
  id: 399,
  nome: 'whoa offensively atop',
  cnpj: 'alpenhorn frig',
  inscricaoEstadual: 'brilliant that unnec',
  inscricaoMunicipal: 'triumphantly tarrago',
  endereco: 'on',
  complemento: 'cruelly between',
  email: 'Roberto.Moraes87@live.com',
  createdBy: 'hm',
  createdDate: dayjs('2024-01-19T21:29'),
  lastModifiedBy: 'finally distrust',
  lastModifiedDate: dayjs('2024-01-20T09:24'),
};

export const sampleWithFullData: ITransportadora = {
  id: 23086,
  nome: 'fragrant bowl whether',
  cnpj: 'sanctionXXXXXX',
  razaoSocial: 'yippee burdensome',
  inscricaoEstadual: 'thousand',
  inscricaoMunicipal: 'even about deglaze',
  responsavel: 'refuel',
  cep: 'joyously',
  endereco: 'even icky',
  numero: 'hastily',
  complemento: 'ugh',
  bairro: 'anenst',
  telefone: 'ew phooeyX',
  email: 'Natalia_Barros@yahoo.com',
  observacao: 'versus wiggle',
  createdBy: 'ferociously ah',
  createdDate: dayjs('2024-01-20T00:56'),
  lastModifiedBy: 'disable',
  lastModifiedDate: dayjs('2024-01-20T14:30'),
};

export const sampleWithNewData: NewTransportadora = {
  nome: 'gum',
  cnpj: 'and immenseXXX',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
