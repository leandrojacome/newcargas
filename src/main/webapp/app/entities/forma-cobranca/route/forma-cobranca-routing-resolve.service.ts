import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormaCobranca } from '../forma-cobranca.model';
import { FormaCobrancaService } from '../service/forma-cobranca.service';

export const formaCobrancaResolve = (route: ActivatedRouteSnapshot): Observable<null | IFormaCobranca> => {
  const id = route.params['id'];
  if (id) {
    return inject(FormaCobrancaService)
      .find(id)
      .pipe(
        mergeMap((formaCobranca: HttpResponse<IFormaCobranca>) => {
          if (formaCobranca.body) {
            return of(formaCobranca.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default formaCobrancaResolve;
