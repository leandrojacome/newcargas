import dayjs from 'dayjs/esm';

import { INotaFiscalColeta, NewNotaFiscalColeta } from './nota-fiscal-coleta.model';

export const sampleWithRequiredData: INotaFiscalColeta = {
  id: 849,
  numero: 'surprisingly',
  serie: 'ick',
};

export const sampleWithPartialData: INotaFiscalColeta = {
  id: 17241,
  numero: 'internalize includin',
  serie: 'among',
  remetente: 'consequently',
  quantidade: 6.21,
  quantidadeTotal: 3,
  observacao: 'responsible haversack',
  dataAtualizacao: dayjs('2024-01-19T22:53'),
};

export const sampleWithFullData: INotaFiscalColeta = {
  id: 30561,
  numero: 'terrific append',
  serie: 'continually steamrol',
  remetente: 'hm silicon pastor',
  destinatario: 'oh openly than',
  metroCubico: 9.83,
  quantidade: 9.48,
  peso: 2.88,
  dataEmissao: dayjs('2024-01-20T01:17'),
  dataSaida: dayjs('2024-01-20T11:53'),
  valorTotal: 6.63,
  pesoTotal: 9.77,
  quantidadeTotal: 1,
  observacao: 'decriminalize snuggle potentially',
  dataCadastro: dayjs('2024-01-20T04:37'),
  dataAtualizacao: dayjs('2024-01-20T01:54'),
};

export const sampleWithNewData: NewNotaFiscalColeta = {
  numero: 'um',
  serie: 'near rudely',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
