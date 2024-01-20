import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISolicitacaoColeta } from '../solicitacao-coleta.model';
import { SolicitacaoColetaService } from '../service/solicitacao-coleta.service';

import solicitacaoColetaResolve from './solicitacao-coleta-routing-resolve.service';

describe('SolicitacaoColeta routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: SolicitacaoColetaService;
  let resultSolicitacaoColeta: ISolicitacaoColeta | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(SolicitacaoColetaService);
    resultSolicitacaoColeta = undefined;
  });

  describe('resolve', () => {
    it('should return ISolicitacaoColeta returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        solicitacaoColetaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSolicitacaoColeta = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSolicitacaoColeta).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        solicitacaoColetaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSolicitacaoColeta = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSolicitacaoColeta).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISolicitacaoColeta>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        solicitacaoColetaResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSolicitacaoColeta = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSolicitacaoColeta).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
