import dayjs from 'dayjs/esm';

import { IContaBancaria, NewContaBancaria } from './conta-bancaria.model';

export const sampleWithRequiredData: IContaBancaria = {
  id: 31391,
  agencia: 'actually s',
  conta: 'usefully g',
  dataCadastro: dayjs('2024-01-20T11:47'),
};

export const sampleWithPartialData: IContaBancaria = {
  id: 19369,
  agencia: 'previous b',
  conta: 'for midst',
  tipo: 'although',
  pix: 'into pish near',
  dataCadastro: dayjs('2024-01-20T13:20'),
};

export const sampleWithFullData: IContaBancaria = {
  id: 8472,
  agencia: 'pish',
  conta: 'searchingl',
  observacao: 'quarterly since',
  tipo: 'obsess when',
  pix: 'downforce word bowling',
  titular: 'recline',
  dataCadastro: dayjs('2024-01-19T17:05'),
  dataAtualizacao: dayjs('2024-01-19T20:36'),
};

export const sampleWithNewData: NewContaBancaria = {
  agencia: 'giddy',
  conta: 'meager til',
  dataCadastro: dayjs('2024-01-20T00:47'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
