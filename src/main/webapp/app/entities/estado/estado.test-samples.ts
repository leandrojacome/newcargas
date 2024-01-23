import dayjs from 'dayjs/esm';

import { IEstado, NewEstado } from './estado.model';

export const sampleWithRequiredData: IEstado = {
  id: 878,
  nome: 'researches via',
  sigla: 'wo',
};

export const sampleWithPartialData: IEstado = {
  id: 7208,
  nome: 'flake frank or',
  sigla: 'se',
  codigoIbge: 7,
  createdBy: 'serene',
  createdDate: dayjs('2024-01-19T17:19'),
};

export const sampleWithFullData: IEstado = {
  id: 13121,
  nome: 'terrarium',
  sigla: 'wh',
  codigoIbge: 7,
  createdBy: 'huzzah maintenance unhappy',
  createdDate: dayjs('2024-01-20T01:16'),
  lastModifiedBy: 'off',
  lastModifiedDate: dayjs('2024-01-20T14:28'),
};

export const sampleWithNewData: NewEstado = {
  nome: 'never',
  sigla: 'he',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
