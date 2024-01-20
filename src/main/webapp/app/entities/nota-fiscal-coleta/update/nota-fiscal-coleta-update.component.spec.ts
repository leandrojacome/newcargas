import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { SolicitacaoColetaService } from 'app/entities/solicitacao-coleta/service/solicitacao-coleta.service';
import { NotaFiscalColetaService } from '../service/nota-fiscal-coleta.service';
import { INotaFiscalColeta } from '../nota-fiscal-coleta.model';
import { NotaFiscalColetaFormService } from './nota-fiscal-coleta-form.service';

import { NotaFiscalColetaUpdateComponent } from './nota-fiscal-coleta-update.component';

describe('NotaFiscalColeta Management Update Component', () => {
  let comp: NotaFiscalColetaUpdateComponent;
  let fixture: ComponentFixture<NotaFiscalColetaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notaFiscalColetaFormService: NotaFiscalColetaFormService;
  let notaFiscalColetaService: NotaFiscalColetaService;
  let solicitacaoColetaService: SolicitacaoColetaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), NotaFiscalColetaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(NotaFiscalColetaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotaFiscalColetaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notaFiscalColetaFormService = TestBed.inject(NotaFiscalColetaFormService);
    notaFiscalColetaService = TestBed.inject(NotaFiscalColetaService);
    solicitacaoColetaService = TestBed.inject(SolicitacaoColetaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SolicitacaoColeta query and add missing value', () => {
      const notaFiscalColeta: INotaFiscalColeta = { id: 456 };
      const solicitacaoColeta: ISolicitacaoColeta = { id: 1742 };
      notaFiscalColeta.solicitacaoColeta = solicitacaoColeta;

      const solicitacaoColetaCollection: ISolicitacaoColeta[] = [{ id: 17077 }];
      jest.spyOn(solicitacaoColetaService, 'query').mockReturnValue(of(new HttpResponse({ body: solicitacaoColetaCollection })));
      const additionalSolicitacaoColetas = [solicitacaoColeta];
      const expectedCollection: ISolicitacaoColeta[] = [...additionalSolicitacaoColetas, ...solicitacaoColetaCollection];
      jest.spyOn(solicitacaoColetaService, 'addSolicitacaoColetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notaFiscalColeta });
      comp.ngOnInit();

      expect(solicitacaoColetaService.query).toHaveBeenCalled();
      expect(solicitacaoColetaService.addSolicitacaoColetaToCollectionIfMissing).toHaveBeenCalledWith(
        solicitacaoColetaCollection,
        ...additionalSolicitacaoColetas.map(expect.objectContaining),
      );
      expect(comp.solicitacaoColetasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const notaFiscalColeta: INotaFiscalColeta = { id: 456 };
      const solicitacaoColeta: ISolicitacaoColeta = { id: 32186 };
      notaFiscalColeta.solicitacaoColeta = solicitacaoColeta;

      activatedRoute.data = of({ notaFiscalColeta });
      comp.ngOnInit();

      expect(comp.solicitacaoColetasSharedCollection).toContain(solicitacaoColeta);
      expect(comp.notaFiscalColeta).toEqual(notaFiscalColeta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotaFiscalColeta>>();
      const notaFiscalColeta = { id: 123 };
      jest.spyOn(notaFiscalColetaFormService, 'getNotaFiscalColeta').mockReturnValue(notaFiscalColeta);
      jest.spyOn(notaFiscalColetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notaFiscalColeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notaFiscalColeta }));
      saveSubject.complete();

      // THEN
      expect(notaFiscalColetaFormService.getNotaFiscalColeta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notaFiscalColetaService.update).toHaveBeenCalledWith(expect.objectContaining(notaFiscalColeta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotaFiscalColeta>>();
      const notaFiscalColeta = { id: 123 };
      jest.spyOn(notaFiscalColetaFormService, 'getNotaFiscalColeta').mockReturnValue({ id: null });
      jest.spyOn(notaFiscalColetaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notaFiscalColeta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notaFiscalColeta }));
      saveSubject.complete();

      // THEN
      expect(notaFiscalColetaFormService.getNotaFiscalColeta).toHaveBeenCalled();
      expect(notaFiscalColetaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotaFiscalColeta>>();
      const notaFiscalColeta = { id: 123 };
      jest.spyOn(notaFiscalColetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notaFiscalColeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notaFiscalColetaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSolicitacaoColeta', () => {
      it('Should forward to solicitacaoColetaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(solicitacaoColetaService, 'compareSolicitacaoColeta');
        comp.compareSolicitacaoColeta(entity, entity2);
        expect(solicitacaoColetaService.compareSolicitacaoColeta).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
