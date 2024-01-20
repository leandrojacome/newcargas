import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITransportadora, NewTransportadora } from '../transportadora.model';

export type PartialUpdateTransportadora = Partial<ITransportadora> & Pick<ITransportadora, 'id'>;

type RestOf<T extends ITransportadora | NewTransportadora> = Omit<T, 'dataCadastro' | 'dataAtualizacao'> & {
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
};

export type RestTransportadora = RestOf<ITransportadora>;

export type NewRestTransportadora = RestOf<NewTransportadora>;

export type PartialUpdateRestTransportadora = RestOf<PartialUpdateTransportadora>;

export type EntityResponseType = HttpResponse<ITransportadora>;
export type EntityArrayResponseType = HttpResponse<ITransportadora[]>;

@Injectable({ providedIn: 'root' })
export class TransportadoraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transportadoras');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/transportadoras/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(transportadora: NewTransportadora): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transportadora);
    return this.http
      .post<RestTransportadora>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transportadora: ITransportadora): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transportadora);
    return this.http
      .put<RestTransportadora>(`${this.resourceUrl}/${this.getTransportadoraIdentifier(transportadora)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transportadora: PartialUpdateTransportadora): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transportadora);
    return this.http
      .patch<RestTransportadora>(`${this.resourceUrl}/${this.getTransportadoraIdentifier(transportadora)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTransportadora>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransportadora[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTransportadora[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ITransportadora[]>()], asapScheduler)),
    );
  }

  getTransportadoraIdentifier(transportadora: Pick<ITransportadora, 'id'>): number {
    return transportadora.id;
  }

  compareTransportadora(o1: Pick<ITransportadora, 'id'> | null, o2: Pick<ITransportadora, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransportadoraIdentifier(o1) === this.getTransportadoraIdentifier(o2) : o1 === o2;
  }

  addTransportadoraToCollectionIfMissing<Type extends Pick<ITransportadora, 'id'>>(
    transportadoraCollection: Type[],
    ...transportadorasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transportadoras: Type[] = transportadorasToCheck.filter(isPresent);
    if (transportadoras.length > 0) {
      const transportadoraCollectionIdentifiers = transportadoraCollection.map(
        transportadoraItem => this.getTransportadoraIdentifier(transportadoraItem)!,
      );
      const transportadorasToAdd = transportadoras.filter(transportadoraItem => {
        const transportadoraIdentifier = this.getTransportadoraIdentifier(transportadoraItem);
        if (transportadoraCollectionIdentifiers.includes(transportadoraIdentifier)) {
          return false;
        }
        transportadoraCollectionIdentifiers.push(transportadoraIdentifier);
        return true;
      });
      return [...transportadorasToAdd, ...transportadoraCollection];
    }
    return transportadoraCollection;
  }

  protected convertDateFromClient<T extends ITransportadora | NewTransportadora | PartialUpdateTransportadora>(
    transportadora: T,
  ): RestOf<T> {
    return {
      ...transportadora,
      dataCadastro: transportadora.dataCadastro?.toJSON() ?? null,
      dataAtualizacao: transportadora.dataAtualizacao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTransportadora: RestTransportadora): ITransportadora {
    return {
      ...restTransportadora,
      dataCadastro: restTransportadora.dataCadastro ? dayjs(restTransportadora.dataCadastro) : undefined,
      dataAtualizacao: restTransportadora.dataAtualizacao ? dayjs(restTransportadora.dataAtualizacao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTransportadora>): HttpResponse<ITransportadora> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTransportadora[]>): HttpResponse<ITransportadora[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
