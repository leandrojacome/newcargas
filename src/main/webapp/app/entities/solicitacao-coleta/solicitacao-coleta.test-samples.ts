import dayjs from 'dayjs/esm';

import { ISolicitacaoColeta, NewSolicitacaoColeta } from './solicitacao-coleta.model';

export const sampleWithRequiredData: ISolicitacaoColeta = {
  id: 2220,
  coletado: false,
  dataHoraColeta: dayjs('2024-01-20T12:32'),
  entregue: false,
  dataCadastro: dayjs('2024-01-20T04:12'),
};

export const sampleWithPartialData: ISolicitacaoColeta = {
  id: 10043,
  coletado: true,
  dataHoraColeta: dayjs('2024-01-20T04:27'),
  entregue: true,
  dataHoraEntrega: dayjs('2024-01-20T14:46'),
  observacao: 'crook geez',
  dataCadastro: dayjs('2024-01-20T16:04'),
  cancelado: true,
  dataRemocao: dayjs('2024-01-19T23:08'),
  usuarioRemocao: 'through scholarly concur',
};

export const sampleWithFullData: ISolicitacaoColeta = {
  id: 21714,
  coletado: true,
  dataHoraColeta: dayjs('2024-01-20T02:09'),
  entregue: false,
  dataHoraEntrega: dayjs('2024-01-20T07:12'),
  valorTotal: 6.79,
  observacao: 'contextualize partnership bottom',
  dataCadastro: dayjs('2024-01-20T11:01'),
  dataAtualizacao: dayjs('2024-01-19T17:30'),
  cancelado: true,
  dataCancelamento: dayjs('2024-01-20T09:22'),
  usuarioCancelamento: 'recklessly irritating whose',
  removido: false,
  dataRemocao: dayjs('2024-01-20T06:25'),
  usuarioRemocao: 'elegantly rigidly',
};

export const sampleWithNewData: NewSolicitacaoColeta = {
  coletado: false,
  dataHoraColeta: dayjs('2024-01-19T21:02'),
  entregue: true,
  dataCadastro: dayjs('2024-01-19T22:08'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
