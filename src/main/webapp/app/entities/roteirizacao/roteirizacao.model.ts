import dayjs from 'dayjs/esm';
import { IHistoricoStatusColeta } from 'app/entities/historico-status-coleta/historico-status-coleta.model';
import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { ITomadaPreco } from 'app/entities/tomada-preco/tomada-preco.model';
import { IStatusColeta } from 'app/entities/status-coleta/status-coleta.model';

export interface IRoteirizacao {
  id: number;
  dataHoraPrimeiraColeta?: dayjs.Dayjs | null;
  dataHoraUltimaColeta?: dayjs.Dayjs | null;
  dataHoraPrimeiraEntrega?: dayjs.Dayjs | null;
  dataHoraUltimaEntrega?: dayjs.Dayjs | null;
  valorTotal?: number | null;
  observacao?: string | null;
  cancelado?: boolean | null;
  removido?: boolean | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  historicoStatusColetas?: Pick<IHistoricoStatusColeta, 'id'>[] | null;
  solitacaoColetas?: Pick<ISolicitacaoColeta, 'id'>[] | null;
  tomadaPrecos?: Pick<ITomadaPreco, 'id'>[] | null;
  statusColeta?: Pick<IStatusColeta, 'id'> | null;
}

export type NewRoteirizacao = Omit<IRoteirizacao, 'id'> & { id: null };
