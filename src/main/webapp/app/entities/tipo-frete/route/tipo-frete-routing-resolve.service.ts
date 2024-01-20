import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoFrete } from '../tipo-frete.model';
import { TipoFreteService } from '../service/tipo-frete.service';

export const tipoFreteResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoFrete> => {
  const id = route.params['id'];
  if (id) {
    return inject(TipoFreteService)
      .find(id)
      .pipe(
        mergeMap((tipoFrete: HttpResponse<ITipoFrete>) => {
          if (tipoFrete.body) {
            return of(tipoFrete.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default tipoFreteResolve;
