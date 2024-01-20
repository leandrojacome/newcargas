import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICidade } from '../cidade.model';
import { CidadeService } from '../service/cidade.service';

export const cidadeResolve = (route: ActivatedRouteSnapshot): Observable<null | ICidade> => {
  const id = route.params['id'];
  if (id) {
    return inject(CidadeService)
      .find(id)
      .pipe(
        mergeMap((cidade: HttpResponse<ICidade>) => {
          if (cidade.body) {
            return of(cidade.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default cidadeResolve;
