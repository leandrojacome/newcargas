import dayjs from 'dayjs/esm';
import { IContaBancaria } from 'app/entities/conta-bancaria/conta-bancaria.model';

export interface IBanco {
  id: number;
  nome?: string | null;
  codigo?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  contaBancarias?: Pick<IContaBancaria, 'id'>[] | null;
}

export type NewBanco = Omit<IBanco, 'id'> & { id: null };
