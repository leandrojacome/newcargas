import dayjs from 'dayjs/esm';
import { IContratacao } from 'app/entities/contratacao/contratacao.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';

export interface ITomadaPreco {
  id: number;
  dataHoraEnvio?: dayjs.Dayjs | null;
  prazoResposta?: number | null;
  valorTotal?: number | null;
  observacao?: string | null;
  aprovado?: boolean | null;
  cancelado?: boolean | null;
  removido?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  contratacao?: Pick<IContratacao, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
  roteirizacao?: Pick<IRoteirizacao, 'id'> | null;
}

export type NewTomadaPreco = Omit<ITomadaPreco, 'id'> & { id: null };
