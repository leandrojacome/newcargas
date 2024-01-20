import dayjs from 'dayjs/esm';

import { IEmbarcador, NewEmbarcador } from './embarcador.model';

export const sampleWithRequiredData: IEmbarcador = {
  id: 17450,
  nome: 'whoa superior',
  cnpj: 'misplace simil',
  dataCadastro: dayjs('2024-01-20T09:35'),
};

export const sampleWithPartialData: IEmbarcador = {
  id: 26180,
  nome: 'scaly',
  cnpj: 'collapseXXXXXX',
  inscricaoMunicipal: 'wonderfully abnegate',
  numero: 'astride vi',
  complemento: 'ferociously frigid and',
  bairro: 'assured mousse',
  observacao: 'growing hard atop',
  dataCadastro: dayjs('2024-01-20T06:24'),
  usuarioCadastro: 'mumbling',
  dataAtualizacao: dayjs('2024-01-20T06:37'),
};

export const sampleWithFullData: IEmbarcador = {
  id: 11170,
  nome: 'instead',
  cnpj: 'not upwardlyXX',
  razaoSocial: 'loosen qua survival',
  inscricaoEstadual: 'excluding seriously ',
  inscricaoMunicipal: 'wherever only even',
  responsavel: 'what',
  cep: 'deadXXXX',
  endereco: 'aw',
  numero: 'hmph pussy',
  complemento: 'scarf duh',
  bairro: 'wan mime',
  telefone: 'live tired',
  email: 'Heitor6@hotmail.com',
  observacao: 'what bomb',
  dataCadastro: dayjs('2024-01-20T10:36'),
  usuarioCadastro: 'duckling',
  dataAtualizacao: dayjs('2024-01-19T21:09'),
  usuarioAtualizacao: 'dearest yippee gambol',
};

export const sampleWithNewData: NewEmbarcador = {
  nome: 'toward why',
  cnpj: 'earnings glide',
  dataCadastro: dayjs('2024-01-20T16:16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
