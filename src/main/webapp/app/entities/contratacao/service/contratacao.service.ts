import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IContratacao, NewContratacao } from '../contratacao.model';

export type PartialUpdateContratacao = Partial<IContratacao> & Pick<IContratacao, 'id'>;

type RestOf<T extends IContratacao | NewContratacao> = Omit<T, 'dataValidade' | 'createdDate' | 'lastModifiedDate'> & {
  dataValidade?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestContratacao = RestOf<IContratacao>;

export type NewRestContratacao = RestOf<NewContratacao>;

export type PartialUpdateRestContratacao = RestOf<PartialUpdateContratacao>;

export type EntityResponseType = HttpResponse<IContratacao>;
export type EntityArrayResponseType = HttpResponse<IContratacao[]>;

@Injectable({ providedIn: 'root' })
export class ContratacaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contratacaos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/contratacaos/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(contratacao: NewContratacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contratacao);
    return this.http
      .post<RestContratacao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contratacao: IContratacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contratacao);
    return this.http
      .put<RestContratacao>(`${this.resourceUrl}/${this.getContratacaoIdentifier(contratacao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contratacao: PartialUpdateContratacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contratacao);
    return this.http
      .patch<RestContratacao>(`${this.resourceUrl}/${this.getContratacaoIdentifier(contratacao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContratacao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContratacao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestContratacao[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IContratacao[]>()], asapScheduler)),
    );
  }

  getContratacaoIdentifier(contratacao: Pick<IContratacao, 'id'>): number {
    return contratacao.id;
  }

  compareContratacao(o1: Pick<IContratacao, 'id'> | null, o2: Pick<IContratacao, 'id'> | null): boolean {
    return o1 && o2 ? this.getContratacaoIdentifier(o1) === this.getContratacaoIdentifier(o2) : o1 === o2;
  }

  addContratacaoToCollectionIfMissing<Type extends Pick<IContratacao, 'id'>>(
    contratacaoCollection: Type[],
    ...contratacaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contratacaos: Type[] = contratacaosToCheck.filter(isPresent);
    if (contratacaos.length > 0) {
      const contratacaoCollectionIdentifiers = contratacaoCollection.map(
        contratacaoItem => this.getContratacaoIdentifier(contratacaoItem)!,
      );
      const contratacaosToAdd = contratacaos.filter(contratacaoItem => {
        const contratacaoIdentifier = this.getContratacaoIdentifier(contratacaoItem);
        if (contratacaoCollectionIdentifiers.includes(contratacaoIdentifier)) {
          return false;
        }
        contratacaoCollectionIdentifiers.push(contratacaoIdentifier);
        return true;
      });
      return [...contratacaosToAdd, ...contratacaoCollection];
    }
    return contratacaoCollection;
  }

  protected convertDateFromClient<T extends IContratacao | NewContratacao | PartialUpdateContratacao>(contratacao: T): RestOf<T> {
    return {
      ...contratacao,
      dataValidade: contratacao.dataValidade?.format(DATE_FORMAT) ?? null,
      createdDate: contratacao.createdDate?.toJSON() ?? null,
      lastModifiedDate: contratacao.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restContratacao: RestContratacao): IContratacao {
    return {
      ...restContratacao,
      dataValidade: restContratacao.dataValidade ? dayjs(restContratacao.dataValidade) : undefined,
      createdDate: restContratacao.createdDate ? dayjs(restContratacao.createdDate) : undefined,
      lastModifiedDate: restContratacao.lastModifiedDate ? dayjs(restContratacao.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContratacao>): HttpResponse<IContratacao> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContratacao[]>): HttpResponse<IContratacao[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
