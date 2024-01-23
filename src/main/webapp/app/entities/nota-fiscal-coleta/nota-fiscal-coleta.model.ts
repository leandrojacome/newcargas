import dayjs from 'dayjs/esm';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';

export interface INotaFiscalColeta {
  id: number;
  numero?: string | null;
  serie?: string | null;
  remetente?: string | null;
  destinatario?: string | null;
  metroCubico?: number | null;
  quantidade?: number | null;
  peso?: number | null;
  dataEmissao?: dayjs.Dayjs | null;
  dataSaida?: dayjs.Dayjs | null;
  valorTotal?: number | null;
  pesoTotal?: number | null;
  quantidadeTotal?: number | null;
  observacao?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  enderecoOrigems?: Pick<IEndereco, 'id'>[] | null;
  enderecoDestinos?: Pick<IEndereco, 'id'>[] | null;
  solicitacaoColeta?: Pick<ISolicitacaoColeta, 'id'> | null;
}

export type NewNotaFiscalColeta = Omit<INotaFiscalColeta, 'id'> & { id: null };
