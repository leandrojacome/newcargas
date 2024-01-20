import dayjs from 'dayjs/esm';
import { IContratacao } from 'app/entities/contratacao/contratacao.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';

export interface ITomadaPreco {
  id: number;
  dataHoraEnvio?: dayjs.Dayjs | null;
  prazoResposta?: number | null;
  valorTotal?: number | null;
  observacao?: string | null;
  dataCadastro?: dayjs.Dayjs | null;
  usuarioCadastro?: string | null;
  dataAtualizacao?: dayjs.Dayjs | null;
  usuarioAtualizacao?: string | null;
  aprovado?: boolean | null;
  dataAprovacao?: dayjs.Dayjs | null;
  usuarioAprovacao?: string | null;
  cancelado?: boolean | null;
  dataCancelamento?: dayjs.Dayjs | null;
  usuarioCancelamento?: string | null;
  removido?: boolean | null;
  dataRemocao?: dayjs.Dayjs | null;
  usuarioRemocao?: string | null;
  contratacao?: Pick<IContratacao, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
  roteirizacao?: Pick<IRoteirizacao, 'id'> | null;
}

export type NewTomadaPreco = Omit<ITomadaPreco, 'id'> & { id: null };
