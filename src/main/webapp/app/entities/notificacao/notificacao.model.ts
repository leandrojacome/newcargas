import dayjs from 'dayjs/esm';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TipoNotificacao } from 'app/entities/enumerations/tipo-notificacao.model';

export interface INotificacao {
  id: number;
  tipo?: keyof typeof TipoNotificacao | null;
  email?: string | null;
  telefone?: string | null;
  assunto?: string | null;
  mensagem?: string | null;
  dataHoraEnvio?: dayjs.Dayjs | null;
  dataHoraLeitura?: dayjs.Dayjs | null;
  dataCadastro?: dayjs.Dayjs | null;
  dataAtualizacao?: dayjs.Dayjs | null;
  lido?: boolean | null;
  dataLeitura?: dayjs.Dayjs | null;
  removido?: boolean | null;
  dataRemocao?: dayjs.Dayjs | null;
  usuarioRemocao?: string | null;
  embarcador?: Pick<IEmbarcador, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
}

export type NewNotificacao = Omit<INotificacao, 'id'> & { id: null };
