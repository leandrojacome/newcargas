import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEndereco, NewEndereco } from '../endereco.model';

export type PartialUpdateEndereco = Partial<IEndereco> & Pick<IEndereco, 'id'>;

type RestOf<T extends IEndereco | NewEndereco> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestEndereco = RestOf<IEndereco>;

export type NewRestEndereco = RestOf<NewEndereco>;

export type PartialUpdateRestEndereco = RestOf<PartialUpdateEndereco>;

export type EntityResponseType = HttpResponse<IEndereco>;
export type EntityArrayResponseType = HttpResponse<IEndereco[]>;

@Injectable({ providedIn: 'root' })
export class EnderecoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/enderecos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/enderecos/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(endereco: NewEndereco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(endereco);
    return this.http
      .post<RestEndereco>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(endereco: IEndereco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(endereco);
    return this.http
      .put<RestEndereco>(`${this.resourceUrl}/${this.getEnderecoIdentifier(endereco)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(endereco: PartialUpdateEndereco): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(endereco);
    return this.http
      .patch<RestEndereco>(`${this.resourceUrl}/${this.getEnderecoIdentifier(endereco)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEndereco>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEndereco[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestEndereco[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IEndereco[]>()], asapScheduler)),
    );
  }

  getEnderecoIdentifier(endereco: Pick<IEndereco, 'id'>): number {
    return endereco.id;
  }

  compareEndereco(o1: Pick<IEndereco, 'id'> | null, o2: Pick<IEndereco, 'id'> | null): boolean {
    return o1 && o2 ? this.getEnderecoIdentifier(o1) === this.getEnderecoIdentifier(o2) : o1 === o2;
  }

  addEnderecoToCollectionIfMissing<Type extends Pick<IEndereco, 'id'>>(
    enderecoCollection: Type[],
    ...enderecosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const enderecos: Type[] = enderecosToCheck.filter(isPresent);
    if (enderecos.length > 0) {
      const enderecoCollectionIdentifiers = enderecoCollection.map(enderecoItem => this.getEnderecoIdentifier(enderecoItem)!);
      const enderecosToAdd = enderecos.filter(enderecoItem => {
        const enderecoIdentifier = this.getEnderecoIdentifier(enderecoItem);
        if (enderecoCollectionIdentifiers.includes(enderecoIdentifier)) {
          return false;
        }
        enderecoCollectionIdentifiers.push(enderecoIdentifier);
        return true;
      });
      return [...enderecosToAdd, ...enderecoCollection];
    }
    return enderecoCollection;
  }

  protected convertDateFromClient<T extends IEndereco | NewEndereco | PartialUpdateEndereco>(endereco: T): RestOf<T> {
    return {
      ...endereco,
      createdDate: endereco.createdDate?.toJSON() ?? null,
      lastModifiedDate: endereco.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEndereco: RestEndereco): IEndereco {
    return {
      ...restEndereco,
      createdDate: restEndereco.createdDate ? dayjs(restEndereco.createdDate) : undefined,
      lastModifiedDate: restEndereco.lastModifiedDate ? dayjs(restEndereco.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEndereco>): HttpResponse<IEndereco> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEndereco[]>): HttpResponse<IEndereco[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
