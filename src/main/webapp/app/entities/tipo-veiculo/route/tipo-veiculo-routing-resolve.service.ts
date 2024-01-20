import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoVeiculo } from '../tipo-veiculo.model';
import { TipoVeiculoService } from '../service/tipo-veiculo.service';

export const tipoVeiculoResolve = (route: ActivatedRouteSnapshot): Observable<null | ITipoVeiculo> => {
  const id = route.params['id'];
  if (id) {
    return inject(TipoVeiculoService)
      .find(id)
      .pipe(
        mergeMap((tipoVeiculo: HttpResponse<ITipoVeiculo>) => {
          if (tipoVeiculo.body) {
            return of(tipoVeiculo.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default tipoVeiculoResolve;
