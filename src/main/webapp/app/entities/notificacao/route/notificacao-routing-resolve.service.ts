import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificacao } from '../notificacao.model';
import { NotificacaoService } from '../service/notificacao.service';

export const notificacaoResolve = (route: ActivatedRouteSnapshot): Observable<null | INotificacao> => {
  const id = route.params['id'];
  if (id) {
    return inject(NotificacaoService)
      .find(id)
      .pipe(
        mergeMap((notificacao: HttpResponse<INotificacao>) => {
          if (notificacao.body) {
            return of(notificacao.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default notificacaoResolve;
