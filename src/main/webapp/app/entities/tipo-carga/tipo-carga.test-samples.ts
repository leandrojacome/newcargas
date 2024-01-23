import dayjs from 'dayjs/esm';

import { ITipoCarga, NewTipoCarga } from './tipo-carga.model';

export const sampleWithRequiredData: ITipoCarga = {
  id: 31486,
  nome: 'bride midst lively',
};

export const sampleWithPartialData: ITipoCarga = {
  id: 9489,
  nome: 'racer concerning embody',
  descricao: 'judgementally hacksaw squeaky',
  createdDate: dayjs('2024-01-19T18:41'),
};

export const sampleWithFullData: ITipoCarga = {
  id: 2972,
  nome: 'warm fragrant',
  descricao: 'jaggedly',
  createdBy: 'pro body',
  createdDate: dayjs('2024-01-20T07:55'),
  lastModifiedBy: 'waterlogged yippee before',
  lastModifiedDate: dayjs('2024-01-20T12:58'),
};

export const sampleWithNewData: NewTipoCarga = {
  nome: 'boo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
