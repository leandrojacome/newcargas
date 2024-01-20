import { IRegiao, NewRegiao } from './regiao.model';

export const sampleWithRequiredData: IRegiao = {
  id: 1258,
  nome: 'resemble vinyl phooey',
};

export const sampleWithPartialData: IRegiao = {
  id: 30263,
  nome: 'whereas inventory valiantly',
  sigla: 'exclu',
  descricao: 'duh now',
};

export const sampleWithFullData: IRegiao = {
  id: 17622,
  nome: 'if oh',
  sigla: 'corra',
  descricao: 'ha closed gee',
};

export const sampleWithNewData: NewRegiao = {
  nome: 'unaccountably',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
