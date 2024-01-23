import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITipoFrete, NewTipoFrete } from '../tipo-frete.model';

export type PartialUpdateTipoFrete = Partial<ITipoFrete> & Pick<ITipoFrete, 'id'>;

type RestOf<T extends ITipoFrete | NewTipoFrete> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestTipoFrete = RestOf<ITipoFrete>;

export type NewRestTipoFrete = RestOf<NewTipoFrete>;

export type PartialUpdateRestTipoFrete = RestOf<PartialUpdateTipoFrete>;

export type EntityResponseType = HttpResponse<ITipoFrete>;
export type EntityArrayResponseType = HttpResponse<ITipoFrete[]>;

@Injectable({ providedIn: 'root' })
export class TipoFreteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-fretes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tipo-fretes/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(tipoFrete: NewTipoFrete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoFrete);
    return this.http
      .post<RestTipoFrete>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tipoFrete: ITipoFrete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoFrete);
    return this.http
      .put<RestTipoFrete>(`${this.resourceUrl}/${this.getTipoFreteIdentifier(tipoFrete)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tipoFrete: PartialUpdateTipoFrete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoFrete);
    return this.http
      .patch<RestTipoFrete>(`${this.resourceUrl}/${this.getTipoFreteIdentifier(tipoFrete)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTipoFrete>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTipoFrete[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTipoFrete[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ITipoFrete[]>()], asapScheduler)),
    );
  }

  getTipoFreteIdentifier(tipoFrete: Pick<ITipoFrete, 'id'>): number {
    return tipoFrete.id;
  }

  compareTipoFrete(o1: Pick<ITipoFrete, 'id'> | null, o2: Pick<ITipoFrete, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoFreteIdentifier(o1) === this.getTipoFreteIdentifier(o2) : o1 === o2;
  }

  addTipoFreteToCollectionIfMissing<Type extends Pick<ITipoFrete, 'id'>>(
    tipoFreteCollection: Type[],
    ...tipoFretesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoFretes: Type[] = tipoFretesToCheck.filter(isPresent);
    if (tipoFretes.length > 0) {
      const tipoFreteCollectionIdentifiers = tipoFreteCollection.map(tipoFreteItem => this.getTipoFreteIdentifier(tipoFreteItem)!);
      const tipoFretesToAdd = tipoFretes.filter(tipoFreteItem => {
        const tipoFreteIdentifier = this.getTipoFreteIdentifier(tipoFreteItem);
        if (tipoFreteCollectionIdentifiers.includes(tipoFreteIdentifier)) {
          return false;
        }
        tipoFreteCollectionIdentifiers.push(tipoFreteIdentifier);
        return true;
      });
      return [...tipoFretesToAdd, ...tipoFreteCollection];
    }
    return tipoFreteCollection;
  }

  protected convertDateFromClient<T extends ITipoFrete | NewTipoFrete | PartialUpdateTipoFrete>(tipoFrete: T): RestOf<T> {
    return {
      ...tipoFrete,
      createdDate: tipoFrete.createdDate?.toJSON() ?? null,
      lastModifiedDate: tipoFrete.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTipoFrete: RestTipoFrete): ITipoFrete {
    return {
      ...restTipoFrete,
      createdDate: restTipoFrete.createdDate ? dayjs(restTipoFrete.createdDate) : undefined,
      lastModifiedDate: restTipoFrete.lastModifiedDate ? dayjs(restTipoFrete.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTipoFrete>): HttpResponse<ITipoFrete> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTipoFrete[]>): HttpResponse<ITipoFrete[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
