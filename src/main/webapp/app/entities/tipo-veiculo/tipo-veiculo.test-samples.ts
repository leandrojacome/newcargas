import { ITipoVeiculo, NewTipoVeiculo } from './tipo-veiculo.model';

export const sampleWithRequiredData: ITipoVeiculo = {
  id: 21118,
  nome: 'extremely rumble ajar',
};

export const sampleWithPartialData: ITipoVeiculo = {
  id: 25133,
  nome: 'mature',
};

export const sampleWithFullData: ITipoVeiculo = {
  id: 4905,
  nome: 'yahoo usually',
  descricao: 'lounge',
};

export const sampleWithNewData: NewTipoVeiculo = {
  nome: 'bravely',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
