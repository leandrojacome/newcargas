import dayjs from 'dayjs/esm';

import { ITabelaFrete, NewTabelaFrete } from './tabela-frete.model';

export const sampleWithRequiredData: ITabelaFrete = {
  id: 31178,
  tipo: 'EMBARCADOR',
  nome: 'voice chart',
  dataCadastro: dayjs('2024-01-20T05:25'),
};

export const sampleWithPartialData: ITabelaFrete = {
  id: 26022,
  tipo: 'TRANSPORTADOR',
  nome: 'at',
  descricao: 'calibrate pilot',
  leadTime: 3,
  freteMinimo: 4.37,
  valorTonelada: 6.78,
  valorMetroCubico: 4.41,
  valorEntrega: 3.67,
  valorTotal: 4.05,
  dataCadastro: dayjs('2024-01-20T05:35'),
  dataAtualizacao: dayjs('2024-01-19T22:38'),
};

export const sampleWithFullData: ITabelaFrete = {
  id: 11351,
  tipo: 'TRANSPORTADOR',
  nome: 'honoree pushy',
  descricao: 'boo amongst raise',
  leadTime: 1,
  freteMinimo: 9.66,
  valorTonelada: 3.77,
  valorMetroCubico: 8.93,
  valorUnidade: 3.65,
  valorKm: 3.65,
  valorAdicional: 5.65,
  valorColeta: 9.12,
  valorEntrega: 8.49,
  valorTotal: 7.89,
  valorKmAdicional: 1.85,
  dataCadastro: dayjs('2024-01-20T09:39'),
  dataAtualizacao: dayjs('2024-01-19T19:42'),
};

export const sampleWithNewData: NewTabelaFrete = {
  tipo: 'TRANSPORTADOR',
  nome: 'oven buffet partially',
  dataCadastro: dayjs('2024-01-20T09:54'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
