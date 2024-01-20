import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransportadora } from '../transportadora.model';
import { TransportadoraService } from '../service/transportadora.service';

export const transportadoraResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransportadora> => {
  const id = route.params['id'];
  if (id) {
    return inject(TransportadoraService)
      .find(id)
      .pipe(
        mergeMap((transportadora: HttpResponse<ITransportadora>) => {
          if (transportadora.body) {
            return of(transportadora.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default transportadoraResolve;
