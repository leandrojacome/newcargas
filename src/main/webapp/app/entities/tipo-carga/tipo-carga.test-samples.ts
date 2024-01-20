import { ITipoCarga, NewTipoCarga } from './tipo-carga.model';

export const sampleWithRequiredData: ITipoCarga = {
  id: 31486,
  nome: 'bride midst lively',
};

export const sampleWithPartialData: ITipoCarga = {
  id: 20309,
  nome: 'queen',
  descricao: 'above',
};

export const sampleWithFullData: ITipoCarga = {
  id: 3092,
  nome: 'pfft phooey',
  descricao: 'mist sophisticated',
};

export const sampleWithNewData: NewTipoCarga = {
  nome: 'gee sparkling when',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
