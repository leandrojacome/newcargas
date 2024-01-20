import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StatusColetaService } from '../service/status-coleta.service';
import { IStatusColeta } from '../status-coleta.model';
import { StatusColetaFormService } from './status-coleta-form.service';

import { StatusColetaUpdateComponent } from './status-coleta-update.component';

describe('StatusColeta Management Update Component', () => {
  let comp: StatusColetaUpdateComponent;
  let fixture: ComponentFixture<StatusColetaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let statusColetaFormService: StatusColetaFormService;
  let statusColetaService: StatusColetaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), StatusColetaUpdateComponent],
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
      .overrideTemplate(StatusColetaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StatusColetaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    statusColetaFormService = TestBed.inject(StatusColetaFormService);
    statusColetaService = TestBed.inject(StatusColetaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call StatusColeta query and add missing value', () => {
      const statusColeta: IStatusColeta = { id: 456 };
      const statusColetaOrigems: IStatusColeta[] = [{ id: 30163 }];
      statusColeta.statusColetaOrigems = statusColetaOrigems;

      const statusColetaCollection: IStatusColeta[] = [{ id: 19800 }];
      jest.spyOn(statusColetaService, 'query').mockReturnValue(of(new HttpResponse({ body: statusColetaCollection })));
      const additionalStatusColetas = [...statusColetaOrigems];
      const expectedCollection: IStatusColeta[] = [...additionalStatusColetas, ...statusColetaCollection];
      jest.spyOn(statusColetaService, 'addStatusColetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ statusColeta });
      comp.ngOnInit();

      expect(statusColetaService.query).toHaveBeenCalled();
      expect(statusColetaService.addStatusColetaToCollectionIfMissing).toHaveBeenCalledWith(
        statusColetaCollection,
        ...additionalStatusColetas.map(expect.objectContaining),
      );
      expect(comp.statusColetasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const statusColeta: IStatusColeta = { id: 456 };
      const statusColetaOrigem: IStatusColeta = { id: 32214 };
      statusColeta.statusColetaOrigems = [statusColetaOrigem];

      activatedRoute.data = of({ statusColeta });
      comp.ngOnInit();

      expect(comp.statusColetasSharedCollection).toContain(statusColetaOrigem);
      expect(comp.statusColeta).toEqual(statusColeta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatusColeta>>();
      const statusColeta = { id: 123 };
      jest.spyOn(statusColetaFormService, 'getStatusColeta').mockReturnValue(statusColeta);
      jest.spyOn(statusColetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusColeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: statusColeta }));
      saveSubject.complete();

      // THEN
      expect(statusColetaFormService.getStatusColeta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(statusColetaService.update).toHaveBeenCalledWith(expect.objectContaining(statusColeta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatusColeta>>();
      const statusColeta = { id: 123 };
      jest.spyOn(statusColetaFormService, 'getStatusColeta').mockReturnValue({ id: null });
      jest.spyOn(statusColetaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusColeta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: statusColeta }));
      saveSubject.complete();

      // THEN
      expect(statusColetaFormService.getStatusColeta).toHaveBeenCalled();
      expect(statusColetaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStatusColeta>>();
      const statusColeta = { id: 123 };
      jest.spyOn(statusColetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ statusColeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(statusColetaService.update).toHaveBeenCalled();
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
