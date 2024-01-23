import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITipoVeiculo, NewTipoVeiculo } from '../tipo-veiculo.model';

export type PartialUpdateTipoVeiculo = Partial<ITipoVeiculo> & Pick<ITipoVeiculo, 'id'>;

type RestOf<T extends ITipoVeiculo | NewTipoVeiculo> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestTipoVeiculo = RestOf<ITipoVeiculo>;

export type NewRestTipoVeiculo = RestOf<NewTipoVeiculo>;

export type PartialUpdateRestTipoVeiculo = RestOf<PartialUpdateTipoVeiculo>;

export type EntityResponseType = HttpResponse<ITipoVeiculo>;
export type EntityArrayResponseType = HttpResponse<ITipoVeiculo[]>;

@Injectable({ providedIn: 'root' })
export class TipoVeiculoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipo-veiculos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tipo-veiculos/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(tipoVeiculo: NewTipoVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoVeiculo);
    return this.http
      .post<RestTipoVeiculo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tipoVeiculo: ITipoVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoVeiculo);
    return this.http
      .put<RestTipoVeiculo>(`${this.resourceUrl}/${this.getTipoVeiculoIdentifier(tipoVeiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tipoVeiculo: PartialUpdateTipoVeiculo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tipoVeiculo);
    return this.http
      .patch<RestTipoVeiculo>(`${this.resourceUrl}/${this.getTipoVeiculoIdentifier(tipoVeiculo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTipoVeiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTipoVeiculo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTipoVeiculo[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<ITipoVeiculo[]>()], asapScheduler)),
    );
  }

  getTipoVeiculoIdentifier(tipoVeiculo: Pick<ITipoVeiculo, 'id'>): number {
    return tipoVeiculo.id;
  }

  compareTipoVeiculo(o1: Pick<ITipoVeiculo, 'id'> | null, o2: Pick<ITipoVeiculo, 'id'> | null): boolean {
    return o1 && o2 ? this.getTipoVeiculoIdentifier(o1) === this.getTipoVeiculoIdentifier(o2) : o1 === o2;
  }

  addTipoVeiculoToCollectionIfMissing<Type extends Pick<ITipoVeiculo, 'id'>>(
    tipoVeiculoCollection: Type[],
    ...tipoVeiculosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tipoVeiculos: Type[] = tipoVeiculosToCheck.filter(isPresent);
    if (tipoVeiculos.length > 0) {
      const tipoVeiculoCollectionIdentifiers = tipoVeiculoCollection.map(
        tipoVeiculoItem => this.getTipoVeiculoIdentifier(tipoVeiculoItem)!,
      );
      const tipoVeiculosToAdd = tipoVeiculos.filter(tipoVeiculoItem => {
        const tipoVeiculoIdentifier = this.getTipoVeiculoIdentifier(tipoVeiculoItem);
        if (tipoVeiculoCollectionIdentifiers.includes(tipoVeiculoIdentifier)) {
          return false;
        }
        tipoVeiculoCollectionIdentifiers.push(tipoVeiculoIdentifier);
        return true;
      });
      return [...tipoVeiculosToAdd, ...tipoVeiculoCollection];
    }
    return tipoVeiculoCollection;
  }

  protected convertDateFromClient<T extends ITipoVeiculo | NewTipoVeiculo | PartialUpdateTipoVeiculo>(tipoVeiculo: T): RestOf<T> {
    return {
      ...tipoVeiculo,
      createdDate: tipoVeiculo.createdDate?.toJSON() ?? null,
      lastModifiedDate: tipoVeiculo.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTipoVeiculo: RestTipoVeiculo): ITipoVeiculo {
    return {
      ...restTipoVeiculo,
      createdDate: restTipoVeiculo.createdDate ? dayjs(restTipoVeiculo.createdDate) : undefined,
      lastModifiedDate: restTipoVeiculo.lastModifiedDate ? dayjs(restTipoVeiculo.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTipoVeiculo>): HttpResponse<ITipoVeiculo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTipoVeiculo[]>): HttpResponse<ITipoVeiculo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
