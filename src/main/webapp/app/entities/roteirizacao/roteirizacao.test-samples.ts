import dayjs from 'dayjs/esm';

import { IRoteirizacao, NewRoteirizacao } from './roteirizacao.model';

export const sampleWithRequiredData: IRoteirizacao = {
  id: 24160,
  dataHoraPrimeiraColeta: dayjs('2024-01-20T08:52'),
};

export const sampleWithPartialData: IRoteirizacao = {
  id: 14370,
  dataHoraPrimeiraColeta: dayjs('2024-01-19T20:15'),
  dataHoraUltimaEntrega: dayjs('2024-01-20T15:13'),
  valorTotal: 9.13,
  observacao: 'whoever gosh',
  cancelado: false,
  createdBy: 'wherever',
  lastModifiedBy: 'woot glass whoever',
};

export const sampleWithFullData: IRoteirizacao = {
  id: 6062,
  dataHoraPrimeiraColeta: dayjs('2024-01-20T07:51'),
  dataHoraUltimaColeta: dayjs('2024-01-19T19:47'),
  dataHoraPrimeiraEntrega: dayjs('2024-01-20T02:06'),
  dataHoraUltimaEntrega: dayjs('2024-01-20T16:25'),
  valorTotal: 5.83,
  observacao: 'robe below kissingly',
  cancelado: false,
  removido: false,
  createdBy: 'geez resume',
  createdDate: dayjs('2024-01-19T22:48'),
  lastModifiedBy: 'coruscate ornament triumph',
  lastModifiedDate: dayjs('2024-01-20T00:31'),
};

export const sampleWithNewData: NewRoteirizacao = {
  dataHoraPrimeiraColeta: dayjs('2024-01-19T23:23'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
