import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRoteirizacao, NewRoteirizacao } from '../roteirizacao.model';

export type PartialUpdateRoteirizacao = Partial<IRoteirizacao> & Pick<IRoteirizacao, 'id'>;

type RestOf<T extends IRoteirizacao | NewRoteirizacao> = Omit<
  T,
  | 'dataHoraPrimeiraColeta'
  | 'dataHoraUltimaColeta'
  | 'dataHoraPrimeiraEntrega'
  | 'dataHoraUltimaEntrega'
  | 'createdDate'
  | 'lastModifiedDate'
> & {
  dataHoraPrimeiraColeta?: string | null;
  dataHoraUltimaColeta?: string | null;
  dataHoraPrimeiraEntrega?: string | null;
  dataHoraUltimaEntrega?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestRoteirizacao = RestOf<IRoteirizacao>;

export type NewRestRoteirizacao = RestOf<NewRoteirizacao>;

export type PartialUpdateRestRoteirizacao = RestOf<PartialUpdateRoteirizacao>;

export type EntityResponseType = HttpResponse<IRoteirizacao>;
export type EntityArrayResponseType = HttpResponse<IRoteirizacao[]>;

@Injectable({ providedIn: 'root' })
export class RoteirizacaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/roteirizacaos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/roteirizacaos/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(roteirizacao: NewRoteirizacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roteirizacao);
    return this.http
      .post<RestRoteirizacao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(roteirizacao: IRoteirizacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roteirizacao);
    return this.http
      .put<RestRoteirizacao>(`${this.resourceUrl}/${this.getRoteirizacaoIdentifier(roteirizacao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(roteirizacao: PartialUpdateRoteirizacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roteirizacao);
    return this.http
      .patch<RestRoteirizacao>(`${this.resourceUrl}/${this.getRoteirizacaoIdentifier(roteirizacao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRoteirizacao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRoteirizacao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestRoteirizacao[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IRoteirizacao[]>()], asapScheduler)),
    );
  }

  getRoteirizacaoIdentifier(roteirizacao: Pick<IRoteirizacao, 'id'>): number {
    return roteirizacao.id;
  }

  compareRoteirizacao(o1: Pick<IRoteirizacao, 'id'> | null, o2: Pick<IRoteirizacao, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoteirizacaoIdentifier(o1) === this.getRoteirizacaoIdentifier(o2) : o1 === o2;
  }

  addRoteirizacaoToCollectionIfMissing<Type extends Pick<IRoteirizacao, 'id'>>(
    roteirizacaoCollection: Type[],
    ...roteirizacaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roteirizacaos: Type[] = roteirizacaosToCheck.filter(isPresent);
    if (roteirizacaos.length > 0) {
      const roteirizacaoCollectionIdentifiers = roteirizacaoCollection.map(
        roteirizacaoItem => this.getRoteirizacaoIdentifier(roteirizacaoItem)!,
      );
      const roteirizacaosToAdd = roteirizacaos.filter(roteirizacaoItem => {
        const roteirizacaoIdentifier = this.getRoteirizacaoIdentifier(roteirizacaoItem);
        if (roteirizacaoCollectionIdentifiers.includes(roteirizacaoIdentifier)) {
          return false;
        }
        roteirizacaoCollectionIdentifiers.push(roteirizacaoIdentifier);
        return true;
      });
      return [...roteirizacaosToAdd, ...roteirizacaoCollection];
    }
    return roteirizacaoCollection;
  }

  protected convertDateFromClient<T extends IRoteirizacao | NewRoteirizacao | PartialUpdateRoteirizacao>(roteirizacao: T): RestOf<T> {
    return {
      ...roteirizacao,
      dataHoraPrimeiraColeta: roteirizacao.dataHoraPrimeiraColeta?.toJSON() ?? null,
      dataHoraUltimaColeta: roteirizacao.dataHoraUltimaColeta?.toJSON() ?? null,
      dataHoraPrimeiraEntrega: roteirizacao.dataHoraPrimeiraEntrega?.toJSON() ?? null,
      dataHoraUltimaEntrega: roteirizacao.dataHoraUltimaEntrega?.toJSON() ?? null,
      createdDate: roteirizacao.createdDate?.toJSON() ?? null,
      lastModifiedDate: roteirizacao.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRoteirizacao: RestRoteirizacao): IRoteirizacao {
    return {
      ...restRoteirizacao,
      dataHoraPrimeiraColeta: restRoteirizacao.dataHoraPrimeiraColeta ? dayjs(restRoteirizacao.dataHoraPrimeiraColeta) : undefined,
      dataHoraUltimaColeta: restRoteirizacao.dataHoraUltimaColeta ? dayjs(restRoteirizacao.dataHoraUltimaColeta) : undefined,
      dataHoraPrimeiraEntrega: restRoteirizacao.dataHoraPrimeiraEntrega ? dayjs(restRoteirizacao.dataHoraPrimeiraEntrega) : undefined,
      dataHoraUltimaEntrega: restRoteirizacao.dataHoraUltimaEntrega ? dayjs(restRoteirizacao.dataHoraUltimaEntrega) : undefined,
      createdDate: restRoteirizacao.createdDate ? dayjs(restRoteirizacao.createdDate) : undefined,
      lastModifiedDate: restRoteirizacao.lastModifiedDate ? dayjs(restRoteirizacao.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRoteirizacao>): HttpResponse<IRoteirizacao> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRoteirizacao[]>): HttpResponse<IRoteirizacao[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
