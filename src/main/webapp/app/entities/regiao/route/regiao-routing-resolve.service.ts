import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRegiao } from '../regiao.model';
import { RegiaoService } from '../service/regiao.service';

export const regiaoResolve = (route: ActivatedRouteSnapshot): Observable<null | IRegiao> => {
  const id = route.params['id'];
  if (id) {
    return inject(RegiaoService)
      .find(id)
      .pipe(
        mergeMap((regiao: HttpResponse<IRegiao>) => {
          if (regiao.body) {
            return of(regiao.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default regiaoResolve;
