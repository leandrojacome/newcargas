import dayjs from 'dayjs/esm';

import { ISolicitacaoColeta, NewSolicitacaoColeta } from './solicitacao-coleta.model';

export const sampleWithRequiredData: ISolicitacaoColeta = {
  id: 18163,
  coletado: true,
  dataHoraColeta: dayjs('2024-01-19T19:29'),
  entregue: true,
};

export const sampleWithPartialData: ISolicitacaoColeta = {
  id: 6061,
  coletado: false,
  dataHoraColeta: dayjs('2024-01-20T06:47'),
  entregue: false,
  dataHoraEntrega: dayjs('2024-01-20T00:47'),
  valorTotal: 7.85,
  observacao: 'gah',
  removido: false,
  lastModifiedBy: 'deliberately',
};

export const sampleWithFullData: ISolicitacaoColeta = {
  id: 32505,
  coletado: true,
  dataHoraColeta: dayjs('2024-01-19T21:52'),
  entregue: true,
  dataHoraEntrega: dayjs('2024-01-20T11:24'),
  valorTotal: 1.34,
  observacao: 'oof',
  cancelado: false,
  removido: false,
  createdBy: 'meaty pinstripe academic',
  createdDate: dayjs('2024-01-20T11:18'),
  lastModifiedBy: 'outside pace',
  lastModifiedDate: dayjs('2024-01-20T02:22'),
};

export const sampleWithNewData: NewSolicitacaoColeta = {
  coletado: true,
  dataHoraColeta: dayjs('2024-01-19T18:10'),
  entregue: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
