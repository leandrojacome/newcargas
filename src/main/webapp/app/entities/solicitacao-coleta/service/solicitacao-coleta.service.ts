import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISolicitacaoColeta, NewSolicitacaoColeta } from '../solicitacao-coleta.model';

export type PartialUpdateSolicitacaoColeta = Partial<ISolicitacaoColeta> & Pick<ISolicitacaoColeta, 'id'>;

type RestOf<T extends ISolicitacaoColeta | NewSolicitacaoColeta> = Omit<
  T,
  'dataHoraColeta' | 'dataHoraEntrega' | 'createdDate' | 'lastModifiedDate'
> & {
  dataHoraColeta?: string | null;
  dataHoraEntrega?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestSolicitacaoColeta = RestOf<ISolicitacaoColeta>;

export type NewRestSolicitacaoColeta = RestOf<NewSolicitacaoColeta>;

export type PartialUpdateRestSolicitacaoColeta = RestOf<PartialUpdateSolicitacaoColeta>;

export type EntityResponseType = HttpResponse<ISolicitacaoColeta>;
export type EntityArrayResponseType = HttpResponse<ISolicitacaoColeta[]>;

@Injectable({ providedIn: 'root' })
export class SolicitacaoColetaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/solicitacao-coletas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/solicitacao-coletas/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(solicitacaoColeta: NewSolicitacaoColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacaoColeta);
    return this.http
      .post<RestSolicitacaoColeta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(solicitacaoColeta: ISolicitacaoColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacaoColeta);
    return this.http
      .put<RestSolicitacaoColeta>(`${this.resourceUrl}/${this.getSolicitacaoColetaIdentifier(solicitacaoColeta)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(solicitacaoColeta: PartialUpdateSolicitacaoColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solicitacaoColeta);
    return this.http
      .patch<RestSolicitacaoColeta>(`${this.resourceUrl}/${this.getSolicitacaoColetaIdentifier(solicitacaoColeta)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSolicitacaoColeta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSolicitacaoColeta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestSolicitacaoColeta[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ISolicitacaoColeta[]>()], asapScheduler)),
    );
  }

  getSolicitacaoColetaIdentifier(solicitacaoColeta: Pick<ISolicitacaoColeta, 'id'>): number {
    return solicitacaoColeta.id;
  }

  compareSolicitacaoColeta(o1: Pick<ISolicitacaoColeta, 'id'> | null, o2: Pick<ISolicitacaoColeta, 'id'> | null): boolean {
    return o1 && o2 ? this.getSolicitacaoColetaIdentifier(o1) === this.getSolicitacaoColetaIdentifier(o2) : o1 === o2;
  }

  addSolicitacaoColetaToCollectionIfMissing<Type extends Pick<ISolicitacaoColeta, 'id'>>(
    solicitacaoColetaCollection: Type[],
    ...solicitacaoColetasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const solicitacaoColetas: Type[] = solicitacaoColetasToCheck.filter(isPresent);
    if (solicitacaoColetas.length > 0) {
      const solicitacaoColetaCollectionIdentifiers = solicitacaoColetaCollection.map(
        solicitacaoColetaItem => this.getSolicitacaoColetaIdentifier(solicitacaoColetaItem)!,
      );
      const solicitacaoColetasToAdd = solicitacaoColetas.filter(solicitacaoColetaItem => {
        const solicitacaoColetaIdentifier = this.getSolicitacaoColetaIdentifier(solicitacaoColetaItem);
        if (solicitacaoColetaCollectionIdentifiers.includes(solicitacaoColetaIdentifier)) {
          return false;
        }
        solicitacaoColetaCollectionIdentifiers.push(solicitacaoColetaIdentifier);
        return true;
      });
      return [...solicitacaoColetasToAdd, ...solicitacaoColetaCollection];
    }
    return solicitacaoColetaCollection;
  }

  protected convertDateFromClient<T extends ISolicitacaoColeta | NewSolicitacaoColeta | PartialUpdateSolicitacaoColeta>(
    solicitacaoColeta: T,
  ): RestOf<T> {
    return {
      ...solicitacaoColeta,
      dataHoraColeta: solicitacaoColeta.dataHoraColeta?.toJSON() ?? null,
      dataHoraEntrega: solicitacaoColeta.dataHoraEntrega?.toJSON() ?? null,
      createdDate: solicitacaoColeta.createdDate?.toJSON() ?? null,
      lastModifiedDate: solicitacaoColeta.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSolicitacaoColeta: RestSolicitacaoColeta): ISolicitacaoColeta {
    return {
      ...restSolicitacaoColeta,
      dataHoraColeta: restSolicitacaoColeta.dataHoraColeta ? dayjs(restSolicitacaoColeta.dataHoraColeta) : undefined,
      dataHoraEntrega: restSolicitacaoColeta.dataHoraEntrega ? dayjs(restSolicitacaoColeta.dataHoraEntrega) : undefined,
      createdDate: restSolicitacaoColeta.createdDate ? dayjs(restSolicitacaoColeta.createdDate) : undefined,
      lastModifiedDate: restSolicitacaoColeta.lastModifiedDate ? dayjs(restSolicitacaoColeta.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSolicitacaoColeta>): HttpResponse<ISolicitacaoColeta> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSolicitacaoColeta[]>): HttpResponse<ISolicitacaoColeta[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
