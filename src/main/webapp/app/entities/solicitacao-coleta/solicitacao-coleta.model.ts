import dayjs from 'dayjs/esm';
import { INotaFiscalColeta } from 'app/entities/nota-fiscal-coleta/nota-fiscal-coleta.model';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { IHistoricoStatusColeta } from 'app/entities/historico-status-coleta/historico-status-coleta.model';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { IStatusColeta } from 'app/entities/status-coleta/status-coleta.model';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';
import { ITipoVeiculo } from 'app/entities/tipo-veiculo/tipo-veiculo.model';

export interface ISolicitacaoColeta {
  id: number;
  coletado?: boolean | null;
  dataHoraColeta?: dayjs.Dayjs | null;
  entregue?: boolean | null;
  dataHoraEntrega?: dayjs.Dayjs | null;
  valorTotal?: number | null;
  observacao?: string | null;
  dataCadastro?: dayjs.Dayjs | null;
  dataAtualizacao?: dayjs.Dayjs | null;
  cancelado?: boolean | null;
  dataCancelamento?: dayjs.Dayjs | null;
  usuarioCancelamento?: string | null;
  removido?: boolean | null;
  dataRemocao?: dayjs.Dayjs | null;
  usuarioRemocao?: string | null;
  notaFiscalColetas?: Pick<INotaFiscalColeta, 'id'>[] | null;
  enderecoOrigems?: Pick<IEndereco, 'id'>[] | null;
  enderecoDestinos?: Pick<IEndereco, 'id'>[] | null;
  historicoStatusColetas?: Pick<IHistoricoStatusColeta, 'id'>[] | null;
  embarcador?: Pick<IEmbarcador, 'id'> | null;
  statusColeta?: Pick<IStatusColeta, 'id'> | null;
  roteirizacao?: Pick<IRoteirizacao, 'id'> | null;
  tipoVeiculo?: Pick<ITipoVeiculo, 'id'> | null;
}

export type NewSolicitacaoColeta = Omit<ISolicitacaoColeta, 'id'> & { id: null };
