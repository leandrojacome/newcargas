import dayjs from 'dayjs/esm';

import { IRoteirizacao, NewRoteirizacao } from './roteirizacao.model';

export const sampleWithRequiredData: IRoteirizacao = {
  id: 20475,
  dataHoraPrimeiraColeta: dayjs('2024-01-20T14:20'),
  dataCadastro: dayjs('2024-01-20T01:50'),
};

export const sampleWithPartialData: IRoteirizacao = {
  id: 9352,
  dataHoraPrimeiraColeta: dayjs('2024-01-20T11:57'),
  dataHoraUltimaColeta: dayjs('2024-01-20T03:21'),
  dataHoraUltimaEntrega: dayjs('2024-01-20T04:08'),
  observacao: 'furthermore calendar',
  dataCadastro: dayjs('2024-01-20T05:38'),
  dataCancelamento: dayjs('2024-01-20T07:38'),
  removido: false,
};

export const sampleWithFullData: IRoteirizacao = {
  id: 10001,
  dataHoraPrimeiraColeta: dayjs('2024-01-20T06:38'),
  dataHoraUltimaColeta: dayjs('2024-01-20T11:55'),
  dataHoraPrimeiraEntrega: dayjs('2024-01-20T14:09'),
  dataHoraUltimaEntrega: dayjs('2024-01-20T05:57'),
  valorTotal: 8.27,
  observacao: 'kiosk amount',
  dataCadastro: dayjs('2024-01-20T04:09'),
  usuarioCadastro: 'robe below kissingly',
  dataAtualizacao: dayjs('2024-01-19T22:17'),
  usuarioAtualizacao: 'wet concerning impossible',
  cancelado: false,
  dataCancelamento: dayjs('2024-01-20T15:22'),
  usuarioCancelamento: 'ornament',
  removido: true,
  dataRemocao: dayjs('2024-01-19T18:32'),
  usuarioRemocao: 'patch ha',
};

export const sampleWithNewData: NewRoteirizacao = {
  dataHoraPrimeiraColeta: dayjs('2024-01-20T07:41'),
  dataCadastro: dayjs('2024-01-19T21:02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
