import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICidade, NewCidade } from '../cidade.model';

export type PartialUpdateCidade = Partial<ICidade> & Pick<ICidade, 'id'>;

type RestOf<T extends ICidade | NewCidade> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestCidade = RestOf<ICidade>;

export type NewRestCidade = RestOf<NewCidade>;

export type PartialUpdateRestCidade = RestOf<PartialUpdateCidade>;

export type EntityResponseType = HttpResponse<ICidade>;
export type EntityArrayResponseType = HttpResponse<ICidade[]>;

@Injectable({ providedIn: 'root' })
export class CidadeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cidades');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/cidades/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(cidade: NewCidade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cidade);
    return this.http
      .post<RestCidade>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cidade: ICidade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cidade);
    return this.http
      .put<RestCidade>(`${this.resourceUrl}/${this.getCidadeIdentifier(cidade)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cidade: PartialUpdateCidade): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cidade);
    return this.http
      .patch<RestCidade>(`${this.resourceUrl}/${this.getCidadeIdentifier(cidade)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCidade>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCidade[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestCidade[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ICidade[]>()], asapScheduler)),
    );
  }

  getCidadeIdentifier(cidade: Pick<ICidade, 'id'>): number {
    return cidade.id;
  }

  compareCidade(o1: Pick<ICidade, 'id'> | null, o2: Pick<ICidade, 'id'> | null): boolean {
    return o1 && o2 ? this.getCidadeIdentifier(o1) === this.getCidadeIdentifier(o2) : o1 === o2;
  }

  addCidadeToCollectionIfMissing<Type extends Pick<ICidade, 'id'>>(
    cidadeCollection: Type[],
    ...cidadesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cidades: Type[] = cidadesToCheck.filter(isPresent);
    if (cidades.length > 0) {
      const cidadeCollectionIdentifiers = cidadeCollection.map(cidadeItem => this.getCidadeIdentifier(cidadeItem)!);
      const cidadesToAdd = cidades.filter(cidadeItem => {
        const cidadeIdentifier = this.getCidadeIdentifier(cidadeItem);
        if (cidadeCollectionIdentifiers.includes(cidadeIdentifier)) {
          return false;
        }
        cidadeCollectionIdentifiers.push(cidadeIdentifier);
        return true;
      });
      return [...cidadesToAdd, ...cidadeCollection];
    }
    return cidadeCollection;
  }

  protected convertDateFromClient<T extends ICidade | NewCidade | PartialUpdateCidade>(cidade: T): RestOf<T> {
    return {
      ...cidade,
      createdDate: cidade.createdDate?.toJSON() ?? null,
      lastModifiedDate: cidade.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCidade: RestCidade): ICidade {
    return {
      ...restCidade,
      createdDate: restCidade.createdDate ? dayjs(restCidade.createdDate) : undefined,
      lastModifiedDate: restCidade.lastModifiedDate ? dayjs(restCidade.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCidade>): HttpResponse<ICidade> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCidade[]>): HttpResponse<ICidade[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
