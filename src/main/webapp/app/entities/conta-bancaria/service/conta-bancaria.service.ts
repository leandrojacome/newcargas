import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IContaBancaria, NewContaBancaria } from '../conta-bancaria.model';

export type PartialUpdateContaBancaria = Partial<IContaBancaria> & Pick<IContaBancaria, 'id'>;

type RestOf<T extends IContaBancaria | NewContaBancaria> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestContaBancaria = RestOf<IContaBancaria>;

export type NewRestContaBancaria = RestOf<NewContaBancaria>;

export type PartialUpdateRestContaBancaria = RestOf<PartialUpdateContaBancaria>;

export type EntityResponseType = HttpResponse<IContaBancaria>;
export type EntityArrayResponseType = HttpResponse<IContaBancaria[]>;

@Injectable({ providedIn: 'root' })
export class ContaBancariaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conta-bancarias');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/conta-bancarias/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(contaBancaria: NewContaBancaria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contaBancaria);
    return this.http
      .post<RestContaBancaria>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contaBancaria: IContaBancaria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contaBancaria);
    return this.http
      .put<RestContaBancaria>(`${this.resourceUrl}/${this.getContaBancariaIdentifier(contaBancaria)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contaBancaria: PartialUpdateContaBancaria): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contaBancaria);
    return this.http
      .patch<RestContaBancaria>(`${this.resourceUrl}/${this.getContaBancariaIdentifier(contaBancaria)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContaBancaria>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContaBancaria[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestContaBancaria[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IContaBancaria[]>()], asapScheduler)),
    );
  }

  getContaBancariaIdentifier(contaBancaria: Pick<IContaBancaria, 'id'>): number {
    return contaBancaria.id;
  }

  compareContaBancaria(o1: Pick<IContaBancaria, 'id'> | null, o2: Pick<IContaBancaria, 'id'> | null): boolean {
    return o1 && o2 ? this.getContaBancariaIdentifier(o1) === this.getContaBancariaIdentifier(o2) : o1 === o2;
  }

  addContaBancariaToCollectionIfMissing<Type extends Pick<IContaBancaria, 'id'>>(
    contaBancariaCollection: Type[],
    ...contaBancariasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contaBancarias: Type[] = contaBancariasToCheck.filter(isPresent);
    if (contaBancarias.length > 0) {
      const contaBancariaCollectionIdentifiers = contaBancariaCollection.map(
        contaBancariaItem => this.getContaBancariaIdentifier(contaBancariaItem)!,
      );
      const contaBancariasToAdd = contaBancarias.filter(contaBancariaItem => {
        const contaBancariaIdentifier = this.getContaBancariaIdentifier(contaBancariaItem);
        if (contaBancariaCollectionIdentifiers.includes(contaBancariaIdentifier)) {
          return false;
        }
        contaBancariaCollectionIdentifiers.push(contaBancariaIdentifier);
        return true;
      });
      return [...contaBancariasToAdd, ...contaBancariaCollection];
    }
    return contaBancariaCollection;
  }

  protected convertDateFromClient<T extends IContaBancaria | NewContaBancaria | PartialUpdateContaBancaria>(contaBancaria: T): RestOf<T> {
    return {
      ...contaBancaria,
      createdDate: contaBancaria.createdDate?.toJSON() ?? null,
      lastModifiedDate: contaBancaria.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restContaBancaria: RestContaBancaria): IContaBancaria {
    return {
      ...restContaBancaria,
      createdDate: restContaBancaria.createdDate ? dayjs(restContaBancaria.createdDate) : undefined,
      lastModifiedDate: restContaBancaria.lastModifiedDate ? dayjs(restContaBancaria.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContaBancaria>): HttpResponse<IContaBancaria> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContaBancaria[]>): HttpResponse<IContaBancaria[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
