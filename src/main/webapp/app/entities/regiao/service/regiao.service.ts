import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRegiao, NewRegiao } from '../regiao.model';

export type PartialUpdateRegiao = Partial<IRegiao> & Pick<IRegiao, 'id'>;

type RestOf<T extends IRegiao | NewRegiao> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestRegiao = RestOf<IRegiao>;

export type NewRestRegiao = RestOf<NewRegiao>;

export type PartialUpdateRestRegiao = RestOf<PartialUpdateRegiao>;

export type EntityResponseType = HttpResponse<IRegiao>;
export type EntityArrayResponseType = HttpResponse<IRegiao[]>;

@Injectable({ providedIn: 'root' })
export class RegiaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/regiaos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/regiaos/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(regiao: NewRegiao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(regiao);
    return this.http
      .post<RestRegiao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(regiao: IRegiao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(regiao);
    return this.http
      .put<RestRegiao>(`${this.resourceUrl}/${this.getRegiaoIdentifier(regiao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(regiao: PartialUpdateRegiao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(regiao);
    return this.http
      .patch<RestRegiao>(`${this.resourceUrl}/${this.getRegiaoIdentifier(regiao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRegiao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRegiao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestRegiao[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IRegiao[]>()], asapScheduler)),
    );
  }

  getRegiaoIdentifier(regiao: Pick<IRegiao, 'id'>): number {
    return regiao.id;
  }

  compareRegiao(o1: Pick<IRegiao, 'id'> | null, o2: Pick<IRegiao, 'id'> | null): boolean {
    return o1 && o2 ? this.getRegiaoIdentifier(o1) === this.getRegiaoIdentifier(o2) : o1 === o2;
  }

  addRegiaoToCollectionIfMissing<Type extends Pick<IRegiao, 'id'>>(
    regiaoCollection: Type[],
    ...regiaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const regiaos: Type[] = regiaosToCheck.filter(isPresent);
    if (regiaos.length > 0) {
      const regiaoCollectionIdentifiers = regiaoCollection.map(regiaoItem => this.getRegiaoIdentifier(regiaoItem)!);
      const regiaosToAdd = regiaos.filter(regiaoItem => {
        const regiaoIdentifier = this.getRegiaoIdentifier(regiaoItem);
        if (regiaoCollectionIdentifiers.includes(regiaoIdentifier)) {
          return false;
        }
        regiaoCollectionIdentifiers.push(regiaoIdentifier);
        return true;
      });
      return [...regiaosToAdd, ...regiaoCollection];
    }
    return regiaoCollection;
  }

  protected convertDateFromClient<T extends IRegiao | NewRegiao | PartialUpdateRegiao>(regiao: T): RestOf<T> {
    return {
      ...regiao,
      createdDate: regiao.createdDate?.toJSON() ?? null,
      lastModifiedDate: regiao.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRegiao: RestRegiao): IRegiao {
    return {
      ...restRegiao,
      createdDate: restRegiao.createdDate ? dayjs(restRegiao.createdDate) : undefined,
      lastModifiedDate: restRegiao.lastModifiedDate ? dayjs(restRegiao.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRegiao>): HttpResponse<IRegiao> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRegiao[]>): HttpResponse<IRegiao[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
