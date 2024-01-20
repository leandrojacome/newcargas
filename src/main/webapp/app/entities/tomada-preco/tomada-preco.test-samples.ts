import dayjs from 'dayjs/esm';

import { ITomadaPreco, NewTomadaPreco } from './tomada-preco.model';

export const sampleWithRequiredData: ITomadaPreco = {
  id: 17965,
  dataHoraEnvio: dayjs('2024-01-19T21:37'),
  dataCadastro: dayjs('2024-01-19T19:29'),
};

export const sampleWithPartialData: ITomadaPreco = {
  id: 3771,
  dataHoraEnvio: dayjs('2024-01-19T23:29'),
  valorTotal: 9.63,
  observacao: 'abnormally furthermore mushy',
  dataCadastro: dayjs('2024-01-20T05:08'),
  dataAprovacao: dayjs('2024-01-20T06:07'),
  usuarioAprovacao: 'whereas gee',
  dataCancelamento: dayjs('2024-01-20T02:48'),
  usuarioCancelamento: 'why jealously',
  dataRemocao: dayjs('2024-01-19T22:19'),
};

export const sampleWithFullData: ITomadaPreco = {
  id: 4720,
  dataHoraEnvio: dayjs('2024-01-20T02:10'),
  prazoResposta: 2,
  valorTotal: 2.47,
  observacao: 'ice-cream what mmm',
  dataCadastro: dayjs('2024-01-19T19:28'),
  usuarioCadastro: 'daintily usually',
  dataAtualizacao: dayjs('2024-01-20T01:19'),
  usuarioAtualizacao: 'aha jaggedly',
  aprovado: false,
  dataAprovacao: dayjs('2024-01-20T06:52'),
  usuarioAprovacao: 'above frizzy',
  cancelado: true,
  dataCancelamento: dayjs('2024-01-20T15:57'),
  usuarioCancelamento: 'trousers gah',
  removido: true,
  dataRemocao: dayjs('2024-01-20T08:33'),
  usuarioRemocao: 'aw underneath clever',
};

export const sampleWithNewData: NewTomadaPreco = {
  dataHoraEnvio: dayjs('2024-01-19T21:25'),
  dataCadastro: dayjs('2024-01-20T11:14'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
