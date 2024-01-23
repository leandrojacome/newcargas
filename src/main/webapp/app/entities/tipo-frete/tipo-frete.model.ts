import dayjs from 'dayjs/esm';
import { ITabelaFrete } from 'app/entities/tabela-frete/tabela-frete.model';

export interface ITipoFrete {
  id: number;
  nome?: string | null;
  descricao?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  tabelaFretes?: Pick<ITabelaFrete, 'id'>[] | null;
}

export type NewTipoFrete = Omit<ITipoFrete, 'id'> & { id: null };
