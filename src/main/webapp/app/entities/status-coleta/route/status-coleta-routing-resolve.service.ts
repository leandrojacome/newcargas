import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStatusColeta } from '../status-coleta.model';
import { StatusColetaService } from '../service/status-coleta.service';

export const statusColetaResolve = (route: ActivatedRouteSnapshot): Observable<null | IStatusColeta> => {
  const id = route.params['id'];
  if (id) {
    return inject(StatusColetaService)
      .find(id)
      .pipe(
        mergeMap((statusColeta: HttpResponse<IStatusColeta>) => {
          if (statusColeta.body) {
            return of(statusColeta.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default statusColetaResolve;
