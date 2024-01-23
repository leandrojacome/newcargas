import dayjs from 'dayjs/esm';
import { IFatura } from 'app/entities/fatura/fatura.model';
import { ITomadaPreco } from 'app/entities/tomada-preco/tomada-preco.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';

export interface IContratacao {
  id: number;
  valorTotal?: number | null;
  validadeEmDias?: number | null;
  dataValidade?: dayjs.Dayjs | null;
  observacao?: string | null;
  cancelado?: boolean | null;
  removido?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  faturas?: Pick<IFatura, 'id'>[] | null;
  solicitacaoColeta?: Pick<ITomadaPreco, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
}

export type NewContratacao = Omit<IContratacao, 'id'> & { id: null };
