import { ICidade, NewCidade } from './cidade.model';

export const sampleWithRequiredData: ICidade = {
  id: 27564,
  nome: 'or among',
};

export const sampleWithPartialData: ICidade = {
  id: 17510,
  nome: 'wherever phew abnormally',
  codigoIbge: 7,
};

export const sampleWithFullData: ICidade = {
  id: 19870,
  nome: 'favorite modeling',
  codigoIbge: 7,
};

export const sampleWithNewData: NewCidade = {
  nome: 'husky towards',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
