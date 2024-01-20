import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { INotificacao, NewNotificacao } from '../notificacao.model';

export type PartialUpdateNotificacao = Partial<INotificacao> & Pick<INotificacao, 'id'>;

type RestOf<T extends INotificacao | NewNotificacao> = Omit<
  T,
  'dataHoraEnvio' | 'dataHoraLeitura' | 'dataCadastro' | 'dataAtualizacao' | 'dataLeitura' | 'dataRemocao'
> & {
  dataHoraEnvio?: string | null;
  dataHoraLeitura?: string | null;
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
  dataLeitura?: string | null;
  dataRemocao?: string | null;
};

export type RestNotificacao = RestOf<INotificacao>;

export type NewRestNotificacao = RestOf<NewNotificacao>;

export type PartialUpdateRestNotificacao = RestOf<PartialUpdateNotificacao>;

export type EntityResponseType = HttpResponse<INotificacao>;
export type EntityArrayResponseType = HttpResponse<INotificacao[]>;

@Injectable({ providedIn: 'root' })
export class NotificacaoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notificacaos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/notificacaos/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(notificacao: NewNotificacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificacao);
    return this.http
      .post<RestNotificacao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(notificacao: INotificacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificacao);
    return this.http
      .put<RestNotificacao>(`${this.resourceUrl}/${this.getNotificacaoIdentifier(notificacao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(notificacao: PartialUpdateNotificacao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificacao);
    return this.http
      .patch<RestNotificacao>(`${this.resourceUrl}/${this.getNotificacaoIdentifier(notificacao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNotificacao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotificacao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestNotificacao[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<INotificacao[]>()], asapScheduler)),
    );
  }

  getNotificacaoIdentifier(notificacao: Pick<INotificacao, 'id'>): number {
    return notificacao.id;
  }

  compareNotificacao(o1: Pick<INotificacao, 'id'> | null, o2: Pick<INotificacao, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotificacaoIdentifier(o1) === this.getNotificacaoIdentifier(o2) : o1 === o2;
  }

  addNotificacaoToCollectionIfMissing<Type extends Pick<INotificacao, 'id'>>(
    notificacaoCollection: Type[],
    ...notificacaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notificacaos: Type[] = notificacaosToCheck.filter(isPresent);
    if (notificacaos.length > 0) {
      const notificacaoCollectionIdentifiers = notificacaoCollection.map(
        notificacaoItem => this.getNotificacaoIdentifier(notificacaoItem)!,
      );
      const notificacaosToAdd = notificacaos.filter(notificacaoItem => {
        const notificacaoIdentifier = this.getNotificacaoIdentifier(notificacaoItem);
        if (notificacaoCollectionIdentifiers.includes(notificacaoIdentifier)) {
          return false;
        }
        notificacaoCollectionIdentifiers.push(notificacaoIdentifier);
        return true;
      });
      return [...notificacaosToAdd, ...notificacaoCollection];
    }
    return notificacaoCollection;
  }

  protected convertDateFromClient<T extends INotificacao | NewNotificacao | PartialUpdateNotificacao>(notificacao: T): RestOf<T> {
    return {
      ...notificacao,
      dataHoraEnvio: notificacao.dataHoraEnvio?.toJSON() ?? null,
      dataHoraLeitura: notificacao.dataHoraLeitura?.toJSON() ?? null,
      dataCadastro: notificacao.dataCadastro?.toJSON() ?? null,
      dataAtualizacao: notificacao.dataAtualizacao?.toJSON() ?? null,
      dataLeitura: notificacao.dataLeitura?.toJSON() ?? null,
      dataRemocao: notificacao.dataRemocao?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restNotificacao: RestNotificacao): INotificacao {
    return {
      ...restNotificacao,
      dataHoraEnvio: restNotificacao.dataHoraEnvio ? dayjs(restNotificacao.dataHoraEnvio) : undefined,
      dataHoraLeitura: restNotificacao.dataHoraLeitura ? dayjs(restNotificacao.dataHoraLeitura) : undefined,
      dataCadastro: restNotificacao.dataCadastro ? dayjs(restNotificacao.dataCadastro) : undefined,
      dataAtualizacao: restNotificacao.dataAtualizacao ? dayjs(restNotificacao.dataAtualizacao) : undefined,
      dataLeitura: restNotificacao.dataLeitura ? dayjs(restNotificacao.dataLeitura) : undefined,
      dataRemocao: restNotificacao.dataRemocao ? dayjs(restNotificacao.dataRemocao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNotificacao>): HttpResponse<INotificacao> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNotificacao[]>): HttpResponse<INotificacao[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
