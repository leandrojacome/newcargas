import dayjs from 'dayjs/esm';

import { IContaBancaria, NewContaBancaria } from './conta-bancaria.model';

export const sampleWithRequiredData: IContaBancaria = {
  id: 8745,
  agencia: 'tulip sinc',
  conta: 'successful',
};

export const sampleWithPartialData: IContaBancaria = {
  id: 19369,
  agencia: 'previous b',
  conta: 'for midst',
  pix: 'although',
  createdBy: 'into pish near',
  createdDate: dayjs('2024-01-20T13:20'),
};

export const sampleWithFullData: IContaBancaria = {
  id: 8472,
  agencia: 'pish',
  conta: 'searchingl',
  observacao: 'quarterly since',
  tipo: 'obsess when',
  pix: 'downforce word bowling',
  titular: 'recline',
  createdBy: 'whether through dirty',
  createdDate: dayjs('2024-01-20T14:25'),
  lastModifiedBy: 'failing notwithstanding in',
  lastModifiedDate: dayjs('2024-01-20T09:41'),
};

export const sampleWithNewData: NewContaBancaria = {
  agencia: 'yippee',
  conta: 'reject mea',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
