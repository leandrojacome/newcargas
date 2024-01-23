import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { INotaFiscalColeta, NewNotaFiscalColeta } from '../nota-fiscal-coleta.model';

export type PartialUpdateNotaFiscalColeta = Partial<INotaFiscalColeta> & Pick<INotaFiscalColeta, 'id'>;

type RestOf<T extends INotaFiscalColeta | NewNotaFiscalColeta> = Omit<
  T,
  'dataEmissao' | 'dataSaida' | 'createdDate' | 'lastModifiedDate'
> & {
  dataEmissao?: string | null;
  dataSaida?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestNotaFiscalColeta = RestOf<INotaFiscalColeta>;

export type NewRestNotaFiscalColeta = RestOf<NewNotaFiscalColeta>;

export type PartialUpdateRestNotaFiscalColeta = RestOf<PartialUpdateNotaFiscalColeta>;

export type EntityResponseType = HttpResponse<INotaFiscalColeta>;
export type EntityArrayResponseType = HttpResponse<INotaFiscalColeta[]>;

@Injectable({ providedIn: 'root' })
export class NotaFiscalColetaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nota-fiscal-coletas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/nota-fiscal-coletas/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(notaFiscalColeta: NewNotaFiscalColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notaFiscalColeta);
    return this.http
      .post<RestNotaFiscalColeta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(notaFiscalColeta: INotaFiscalColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notaFiscalColeta);
    return this.http
      .put<RestNotaFiscalColeta>(`${this.resourceUrl}/${this.getNotaFiscalColetaIdentifier(notaFiscalColeta)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(notaFiscalColeta: PartialUpdateNotaFiscalColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notaFiscalColeta);
    return this.http
      .patch<RestNotaFiscalColeta>(`${this.resourceUrl}/${this.getNotaFiscalColetaIdentifier(notaFiscalColeta)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNotaFiscalColeta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotaFiscalColeta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestNotaFiscalColeta[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<INotaFiscalColeta[]>()], asapScheduler)),
    );
  }

  getNotaFiscalColetaIdentifier(notaFiscalColeta: Pick<INotaFiscalColeta, 'id'>): number {
    return notaFiscalColeta.id;
  }

  compareNotaFiscalColeta(o1: Pick<INotaFiscalColeta, 'id'> | null, o2: Pick<INotaFiscalColeta, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotaFiscalColetaIdentifier(o1) === this.getNotaFiscalColetaIdentifier(o2) : o1 === o2;
  }

  addNotaFiscalColetaToCollectionIfMissing<Type extends Pick<INotaFiscalColeta, 'id'>>(
    notaFiscalColetaCollection: Type[],
    ...notaFiscalColetasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notaFiscalColetas: Type[] = notaFiscalColetasToCheck.filter(isPresent);
    if (notaFiscalColetas.length > 0) {
      const notaFiscalColetaCollectionIdentifiers = notaFiscalColetaCollection.map(
        notaFiscalColetaItem => this.getNotaFiscalColetaIdentifier(notaFiscalColetaItem)!,
      );
      const notaFiscalColetasToAdd = notaFiscalColetas.filter(notaFiscalColetaItem => {
        const notaFiscalColetaIdentifier = this.getNotaFiscalColetaIdentifier(notaFiscalColetaItem);
        if (notaFiscalColetaCollectionIdentifiers.includes(notaFiscalColetaIdentifier)) {
          return false;
        }
        notaFiscalColetaCollectionIdentifiers.push(notaFiscalColetaIdentifier);
        return true;
      });
      return [...notaFiscalColetasToAdd, ...notaFiscalColetaCollection];
    }
    return notaFiscalColetaCollection;
  }

  protected convertDateFromClient<T extends INotaFiscalColeta | NewNotaFiscalColeta | PartialUpdateNotaFiscalColeta>(
    notaFiscalColeta: T,
  ): RestOf<T> {
    return {
      ...notaFiscalColeta,
      dataEmissao: notaFiscalColeta.dataEmissao?.toJSON() ?? null,
      dataSaida: notaFiscalColeta.dataSaida?.toJSON() ?? null,
      createdDate: notaFiscalColeta.createdDate?.toJSON() ?? null,
      lastModifiedDate: notaFiscalColeta.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restNotaFiscalColeta: RestNotaFiscalColeta): INotaFiscalColeta {
    return {
      ...restNotaFiscalColeta,
      dataEmissao: restNotaFiscalColeta.dataEmissao ? dayjs(restNotaFiscalColeta.dataEmissao) : undefined,
      dataSaida: restNotaFiscalColeta.dataSaida ? dayjs(restNotaFiscalColeta.dataSaida) : undefined,
      createdDate: restNotaFiscalColeta.createdDate ? dayjs(restNotaFiscalColeta.createdDate) : undefined,
      lastModifiedDate: restNotaFiscalColeta.lastModifiedDate ? dayjs(restNotaFiscalColeta.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNotaFiscalColeta>): HttpResponse<INotaFiscalColeta> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNotaFiscalColeta[]>): HttpResponse<INotaFiscalColeta[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
