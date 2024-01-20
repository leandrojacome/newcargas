import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFormaCobranca, NewFormaCobranca } from '../forma-cobranca.model';

export type PartialUpdateFormaCobranca = Partial<IFormaCobranca> & Pick<IFormaCobranca, 'id'>;

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
    return this.http.post<IFormaCobranca>(this.resourceUrl, formaCobranca, { observe: 'response' });
  }

  update(formaCobranca: IFormaCobranca): Observable<EntityResponseType> {
    return this.http.put<IFormaCobranca>(`${this.resourceUrl}/${this.getFormaCobrancaIdentifier(formaCobranca)}`, formaCobranca, {
      observe: 'response',
    });
  }

  partialUpdate(formaCobranca: PartialUpdateFormaCobranca): Observable<EntityResponseType> {
    return this.http.patch<IFormaCobranca>(`${this.resourceUrl}/${this.getFormaCobrancaIdentifier(formaCobranca)}`, formaCobranca, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFormaCobranca>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFormaCobranca[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFormaCobranca[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFormaCobranca[]>()], asapScheduler)));
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
}
