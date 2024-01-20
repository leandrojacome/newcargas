import dayjs from 'dayjs/esm';
import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { IHistoricoStatusColeta } from 'app/entities/historico-status-coleta/historico-status-coleta.model';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';

export interface IStatusColeta {
  id: number;
  nome?: string | null;
  cor?: string | null;
  ordem?: number | null;
  estadoInicial?: boolean | null;
  estadoFinal?: boolean | null;
  permiteCancelar?: boolean | null;
  permiteEditar?: boolean | null;
  permiteExcluir?: boolean | null;
  descricao?: string | null;
  dataCadastro?: dayjs.Dayjs | null;
  usuarioCadastro?: string | null;
  dataAtualizacao?: dayjs.Dayjs | null;
  usuarioAtualizacao?: string | null;
  ativo?: boolean | null;
  removido?: boolean | null;
  dataRemocao?: dayjs.Dayjs | null;
  usuarioRemocao?: string | null;
  solicitacaoColetas?: Pick<ISolicitacaoColeta, 'id'>[] | null;
  historicoStatusColetaOrigems?: Pick<IHistoricoStatusColeta, 'id'>[] | null;
  historicoStatusColetaDestinos?: Pick<IHistoricoStatusColeta, 'id'>[] | null;
  roteirizacaos?: Pick<IRoteirizacao, 'id'>[] | null;
  statusColetaOrigems?: Pick<IStatusColeta, 'id'>[] | null;
  statusColetaDestinos?: Pick<IStatusColeta, 'id'>[] | null;
}

export type NewStatusColeta = Omit<IStatusColeta, 'id'> & { id: null };
