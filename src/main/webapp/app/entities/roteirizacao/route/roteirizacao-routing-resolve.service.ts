import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoteirizacao } from '../roteirizacao.model';
import { RoteirizacaoService } from '../service/roteirizacao.service';

export const roteirizacaoResolve = (route: ActivatedRouteSnapshot): Observable<null | IRoteirizacao> => {
  const id = route.params['id'];
  if (id) {
    return inject(RoteirizacaoService)
      .find(id)
      .pipe(
        mergeMap((roteirizacao: HttpResponse<IRoteirizacao>) => {
          if (roteirizacao.body) {
            return of(roteirizacao.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default roteirizacaoResolve;
