import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContratacao } from '../contratacao.model';
import { ContratacaoService } from '../service/contratacao.service';

export const contratacaoResolve = (route: ActivatedRouteSnapshot): Observable<null | IContratacao> => {
  const id = route.params['id'];
  if (id) {
    return inject(ContratacaoService)
      .find(id)
      .pipe(
        mergeMap((contratacao: HttpResponse<IContratacao>) => {
          if (contratacao.body) {
            return of(contratacao.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default contratacaoResolve;
