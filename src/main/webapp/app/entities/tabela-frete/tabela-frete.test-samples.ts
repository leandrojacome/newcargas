import dayjs from 'dayjs/esm';

import { ITabelaFrete, NewTabelaFrete } from './tabela-frete.model';

export const sampleWithRequiredData: ITabelaFrete = {
  id: 27091,
  tipo: 'EMBARCADOR',
  nome: 'on phew keenly',
};

export const sampleWithPartialData: ITabelaFrete = {
  id: 2494,
  tipo: 'TRANSPORTADOR',
  nome: 'vacantly',
  descricao: 'pilot',
  valorUnidade: 5.58,
  valorKm: 4.37,
  valorColeta: 6.78,
  valorKmAdicional: 4.41,
  lastModifiedBy: 'inconsequential',
};

export const sampleWithFullData: ITabelaFrete = {
  id: 1631,
  tipo: 'EMBARCADOR',
  nome: 'flaky',
  descricao: 'so why furthermore',
  leadTime: 2,
  freteMinimo: 4.29,
  valorTonelada: 7.48,
  valorMetroCubico: 2.65,
  valorUnidade: 9.66,
  valorKm: 3.77,
  valorAdicional: 8.93,
  valorColeta: 3.65,
  valorEntrega: 3.65,
  valorTotal: 5.65,
  valorKmAdicional: 9.12,
  createdBy: 'object across funk',
  createdDate: dayjs('2024-01-20T13:19'),
  lastModifiedBy: 'sleepily',
  lastModifiedDate: dayjs('2024-01-20T10:16'),
};

export const sampleWithNewData: NewTabelaFrete = {
  tipo: 'TRANSPORTADOR',
  nome: 'monthly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
