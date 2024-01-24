import dayjs from 'dayjs/esm';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { ITipoCarga } from 'app/entities/tipo-carga/tipo-carga.model';
import { ITipoFrete } from 'app/entities/tipo-frete/tipo-frete.model';
import { IFormaCobranca } from 'app/entities/forma-cobranca/forma-cobranca.model';
import { IRegiao } from 'app/entities/regiao/regiao.model';
import { TipoTabelaFrete } from 'app/entities/enumerations/tipo-tabela-frete.model';
import { ITipoVeiculo } from '../tipo-veiculo/tipo-veiculo.model';

export interface ITabelaFrete {
  id: number;
  tipo?: keyof typeof TipoTabelaFrete | null;
  nome?: string | null;
  descricao?: string | null;
  leadTime?: number | null;
  freteMinimo?: number | null;
  valorTonelada?: number | null;
  valorMetroCubico?: number | null;
  valorUnidade?: number | null;
  valorKm?: number | null;
  valorAdicional?: number | null;
  valorColeta?: number | null;
  valorEntrega?: number | null;
  valorTotal?: number | null;
  valorKmAdicional?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  embarcador?: Pick<IEmbarcador, 'id'> | null;
  transportadora?: Pick<ITransportadora, 'id'> | null;
  tipoCarga?: Pick<ITipoCarga, 'id'> | null;
  tipoFrete?: Pick<ITipoFrete, 'id'> | null;
  formaCobranca?: Pick<IFormaCobranca, 'id'> | null;
  regiaoOrigem?: Pick<IRegiao, 'id'> | null;
  regiaoDestino?: Pick<IRegiao, 'id'> | null;
  tipoVeiculo?: Pick<ITipoVeiculo, 'id'> | null;
}

export type NewTabelaFrete = Omit<ITabelaFrete, 'id'> & { id: null };
