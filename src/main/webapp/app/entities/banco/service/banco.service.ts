import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IBanco, NewBanco } from '../banco.model';

export type PartialUpdateBanco = Partial<IBanco> & Pick<IBanco, 'id'>;

type RestOf<T extends IBanco | NewBanco> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBanco = RestOf<IBanco>;

export type NewRestBanco = RestOf<NewBanco>;

export type PartialUpdateRestBanco = RestOf<PartialUpdateBanco>;

export type EntityResponseType = HttpResponse<IBanco>;
export type EntityArrayResponseType = HttpResponse<IBanco[]>;

@Injectable({ providedIn: 'root' })
export class BancoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bancos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/bancos/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(banco: NewBanco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(banco);
    return this.http.post<RestBanco>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(banco: IBanco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(banco);
    return this.http
      .put<RestBanco>(`${this.resourceUrl}/${this.getBancoIdentifier(banco)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(banco: PartialUpdateBanco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(banco);
    return this.http
      .patch<RestBanco>(`${this.resourceUrl}/${this.getBancoIdentifier(banco)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBanco>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBanco[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestBanco[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IBanco[]>()], asapScheduler)),
    );
  }

  getBancoIdentifier(banco: Pick<IBanco, 'id'>): number {
    return banco.id;
  }

  compareBanco(o1: Pick<IBanco, 'id'> | null, o2: Pick<IBanco, 'id'> | null): boolean {
    return o1 && o2 ? this.getBancoIdentifier(o1) === this.getBancoIdentifier(o2) : o1 === o2;
  }

  addBancoToCollectionIfMissing<Type extends Pick<IBanco, 'id'>>(
    bancoCollection: Type[],
    ...bancosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bancos: Type[] = bancosToCheck.filter(isPresent);
    if (bancos.length > 0) {
      const bancoCollectionIdentifiers = bancoCollection.map(bancoItem => this.getBancoIdentifier(bancoItem)!);
      const bancosToAdd = bancos.filter(bancoItem => {
        const bancoIdentifier = this.getBancoIdentifier(bancoItem);
        if (bancoCollectionIdentifiers.includes(bancoIdentifier)) {
          return false;
        }
        bancoCollectionIdentifiers.push(bancoIdentifier);
        return true;
      });
      return [...bancosToAdd, ...bancoCollection];
    }
    return bancoCollection;
  }

  protected convertDateFromClient<T extends IBanco | NewBanco | PartialUpdateBanco>(banco: T): RestOf<T> {
    return {
      ...banco,
      createdDate: banco.createdDate?.toJSON() ?? null,
      lastModifiedDate: banco.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBanco: RestBanco): IBanco {
    return {
      ...restBanco,
      createdDate: restBanco.createdDate ? dayjs(restBanco.createdDate) : undefined,
      lastModifiedDate: restBanco.lastModifiedDate ? dayjs(restBanco.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBanco>): HttpResponse<IBanco> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBanco[]>): HttpResponse<IBanco[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
