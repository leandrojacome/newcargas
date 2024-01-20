import { ICidade } from 'app/entities/cidade/cidade.model';

export interface IEstado {
  id: number;
  nome?: string | null;
  sigla?: string | null;
  codigoIbge?: number | null;
  cidades?: Pick<ICidade, 'id'>[] | null;
}

export type NewEstado = Omit<IEstado, 'id'> & { id: null };
