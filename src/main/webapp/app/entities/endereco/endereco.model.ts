import { ICidade } from 'app/entities/cidade/cidade.model';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { INotaFiscalColeta } from 'app/entities/nota-fiscal-coleta/nota-fiscal-coleta.model';
import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { TipoEndereco } from 'app/entities/enumerations/tipo-endereco.model';

export interface IEndereco {
  id: number;
  tipo?: keyof typeof TipoEndereco | null;
  cep?: string | null;
  endereco?: string | null;
  numero?: string | null;
  complemento?: string | null;
  bairro?: string | null;
  cidade?: Pick<ICidade, 'id'> | null;
  embarcador?: Pick<IEmbarcador, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
  notaFiscalColetaOrigem?: Pick<INotaFiscalColeta, 'id'> | null;
  notaFiscalColetaDestino?: Pick<INotaFiscalColeta, 'id'> | null;
  solicitacaoColetaOrigem?: Pick<ISolicitacaoColeta, 'id'> | null;
  solicitacaoColetaDestino?: Pick<ISolicitacaoColeta, 'id'> | null;
}

export type NewEndereco = Omit<IEndereco, 'id'> & { id: null };
