import dayjs from 'dayjs/esm';
import { ITabelaFrete } from 'app/entities/tabela-frete/tabela-frete.model';

export interface IRegiao {
  id: number;
  nome?: string | null;
  sigla?: string | null;
  descricao?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  tabelaFreteOrigems?: Pick<ITabelaFrete, 'id'>[] | null;
  tabelaFreteDestinos?: Pick<ITabelaFrete, 'id'>[] | null;
}

export type NewRegiao = Omit<IRegiao, 'id'> & { id: null };
