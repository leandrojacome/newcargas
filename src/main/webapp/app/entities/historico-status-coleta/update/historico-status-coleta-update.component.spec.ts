import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { SolicitacaoColetaService } from 'app/entities/solicitacao-coleta/service/solicitacao-coleta.service';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';
import { RoteirizacaoService } from 'app/entities/roteirizacao/service/roteirizacao.service';
import { IStatusColeta } from 'app/entities/status-coleta/status-coleta.model';
import { StatusColetaService } from 'app/entities/status-coleta/service/status-coleta.service';
import { IHistoricoStatusColeta } from '../historico-status-coleta.model';
import { HistoricoStatusColetaService } from '../service/historico-status-coleta.service';
import { HistoricoStatusColetaFormService } from './historico-status-coleta-form.service';

import { HistoricoStatusColetaUpdateComponent } from './historico-status-coleta-update.component';

describe('HistoricoStatusColeta Management Update Component', () => {
  let comp: HistoricoStatusColetaUpdateComponent;
  let fixture: ComponentFixture<HistoricoStatusColetaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let historicoStatusColetaFormService: HistoricoStatusColetaFormService;
  let historicoStatusColetaService: HistoricoStatusColetaService;
  let solicitacaoColetaService: SolicitacaoColetaService;
  let roteirizacaoService: RoteirizacaoService;
  let statusColetaService: StatusColetaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), HistoricoStatusColetaUpdateComponent],
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
      .overrideTemplate(HistoricoStatusColetaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HistoricoStatusColetaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    historicoStatusColetaFormService = TestBed.inject(HistoricoStatusColetaFormService);
    historicoStatusColetaService = TestBed.inject(HistoricoStatusColetaService);
    solicitacaoColetaService = TestBed.inject(SolicitacaoColetaService);
    roteirizacaoService = TestBed.inject(RoteirizacaoService);
    statusColetaService = TestBed.inject(StatusColetaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SolicitacaoColeta query and add missing value', () => {
      const historicoStatusColeta: IHistoricoStatusColeta = { id: 456 };
      const solicitacaoColeta: ISolicitacaoColeta = { id: 10385 };
      historicoStatusColeta.solicitacaoColeta = solicitacaoColeta;

      const solicitacaoColetaCollection: ISolicitacaoColeta[] = [{ id: 31460 }];
      jest.spyOn(solicitacaoColetaService, 'query').mockReturnValue(of(new HttpResponse({ body: solicitacaoColetaCollection })));
      const additionalSolicitacaoColetas = [solicitacaoColeta];
      const expectedCollection: ISolicitacaoColeta[] = [...additionalSolicitacaoColetas, ...solicitacaoColetaCollection];
      jest.spyOn(solicitacaoColetaService, 'addSolicitacaoColetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historicoStatusColeta });
      comp.ngOnInit();

      expect(solicitacaoColetaService.query).toHaveBeenCalled();
      expect(solicitacaoColetaService.addSolicitacaoColetaToCollectionIfMissing).toHaveBeenCalledWith(
        solicitacaoColetaCollection,
        ...additionalSolicitacaoColetas.map(expect.objectContaining),
      );
      expect(comp.solicitacaoColetasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Roteirizacao query and add missing value', () => {
      const historicoStatusColeta: IHistoricoStatusColeta = { id: 456 };
      const roteirizacao: IRoteirizacao = { id: 2997 };
      historicoStatusColeta.roteirizacao = roteirizacao;

      const roteirizacaoCollection: IRoteirizacao[] = [{ id: 22692 }];
      jest.spyOn(roteirizacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: roteirizacaoCollection })));
      const additionalRoteirizacaos = [roteirizacao];
      const expectedCollection: IRoteirizacao[] = [...additionalRoteirizacaos, ...roteirizacaoCollection];
      jest.spyOn(roteirizacaoService, 'addRoteirizacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historicoStatusColeta });
      comp.ngOnInit();

      expect(roteirizacaoService.query).toHaveBeenCalled();
      expect(roteirizacaoService.addRoteirizacaoToCollectionIfMissing).toHaveBeenCalledWith(
        roteirizacaoCollection,
        ...additionalRoteirizacaos.map(expect.objectContaining),
      );
      expect(comp.roteirizacaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call StatusColeta query and add missing value', () => {
      const historicoStatusColeta: IHistoricoStatusColeta = { id: 456 };
      const statusColetaOrigem: IStatusColeta = { id: 32424 };
      historicoStatusColeta.statusColetaOrigem = statusColetaOrigem;
      const statusColetaDestino: IStatusColeta = { id: 6225 };
      historicoStatusColeta.statusColetaDestino = statusColetaDestino;

      const statusColetaCollection: IStatusColeta[] = [{ id: 29070 }];
      jest.spyOn(statusColetaService, 'query').mockReturnValue(of(new HttpResponse({ body: statusColetaCollection })));
      const additionalStatusColetas = [statusColetaOrigem, statusColetaDestino];
      const expectedCollection: IStatusColeta[] = [...additionalStatusColetas, ...statusColetaCollection];
      jest.spyOn(statusColetaService, 'addStatusColetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historicoStatusColeta });
      comp.ngOnInit();

      expect(statusColetaService.query).toHaveBeenCalled();
      expect(statusColetaService.addStatusColetaToCollectionIfMissing).toHaveBeenCalledWith(
        statusColetaCollection,
        ...additionalStatusColetas.map(expect.objectContaining),
      );
      expect(comp.statusColetasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const historicoStatusColeta: IHistoricoStatusColeta = { id: 456 };
      const solicitacaoColeta: ISolicitacaoColeta = { id: 30314 };
      historicoStatusColeta.solicitacaoColeta = solicitacaoColeta;
      const roteirizacao: IRoteirizacao = { id: 27185 };
      historicoStatusColeta.roteirizacao = roteirizacao;
      const statusColetaOrigem: IStatusColeta = { id: 3355 };
      historicoStatusColeta.statusColetaOrigem = statusColetaOrigem;
      const statusColetaDestino: IStatusColeta = { id: 12005 };
      historicoStatusColeta.statusColetaDestino = statusColetaDestino;

      activatedRoute.data = of({ historicoStatusColeta });
      comp.ngOnInit();

      expect(comp.solicitacaoColetasSharedCollection).toContain(solicitacaoColeta);
      expect(comp.roteirizacaosSharedCollection).toContain(roteirizacao);
      expect(comp.statusColetasSharedCollection).toContain(statusColetaOrigem);
      expect(comp.statusColetasSharedCollection).toContain(statusColetaDestino);
      expect(comp.historicoStatusColeta).toEqual(historicoStatusColeta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoricoStatusColeta>>();
      const historicoStatusColeta = { id: 123 };
      jest.spyOn(historicoStatusColetaFormService, 'getHistoricoStatusColeta').mockReturnValue(historicoStatusColeta);
      jest.spyOn(historicoStatusColetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historicoStatusColeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historicoStatusColeta }));
      saveSubject.complete();

      // THEN
      expect(historicoStatusColetaFormService.getHistoricoStatusColeta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(historicoStatusColetaService.update).toHaveBeenCalledWith(expect.objectContaining(historicoStatusColeta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoricoStatusColeta>>();
      const historicoStatusColeta = { id: 123 };
      jest.spyOn(historicoStatusColetaFormService, 'getHistoricoStatusColeta').mockReturnValue({ id: null });
      jest.spyOn(historicoStatusColetaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historicoStatusColeta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historicoStatusColeta }));
      saveSubject.complete();

      // THEN
      expect(historicoStatusColetaFormService.getHistoricoStatusColeta).toHaveBeenCalled();
      expect(historicoStatusColetaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoricoStatusColeta>>();
      const historicoStatusColeta = { id: 123 };
      jest.spyOn(historicoStatusColetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historicoStatusColeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(historicoStatusColetaService.update).toHaveBeenCalled();
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

    describe('compareRoteirizacao', () => {
      it('Should forward to roteirizacaoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roteirizacaoService, 'compareRoteirizacao');
        comp.compareRoteirizacao(entity, entity2);
        expect(roteirizacaoService.compareRoteirizacao).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStatusColeta', () => {
      it('Should forward to statusColetaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(statusColetaService, 'compareStatusColeta');
        comp.compareStatusColeta(entity, entity2);
        expect(statusColetaService.compareStatusColeta).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
