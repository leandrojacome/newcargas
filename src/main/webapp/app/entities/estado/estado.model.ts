import dayjs from 'dayjs/esm';
import { ICidade } from 'app/entities/cidade/cidade.model';

export interface IEstado {
  id: number;
  nome?: string | null;
  sigla?: string | null;
  codigoIbge?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  cidades?: Pick<ICidade, 'id'>[] | null;
}

export type NewEstado = Omit<IEstado, 'id'> & { id: null };
