import dayjs from 'dayjs/esm';

import { ICidade, NewCidade } from './cidade.model';

export const sampleWithRequiredData: ICidade = {
  id: 27564,
  nome: 'or among',
};

export const sampleWithPartialData: ICidade = {
  id: 1772,
  nome: 'defile',
  codigoIbge: 7,
};

export const sampleWithFullData: ICidade = {
  id: 32121,
  nome: 'dressing spattering millet',
  codigoIbge: 7,
  createdBy: 'key',
  createdDate: dayjs('2024-01-19T19:11'),
  lastModifiedBy: 'zoo mid down',
  lastModifiedDate: dayjs('2024-01-20T14:05'),
};

export const sampleWithNewData: NewCidade = {
  nome: 'um',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
