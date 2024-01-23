import dayjs from 'dayjs/esm';

import { IHistoricoStatusColeta, NewHistoricoStatusColeta } from './historico-status-coleta.model';

export const sampleWithRequiredData: IHistoricoStatusColeta = {
  id: 2257,
  dataCriacao: dayjs('2024-01-19T18:39'),
};

export const sampleWithPartialData: IHistoricoStatusColeta = {
  id: 925,
  dataCriacao: dayjs('2024-01-20T07:54'),
  createdBy: 'education',
  lastModifiedDate: dayjs('2024-01-20T16:56'),
};

export const sampleWithFullData: IHistoricoStatusColeta = {
  id: 4950,
  dataCriacao: dayjs('2024-01-20T04:59'),
  observacao: 'enchanted midst underneath',
  createdBy: 'infantile eek',
  createdDate: dayjs('2024-01-19T22:07'),
  lastModifiedBy: 'pfft whenever along',
  lastModifiedDate: dayjs('2024-01-20T15:48'),
};

export const sampleWithNewData: NewHistoricoStatusColeta = {
  dataCriacao: dayjs('2024-01-20T04:00'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
