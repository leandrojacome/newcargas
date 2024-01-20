import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICidade, NewCidade } from '../cidade.model';

export type PartialUpdateCidade = Partial<ICidade> & Pick<ICidade, 'id'>;

export type EntityResponseType = HttpResponse<ICidade>;
export type EntityArrayResponseType = HttpResponse<ICidade[]>;

@Injectable({ providedIn: 'root' })
export class CidadeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cidades');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/cidades/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(cidade: NewCidade): Observable<EntityResponseType> {
    return this.http.post<ICidade>(this.resourceUrl, cidade, { observe: 'response' });
  }

  update(cidade: ICidade): Observable<EntityResponseType> {
    return this.http.put<ICidade>(`${this.resourceUrl}/${this.getCidadeIdentifier(cidade)}`, cidade, { observe: 'response' });
  }

  partialUpdate(cidade: PartialUpdateCidade): Observable<EntityResponseType> {
    return this.http.patch<ICidade>(`${this.resourceUrl}/${this.getCidadeIdentifier(cidade)}`, cidade, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICidade>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICidade[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICidade[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ICidade[]>()], asapScheduler)));
  }

  getCidadeIdentifier(cidade: Pick<ICidade, 'id'>): number {
    return cidade.id;
  }

  compareCidade(o1: Pick<ICidade, 'id'> | null, o2: Pick<ICidade, 'id'> | null): boolean {
    return o1 && o2 ? this.getCidadeIdentifier(o1) === this.getCidadeIdentifier(o2) : o1 === o2;
  }

  addCidadeToCollectionIfMissing<Type extends Pick<ICidade, 'id'>>(
    cidadeCollection: Type[],
    ...cidadesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cidades: Type[] = cidadesToCheck.filter(isPresent);
    if (cidades.length > 0) {
      const cidadeCollectionIdentifiers = cidadeCollection.map(cidadeItem => this.getCidadeIdentifier(cidadeItem)!);
      const cidadesToAdd = cidades.filter(cidadeItem => {
        const cidadeIdentifier = this.getCidadeIdentifier(cidadeItem);
        if (cidadeCollectionIdentifiers.includes(cidadeIdentifier)) {
          return false;
        }
        cidadeCollectionIdentifiers.push(cidadeIdentifier);
        return true;
      });
      return [...cidadesToAdd, ...cidadeCollection];
    }
    return cidadeCollection;
  }
}
