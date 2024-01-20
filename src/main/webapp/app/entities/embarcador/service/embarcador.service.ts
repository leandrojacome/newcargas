import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEmbarcador, NewEmbarcador } from '../embarcador.model';

export type PartialUpdateEmbarcador = Partial<IEmbarcador> & Pick<IEmbarcador, 'id'>;

type RestOf<T extends IEmbarcador | NewEmbarcador> = Omit<T, 'dataCadastro' | 'dataAtualizacao'> & {
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
};

export type RestEmbarcador = RestOf<IEmbarcador>;

export type NewRestEmbarcador = RestOf<NewEmbarcador>;

export type PartialUpdateRestEmbarcador = RestOf<PartialUpdateEmbarcador>;

export type EntityResponseType = HttpResponse<IEmbarcador>;
export type EntityArrayResponseType = HttpResponse<IEmbarcador[]>;

@Injectable({ providedIn: 'root' })
export class EmbarcadorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/embarcadors');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/embarcadors/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(embarcador: NewEmbarcador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(embarcador);
    return this.http
      .post<RestEmbarcador>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(embarcador: IEmbarcador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(embarcador);
    return this.http
      .put<RestEmbarcador>(`${this.resourceUrl}/${this.getEmbarcadorIdentifier(embarcador)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(embarcador: PartialUpdateEmbarcador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(embarcador);
    return this.http
      .patch<RestEmbarcador>(`${this.resourceUrl}/${this.getEmbarcadorIdentifier(embarcador)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmbarcador>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmbarcador[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestEmbarcador[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IEmbarcador[]>()], asapScheduler)),
    );
  }

  getEmbarcadorIdentifier(embarcador: Pick<IEmbarcador, 'id'>): number {
    return embarcador.id;
  }

  compareEmbarcador(o1: Pick<IEmbarcador, 'id'> | null, o2: Pick<IEmbarcador, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmbarcadorIdentifier(o1) === this.getEmbarcadorIdentifier(o2) : o1 === o2;
  }

  addEmbarcadorToCollectionIfMissing<Type extends Pick<IEmbarcador, 'id'>>(
    embarcadorCollection: Type[],
    ...embarcadorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const embarcadors: Type[] = embarcadorsToCheck.filter(isPresent);
    if (embarcadors.length > 0) {
      const embarcadorCollectionIdentifiers = embarcadorCollection.map(embarcadorItem => this.getEmbarcadorIdentifier(embarcadorItem)!);
      const embarcadorsToAdd = embarcadors.filter(embarcadorItem => {
        const embarcadorIdentifier = this.getEmbarcadorIdentifier(embarcadorItem);
        if (embarcadorCollectionIdentifiers.includes(embarcadorIdentifier)) {
          return false;
        }
        embarcadorCollectionIdentifiers.push(embarcadorIdentifier);
        return true;
      });
      return [...embarcadorsToAdd, ...embarcadorCollection];
    }
    return embarcadorCollection;
  }

  protected convertDateFromClient<T extends IEmbarcador | NewEmbarcador | PartialUpdateEmbarcador>(embarcador: T): RestOf<T> {
    return {
      ...embarcador,
      dataCadastro: embarcador.dataCadastro?.toJSON() ?? null,
      dataAtualizacao: embarcador.dataAtualizacao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmbarcador: RestEmbarcador): IEmbarcador {
    return {
      ...restEmbarcador,
      dataCadastro: restEmbarcador.dataCadastro ? dayjs(restEmbarcador.dataCadastro) : undefined,
      dataAtualizacao: restEmbarcador.dataAtualizacao ? dayjs(restEmbarcador.dataAtualizacao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmbarcador>): HttpResponse<IEmbarcador> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmbarcador[]>): HttpResponse<IEmbarcador[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
