import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITabelaFrete } from '../tabela-frete.model';
import { TabelaFreteService } from '../service/tabela-frete.service';

export const tabelaFreteResolve = (route: ActivatedRouteSnapshot): Observable<null | ITabelaFrete> => {
  const id = route.params['id'];
  if (id) {
    return inject(TabelaFreteService)
      .find(id)
      .pipe(
        mergeMap((tabelaFrete: HttpResponse<ITabelaFrete>) => {
          if (tabelaFrete.body) {
            return of(tabelaFrete.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default tabelaFreteResolve;
