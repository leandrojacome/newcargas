import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContaBancaria } from '../conta-bancaria.model';
import { ContaBancariaService } from '../service/conta-bancaria.service';

export const contaBancariaResolve = (route: ActivatedRouteSnapshot): Observable<null | IContaBancaria> => {
  const id = route.params['id'];
  if (id) {
    return inject(ContaBancariaService)
      .find(id)
      .pipe(
        mergeMap((contaBancaria: HttpResponse<IContaBancaria>) => {
          if (contaBancaria.body) {
            return of(contaBancaria.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default contaBancariaResolve;
