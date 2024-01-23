import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IContratacao } from 'app/entities/contratacao/contratacao.model';
import { ContratacaoService } from 'app/entities/contratacao/service/contratacao.service';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';
import { RoteirizacaoService } from 'app/entities/roteirizacao/service/roteirizacao.service';
import { ITomadaPreco } from '../tomada-preco.model';
import { TomadaPrecoService } from '../service/tomada-preco.service';
import { TomadaPrecoFormService } from './tomada-preco-form.service';

import { TomadaPrecoUpdateComponent } from './tomada-preco-update.component';

describe('TomadaPreco Management Update Component', () => {
  let comp: TomadaPrecoUpdateComponent;
  let fixture: ComponentFixture<TomadaPrecoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tomadaPrecoFormService: TomadaPrecoFormService;
  let tomadaPrecoService: TomadaPrecoService;
  let contratacaoService: ContratacaoService;
  let transportadoraService: TransportadoraService;
  let roteirizacaoService: RoteirizacaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TomadaPrecoUpdateComponent],
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
      .overrideTemplate(TomadaPrecoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TomadaPrecoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tomadaPrecoFormService = TestBed.inject(TomadaPrecoFormService);
    tomadaPrecoService = TestBed.inject(TomadaPrecoService);
    contratacaoService = TestBed.inject(ContratacaoService);
    transportadoraService = TestBed.inject(TransportadoraService);
    roteirizacaoService = TestBed.inject(RoteirizacaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call contratacao query and add missing value', () => {
      const tomadaPreco: ITomadaPreco = { id: 456 };
      const contratacao: IContratacao = { id: 21705 };
      tomadaPreco.contratacao = contratacao;

      const contratacaoCollection: IContratacao[] = [{ id: 6987 }];
      jest.spyOn(contratacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: contratacaoCollection })));
      const expectedCollection: IContratacao[] = [contratacao, ...contratacaoCollection];
      jest.spyOn(contratacaoService, 'addContratacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tomadaPreco });
      comp.ngOnInit();

      expect(contratacaoService.query).toHaveBeenCalled();
      expect(contratacaoService.addContratacaoToCollectionIfMissing).toHaveBeenCalledWith(contratacaoCollection, contratacao);
      expect(comp.contratacaosCollection).toEqual(expectedCollection);
    });

    it('Should call Transportadora query and add missing value', () => {
      const tomadaPreco: ITomadaPreco = { id: 456 };
      const transportadora: ITransportadora = { id: 21107 };
      tomadaPreco.transportadora = transportadora;

      const transportadoraCollection: ITransportadora[] = [{ id: 624 }];
      jest.spyOn(transportadoraService, 'query').mockReturnValue(of(new HttpResponse({ body: transportadoraCollection })));
      const additionalTransportadoras = [transportadora];
      const expectedCollection: ITransportadora[] = [...additionalTransportadoras, ...transportadoraCollection];
      jest.spyOn(transportadoraService, 'addTransportadoraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tomadaPreco });
      comp.ngOnInit();

      expect(transportadoraService.query).toHaveBeenCalled();
      expect(transportadoraService.addTransportadoraToCollectionIfMissing).toHaveBeenCalledWith(
        transportadoraCollection,
        ...additionalTransportadoras.map(expect.objectContaining),
      );
      expect(comp.transportadorasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Roteirizacao query and add missing value', () => {
      const tomadaPreco: ITomadaPreco = { id: 456 };
      const roteirizacao: IRoteirizacao = { id: 19910 };
      tomadaPreco.roteirizacao = roteirizacao;

      const roteirizacaoCollection: IRoteirizacao[] = [{ id: 5791 }];
      jest.spyOn(roteirizacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: roteirizacaoCollection })));
      const additionalRoteirizacaos = [roteirizacao];
      const expectedCollection: IRoteirizacao[] = [...additionalRoteirizacaos, ...roteirizacaoCollection];
      jest.spyOn(roteirizacaoService, 'addRoteirizacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tomadaPreco });
      comp.ngOnInit();

      expect(roteirizacaoService.query).toHaveBeenCalled();
      expect(roteirizacaoService.addRoteirizacaoToCollectionIfMissing).toHaveBeenCalledWith(
        roteirizacaoCollection,
        ...additionalRoteirizacaos.map(expect.objectContaining),
      );
      expect(comp.roteirizacaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tomadaPreco: ITomadaPreco = { id: 456 };
      const contratacao: IContratacao = { id: 26899 };
      tomadaPreco.contratacao = contratacao;
      const transportadora: ITransportadora = { id: 30475 };
      tomadaPreco.transportadora = transportadora;
      const roteirizacao: IRoteirizacao = { id: 32113 };
      tomadaPreco.roteirizacao = roteirizacao;

      activatedRoute.data = of({ tomadaPreco });
      comp.ngOnInit();

      expect(comp.contratacaosCollection).toContain(contratacao);
      expect(comp.transportadorasSharedCollection).toContain(transportadora);
      expect(comp.roteirizacaosSharedCollection).toContain(roteirizacao);
      expect(comp.tomadaPreco).toEqual(tomadaPreco);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITomadaPreco>>();
      const tomadaPreco = { id: 123 };
      jest.spyOn(tomadaPrecoFormService, 'getTomadaPreco').mockReturnValue(tomadaPreco);
      jest.spyOn(tomadaPrecoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tomadaPreco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tomadaPreco }));
      saveSubject.complete();

      // THEN
      expect(tomadaPrecoFormService.getTomadaPreco).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tomadaPrecoService.update).toHaveBeenCalledWith(expect.objectContaining(tomadaPreco));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITomadaPreco>>();
      const tomadaPreco = { id: 123 };
      jest.spyOn(tomadaPrecoFormService, 'getTomadaPreco').mockReturnValue({ id: null });
      jest.spyOn(tomadaPrecoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tomadaPreco: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tomadaPreco }));
      saveSubject.complete();

      // THEN
      expect(tomadaPrecoFormService.getTomadaPreco).toHaveBeenCalled();
      expect(tomadaPrecoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITomadaPreco>>();
      const tomadaPreco = { id: 123 };
      jest.spyOn(tomadaPrecoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tomadaPreco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tomadaPrecoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContratacao', () => {
      it('Should forward to contratacaoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contratacaoService, 'compareContratacao');
        comp.compareContratacao(entity, entity2);
        expect(contratacaoService.compareContratacao).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTransportadora', () => {
      it('Should forward to transportadoraService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transportadoraService, 'compareTransportadora');
        comp.compareTransportadora(entity, entity2);
        expect(transportadoraService.compareTransportadora).toHaveBeenCalledWith(entity, entity2);
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
  });
});
