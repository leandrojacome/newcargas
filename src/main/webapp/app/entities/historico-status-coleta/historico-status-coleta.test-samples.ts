import dayjs from 'dayjs/esm';

import { IHistoricoStatusColeta, NewHistoricoStatusColeta } from './historico-status-coleta.model';

export const sampleWithRequiredData: IHistoricoStatusColeta = {
  id: 2257,
  dataCriacao: dayjs('2024-01-19T18:39'),
};

export const sampleWithPartialData: IHistoricoStatusColeta = {
  id: 1722,
  dataCriacao: dayjs('2024-01-19T23:37'),
};

export const sampleWithFullData: IHistoricoStatusColeta = {
  id: 32163,
  dataCriacao: dayjs('2024-01-20T16:20'),
  observacao: 'drift',
};

export const sampleWithNewData: NewHistoricoStatusColeta = {
  dataCriacao: dayjs('2024-01-20T03:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
