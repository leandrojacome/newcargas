import { ITabelaFrete } from 'app/entities/tabela-frete/tabela-frete.model';
import { IFatura } from 'app/entities/fatura/fatura.model';

export interface IFormaCobranca {
  id: number;
  nome?: string | null;
  descricao?: string | null;
  tabelaFretes?: Pick<ITabelaFrete, 'id'>[] | null;
  fatutas?: Pick<IFatura, 'id'>[] | null;
}

export type NewFormaCobranca = Omit<IFormaCobranca, 'id'> & { id: null };
