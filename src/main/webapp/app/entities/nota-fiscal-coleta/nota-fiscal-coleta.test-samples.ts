import dayjs from 'dayjs/esm';

import { INotaFiscalColeta, NewNotaFiscalColeta } from './nota-fiscal-coleta.model';

export const sampleWithRequiredData: INotaFiscalColeta = {
  id: 16678,
  numero: 'pro',
  serie: 'ferry yoga',
};

export const sampleWithPartialData: INotaFiscalColeta = {
  id: 31605,
  numero: 'including disruption',
  serie: 'among',
  peso: 2.76,
  dataEmissao: dayjs('2024-01-19T21:45'),
  valorTotal: 8.45,
  observacao: 'during',
  createdDate: dayjs('2024-01-20T12:34'),
  lastModifiedBy: 'daring er rewire',
  lastModifiedDate: dayjs('2024-01-19T20:44'),
};

export const sampleWithFullData: INotaFiscalColeta = {
  id: 21396,
  numero: 'whereas what content',
  serie: 'back before celebrat',
  remetente: 'geez gah',
  destinatario: 'mature versus spiral',
  metroCubico: 4.67,
  quantidade: 6.94,
  peso: 8.04,
  dataEmissao: dayjs('2024-01-20T15:21'),
  dataSaida: dayjs('2024-01-20T04:35'),
  valorTotal: 4.97,
  pesoTotal: 2.92,
  quantidadeTotal: 3,
  observacao: 'intern plaster gather',
  createdBy: 'remediate furiously respectful',
  createdDate: dayjs('2024-01-20T04:16'),
  lastModifiedBy: 'frivolous pish hm',
  lastModifiedDate: dayjs('2024-01-20T13:54'),
};

export const sampleWithNewData: NewNotaFiscalColeta = {
  numero: 'consumption as',
  serie: 'yet freely till',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
