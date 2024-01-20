import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotaFiscalColeta } from '../nota-fiscal-coleta.model';
import { NotaFiscalColetaService } from '../service/nota-fiscal-coleta.service';

export const notaFiscalColetaResolve = (route: ActivatedRouteSnapshot): Observable<null | INotaFiscalColeta> => {
  const id = route.params['id'];
  if (id) {
    return inject(NotaFiscalColetaService)
      .find(id)
      .pipe(
        mergeMap((notaFiscalColeta: HttpResponse<INotaFiscalColeta>) => {
          if (notaFiscalColeta.body) {
            return of(notaFiscalColeta.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default notaFiscalColetaResolve;
