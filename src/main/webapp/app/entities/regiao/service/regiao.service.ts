import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRegiao, NewRegiao } from '../regiao.model';

export type PartialUpdateRegiao = Partial<IRegiao> & Pick<IRegiao, 'id'>;

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
    return this.http.post<IRegiao>(this.resourceUrl, regiao, { observe: 'response' });
  }

  update(regiao: IRegiao): Observable<EntityResponseType> {
    return this.http.put<IRegiao>(`${this.resourceUrl}/${this.getRegiaoIdentifier(regiao)}`, regiao, { observe: 'response' });
  }

  partialUpdate(regiao: PartialUpdateRegiao): Observable<EntityResponseType> {
    return this.http.patch<IRegiao>(`${this.resourceUrl}/${this.getRegiaoIdentifier(regiao)}`, regiao, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRegiao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRegiao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRegiao[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IRegiao[]>()], asapScheduler)));
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
}
