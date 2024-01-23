import dayjs from 'dayjs/esm';

import { IBanco, NewBanco } from './banco.model';

export const sampleWithRequiredData: IBanco = {
  id: 22478,
  nome: 'anenst',
};

export const sampleWithPartialData: IBanco = {
  id: 11843,
  nome: 'extremely ew incidentally',
  createdBy: 'nor what tingling',
  createdDate: dayjs('2024-01-19T22:01'),
};

export const sampleWithFullData: IBanco = {
  id: 483,
  nome: 'colorless',
  codigo: 'pag',
  createdBy: 'busk secret yippee',
  createdDate: dayjs('2024-01-20T05:02'),
  lastModifiedBy: 'yowza incompatible',
  lastModifiedDate: dayjs('2024-01-20T12:20'),
};

export const sampleWithNewData: NewBanco = {
  nome: 'excitedly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
