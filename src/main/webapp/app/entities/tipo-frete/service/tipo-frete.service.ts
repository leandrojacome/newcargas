import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITipoFrete, NewTipoFrete } from '../tipo-frete.model';

export type PartialUpdateTipoFrete = Partial<ITipoFrete> & Pick<ITipoFrete, 'id'>;

export type EntityResponseType = HttpResponse<ITipoFrete>;
export type EntityArrayResponseType = HttpResponse<ITipoFrete[]>;

@Injectable({ providedIn: 'root' })
export class TipoFreteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-fretes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tipo-fretes/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(tipoFrete: NewTipoFrete): Observable<EntityResponseType> {
    return this.http.post<ITipoFrete>(this.resourceUrl, tipoFrete, { observe: 'response' });
  }

  update(tipoFrete: ITipoFrete): Observable<EntityResponseType> {
    return this.http.put<ITipoFrete>(`${this.resourceUrl}/${this.getTipoFreteIdentifier(tipoFrete)}`, tipoFrete, { observe: 'response' });
  }

  partialUpdate(tipoFrete: PartialUpdateTipoFrete): Observable<EntityResponseType> {
    return this.http.patch<ITipoFrete>(`${this.resourceUrl}/${this.getTipoFreteIdentifier(tipoFrete)}`, tipoFrete, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoFrete>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoFrete[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITipoFrete[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ITipoFrete[]>()], asapScheduler)));
  }

  getTipoFreteIdentifier(tipoFrete: Pick<ITipoFrete, 'id'>): number {
    return tipoFrete.id;
  }

  compareTipoFrete(o1: Pick<ITipoFrete, 'id'> | null, o2: Pick<ITipoFrete, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoFreteIdentifier(o1) === this.getTipoFreteIdentifier(o2) : o1 === o2;
  }

  addTipoFreteToCollectionIfMissing<Type extends Pick<ITipoFrete, 'id'>>(
    tipoFreteCollection: Type[],
    ...tipoFretesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoFretes: Type[] = tipoFretesToCheck.filter(isPresent);
    if (tipoFretes.length > 0) {
      const tipoFreteCollectionIdentifiers = tipoFreteCollection.map(tipoFreteItem => this.getTipoFreteIdentifier(tipoFreteItem)!);
      const tipoFretesToAdd = tipoFretes.filter(tipoFreteItem => {
        const tipoFreteIdentifier = this.getTipoFreteIdentifier(tipoFreteItem);
        if (tipoFreteCollectionIdentifiers.includes(tipoFreteIdentifier)) {
          return false;
        }
        tipoFreteCollectionIdentifiers.push(tipoFreteIdentifier);
        return true;
      });
      return [...tipoFretesToAdd, ...tipoFreteCollection];
    }
    return tipoFreteCollection;
  }
}
