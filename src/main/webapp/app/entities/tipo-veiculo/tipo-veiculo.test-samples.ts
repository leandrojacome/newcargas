import dayjs from 'dayjs/esm';

import { ITipoVeiculo, NewTipoVeiculo } from './tipo-veiculo.model';

export const sampleWithRequiredData: ITipoVeiculo = {
  id: 21118,
  nome: 'extremely rumble ajar',
};

export const sampleWithPartialData: ITipoVeiculo = {
  id: 14879,
  nome: 'ew hmph',
  createdDate: dayjs('2024-01-20T13:23'),
  lastModifiedBy: 'ouch harbour',
};

export const sampleWithFullData: ITipoVeiculo = {
  id: 25707,
  nome: 'mud until',
  descricao: 'bitterly klutzy gosh',
  createdBy: 'rewarding',
  createdDate: dayjs('2024-01-20T11:51'),
  lastModifiedBy: 'annually inasmuch given',
  lastModifiedDate: dayjs('2024-01-19T20:08'),
};

export const sampleWithNewData: NewTipoVeiculo = {
  nome: 'piggyback outback',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
