import dayjs from 'dayjs/esm';

import { INotificacao, NewNotificacao } from './notificacao.model';

export const sampleWithRequiredData: INotificacao = {
  id: 32732,
  tipo: 'EMBARCADOR',
  assunto: 'fruitful vibrissae meh',
  mensagem: 'idolized fast across',
  dataHoraEnvio: dayjs('2024-01-20T09:30'),
};

export const sampleWithPartialData: INotificacao = {
  id: 27870,
  tipo: 'EMBARCADOR',
  telefone: 'monthlyXXX',
  assunto: 'makeover positive',
  mensagem: 'impish',
  dataHoraEnvio: dayjs('2024-01-20T14:12'),
  dataHoraLeitura: dayjs('2024-01-20T05:34'),
  lido: true,
  dataLeitura: dayjs('2024-01-19T21:56'),
  createdDate: dayjs('2024-01-20T12:11'),
};

export const sampleWithFullData: INotificacao = {
  id: 32557,
  tipo: 'TRANSPORTADORA',
  email: 'AnaClara_Macedo@bol.com.br',
  telefone: 'worthXXXXX',
  assunto: 'declaim which',
  mensagem: 'via naturally searchingly',
  dataHoraEnvio: dayjs('2024-01-20T13:57'),
  dataHoraLeitura: dayjs('2024-01-20T10:56'),
  lido: true,
  dataLeitura: dayjs('2024-01-19T22:07'),
  removido: true,
  createdBy: 'mid flash ugh',
  createdDate: dayjs('2024-01-20T11:48'),
  lastModifiedBy: 'pony',
  lastModifiedDate: dayjs('2024-01-20T05:56'),
};

export const sampleWithNewData: NewNotificacao = {
  tipo: 'TRANSPORTADORA',
  assunto: 'huzzah cargo upstage',
  mensagem: 'parole',
  dataHoraEnvio: dayjs('2024-01-20T16:08'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
