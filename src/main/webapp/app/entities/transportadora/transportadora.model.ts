import dayjs from 'dayjs/esm';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { ICidade } from 'app/entities/cidade/cidade.model';
import { IContaBancaria } from 'app/entities/conta-bancaria/conta-bancaria.model';
import { ITabelaFrete } from 'app/entities/tabela-frete/tabela-frete.model';
import { ITomadaPreco } from 'app/entities/tomada-preco/tomada-preco.model';
import { IContratacao } from 'app/entities/contratacao/contratacao.model';
import { INotificacao } from 'app/entities/notificacao/notificacao.model';
import { IFatura } from 'app/entities/fatura/fatura.model';

export interface ITransportadora {
  id: number;
  nome?: string | null;
  cnpj?: string | null;
  razaoSocial?: string | null;
  inscricaoEstadual?: string | null;
  inscricaoMunicipal?: string | null;
  responsavel?: string | null;
  cep?: string | null;
  endereco?: string | null;
  numero?: string | null;
  complemento?: string | null;
  bairro?: string | null;
  telefone?: string | null;
  email?: string | null;
  observacao?: string | null;
  dataCadastro?: dayjs.Dayjs | null;
  usuarioCadastro?: string | null;
  dataAtualizacao?: dayjs.Dayjs | null;
  usuarioAtualizacao?: string | null;
  enderecos?: Pick<IEndereco, 'id'>[] | null;
  cidades?: Pick<ICidade, 'id'>[] | null;
  contaBancarias?: Pick<IContaBancaria, 'id'>[] | null;
  tabelaFretes?: Pick<ITabelaFrete, 'id'>[] | null;
  tomadaPrecos?: Pick<ITomadaPreco, 'id'>[] | null;
  contratacaos?: Pick<IContratacao, 'id'>[] | null;
  notificacaos?: Pick<INotificacao, 'id'>[] | null;
  faturas?: Pick<IFatura, 'id'>[] | null;
}

export type NewTransportadora = Omit<ITransportadora, 'id'> & { id: null };
