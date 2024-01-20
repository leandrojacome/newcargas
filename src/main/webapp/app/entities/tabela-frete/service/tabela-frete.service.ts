import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITabelaFrete, NewTabelaFrete } from '../tabela-frete.model';

export type PartialUpdateTabelaFrete = Partial<ITabelaFrete> & Pick<ITabelaFrete, 'id'>;

type RestOf<T extends ITabelaFrete | NewTabelaFrete> = Omit<T, 'dataCadastro' | 'dataAtualizacao'> & {
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
};

export type RestTabelaFrete = RestOf<ITabelaFrete>;

export type NewRestTabelaFrete = RestOf<NewTabelaFrete>;

export type PartialUpdateRestTabelaFrete = RestOf<PartialUpdateTabelaFrete>;

export type EntityResponseType = HttpResponse<ITabelaFrete>;
export type EntityArrayResponseType = HttpResponse<ITabelaFrete[]>;

@Injectable({ providedIn: 'root' })
export class TabelaFreteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tabela-fretes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tabela-fretes/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(tabelaFrete: NewTabelaFrete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tabelaFrete);
    return this.http
      .post<RestTabelaFrete>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tabelaFrete: ITabelaFrete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tabelaFrete);
    return this.http
      .put<RestTabelaFrete>(`${this.resourceUrl}/${this.getTabelaFreteIdentifier(tabelaFrete)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tabelaFrete: PartialUpdateTabelaFrete): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tabelaFrete);
    return this.http
      .patch<RestTabelaFrete>(`${this.resourceUrl}/${this.getTabelaFreteIdentifier(tabelaFrete)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTabelaFrete>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTabelaFrete[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTabelaFrete[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ITabelaFrete[]>()], asapScheduler)),
    );
  }

  getTabelaFreteIdentifier(tabelaFrete: Pick<ITabelaFrete, 'id'>): number {
    return tabelaFrete.id;
  }

  compareTabelaFrete(o1: Pick<ITabelaFrete, 'id'> | null, o2: Pick<ITabelaFrete, 'id'> | null): boolean {
    return o1 && o2 ? this.getTabelaFreteIdentifier(o1) === this.getTabelaFreteIdentifier(o2) : o1 === o2;
  }

  addTabelaFreteToCollectionIfMissing<Type extends Pick<ITabelaFrete, 'id'>>(
    tabelaFreteCollection: Type[],
    ...tabelaFretesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tabelaFretes: Type[] = tabelaFretesToCheck.filter(isPresent);
    if (tabelaFretes.length > 0) {
      const tabelaFreteCollectionIdentifiers = tabelaFreteCollection.map(
        tabelaFreteItem => this.getTabelaFreteIdentifier(tabelaFreteItem)!,
      );
      const tabelaFretesToAdd = tabelaFretes.filter(tabelaFreteItem => {
        const tabelaFreteIdentifier = this.getTabelaFreteIdentifier(tabelaFreteItem);
        if (tabelaFreteCollectionIdentifiers.includes(tabelaFreteIdentifier)) {
          return false;
        }
        tabelaFreteCollectionIdentifiers.push(tabelaFreteIdentifier);
        return true;
      });
      return [...tabelaFretesToAdd, ...tabelaFreteCollection];
    }
    return tabelaFreteCollection;
  }

  protected convertDateFromClient<T extends ITabelaFrete | NewTabelaFrete | PartialUpdateTabelaFrete>(tabelaFrete: T): RestOf<T> {
    return {
      ...tabelaFrete,
      dataCadastro: tabelaFrete.dataCadastro?.toJSON() ?? null,
      dataAtualizacao: tabelaFrete.dataAtualizacao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTabelaFrete: RestTabelaFrete): ITabelaFrete {
    return {
      ...restTabelaFrete,
      dataCadastro: restTabelaFrete.dataCadastro ? dayjs(restTabelaFrete.dataCadastro) : undefined,
      dataAtualizacao: restTabelaFrete.dataAtualizacao ? dayjs(restTabelaFrete.dataAtualizacao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTabelaFrete>): HttpResponse<ITabelaFrete> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTabelaFrete[]>): HttpResponse<ITabelaFrete[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
