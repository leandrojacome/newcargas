import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFatura } from '../fatura.model';
import { FaturaService } from '../service/fatura.service';

export const faturaResolve = (route: ActivatedRouteSnapshot): Observable<null | IFatura> => {
  const id = route.params['id'];
  if (id) {
    return inject(FaturaService)
      .find(id)
      .pipe(
        mergeMap((fatura: HttpResponse<IFatura>) => {
          if (fatura.body) {
            return of(fatura.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default faturaResolve;
