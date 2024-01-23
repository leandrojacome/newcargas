import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IEstado } from 'app/entities/estado/estado.model';
import { EstadoService } from 'app/entities/estado/service/estado.service';
import { CidadeService } from '../service/cidade.service';
import { ICidade } from '../cidade.model';
import { CidadeFormService } from './cidade-form.service';

import { CidadeUpdateComponent } from './cidade-update.component';

describe('Cidade Management Update Component', () => {
  let comp: CidadeUpdateComponent;
  let fixture: ComponentFixture<CidadeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cidadeFormService: CidadeFormService;
  let cidadeService: CidadeService;
  let estadoService: EstadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CidadeUpdateComponent],
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
      .overrideTemplate(CidadeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CidadeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cidadeFormService = TestBed.inject(CidadeFormService);
    cidadeService = TestBed.inject(CidadeService);
    estadoService = TestBed.inject(EstadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Estado query and add missing value', () => {
      const cidade: ICidade = { id: 456 };
      const estado: IEstado = { id: 3160 };
      cidade.estado = estado;

      const estadoCollection: IEstado[] = [{ id: 26810 }];
      jest.spyOn(estadoService, 'query').mockReturnValue(of(new HttpResponse({ body: estadoCollection })));
      const additionalEstados = [estado];
      const expectedCollection: IEstado[] = [...additionalEstados, ...estadoCollection];
      jest.spyOn(estadoService, 'addEstadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cidade });
      comp.ngOnInit();

      expect(estadoService.query).toHaveBeenCalled();
      expect(estadoService.addEstadoToCollectionIfMissing).toHaveBeenCalledWith(
        estadoCollection,
        ...additionalEstados.map(expect.objectContaining),
      );
      expect(comp.estadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cidade: ICidade = { id: 456 };
      const estado: IEstado = { id: 3879 };
      cidade.estado = estado;

      activatedRoute.data = of({ cidade });
      comp.ngOnInit();

      expect(comp.estadosSharedCollection).toContain(estado);
      expect(comp.cidade).toEqual(cidade);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICidade>>();
      const cidade = { id: 123 };
      jest.spyOn(cidadeFormService, 'getCidade').mockReturnValue(cidade);
      jest.spyOn(cidadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cidade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cidade }));
      saveSubject.complete();

      // THEN
      expect(cidadeFormService.getCidade).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cidadeService.update).toHaveBeenCalledWith(expect.objectContaining(cidade));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICidade>>();
      const cidade = { id: 123 };
      jest.spyOn(cidadeFormService, 'getCidade').mockReturnValue({ id: null });
      jest.spyOn(cidadeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cidade: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cidade }));
      saveSubject.complete();

      // THEN
      expect(cidadeFormService.getCidade).toHaveBeenCalled();
      expect(cidadeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICidade>>();
      const cidade = { id: 123 };
      jest.spyOn(cidadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cidade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cidadeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEstado', () => {
      it('Should forward to estadoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(estadoService, 'compareEstado');
        comp.compareEstado(entity, entity2);
        expect(estadoService.compareEstado).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
