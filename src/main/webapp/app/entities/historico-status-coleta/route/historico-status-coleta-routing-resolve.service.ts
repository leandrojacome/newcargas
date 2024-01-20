import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistoricoStatusColeta } from '../historico-status-coleta.model';
import { HistoricoStatusColetaService } from '../service/historico-status-coleta.service';

export const historicoStatusColetaResolve = (route: ActivatedRouteSnapshot): Observable<null | IHistoricoStatusColeta> => {
  const id = route.params['id'];
  if (id) {
    return inject(HistoricoStatusColetaService)
      .find(id)
      .pipe(
        mergeMap((historicoStatusColeta: HttpResponse<IHistoricoStatusColeta>) => {
          if (historicoStatusColeta.body) {
            return of(historicoStatusColeta.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default historicoStatusColetaResolve;
