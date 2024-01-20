import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISolicitacaoColeta } from '../solicitacao-coleta.model';
import { SolicitacaoColetaService } from '../service/solicitacao-coleta.service';

export const solicitacaoColetaResolve = (route: ActivatedRouteSnapshot): Observable<null | ISolicitacaoColeta> => {
  const id = route.params['id'];
  if (id) {
    return inject(SolicitacaoColetaService)
      .find(id)
      .pipe(
        mergeMap((solicitacaoColeta: HttpResponse<ISolicitacaoColeta>) => {
          if (solicitacaoColeta.body) {
            return of(solicitacaoColeta.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default solicitacaoColetaResolve;
