import dayjs from 'dayjs/esm';

import { IStatusColeta, NewStatusColeta } from './status-coleta.model';

export const sampleWithRequiredData: IStatusColeta = {
  id: 4280,
  nome: 'yahoo',
  dataCadastro: dayjs('2024-01-20T09:03'),
};

export const sampleWithPartialData: IStatusColeta = {
  id: 20686,
  nome: 'surprisingly',
  cor: 'pfft whi',
  ordem: 2,
  estadoInicial: false,
  estadoFinal: true,
  permiteCancelar: true,
  permiteEditar: false,
  permiteExcluir: true,
  dataCadastro: dayjs('2024-01-19T18:38'),
  usuarioCadastro: 'minor-league neatly',
  dataAtualizacao: dayjs('2024-01-20T04:50'),
  ativo: true,
  removido: true,
  dataRemocao: dayjs('2024-01-19T23:58'),
  usuarioRemocao: 'week accounting whose',
};

export const sampleWithFullData: IStatusColeta = {
  id: 5173,
  nome: 'utterly',
  cor: 'scamper ',
  ordem: 1,
  estadoInicial: true,
  estadoFinal: false,
  permiteCancelar: true,
  permiteEditar: true,
  permiteExcluir: false,
  descricao: 'forwards alongside calm',
  dataCadastro: dayjs('2024-01-19T22:00'),
  usuarioCadastro: 'abaft fortune',
  dataAtualizacao: dayjs('2024-01-20T04:19'),
  usuarioAtualizacao: 'demolish er inwardly',
  ativo: false,
  removido: true,
  dataRemocao: dayjs('2024-01-20T03:03'),
  usuarioRemocao: 'roar',
};

export const sampleWithNewData: NewStatusColeta = {
  nome: 'dry verifiable woot',
  dataCadastro: dayjs('2024-01-20T01:45'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
