import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';

export interface ITipoVeiculo {
  id: number;
  nome?: string | null;
  descricao?: string | null;
  solitacaoColetas?: Pick<ISolicitacaoColeta, 'id'>[] | null;
}

export type NewTipoVeiculo = Omit<ITipoVeiculo, 'id'> & { id: null };
