import dayjs from 'dayjs/esm';

import { IStatusColeta, NewStatusColeta } from './status-coleta.model';

export const sampleWithRequiredData: IStatusColeta = {
  id: 3616,
  nome: 'really',
};

export const sampleWithPartialData: IStatusColeta = {
  id: 20884,
  nome: 'that',
  ordem: 1,
  estadoFinal: true,
  permiteEditar: false,
  permiteExcluir: false,
  descricao: 'who cluster infamous',
  ativo: false,
  removido: true,
  createdBy: 'after indolent',
  createdDate: dayjs('2024-01-20T07:40'),
  lastModifiedBy: 'neatly counselling',
  lastModifiedDate: dayjs('2024-01-20T08:27'),
};

export const sampleWithFullData: IStatusColeta = {
  id: 17376,
  nome: 'expectorate as as',
  cor: 'which pa',
  ordem: 4,
  estadoInicial: false,
  estadoFinal: false,
  permiteCancelar: false,
  permiteEditar: true,
  permiteExcluir: true,
  descricao: 'shyly sever',
  ativo: false,
  removido: false,
  createdBy: 'yuck prance unto',
  createdDate: dayjs('2024-01-20T09:05'),
  lastModifiedBy: 'aw',
  lastModifiedDate: dayjs('2024-01-20T08:18'),
};

export const sampleWithNewData: NewStatusColeta = {
  nome: 'till gosh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
