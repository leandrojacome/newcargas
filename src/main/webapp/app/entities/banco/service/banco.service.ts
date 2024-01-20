import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IBanco, NewBanco } from '../banco.model';

export type PartialUpdateBanco = Partial<IBanco> & Pick<IBanco, 'id'>;

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
    return this.http.post<IBanco>(this.resourceUrl, banco, { observe: 'response' });
  }

  update(banco: IBanco): Observable<EntityResponseType> {
    return this.http.put<IBanco>(`${this.resourceUrl}/${this.getBancoIdentifier(banco)}`, banco, { observe: 'response' });
  }

  partialUpdate(banco: PartialUpdateBanco): Observable<EntityResponseType> {
    return this.http.patch<IBanco>(`${this.resourceUrl}/${this.getBancoIdentifier(banco)}`, banco, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBanco>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBanco[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBanco[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IBanco[]>()], asapScheduler)));
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
}
