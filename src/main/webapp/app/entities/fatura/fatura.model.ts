import dayjs from 'dayjs/esm';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { IContratacao } from 'app/entities/contratacao/contratacao.model';
import { IFormaCobranca } from 'app/entities/forma-cobranca/forma-cobranca.model';
import { TipoFatura } from 'app/entities/enumerations/tipo-fatura.model';

export interface IFatura {
  id: number;
  tipo?: keyof typeof TipoFatura | null;
  dataFatura?: dayjs.Dayjs | null;
  dataVencimento?: dayjs.Dayjs | null;
  dataPagamento?: dayjs.Dayjs | null;
  numeroParcela?: number | null;
  valorTotal?: number | null;
  observacao?: string | null;
  cancelado?: boolean | null;
  removido?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  embarcador?: Pick<IEmbarcador, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
  contratacao?: Pick<IContratacao, 'id'> | null;
  formaCobranca?: Pick<IFormaCobranca, 'id'> | null;
}

export type NewFatura = Omit<IFatura, 'id'> & { id: null };
