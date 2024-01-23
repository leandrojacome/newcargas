import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFatura, NewFatura } from '../fatura.model';

export type PartialUpdateFatura = Partial<IFatura> & Pick<IFatura, 'id'>;

type RestOf<T extends IFatura | NewFatura> = Omit<
  T,
  'dataFatura' | 'dataVencimento' | 'dataPagamento' | 'createdDate' | 'lastModifiedDate'
> & {
  dataFatura?: string | null;
  dataVencimento?: string | null;
  dataPagamento?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestFatura = RestOf<IFatura>;

export type NewRestFatura = RestOf<NewFatura>;

export type PartialUpdateRestFatura = RestOf<PartialUpdateFatura>;

export type EntityResponseType = HttpResponse<IFatura>;
export type EntityArrayResponseType = HttpResponse<IFatura[]>;

@Injectable({ providedIn: 'root' })
export class FaturaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/faturas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/faturas/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(fatura: NewFatura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fatura);
    return this.http
      .post<RestFatura>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fatura: IFatura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fatura);
    return this.http
      .put<RestFatura>(`${this.resourceUrl}/${this.getFaturaIdentifier(fatura)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fatura: PartialUpdateFatura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fatura);
    return this.http
      .patch<RestFatura>(`${this.resourceUrl}/${this.getFaturaIdentifier(fatura)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFatura>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFatura[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestFatura[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IFatura[]>()], asapScheduler)),
    );
  }

  getFaturaIdentifier(fatura: Pick<IFatura, 'id'>): number {
    return fatura.id;
  }

  compareFatura(o1: Pick<IFatura, 'id'> | null, o2: Pick<IFatura, 'id'> | null): boolean {
    return o1 && o2 ? this.getFaturaIdentifier(o1) === this.getFaturaIdentifier(o2) : o1 === o2;
  }

  addFaturaToCollectionIfMissing<Type extends Pick<IFatura, 'id'>>(
    faturaCollection: Type[],
    ...faturasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const faturas: Type[] = faturasToCheck.filter(isPresent);
    if (faturas.length > 0) {
      const faturaCollectionIdentifiers = faturaCollection.map(faturaItem => this.getFaturaIdentifier(faturaItem)!);
      const faturasToAdd = faturas.filter(faturaItem => {
        const faturaIdentifier = this.getFaturaIdentifier(faturaItem);
        if (faturaCollectionIdentifiers.includes(faturaIdentifier)) {
          return false;
        }
        faturaCollectionIdentifiers.push(faturaIdentifier);
        return true;
      });
      return [...faturasToAdd, ...faturaCollection];
    }
    return faturaCollection;
  }

  protected convertDateFromClient<T extends IFatura | NewFatura | PartialUpdateFatura>(fatura: T): RestOf<T> {
    return {
      ...fatura,
      dataFatura: fatura.dataFatura?.toJSON() ?? null,
      dataVencimento: fatura.dataVencimento?.toJSON() ?? null,
      dataPagamento: fatura.dataPagamento?.toJSON() ?? null,
      createdDate: fatura.createdDate?.toJSON() ?? null,
      lastModifiedDate: fatura.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFatura: RestFatura): IFatura {
    return {
      ...restFatura,
      dataFatura: restFatura.dataFatura ? dayjs(restFatura.dataFatura) : undefined,
      dataVencimento: restFatura.dataVencimento ? dayjs(restFatura.dataVencimento) : undefined,
      dataPagamento: restFatura.dataPagamento ? dayjs(restFatura.dataPagamento) : undefined,
      createdDate: restFatura.createdDate ? dayjs(restFatura.createdDate) : undefined,
      lastModifiedDate: restFatura.lastModifiedDate ? dayjs(restFatura.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFatura>): HttpResponse<IFatura> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFatura[]>): HttpResponse<IFatura[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
