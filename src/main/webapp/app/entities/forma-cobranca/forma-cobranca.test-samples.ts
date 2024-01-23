import dayjs from 'dayjs/esm';

import { IFormaCobranca, NewFormaCobranca } from './forma-cobranca.model';

export const sampleWithRequiredData: IFormaCobranca = {
  id: 12433,
  nome: 'relate solemnly',
};

export const sampleWithPartialData: IFormaCobranca = {
  id: 11419,
  nome: 'quizzically which potable',
  descricao: 'that',
  lastModifiedDate: dayjs('2024-01-20T08:41'),
};

export const sampleWithFullData: IFormaCobranca = {
  id: 11824,
  nome: 'instead atop',
  descricao: 'contaminate',
  createdBy: 'blah',
  createdDate: dayjs('2024-01-20T00:54'),
  lastModifiedBy: 'democratise',
  lastModifiedDate: dayjs('2024-01-20T00:01'),
};

export const sampleWithNewData: NewFormaCobranca = {
  nome: 'rejuvenate than',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
