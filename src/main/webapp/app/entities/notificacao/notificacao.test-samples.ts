import dayjs from 'dayjs/esm';

import { INotificacao, NewNotificacao } from './notificacao.model';

export const sampleWithRequiredData: INotificacao = {
  id: 8237,
  tipo: 'TRANSPORTADORA',
  assunto: 'gee',
  mensagem: 'spill lavish',
  dataHoraEnvio: dayjs('2024-01-20T05:06'),
  dataCadastro: dayjs('2024-01-20T01:24'),
};

export const sampleWithPartialData: INotificacao = {
  id: 14795,
  tipo: 'EMBARCADOR',
  email: 'Tertuliano78@gmail.com',
  assunto: 'lipid',
  mensagem: 'physical whoa',
  dataHoraEnvio: dayjs('2024-01-19T21:09'),
  dataHoraLeitura: dayjs('2024-01-19T23:47'),
  dataCadastro: dayjs('2024-01-19T22:21'),
  dataAtualizacao: dayjs('2024-01-20T04:16'),
  dataLeitura: dayjs('2024-01-20T01:28'),
  removido: false,
  dataRemocao: dayjs('2024-01-19T19:50'),
  usuarioRemocao: 'whose yahoo',
};

export const sampleWithFullData: INotificacao = {
  id: 26137,
  tipo: 'EMBARCADOR',
  email: 'Yuri_Silva@gmail.com',
  telefone: 'er heavenX',
  assunto: 'pastel',
  mensagem: 'below deviation especially',
  dataHoraEnvio: dayjs('2024-01-20T14:20'),
  dataHoraLeitura: dayjs('2024-01-20T00:13'),
  dataCadastro: dayjs('2024-01-20T13:57'),
  dataAtualizacao: dayjs('2024-01-20T10:56'),
  lido: true,
  dataLeitura: dayjs('2024-01-19T22:07'),
  removido: true,
  dataRemocao: dayjs('2024-01-20T00:48'),
  usuarioRemocao: 'supposing',
};

export const sampleWithNewData: NewNotificacao = {
  tipo: 'EMBARCADOR',
  assunto: 'ugh',
  mensagem: 'hopelessly',
  dataHoraEnvio: dayjs('2024-01-20T08:52'),
  dataCadastro: dayjs('2024-01-19T18:55'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
