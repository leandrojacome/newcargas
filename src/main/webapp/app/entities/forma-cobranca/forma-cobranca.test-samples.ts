import { IFormaCobranca, NewFormaCobranca } from './forma-cobranca.model';

export const sampleWithRequiredData: IFormaCobranca = {
  id: 12433,
  nome: 'relate solemnly',
};

export const sampleWithPartialData: IFormaCobranca = {
  id: 21678,
  nome: 'who bother',
  descricao: 'rigidly below aha',
};

export const sampleWithFullData: IFormaCobranca = {
  id: 29092,
  nome: 'nor',
  descricao: 'influx hide',
};

export const sampleWithNewData: NewFormaCobranca = {
  nome: 'pfft',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
