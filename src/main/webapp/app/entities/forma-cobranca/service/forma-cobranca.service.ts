import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFormaCobranca, NewFormaCobranca } from '../forma-cobranca.model';

export type PartialUpdateFormaCobranca = Partial<IFormaCobranca> & Pick<IFormaCobranca, 'id'>;

type RestOf<T extends IFormaCobranca | NewFormaCobranca> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestFormaCobranca = RestOf<IFormaCobranca>;

export type NewRestFormaCobranca = RestOf<NewFormaCobranca>;

export type PartialUpdateRestFormaCobranca = RestOf<PartialUpdateFormaCobranca>;

export type EntityResponseType = HttpResponse<IFormaCobranca>;
export type EntityArrayResponseType = HttpResponse<IFormaCobranca[]>;

@Injectable({ providedIn: 'root' })
export class FormaCobrancaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/forma-cobrancas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/forma-cobrancas/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(formaCobranca: NewFormaCobranca): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formaCobranca);
    return this.http
      .post<RestFormaCobranca>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(formaCobranca: IFormaCobranca): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formaCobranca);
    return this.http
      .put<RestFormaCobranca>(`${this.resourceUrl}/${this.getFormaCobrancaIdentifier(formaCobranca)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(formaCobranca: PartialUpdateFormaCobranca): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formaCobranca);
    return this.http
      .patch<RestFormaCobranca>(`${this.resourceUrl}/${this.getFormaCobrancaIdentifier(formaCobranca)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFormaCobranca>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFormaCobranca[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestFormaCobranca[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IFormaCobranca[]>()], asapScheduler)),
    );
  }

  getFormaCobrancaIdentifier(formaCobranca: Pick<IFormaCobranca, 'id'>): number {
    return formaCobranca.id;
  }

  compareFormaCobranca(o1: Pick<IFormaCobranca, 'id'> | null, o2: Pick<IFormaCobranca, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormaCobrancaIdentifier(o1) === this.getFormaCobrancaIdentifier(o2) : o1 === o2;
  }

  addFormaCobrancaToCollectionIfMissing<Type extends Pick<IFormaCobranca, 'id'>>(
    formaCobrancaCollection: Type[],
    ...formaCobrancasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formaCobrancas: Type[] = formaCobrancasToCheck.filter(isPresent);
    if (formaCobrancas.length > 0) {
      const formaCobrancaCollectionIdentifiers = formaCobrancaCollection.map(
        formaCobrancaItem => this.getFormaCobrancaIdentifier(formaCobrancaItem)!,
      );
      const formaCobrancasToAdd = formaCobrancas.filter(formaCobrancaItem => {
        const formaCobrancaIdentifier = this.getFormaCobrancaIdentifier(formaCobrancaItem);
        if (formaCobrancaCollectionIdentifiers.includes(formaCobrancaIdentifier)) {
          return false;
        }
        formaCobrancaCollectionIdentifiers.push(formaCobrancaIdentifier);
        return true;
      });
      return [...formaCobrancasToAdd, ...formaCobrancaCollection];
    }
    return formaCobrancaCollection;
  }

  protected convertDateFromClient<T extends IFormaCobranca | NewFormaCobranca | PartialUpdateFormaCobranca>(formaCobranca: T): RestOf<T> {
    return {
      ...formaCobranca,
      createdDate: formaCobranca.createdDate?.toJSON() ?? null,
      lastModifiedDate: formaCobranca.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFormaCobranca: RestFormaCobranca): IFormaCobranca {
    return {
      ...restFormaCobranca,
      createdDate: restFormaCobranca.createdDate ? dayjs(restFormaCobranca.createdDate) : undefined,
      lastModifiedDate: restFormaCobranca.lastModifiedDate ? dayjs(restFormaCobranca.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFormaCobranca>): HttpResponse<IFormaCobranca> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFormaCobranca[]>): HttpResponse<IFormaCobranca[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
