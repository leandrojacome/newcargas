import { IBanco, NewBanco } from './banco.model';

export const sampleWithRequiredData: IBanco = {
  id: 22478,
  nome: 'anenst',
};

export const sampleWithPartialData: IBanco = {
  id: 2465,
  nome: 'lest',
};

export const sampleWithFullData: IBanco = {
  id: 24758,
  nome: 'encamp of variable',
  codigo: 'loo',
};

export const sampleWithNewData: NewBanco = {
  nome: 'wasp',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
