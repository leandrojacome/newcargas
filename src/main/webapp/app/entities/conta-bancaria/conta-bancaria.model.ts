import dayjs from 'dayjs/esm';
import { IBanco } from 'app/entities/banco/banco.model';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';

export interface IContaBancaria {
  id: number;
  agencia?: string | null;
  conta?: string | null;
  observacao?: string | null;
  tipo?: string | null;
  pix?: string | null;
  titular?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  banco?: Pick<IBanco, 'id'> | null;
  embarcador?: Pick<IEmbarcador, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
}

export type NewContaBancaria = Omit<IContaBancaria, 'id'> & { id: null };
