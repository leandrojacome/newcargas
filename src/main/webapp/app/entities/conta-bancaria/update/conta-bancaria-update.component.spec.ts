import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IBanco } from 'app/entities/banco/banco.model';
import { BancoService } from 'app/entities/banco/service/banco.service';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { EmbarcadorService } from 'app/entities/embarcador/service/embarcador.service';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { IContaBancaria } from '../conta-bancaria.model';
import { ContaBancariaService } from '../service/conta-bancaria.service';
import { ContaBancariaFormService } from './conta-bancaria-form.service';

import { ContaBancariaUpdateComponent } from './conta-bancaria-update.component';

describe('ContaBancaria Management Update Component', () => {
  let comp: ContaBancariaUpdateComponent;
  let fixture: ComponentFixture<ContaBancariaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contaBancariaFormService: ContaBancariaFormService;
  let contaBancariaService: ContaBancariaService;
  let bancoService: BancoService;
  let embarcadorService: EmbarcadorService;
  let transportadoraService: TransportadoraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ContaBancariaUpdateComponent],
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
      .overrideTemplate(ContaBancariaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContaBancariaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contaBancariaFormService = TestBed.inject(ContaBancariaFormService);
    contaBancariaService = TestBed.inject(ContaBancariaService);
    bancoService = TestBed.inject(BancoService);
    embarcadorService = TestBed.inject(EmbarcadorService);
    transportadoraService = TestBed.inject(TransportadoraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Banco query and add missing value', () => {
      const contaBancaria: IContaBancaria = { id: 456 };
      const banco: IBanco = { id: 29875 };
      contaBancaria.banco = banco;

      const bancoCollection: IBanco[] = [{ id: 25709 }];
      jest.spyOn(bancoService, 'query').mockReturnValue(of(new HttpResponse({ body: bancoCollection })));
      const additionalBancos = [banco];
      const expectedCollection: IBanco[] = [...additionalBancos, ...bancoCollection];
      jest.spyOn(bancoService, 'addBancoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contaBancaria });
      comp.ngOnInit();

      expect(bancoService.query).toHaveBeenCalled();
      expect(bancoService.addBancoToCollectionIfMissing).toHaveBeenCalledWith(
        bancoCollection,
        ...additionalBancos.map(expect.objectContaining),
      );
      expect(comp.bancosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Embarcador query and add missing value', () => {
      const contaBancaria: IContaBancaria = { id: 456 };
      const embarcador: IEmbarcador = { id: 8433 };
      contaBancaria.embarcador = embarcador;

      const embarcadorCollection: IEmbarcador[] = [{ id: 3233 }];
      jest.spyOn(embarcadorService, 'query').mockReturnValue(of(new HttpResponse({ body: embarcadorCollection })));
      const additionalEmbarcadors = [embarcador];
      const expectedCollection: IEmbarcador[] = [...additionalEmbarcadors, ...embarcadorCollection];
      jest.spyOn(embarcadorService, 'addEmbarcadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contaBancaria });
      comp.ngOnInit();

      expect(embarcadorService.query).toHaveBeenCalled();
      expect(embarcadorService.addEmbarcadorToCollectionIfMissing).toHaveBeenCalledWith(
        embarcadorCollection,
        ...additionalEmbarcadors.map(expect.objectContaining),
      );
      expect(comp.embarcadorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Transportadora query and add missing value', () => {
      const contaBancaria: IContaBancaria = { id: 456 };
      const transportadora: ITransportadora = { id: 11718 };
      contaBancaria.transportadora = transportadora;

      const transportadoraCollection: ITransportadora[] = [{ id: 24869 }];
      jest.spyOn(transportadoraService, 'query').mockReturnValue(of(new HttpResponse({ body: transportadoraCollection })));
      const additionalTransportadoras = [transportadora];
      const expectedCollection: ITransportadora[] = [...additionalTransportadoras, ...transportadoraCollection];
      jest.spyOn(transportadoraService, 'addTransportadoraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contaBancaria });
      comp.ngOnInit();

      expect(transportadoraService.query).toHaveBeenCalled();
      expect(transportadoraService.addTransportadoraToCollectionIfMissing).toHaveBeenCalledWith(
        transportadoraCollection,
        ...additionalTransportadoras.map(expect.objectContaining),
      );
      expect(comp.transportadorasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contaBancaria: IContaBancaria = { id: 456 };
      const banco: IBanco = { id: 483 };
      contaBancaria.banco = banco;
      const embarcador: IEmbarcador = { id: 7551 };
      contaBancaria.embarcador = embarcador;
      const transportadora: ITransportadora = { id: 32524 };
      contaBancaria.transportadora = transportadora;

      activatedRoute.data = of({ contaBancaria });
      comp.ngOnInit();

      expect(comp.bancosSharedCollection).toContain(banco);
      expect(comp.embarcadorsSharedCollection).toContain(embarcador);
      expect(comp.transportadorasSharedCollection).toContain(transportadora);
      expect(comp.contaBancaria).toEqual(contaBancaria);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContaBancaria>>();
      const contaBancaria = { id: 123 };
      jest.spyOn(contaBancariaFormService, 'getContaBancaria').mockReturnValue(contaBancaria);
      jest.spyOn(contaBancariaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contaBancaria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contaBancaria }));
      saveSubject.complete();

      // THEN
      expect(contaBancariaFormService.getContaBancaria).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contaBancariaService.update).toHaveBeenCalledWith(expect.objectContaining(contaBancaria));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContaBancaria>>();
      const contaBancaria = { id: 123 };
      jest.spyOn(contaBancariaFormService, 'getContaBancaria').mockReturnValue({ id: null });
      jest.spyOn(contaBancariaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contaBancaria: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contaBancaria }));
      saveSubject.complete();

      // THEN
      expect(contaBancariaFormService.getContaBancaria).toHaveBeenCalled();
      expect(contaBancariaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContaBancaria>>();
      const contaBancaria = { id: 123 };
      jest.spyOn(contaBancariaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contaBancaria });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contaBancariaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBanco', () => {
      it('Should forward to bancoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(bancoService, 'compareBanco');
        comp.compareBanco(entity, entity2);
        expect(bancoService.compareBanco).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmbarcador', () => {
      it('Should forward to embarcadorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(embarcadorService, 'compareEmbarcador');
        comp.compareEmbarcador(entity, entity2);
        expect(embarcadorService.compareEmbarcador).toHaveBeenCalledWith(entity, entity2);
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
  });
});
