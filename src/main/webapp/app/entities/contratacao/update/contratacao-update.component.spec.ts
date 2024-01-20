import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { ContratacaoService } from '../service/contratacao.service';
import { IContratacao } from '../contratacao.model';
import { ContratacaoFormService } from './contratacao-form.service';

import { ContratacaoUpdateComponent } from './contratacao-update.component';

describe('Contratacao Management Update Component', () => {
  let comp: ContratacaoUpdateComponent;
  let fixture: ComponentFixture<ContratacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contratacaoFormService: ContratacaoFormService;
  let contratacaoService: ContratacaoService;
  let transportadoraService: TransportadoraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ContratacaoUpdateComponent],
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
      .overrideTemplate(ContratacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContratacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contratacaoFormService = TestBed.inject(ContratacaoFormService);
    contratacaoService = TestBed.inject(ContratacaoService);
    transportadoraService = TestBed.inject(TransportadoraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Transportadora query and add missing value', () => {
      const contratacao: IContratacao = { id: 456 };
      const transportadora: ITransportadora = { id: 1641 };
      contratacao.transportadora = transportadora;

      const transportadoraCollection: ITransportadora[] = [{ id: 29963 }];
      jest.spyOn(transportadoraService, 'query').mockReturnValue(of(new HttpResponse({ body: transportadoraCollection })));
      const additionalTransportadoras = [transportadora];
      const expectedCollection: ITransportadora[] = [...additionalTransportadoras, ...transportadoraCollection];
      jest.spyOn(transportadoraService, 'addTransportadoraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contratacao });
      comp.ngOnInit();

      expect(transportadoraService.query).toHaveBeenCalled();
      expect(transportadoraService.addTransportadoraToCollectionIfMissing).toHaveBeenCalledWith(
        transportadoraCollection,
        ...additionalTransportadoras.map(expect.objectContaining),
      );
      expect(comp.transportadorasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contratacao: IContratacao = { id: 456 };
      const transportadora: ITransportadora = { id: 1887 };
      contratacao.transportadora = transportadora;

      activatedRoute.data = of({ contratacao });
      comp.ngOnInit();

      expect(comp.transportadorasSharedCollection).toContain(transportadora);
      expect(comp.contratacao).toEqual(contratacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContratacao>>();
      const contratacao = { id: 123 };
      jest.spyOn(contratacaoFormService, 'getContratacao').mockReturnValue(contratacao);
      jest.spyOn(contratacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contratacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contratacao }));
      saveSubject.complete();

      // THEN
      expect(contratacaoFormService.getContratacao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contratacaoService.update).toHaveBeenCalledWith(expect.objectContaining(contratacao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContratacao>>();
      const contratacao = { id: 123 };
      jest.spyOn(contratacaoFormService, 'getContratacao').mockReturnValue({ id: null });
      jest.spyOn(contratacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contratacao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contratacao }));
      saveSubject.complete();

      // THEN
      expect(contratacaoFormService.getContratacao).toHaveBeenCalled();
      expect(contratacaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContratacao>>();
      const contratacao = { id: 123 };
      jest.spyOn(contratacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contratacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contratacaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTransportadora', () => {
      it('Should forward to transportadoraService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transportadoraService, 'compareTransportadora');
        comp.compareTransportadora(entity, entity2);
        expect(transportadoraService.compareTransportadora).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
