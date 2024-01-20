import { ITabelaFrete } from 'app/entities/tabela-frete/tabela-frete.model';

export interface ITipoCarga {
  id: number;
  nome?: string | null;
  descricao?: string | null;
  tabelaFretes?: Pick<ITabelaFrete, 'id'>[] | null;
}

export type NewTipoCarga = Omit<ITipoCarga, 'id'> & { id: null };
