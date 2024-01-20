import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IStatusColeta, NewStatusColeta } from '../status-coleta.model';

export type PartialUpdateStatusColeta = Partial<IStatusColeta> & Pick<IStatusColeta, 'id'>;

type RestOf<T extends IStatusColeta | NewStatusColeta> = Omit<T, 'dataCadastro' | 'dataAtualizacao' | 'dataRemocao'> & {
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
  dataRemocao?: string | null;
};

export type RestStatusColeta = RestOf<IStatusColeta>;

export type NewRestStatusColeta = RestOf<NewStatusColeta>;

export type PartialUpdateRestStatusColeta = RestOf<PartialUpdateStatusColeta>;

export type EntityResponseType = HttpResponse<IStatusColeta>;
export type EntityArrayResponseType = HttpResponse<IStatusColeta[]>;

@Injectable({ providedIn: 'root' })
export class StatusColetaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/status-coletas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/status-coletas/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(statusColeta: NewStatusColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(statusColeta);
    return this.http
      .post<RestStatusColeta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(statusColeta: IStatusColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(statusColeta);
    return this.http
      .put<RestStatusColeta>(`${this.resourceUrl}/${this.getStatusColetaIdentifier(statusColeta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(statusColeta: PartialUpdateStatusColeta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(statusColeta);
    return this.http
      .patch<RestStatusColeta>(`${this.resourceUrl}/${this.getStatusColetaIdentifier(statusColeta)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestStatusColeta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestStatusColeta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestStatusColeta[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IStatusColeta[]>()], asapScheduler)),
    );
  }

  getStatusColetaIdentifier(statusColeta: Pick<IStatusColeta, 'id'>): number {
    return statusColeta.id;
  }

  compareStatusColeta(o1: Pick<IStatusColeta, 'id'> | null, o2: Pick<IStatusColeta, 'id'> | null): boolean {
    return o1 && o2 ? this.getStatusColetaIdentifier(o1) === this.getStatusColetaIdentifier(o2) : o1 === o2;
  }

  addStatusColetaToCollectionIfMissing<Type extends Pick<IStatusColeta, 'id'>>(
    statusColetaCollection: Type[],
    ...statusColetasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const statusColetas: Type[] = statusColetasToCheck.filter(isPresent);
    if (statusColetas.length > 0) {
      const statusColetaCollectionIdentifiers = statusColetaCollection.map(
        statusColetaItem => this.getStatusColetaIdentifier(statusColetaItem)!,
      );
      const statusColetasToAdd = statusColetas.filter(statusColetaItem => {
        const statusColetaIdentifier = this.getStatusColetaIdentifier(statusColetaItem);
        if (statusColetaCollectionIdentifiers.includes(statusColetaIdentifier)) {
          return false;
        }
        statusColetaCollectionIdentifiers.push(statusColetaIdentifier);
        return true;
      });
      return [...statusColetasToAdd, ...statusColetaCollection];
    }
    return statusColetaCollection;
  }

  protected convertDateFromClient<T extends IStatusColeta | NewStatusColeta | PartialUpdateStatusColeta>(statusColeta: T): RestOf<T> {
    return {
      ...statusColeta,
      dataCadastro: statusColeta.dataCadastro?.toJSON() ?? null,
      dataAtualizacao: statusColeta.dataAtualizacao?.toJSON() ?? null,
      dataRemocao: statusColeta.dataRemocao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restStatusColeta: RestStatusColeta): IStatusColeta {
    return {
      ...restStatusColeta,
      dataCadastro: restStatusColeta.dataCadastro ? dayjs(restStatusColeta.dataCadastro) : undefined,
      dataAtualizacao: restStatusColeta.dataAtualizacao ? dayjs(restStatusColeta.dataAtualizacao) : undefined,
      dataRemocao: restStatusColeta.dataRemocao ? dayjs(restStatusColeta.dataRemocao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestStatusColeta>): HttpResponse<IStatusColeta> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestStatusColeta[]>): HttpResponse<IStatusColeta[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
