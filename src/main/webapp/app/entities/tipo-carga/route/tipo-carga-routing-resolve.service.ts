import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoCarga } from '../tipo-carga.model';
import { TipoCargaService } from '../service/tipo-carga.service';

export const tipoCargaResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoCarga> => {
  const id = route.params['id'];
  if (id) {
    return inject(TipoCargaService)
      .find(id)
      .pipe(
        mergeMap((tipoCarga: HttpResponse<ITipoCarga>) => {
          if (tipoCarga.body) {
            return of(tipoCarga.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default tipoCargaResolve;
