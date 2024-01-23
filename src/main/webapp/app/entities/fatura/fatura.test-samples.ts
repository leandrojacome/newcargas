import dayjs from 'dayjs/esm';

import { IFatura, NewFatura } from './fatura.model';

export const sampleWithRequiredData: IFatura = {
  id: 2495,
  tipo: 'EMBARCADOR',
  dataFatura: dayjs('2024-01-20T06:53'),
  dataVencimento: dayjs('2024-01-20T02:32'),
  valorTotal: 3.92,
};

export const sampleWithPartialData: IFatura = {
  id: 28952,
  tipo: 'EMBARCADOR',
  dataFatura: dayjs('2024-01-20T16:06'),
  dataVencimento: dayjs('2024-01-19T17:49'),
  numeroParcela: 4,
  valorTotal: 6.45,
  cancelado: true,
  createdBy: 'though scarily part',
  createdDate: dayjs('2024-01-20T14:17'),
  lastModifiedDate: dayjs('2024-01-20T13:46'),
};

export const sampleWithFullData: IFatura = {
  id: 16044,
  tipo: 'EMBARCADOR',
  dataFatura: dayjs('2024-01-20T16:58'),
  dataVencimento: dayjs('2024-01-20T08:25'),
  dataPagamento: dayjs('2024-01-20T16:37'),
  numeroParcela: 4,
  valorTotal: 2.49,
  observacao: 'multicolored dueling a',
  cancelado: true,
  removido: true,
  createdBy: 'amongst religion',
  createdDate: dayjs('2024-01-19T19:08'),
  lastModifiedBy: 'misguided short',
  lastModifiedDate: dayjs('2024-01-20T02:57'),
};

export const sampleWithNewData: NewFatura = {
  tipo: 'EMBARCADOR',
  dataFatura: dayjs('2024-01-20T11:31'),
  dataVencimento: dayjs('2024-01-20T16:01'),
  valorTotal: 9.86,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
