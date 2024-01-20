import { ITipoFrete, NewTipoFrete } from './tipo-frete.model';

export const sampleWithRequiredData: ITipoFrete = {
  id: 27134,
  nome: 'finally strictly',
};

export const sampleWithPartialData: ITipoFrete = {
  id: 13590,
  nome: 'roof',
  descricao: 'stormy round severe',
};

export const sampleWithFullData: ITipoFrete = {
  id: 19619,
  nome: 'innocently',
  descricao: 'attach without deserted',
};

export const sampleWithNewData: NewTipoFrete = {
  nome: 'gah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
