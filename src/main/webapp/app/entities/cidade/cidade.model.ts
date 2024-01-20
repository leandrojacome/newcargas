import { IEndereco } from 'app/entities/endereco/endereco.model';
import { IEstado } from 'app/entities/estado/estado.model';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';

export interface ICidade {
  id: number;
  nome?: string | null;
  codigoIbge?: number | null;
  enderecos?: Pick<IEndereco, 'id'>[] | null;
  estado?: Pick<IEstado, 'id'> | null;
  embarcador?: Pick<IEmbarcador, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
}

export type NewCidade = Omit<ICidade, 'id'> & { id: null };
