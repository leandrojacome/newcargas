import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBanco } from '../banco.model';
import { BancoService } from '../service/banco.service';

export const bancoResolve = (route: ActivatedRouteSnapshot): Observable<null | IBanco> => {
  const id = route.params['id'];
  if (id) {
    return inject(BancoService)
      .find(id)
      .pipe(
        mergeMap((banco: HttpResponse<IBanco>) => {
          if (banco.body) {
            return of(banco.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default bancoResolve;
