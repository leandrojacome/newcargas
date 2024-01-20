import { IEstado, NewEstado } from './estado.model';

export const sampleWithRequiredData: IEstado = {
  id: 878,
  nome: 'researches via',
  sigla: 'wo',
};

export const sampleWithPartialData: IEstado = {
  id: 1949,
  nome: 'wry',
  sigla: 'br',
  codigoIbge: 7,
};

export const sampleWithFullData: IEstado = {
  id: 24858,
  nome: 'plasterboard incidentally engross',
  sigla: 'ra',
  codigoIbge: 7,
};

export const sampleWithNewData: NewEstado = {
  nome: 'billing wherever',
  sigla: 'ad',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
