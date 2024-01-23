import dayjs from 'dayjs/esm';

import { ITomadaPreco, NewTomadaPreco } from './tomada-preco.model';

export const sampleWithRequiredData: ITomadaPreco = {
  id: 6262,
  dataHoraEnvio: dayjs('2024-01-20T14:21'),
};

export const sampleWithPartialData: ITomadaPreco = {
  id: 29456,
  dataHoraEnvio: dayjs('2024-01-19T19:17'),
  prazoResposta: 2,
  valorTotal: 3.24,
  observacao: 'consequently oof innocent',
  cancelado: true,
};

export const sampleWithFullData: ITomadaPreco = {
  id: 23810,
  dataHoraEnvio: dayjs('2024-01-19T21:04'),
  prazoResposta: 4,
  valorTotal: 1.58,
  observacao: 'inexperienced rein',
  aprovado: true,
  cancelado: true,
  removido: true,
  createdBy: 'spin tamper until',
  createdDate: dayjs('2024-01-19T21:22'),
  lastModifiedBy: 'oof ice-cream',
  lastModifiedDate: dayjs('2024-01-20T06:04'),
};

export const sampleWithNewData: NewTomadaPreco = {
  dataHoraEnvio: dayjs('2024-01-19T23:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
