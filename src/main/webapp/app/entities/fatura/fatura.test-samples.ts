import dayjs from 'dayjs/esm';

import { IFatura, NewFatura } from './fatura.model';

export const sampleWithRequiredData: IFatura = {
  id: 4603,
  tipo: 'TRANSPORTADORA',
  dataFatura: dayjs('2024-01-20T07:57'),
  dataVencimento: dayjs('2024-01-20T09:47'),
  valorTotal: 9.33,
  dataCadastro: dayjs('2024-01-20T14:44'),
};

export const sampleWithPartialData: IFatura = {
  id: 10939,
  tipo: 'EMBARCADOR',
  dataFatura: dayjs('2024-01-20T01:15'),
  dataVencimento: dayjs('2024-01-20T08:14'),
  numeroParcela: 4,
  valorTotal: 5.06,
  observacao: 'password a failing',
  dataCadastro: dayjs('2024-01-20T12:47'),
  cancelado: true,
  usuarioRemocao: 'hm salsa unless',
};

export const sampleWithFullData: IFatura = {
  id: 8700,
  tipo: 'EMBARCADOR',
  dataFatura: dayjs('2024-01-20T02:00'),
  dataVencimento: dayjs('2024-01-19T23:27'),
  dataPagamento: dayjs('2024-01-20T15:47'),
  numeroParcela: 1,
  valorTotal: 2.47,
  observacao: 'gah',
  dataCadastro: dayjs('2024-01-20T07:39'),
  usuarioCadastro: 'married corny yowza',
  dataAtualizacao: dayjs('2024-01-19T22:31'),
  usuarioAtualizacao: 'repulsive',
  cancelado: false,
  dataCancelamento: dayjs('2024-01-20T05:39'),
  usuarioCancelamento: 'hungrily',
  removido: false,
  dataRemocao: dayjs('2024-01-20T02:27'),
  usuarioRemocao: 'at activity',
};

export const sampleWithNewData: NewFatura = {
  tipo: 'TRANSPORTADORA',
  dataFatura: dayjs('2024-01-20T04:27'),
  dataVencimento: dayjs('2024-01-19T19:28'),
  valorTotal: 1.48,
  dataCadastro: dayjs('2024-01-20T03:18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
