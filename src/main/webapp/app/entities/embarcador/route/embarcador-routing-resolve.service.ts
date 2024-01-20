import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmbarcador } from '../embarcador.model';
import { EmbarcadorService } from '../service/embarcador.service';

export const embarcadorResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmbarcador> => {
  const id = route.params['id'];
  if (id) {
    return inject(EmbarcadorService)
      .find(id)
      .pipe(
        mergeMap((embarcador: HttpResponse<IEmbarcador>) => {
          if (embarcador.body) {
            return of(embarcador.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default embarcadorResolve;
