import { ITabelaFrete } from 'app/entities/tabela-frete/tabela-frete.model';

export interface ITipoFrete {
  id: number;
  nome?: string | null;
  descricao?: string | null;
  tabelaFretes?: Pick<ITabelaFrete, 'id'>[] | null;
}

export type NewTipoFrete = Omit<ITipoFrete, 'id'> & { id: null };
