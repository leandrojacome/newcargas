import dayjs from 'dayjs/esm';
import { IFatura } from 'app/entities/fatura/fatura.model';
import { ITomadaPreco } from 'app/entities/tomada-preco/tomada-preco.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';

export interface IContratacao {
  id: number;
  valorTotal?: number | null;
  validadeEmDias?: number | null;
  dataValidade?: dayjs.Dayjs | null;
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
  faturas?: Pick<IFatura, 'id'>[] | null;
  solicitacaoColeta?: Pick<ITomadaPreco, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
}

export type NewContratacao = Omit<IContratacao, 'id'> & { id: null };
