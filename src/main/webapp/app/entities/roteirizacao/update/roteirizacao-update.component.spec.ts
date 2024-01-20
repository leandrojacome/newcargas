import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IStatusColeta } from 'app/entities/status-coleta/status-coleta.model';
import { StatusColetaService } from 'app/entities/status-coleta/service/status-coleta.service';
import { RoteirizacaoService } from '../service/roteirizacao.service';
import { IRoteirizacao } from '../roteirizacao.model';
import { RoteirizacaoFormService } from './roteirizacao-form.service';

import { RoteirizacaoUpdateComponent } from './roteirizacao-update.component';

describe('Roteirizacao Management Update Component', () => {
  let comp: RoteirizacaoUpdateComponent;
  let fixture: ComponentFixture<RoteirizacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roteirizacaoFormService: RoteirizacaoFormService;
  let roteirizacaoService: RoteirizacaoService;
  let statusColetaService: StatusColetaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RoteirizacaoUpdateComponent],
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
      .overrideTemplate(RoteirizacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoteirizacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roteirizacaoFormService = TestBed.inject(RoteirizacaoFormService);
    roteirizacaoService = TestBed.inject(RoteirizacaoService);
    statusColetaService = TestBed.inject(StatusColetaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call StatusColeta query and add missing value', () => {
      const roteirizacao: IRoteirizacao = { id: 456 };
      const statusColeta: IStatusColeta = { id: 6240 };
      roteirizacao.statusColeta = statusColeta;

      const statusColetaCollection: IStatusColeta[] = [{ id: 8455 }];
      jest.spyOn(statusColetaService, 'query').mockReturnValue(of(new HttpResponse({ body: statusColetaCollection })));
      const additionalStatusColetas = [statusColeta];
      const expectedCollection: IStatusColeta[] = [...additionalStatusColetas, ...statusColetaCollection];
      jest.spyOn(statusColetaService, 'addStatusColetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ roteirizacao });
      comp.ngOnInit();

      expect(statusColetaService.query).toHaveBeenCalled();
      expect(statusColetaService.addStatusColetaToCollectionIfMissing).toHaveBeenCalledWith(
        statusColetaCollection,
        ...additionalStatusColetas.map(expect.objectContaining),
      );
      expect(comp.statusColetasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const roteirizacao: IRoteirizacao = { id: 456 };
      const statusColeta: IStatusColeta = { id: 15895 };
      roteirizacao.statusColeta = statusColeta;

      activatedRoute.data = of({ roteirizacao });
      comp.ngOnInit();

      expect(comp.statusColetasSharedCollection).toContain(statusColeta);
      expect(comp.roteirizacao).toEqual(roteirizacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoteirizacao>>();
      const roteirizacao = { id: 123 };
      jest.spyOn(roteirizacaoFormService, 'getRoteirizacao').mockReturnValue(roteirizacao);
      jest.spyOn(roteirizacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roteirizacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roteirizacao }));
      saveSubject.complete();

      // THEN
      expect(roteirizacaoFormService.getRoteirizacao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roteirizacaoService.update).toHaveBeenCalledWith(expect.objectContaining(roteirizacao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoteirizacao>>();
      const roteirizacao = { id: 123 };
      jest.spyOn(roteirizacaoFormService, 'getRoteirizacao').mockReturnValue({ id: null });
      jest.spyOn(roteirizacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roteirizacao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roteirizacao }));
      saveSubject.complete();

      // THEN
      expect(roteirizacaoFormService.getRoteirizacao).toHaveBeenCalled();
      expect(roteirizacaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoteirizacao>>();
      const roteirizacao = { id: 123 };
      jest.spyOn(roteirizacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roteirizacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roteirizacaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
