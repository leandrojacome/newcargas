import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEstado, NewEstado } from '../estado.model';

export type PartialUpdateEstado = Partial<IEstado> & Pick<IEstado, 'id'>;

type RestOf<T extends IEstado | NewEstado> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestEstado = RestOf<IEstado>;

export type NewRestEstado = RestOf<NewEstado>;

export type PartialUpdateRestEstado = RestOf<PartialUpdateEstado>;

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
    const copy = this.convertDateFromClient(estado);
    return this.http
      .post<RestEstado>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(estado: IEstado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(estado);
    return this.http
      .put<RestEstado>(`${this.resourceUrl}/${this.getEstadoIdentifier(estado)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(estado: PartialUpdateEstado): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(estado);
    return this.http
      .patch<RestEstado>(`${this.resourceUrl}/${this.getEstadoIdentifier(estado)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEstado>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEstado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestEstado[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IEstado[]>()], asapScheduler)),
    );
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

  protected convertDateFromClient<T extends IEstado | NewEstado | PartialUpdateEstado>(estado: T): RestOf<T> {
    return {
      ...estado,
      createdDate: estado.createdDate?.toJSON() ?? null,
      lastModifiedDate: estado.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEstado: RestEstado): IEstado {
    return {
      ...restEstado,
      createdDate: restEstado.createdDate ? dayjs(restEstado.createdDate) : undefined,
      lastModifiedDate: restEstado.lastModifiedDate ? dayjs(restEstado.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEstado>): HttpResponse<IEstado> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEstado[]>): HttpResponse<IEstado[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
