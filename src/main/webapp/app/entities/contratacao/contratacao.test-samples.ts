import dayjs from 'dayjs/esm';

import { IContratacao, NewContratacao } from './contratacao.model';

export const sampleWithRequiredData: IContratacao = {
  id: 31119,
  valorTotal: 1.35,
  validadeEmDias: 2,
  dataValidade: dayjs('2024-01-20'),
  dataCadastro: dayjs('2024-01-20T16:43'),
};

export const sampleWithPartialData: IContratacao = {
  id: 12728,
  valorTotal: 3.97,
  validadeEmDias: 4,
  dataValidade: dayjs('2024-01-20'),
  dataCadastro: dayjs('2024-01-19T23:42'),
  dataAtualizacao: dayjs('2024-01-20T07:55'),
  cancelado: true,
  dataCancelamento: dayjs('2024-01-20T06:53'),
  dataRemocao: dayjs('2024-01-19T17:42'),
  usuarioRemocao: 'likewise physically remove',
};

export const sampleWithFullData: IContratacao = {
  id: 21942,
  valorTotal: 5.34,
  validadeEmDias: 1,
  dataValidade: dayjs('2024-01-20'),
  observacao: 'rearm',
  dataCadastro: dayjs('2024-01-20T09:32'),
  usuarioCadastro: 'as but',
  dataAtualizacao: dayjs('2024-01-19T21:46'),
  usuarioAtualizacao: 'champion zowie',
  cancelado: false,
  dataCancelamento: dayjs('2024-01-20T03:00'),
  usuarioCancelamento: 'yum actually',
  removido: false,
  dataRemocao: dayjs('2024-01-20T04:38'),
  usuarioRemocao: 'pish',
};

export const sampleWithNewData: NewContratacao = {
  valorTotal: 4.73,
  validadeEmDias: 1,
  dataValidade: dayjs('2024-01-20'),
  dataCadastro: dayjs('2024-01-20T16:35'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
