import dayjs from 'dayjs/esm';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { IContratacao } from 'app/entities/contratacao/contratacao.model';
import { IFormaCobranca } from 'app/entities/forma-cobranca/forma-cobranca.model';
import { TipoFatura } from 'app/entities/enumerations/tipo-fatura.model';

export interface IFatura {
  id: number;
  tipo?: keyof typeof TipoFatura | null;
  dataFatura?: dayjs.Dayjs | null;
  dataVencimento?: dayjs.Dayjs | null;
  dataPagamento?: dayjs.Dayjs | null;
  numeroParcela?: number | null;
  valorTotal?: number | null;
  observacao?: string | null;
  dataCadastro?: dayjs.Dayjs | null;
  usuarioCadastro?: string | null;
  dataAtualizacao?: dayjs.Dayjs | null;
  usuarioAtualizacao?: string | null;
  cancelado?: boolean | null;
  dataCancelamento?: dayjs.Dayjs | null;
  usuarioCancelamento?: string | null;
  removido?: boolean | null;
  dataRemocao?: dayjs.Dayjs | null;
  usuarioRemocao?: string | null;
  embarcador?: Pick<IEmbarcador, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
  contratacao?: Pick<IContratacao, 'id'> | null;
  formaCobranca?: Pick<IFormaCobranca, 'id'> | null;
}

export type NewFatura = Omit<IFatura, 'id'> & { id: null };
