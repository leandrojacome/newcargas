import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEstado, NewEstado } from '../estado.model';

export type PartialUpdateEstado = Partial<IEstado> & Pick<IEstado, 'id'>;

export type EntityResponseType = HttpResponse<IEstado>;
export type EntityArrayResponseType = HttpResponse<IEstado[]>;

@Injectable({ providedIn: 'root' })
export class EstadoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/estados');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/estados/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(estado: NewEstado): Observable<EntityResponseType> {
    return this.http.post<IEstado>(this.resourceUrl, estado, { observe: 'response' });
  }

  update(estado: IEstado): Observable<EntityResponseType> {
    return this.http.put<IEstado>(`${this.resourceUrl}/${this.getEstadoIdentifier(estado)}`, estado, { observe: 'response' });
  }

  partialUpdate(estado: PartialUpdateEstado): Observable<EntityResponseType> {
    return this.http.patch<IEstado>(`${this.resourceUrl}/${this.getEstadoIdentifier(estado)}`, estado, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEstado>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEstado[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEstado[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IEstado[]>()], asapScheduler)));
  }

  getEstadoIdentifier(estado: Pick<IEstado, 'id'>): number {
    return estado.id;
  }

  compareEstado(o1: Pick<IEstado, 'id'> | null, o2: Pick<IEstado, 'id'> | null): boolean {
    return o1 && o2 ? this.getEstadoIdentifier(o1) === this.getEstadoIdentifier(o2) : o1 === o2;
  }

  addEstadoToCollectionIfMissing<Type extends Pick<IEstado, 'id'>>(
    estadoCollection: Type[],
    ...estadosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const estados: Type[] = estadosToCheck.filter(isPresent);
    if (estados.length > 0) {
      const estadoCollectionIdentifiers = estadoCollection.map(estadoItem => this.getEstadoIdentifier(estadoItem)!);
      const estadosToAdd = estados.filter(estadoItem => {
        const estadoIdentifier = this.getEstadoIdentifier(estadoItem);
        if (estadoCollectionIdentifiers.includes(estadoIdentifier)) {
          return false;
        }
        estadoCollectionIdentifiers.push(estadoIdentifier);
        return true;
      });
      return [...estadosToAdd, ...estadoCollection];
    }
    return estadoCollection;
  }
}
