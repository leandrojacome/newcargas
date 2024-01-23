import dayjs from 'dayjs/esm';
import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';

export interface ITipoVeiculo {
  id: number;
  nome?: string | null;
  descricao?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  solitacaoColetas?: Pick<ISolicitacaoColeta, 'id'>[] | null;
}

export type NewTipoVeiculo = Omit<ITipoVeiculo, 'id'> & { id: null };
