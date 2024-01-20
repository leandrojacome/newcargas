import dayjs from 'dayjs/esm';
import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';
import { IStatusColeta } from 'app/entities/status-coleta/status-coleta.model';

export interface IHistoricoStatusColeta {
  id: number;
  dataCriacao?: dayjs.Dayjs | null;
  observacao?: string | null;
  solicitacaoColeta?: Pick<ISolicitacaoColeta, 'id'> | null;
  roteirizacao?: Pick<IRoteirizacao, 'id'> | null;
  statusColetaOrigem?: Pick<IStatusColeta, 'id'> | null;
  statusColetaDestino?: Pick<IStatusColeta, 'id'> | null;
}

export type NewHistoricoStatusColeta = Omit<IHistoricoStatusColeta, 'id'> & { id: null };
