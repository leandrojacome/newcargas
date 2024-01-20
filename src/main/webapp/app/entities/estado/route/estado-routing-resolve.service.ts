import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEstado } from '../estado.model';
import { EstadoService } from '../service/estado.service';

export const estadoResolve = (route: ActivatedRouteSnapshot): Observable<null | IEstado> => {
  const id = route.params['id'];
  if (id) {
    return inject(EstadoService)
      .find(id)
      .pipe(
        mergeMap((estado: HttpResponse<IEstado>) => {
          if (estado.body) {
            return of(estado.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default estadoResolve;
