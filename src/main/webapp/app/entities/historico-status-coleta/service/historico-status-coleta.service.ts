import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IHistoricoStatusColeta, NewHistoricoStatusColeta } from '../historico-status-coleta.model';

export type PartialUpdateHistoricoStatusColeta = Partial<IHistoricoStatusColeta> & Pick<IHistoricoStatusColeta, 'id'>;

type RestOf<T extends IHistoricoStatusColeta | NewHistoricoStatusColeta> = Omit<T, 'dataCriacao' | 'createdDate' | 'lastModifiedDate'> & {
  dataCriacao?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestHistoricoStatusColeta = RestOf<IHistoricoStatusColeta>;

export type NewRestHistoricoStatusColeta = RestOf<NewHistoricoStatusColeta>;

export type PartialUpdateRestHistoricoStatusColeta = RestOf<PartialUpdateHistoricoStatusColeta>;

export type EntityResponseType = HttpResponse<IHistoricoStatusColeta>;
export type EntityArrayResponseType = HttpResponse<IHistoricoStatusColeta[]>;

@Injectable({ providedIn: 'root' })
export class HistoricoStatusColetaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/historico-status-coletas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/historico-status-coletas/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(historicoStatusColeta: NewHistoricoStatusColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historicoStatusColeta);
    return this.http
      .post<RestHistoricoStatusColeta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(historicoStatusColeta: IHistoricoStatusColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historicoStatusColeta);
    return this.http
      .put<RestHistoricoStatusColeta>(`${this.resourceUrl}/${this.getHistoricoStatusColetaIdentifier(historicoStatusColeta)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(historicoStatusColeta: PartialUpdateHistoricoStatusColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historicoStatusColeta);
    return this.http
      .patch<RestHistoricoStatusColeta>(`${this.resourceUrl}/${this.getHistoricoStatusColetaIdentifier(historicoStatusColeta)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHistoricoStatusColeta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHistoricoStatusColeta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestHistoricoStatusColeta[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IHistoricoStatusColeta[]>()], asapScheduler)),
    );
  }

  getHistoricoStatusColetaIdentifier(historicoStatusColeta: Pick<IHistoricoStatusColeta, 'id'>): number {
    return historicoStatusColeta.id;
  }

  compareHistoricoStatusColeta(o1: Pick<IHistoricoStatusColeta, 'id'> | null, o2: Pick<IHistoricoStatusColeta, 'id'> | null): boolean {
    return o1 && o2 ? this.getHistoricoStatusColetaIdentifier(o1) === this.getHistoricoStatusColetaIdentifier(o2) : o1 === o2;
  }

  addHistoricoStatusColetaToCollectionIfMissing<Type extends Pick<IHistoricoStatusColeta, 'id'>>(
    historicoStatusColetaCollection: Type[],
    ...historicoStatusColetasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const historicoStatusColetas: Type[] = historicoStatusColetasToCheck.filter(isPresent);
    if (historicoStatusColetas.length > 0) {
      const historicoStatusColetaCollectionIdentifiers = historicoStatusColetaCollection.map(
        historicoStatusColetaItem => this.getHistoricoStatusColetaIdentifier(historicoStatusColetaItem)!,
      );
      const historicoStatusColetasToAdd = historicoStatusColetas.filter(historicoStatusColetaItem => {
        const historicoStatusColetaIdentifier = this.getHistoricoStatusColetaIdentifier(historicoStatusColetaItem);
        if (historicoStatusColetaCollectionIdentifiers.includes(historicoStatusColetaIdentifier)) {
          return false;
        }
        historicoStatusColetaCollectionIdentifiers.push(historicoStatusColetaIdentifier);
        return true;
      });
      return [...historicoStatusColetasToAdd, ...historicoStatusColetaCollection];
    }
    return historicoStatusColetaCollection;
  }

  protected convertDateFromClient<T extends IHistoricoStatusColeta | NewHistoricoStatusColeta | PartialUpdateHistoricoStatusColeta>(
    historicoStatusColeta: T,
  ): RestOf<T> {
    return {
      ...historicoStatusColeta,
      dataCriacao: historicoStatusColeta.dataCriacao?.toJSON() ?? null,
      createdDate: historicoStatusColeta.createdDate?.toJSON() ?? null,
      lastModifiedDate: historicoStatusColeta.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHistoricoStatusColeta: RestHistoricoStatusColeta): IHistoricoStatusColeta {
    return {
      ...restHistoricoStatusColeta,
      dataCriacao: restHistoricoStatusColeta.dataCriacao ? dayjs(restHistoricoStatusColeta.dataCriacao) : undefined,
      createdDate: restHistoricoStatusColeta.createdDate ? dayjs(restHistoricoStatusColeta.createdDate) : undefined,
      lastModifiedDate: restHistoricoStatusColeta.lastModifiedDate ? dayjs(restHistoricoStatusColeta.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHistoricoStatusColeta>): HttpResponse<IHistoricoStatusColeta> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHistoricoStatusColeta[]>): HttpResponse<IHistoricoStatusColeta[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
