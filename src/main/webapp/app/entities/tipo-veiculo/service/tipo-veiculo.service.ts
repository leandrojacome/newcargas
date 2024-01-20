import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITipoVeiculo, NewTipoVeiculo } from '../tipo-veiculo.model';

export type PartialUpdateTipoVeiculo = Partial<ITipoVeiculo> & Pick<ITipoVeiculo, 'id'>;

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
    return this.http.post<ITipoVeiculo>(this.resourceUrl, tipoVeiculo, { observe: 'response' });
  }

  update(tipoVeiculo: ITipoVeiculo): Observable<EntityResponseType> {
    return this.http.put<ITipoVeiculo>(`${this.resourceUrl}/${this.getTipoVeiculoIdentifier(tipoVeiculo)}`, tipoVeiculo, {
      observe: 'response',
    });
  }

  partialUpdate(tipoVeiculo: PartialUpdateTipoVeiculo): Observable<EntityResponseType> {
    return this.http.patch<ITipoVeiculo>(`${this.resourceUrl}/${this.getTipoVeiculoIdentifier(tipoVeiculo)}`, tipoVeiculo, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoVeiculo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoVeiculo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITipoVeiculo[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ITipoVeiculo[]>()], asapScheduler)));
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
}
