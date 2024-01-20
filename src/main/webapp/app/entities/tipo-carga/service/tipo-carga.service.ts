import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITipoCarga, NewTipoCarga } from '../tipo-carga.model';

export type PartialUpdateTipoCarga = Partial<ITipoCarga> & Pick<ITipoCarga, 'id'>;

export type EntityResponseType = HttpResponse<ITipoCarga>;
export type EntityArrayResponseType = HttpResponse<ITipoCarga[]>;

@Injectable({ providedIn: 'root' })
export class TipoCargaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-cargas');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tipo-cargas/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(tipoCarga: NewTipoCarga): Observable<EntityResponseType> {
    return this.http.post<ITipoCarga>(this.resourceUrl, tipoCarga, { observe: 'response' });
  }

  update(tipoCarga: ITipoCarga): Observable<EntityResponseType> {
    return this.http.put<ITipoCarga>(`${this.resourceUrl}/${this.getTipoCargaIdentifier(tipoCarga)}`, tipoCarga, { observe: 'response' });
  }

  partialUpdate(tipoCarga: PartialUpdateTipoCarga): Observable<EntityResponseType> {
    return this.http.patch<ITipoCarga>(`${this.resourceUrl}/${this.getTipoCargaIdentifier(tipoCarga)}`, tipoCarga, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoCarga>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoCarga[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITipoCarga[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ITipoCarga[]>()], asapScheduler)));
  }

  getTipoCargaIdentifier(tipoCarga: Pick<ITipoCarga, 'id'>): number {
    return tipoCarga.id;
  }

  compareTipoCarga(o1: Pick<ITipoCarga, 'id'> | null, o2: Pick<ITipoCarga, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoCargaIdentifier(o1) === this.getTipoCargaIdentifier(o2) : o1 === o2;
  }

  addTipoCargaToCollectionIfMissing<Type extends Pick<ITipoCarga, 'id'>>(
    tipoCargaCollection: Type[],
    ...tipoCargasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoCargas: Type[] = tipoCargasToCheck.filter(isPresent);
    if (tipoCargas.length > 0) {
      const tipoCargaCollectionIdentifiers = tipoCargaCollection.map(tipoCargaItem => this.getTipoCargaIdentifier(tipoCargaItem)!);
      const tipoCargasToAdd = tipoCargas.filter(tipoCargaItem => {
        const tipoCargaIdentifier = this.getTipoCargaIdentifier(tipoCargaItem);
        if (tipoCargaCollectionIdentifiers.includes(tipoCargaIdentifier)) {
          return false;
        }
        tipoCargaCollectionIdentifiers.push(tipoCargaIdentifier);
        return true;
      });
      return [...tipoCargasToAdd, ...tipoCargaCollection];
    }
    return tipoCargaCollection;
  }
}
