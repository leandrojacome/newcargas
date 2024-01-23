import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITipoCarga, NewTipoCarga } from '../tipo-carga.model';

export type PartialUpdateTipoCarga = Partial<ITipoCarga> & Pick<ITipoCarga, 'id'>;

type RestOf<T extends ITipoCarga | NewTipoCarga> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestTipoCarga = RestOf<ITipoCarga>;

export type NewRestTipoCarga = RestOf<NewTipoCarga>;

export type PartialUpdateRestTipoCarga = RestOf<PartialUpdateTipoCarga>;

export type EntityResponseType = HttpResponse<ITipoCarga>;
export type EntityArrayResponseType = HttpResponse<ITipoCarga[]>;

@Injectable({ providedIn: 'root' })
export class TipoCargaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-cargas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tipo-cargas/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(tipoCarga: NewTipoCarga): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoCarga);
    return this.http
      .post<RestTipoCarga>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tipoCarga: ITipoCarga): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoCarga);
    return this.http
      .put<RestTipoCarga>(`${this.resourceUrl}/${this.getTipoCargaIdentifier(tipoCarga)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tipoCarga: PartialUpdateTipoCarga): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoCarga);
    return this.http
      .patch<RestTipoCarga>(`${this.resourceUrl}/${this.getTipoCargaIdentifier(tipoCarga)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTipoCarga>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTipoCarga[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTipoCarga[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ITipoCarga[]>()], asapScheduler)),
    );
  }

  getTipoCargaIdentifier(tipoCarga: Pick<ITipoCarga, 'id'>): number {
    return tipoCarga.id;
  }

  compareTipoCarga(o1: Pick<ITipoCarga, 'id'> | null, o2: Pick<ITipoCarga, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoCargaIdentifier(o1) === this.getTipoCargaIdentifier(o2) : o1 === o2;
  }

  addTipoCargaToCollectionIfMissing<Type extends Pick<ITipoCarga, 'id'>>(
    tipoCargaCollection: Type[],
    ...tipoCargasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoCargas: Type[] = tipoCargasToCheck.filter(isPresent);
    if (tipoCargas.length > 0) {
      const tipoCargaCollectionIdentifiers = tipoCargaCollection.map(tipoCargaItem => this.getTipoCargaIdentifier(tipoCargaItem)!);
      const tipoCargasToAdd = tipoCargas.filter(tipoCargaItem => {
        const tipoCargaIdentifier = this.getTipoCargaIdentifier(tipoCargaItem);
        if (tipoCargaCollectionIdentifiers.includes(tipoCargaIdentifier)) {
          return false;
        }
        tipoCargaCollectionIdentifiers.push(tipoCargaIdentifier);
        return true;
      });
      return [...tipoCargasToAdd, ...tipoCargaCollection];
    }
    return tipoCargaCollection;
  }

  protected convertDateFromClient<T extends ITipoCarga | NewTipoCarga | PartialUpdateTipoCarga>(tipoCarga: T): RestOf<T> {
    return {
      ...tipoCarga,
      createdDate: tipoCarga.createdDate?.toJSON() ?? null,
      lastModifiedDate: tipoCarga.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTipoCarga: RestTipoCarga): ITipoCarga {
    return {
      ...restTipoCarga,
      createdDate: restTipoCarga.createdDate ? dayjs(restTipoCarga.createdDate) : undefined,
      lastModifiedDate: restTipoCarga.lastModifiedDate ? dayjs(restTipoCarga.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTipoCarga>): HttpResponse<ITipoCarga> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTipoCarga[]>): HttpResponse<ITipoCarga[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
