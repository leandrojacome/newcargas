import dayjs from 'dayjs/esm';

import { IContratacao, NewContratacao } from './contratacao.model';

export const sampleWithRequiredData: IContratacao = {
  id: 21431,
  valorTotal: 6.59,
  validadeEmDias: 4,
  dataValidade: dayjs('2024-01-20'),
};

export const sampleWithPartialData: IContratacao = {
  id: 19147,
  valorTotal: 1.14,
  validadeEmDias: 3,
  dataValidade: dayjs('2024-01-20'),
  cancelado: true,
  createdBy: 'despite herring',
  lastModifiedBy: 'teaching complexity avaricious',
  lastModifiedDate: dayjs('2024-01-19T23:47'),
};

export const sampleWithFullData: IContratacao = {
  id: 21942,
  valorTotal: 5.34,
  validadeEmDias: 1,
  dataValidade: dayjs('2024-01-20'),
  observacao: 'rearm',
  cancelado: true,
  removido: false,
  createdBy: 'entangle tensely ha',
  createdDate: dayjs('2024-01-20T13:36'),
  lastModifiedBy: 'nightingale among empty',
  lastModifiedDate: dayjs('2024-01-20T07:08'),
};

export const sampleWithNewData: NewContratacao = {
  valorTotal: 1.12,
  validadeEmDias: 3,
  dataValidade: dayjs('2024-01-20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
