import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITomadaPreco, NewTomadaPreco } from '../tomada-preco.model';

export type PartialUpdateTomadaPreco = Partial<ITomadaPreco> & Pick<ITomadaPreco, 'id'>;

type RestOf<T extends ITomadaPreco | NewTomadaPreco> = Omit<T, 'dataHoraEnvio' | 'createdDate' | 'lastModifiedDate'> & {
  dataHoraEnvio?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestTomadaPreco = RestOf<ITomadaPreco>;

export type NewRestTomadaPreco = RestOf<NewTomadaPreco>;

export type PartialUpdateRestTomadaPreco = RestOf<PartialUpdateTomadaPreco>;

export type EntityResponseType = HttpResponse<ITomadaPreco>;
export type EntityArrayResponseType = HttpResponse<ITomadaPreco[]>;

@Injectable({ providedIn: 'root' })
export class TomadaPrecoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tomada-precos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tomada-precos/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(tomadaPreco: NewTomadaPreco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tomadaPreco);
    return this.http
      .post<RestTomadaPreco>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tomadaPreco: ITomadaPreco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tomadaPreco);
    return this.http
      .put<RestTomadaPreco>(`${this.resourceUrl}/${this.getTomadaPrecoIdentifier(tomadaPreco)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tomadaPreco: PartialUpdateTomadaPreco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tomadaPreco);
    return this.http
      .patch<RestTomadaPreco>(`${this.resourceUrl}/${this.getTomadaPrecoIdentifier(tomadaPreco)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTomadaPreco>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTomadaPreco[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTomadaPreco[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ITomadaPreco[]>()], asapScheduler)),
    );
  }

  getTomadaPrecoIdentifier(tomadaPreco: Pick<ITomadaPreco, 'id'>): number {
    return tomadaPreco.id;
  }

  compareTomadaPreco(o1: Pick<ITomadaPreco, 'id'> | null, o2: Pick<ITomadaPreco, 'id'> | null): boolean {
    return o1 && o2 ? this.getTomadaPrecoIdentifier(o1) === this.getTomadaPrecoIdentifier(o2) : o1 === o2;
  }

  addTomadaPrecoToCollectionIfMissing<Type extends Pick<ITomadaPreco, 'id'>>(
    tomadaPrecoCollection: Type[],
    ...tomadaPrecosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tomadaPrecos: Type[] = tomadaPrecosToCheck.filter(isPresent);
    if (tomadaPrecos.length > 0) {
      const tomadaPrecoCollectionIdentifiers = tomadaPrecoCollection.map(
        tomadaPrecoItem => this.getTomadaPrecoIdentifier(tomadaPrecoItem)!,
      );
      const tomadaPrecosToAdd = tomadaPrecos.filter(tomadaPrecoItem => {
        const tomadaPrecoIdentifier = this.getTomadaPrecoIdentifier(tomadaPrecoItem);
        if (tomadaPrecoCollectionIdentifiers.includes(tomadaPrecoIdentifier)) {
          return false;
        }
        tomadaPrecoCollectionIdentifiers.push(tomadaPrecoIdentifier);
        return true;
      });
      return [...tomadaPrecosToAdd, ...tomadaPrecoCollection];
    }
    return tomadaPrecoCollection;
  }

  protected convertDateFromClient<T extends ITomadaPreco | NewTomadaPreco | PartialUpdateTomadaPreco>(tomadaPreco: T): RestOf<T> {
    return {
      ...tomadaPreco,
      dataHoraEnvio: tomadaPreco.dataHoraEnvio?.toJSON() ?? null,
      createdDate: tomadaPreco.createdDate?.toJSON() ?? null,
      lastModifiedDate: tomadaPreco.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTomadaPreco: RestTomadaPreco): ITomadaPreco {
    return {
      ...restTomadaPreco,
      dataHoraEnvio: restTomadaPreco.dataHoraEnvio ? dayjs(restTomadaPreco.dataHoraEnvio) : undefined,
      createdDate: restTomadaPreco.createdDate ? dayjs(restTomadaPreco.createdDate) : undefined,
      lastModifiedDate: restTomadaPreco.lastModifiedDate ? dayjs(restTomadaPreco.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTomadaPreco>): HttpResponse<ITomadaPreco> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTomadaPreco[]>): HttpResponse<ITomadaPreco[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
