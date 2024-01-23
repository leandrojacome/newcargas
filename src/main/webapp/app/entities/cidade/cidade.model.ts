import dayjs from 'dayjs/esm';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { IEstado } from 'app/entities/estado/estado.model';

export interface ICidade {
  id: number;
  nome?: string | null;
  codigoIbge?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  enderecos?: Pick<IEndereco, 'id'>[] | null;
  embarcadors?: Pick<IEmbarcador, 'id'>[] | null;
  transportadoras?: Pick<ITransportadora, 'id'>[] | null;
  estado?: Pick<IEstado, 'id' | 'nome'> | null;
}

export type NewCidade = Omit<ICidade, 'id'> & { id: null };
