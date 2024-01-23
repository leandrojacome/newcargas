import dayjs from 'dayjs/esm';

import { IRegiao, NewRegiao } from './regiao.model';

export const sampleWithRequiredData: IRegiao = {
  id: 1258,
  nome: 'resemble vinyl phooey',
};

export const sampleWithPartialData: IRegiao = {
  id: 8292,
  nome: 'or',
  sigla: 'oh la',
  descricao: 'brightly third um',
};

export const sampleWithFullData: IRegiao = {
  id: 28526,
  nome: 'scallop phooey',
  sigla: 'cash ',
  descricao: 'yet',
  createdBy: 'ring weary',
  createdDate: dayjs('2024-01-20T06:42'),
  lastModifiedBy: 'aw oh aw',
  lastModifiedDate: dayjs('2024-01-19T17:14'),
};

export const sampleWithNewData: NewRegiao = {
  nome: 'whoa stave',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
