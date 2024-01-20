import dayjs from 'dayjs/esm';

import { ITransportadora, NewTransportadora } from './transportadora.model';

export const sampleWithRequiredData: ITransportadora = {
  id: 16392,
  nome: 'ugh',
  cnpj: 'regularly booX',
  dataCadastro: dayjs('2024-01-20T08:39'),
};

export const sampleWithPartialData: ITransportadora = {
  id: 6260,
  nome: 'atop slapstick',
  cnpj: 'frightfullyXXX',
  razaoSocial: 'ship',
  inscricaoEstadual: 'wrapping ugh',
  responsavel: 'pray',
  cep: 'plush ic',
  endereco: 'scarcely to',
  numero: 'prime terr',
  bairro: 'finally distrust',
  telefone: 'hustleXXXX',
  email: 'MariaCecilia.Santos18@gmail.com',
  observacao: 'regularly',
  dataCadastro: dayjs('2024-01-19T20:34'),
};

export const sampleWithFullData: ITransportadora = {
  id: 1979,
  nome: 'modulo yippee burdensome',
  cnpj: 'thousandXXXXXX',
  razaoSocial: 'even about deglaze',
  inscricaoEstadual: 'refuel',
  inscricaoMunicipal: 'joyously pfft',
  responsavel: 'even icky',
  cep: 'hastilyX',
  endereco: 'ugh',
  numero: 'anenst',
  complemento: 'ew phooey',
  bairro: 'immediate how now',
  telefone: 'indeedXXXX',
  email: 'Yago29@bol.com.br',
  observacao: 'regarding hm',
  dataCadastro: dayjs('2024-01-20T00:13'),
  usuarioCadastro: 'fooey',
  dataAtualizacao: dayjs('2024-01-20T02:32'),
  usuarioAtualizacao: 'immense',
};

export const sampleWithNewData: NewTransportadora = {
  nome: 'overconfidently thoroughly',
  cnpj: 'finally doctor',
  dataCadastro: dayjs('2024-01-20T15:21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
