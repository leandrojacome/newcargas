import dayjs from 'dayjs/esm';
import { ICidade } from 'app/entities/cidade/cidade.model';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { INotaFiscalColeta } from 'app/entities/nota-fiscal-coleta/nota-fiscal-coleta.model';
import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { TipoEndereco } from 'app/entities/enumerations/tipo-endereco.model';
import { IEstado } from '../estado/estado.model';

export interface IEndereco {
  id: number;
  tipo?: keyof typeof TipoEndereco | null;
  cep?: string | null;
  endereco?: string | null;
  numero?: string | null;
  complemento?: string | null;
  bairro?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  estado?: Pick<IEstado, 'id' | 'nome'> | null;
  cidade?: Pick<ICidade, 'id' | 'estado' | 'nome'> | null;
  embarcador?: Pick<IEmbarcador, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
  notaFiscalColetaOrigem?: Pick<INotaFiscalColeta, 'id'> | null;
  notaFiscalColetaDestino?: Pick<INotaFiscalColeta, 'id'> | null;
  solicitacaoColetaOrigem?: Pick<ISolicitacaoColeta, 'id'> | null;
  solicitacaoColetaDestino?: Pick<ISolicitacaoColeta, 'id'> | null;
}

export type NewEndereco = Omit<IEndereco, 'id'> & { id?: number | null };
