import dayjs from 'dayjs/esm';

import { ITipoFrete, NewTipoFrete } from './tipo-frete.model';

export const sampleWithRequiredData: ITipoFrete = {
  id: 27134,
  nome: 'finally strictly',
};

export const sampleWithPartialData: ITipoFrete = {
  id: 23750,
  nome: 'taut famously',
  descricao: 'loud circa queasy',
  createdBy: 'seemingly',
  createdDate: dayjs('2024-01-20T13:35'),
  lastModifiedDate: dayjs('2024-01-20T08:47'),
};

export const sampleWithFullData: ITipoFrete = {
  id: 25543,
  nome: 'quixotic how track',
  descricao: 'brr',
  createdBy: 'swill',
  createdDate: dayjs('2024-01-19T19:43'),
  lastModifiedBy: 'blah',
  lastModifiedDate: dayjs('2024-01-20T06:00'),
};

export const sampleWithNewData: NewTipoFrete = {
  nome: 'intercede hm',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
