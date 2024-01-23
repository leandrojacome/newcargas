import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { EmbarcadorService } from 'app/entities/embarcador/service/embarcador.service';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { IContratacao } from 'app/entities/contratacao/contratacao.model';
import { ContratacaoService } from 'app/entities/contratacao/service/contratacao.service';
import { IFormaCobranca } from 'app/entities/forma-cobranca/forma-cobranca.model';
import { FormaCobrancaService } from 'app/entities/forma-cobranca/service/forma-cobranca.service';
import { IFatura } from '../fatura.model';
import { FaturaService } from '../service/fatura.service';
import { FaturaFormService } from './fatura-form.service';

import { FaturaUpdateComponent } from './fatura-update.component';

describe('Fatura Management Update Component', () => {
  let comp: FaturaUpdateComponent;
  let fixture: ComponentFixture<FaturaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let faturaFormService: FaturaFormService;
  let faturaService: FaturaService;
  let embarcadorService: EmbarcadorService;
  let transportadoraService: TransportadoraService;
  let contratacaoService: ContratacaoService;
  let formaCobrancaService: FormaCobrancaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FaturaUpdateComponent],
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
      .overrideTemplate(FaturaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FaturaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    faturaFormService = TestBed.inject(FaturaFormService);
    faturaService = TestBed.inject(FaturaService);
    embarcadorService = TestBed.inject(EmbarcadorService);
    transportadoraService = TestBed.inject(TransportadoraService);
    contratacaoService = TestBed.inject(ContratacaoService);
    formaCobrancaService = TestBed.inject(FormaCobrancaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Embarcador query and add missing value', () => {
      const fatura: IFatura = { id: 456 };
      const embarcador: IEmbarcador = { id: 20068 };
      fatura.embarcador = embarcador;

      const embarcadorCollection: IEmbarcador[] = [{ id: 21565 }];
      jest.spyOn(embarcadorService, 'query').mockReturnValue(of(new HttpResponse({ body: embarcadorCollection })));
      const additionalEmbarcadors = [embarcador];
      const expectedCollection: IEmbarcador[] = [...additionalEmbarcadors, ...embarcadorCollection];
      jest.spyOn(embarcadorService, 'addEmbarcadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fatura });
      comp.ngOnInit();

      expect(embarcadorService.query).toHaveBeenCalled();
      expect(embarcadorService.addEmbarcadorToCollectionIfMissing).toHaveBeenCalledWith(
        embarcadorCollection,
        ...additionalEmbarcadors.map(expect.objectContaining),
      );
      expect(comp.embarcadorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Transportadora query and add missing value', () => {
      const fatura: IFatura = { id: 456 };
      const transportadora: ITransportadora = { id: 30091 };
      fatura.transportadora = transportadora;

      const transportadoraCollection: ITransportadora[] = [{ id: 12944 }];
      jest.spyOn(transportadoraService, 'query').mockReturnValue(of(new HttpResponse({ body: transportadoraCollection })));
      const additionalTransportadoras = [transportadora];
      const expectedCollection: ITransportadora[] = [...additionalTransportadoras, ...transportadoraCollection];
      jest.spyOn(transportadoraService, 'addTransportadoraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fatura });
      comp.ngOnInit();

      expect(transportadoraService.query).toHaveBeenCalled();
      expect(transportadoraService.addTransportadoraToCollectionIfMissing).toHaveBeenCalledWith(
        transportadoraCollection,
        ...additionalTransportadoras.map(expect.objectContaining),
      );
      expect(comp.transportadorasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Contratacao query and add missing value', () => {
      const fatura: IFatura = { id: 456 };
      const contratacao: IContratacao = { id: 2634 };
      fatura.contratacao = contratacao;

      const contratacaoCollection: IContratacao[] = [{ id: 25491 }];
      jest.spyOn(contratacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: contratacaoCollection })));
      const additionalContratacaos = [contratacao];
      const expectedCollection: IContratacao[] = [...additionalContratacaos, ...contratacaoCollection];
      jest.spyOn(contratacaoService, 'addContratacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fatura });
      comp.ngOnInit();

      expect(contratacaoService.query).toHaveBeenCalled();
      expect(contratacaoService.addContratacaoToCollectionIfMissing).toHaveBeenCalledWith(
        contratacaoCollection,
        ...additionalContratacaos.map(expect.objectContaining),
      );
      expect(comp.contratacaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FormaCobranca query and add missing value', () => {
      const fatura: IFatura = { id: 456 };
      const formaCobranca: IFormaCobranca = { id: 32306 };
      fatura.formaCobranca = formaCobranca;

      const formaCobrancaCollection: IFormaCobranca[] = [{ id: 2205 }];
      jest.spyOn(formaCobrancaService, 'query').mockReturnValue(of(new HttpResponse({ body: formaCobrancaCollection })));
      const additionalFormaCobrancas = [formaCobranca];
      const expectedCollection: IFormaCobranca[] = [...additionalFormaCobrancas, ...formaCobrancaCollection];
      jest.spyOn(formaCobrancaService, 'addFormaCobrancaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ fatura });
      comp.ngOnInit();

      expect(formaCobrancaService.query).toHaveBeenCalled();
      expect(formaCobrancaService.addFormaCobrancaToCollectionIfMissing).toHaveBeenCalledWith(
        formaCobrancaCollection,
        ...additionalFormaCobrancas.map(expect.objectContaining),
      );
      expect(comp.formaCobrancasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const fatura: IFatura = { id: 456 };
      const embarcador: IEmbarcador = { id: 16372 };
      fatura.embarcador = embarcador;
      const transportadora: ITransportadora = { id: 21232 };
      fatura.transportadora = transportadora;
      const contratacao: IContratacao = { id: 2693 };
      fatura.contratacao = contratacao;
      const formaCobranca: IFormaCobranca = { id: 23109 };
      fatura.formaCobranca = formaCobranca;

      activatedRoute.data = of({ fatura });
      comp.ngOnInit();

      expect(comp.embarcadorsSharedCollection).toContain(embarcador);
      expect(comp.transportadorasSharedCollection).toContain(transportadora);
      expect(comp.contratacaosSharedCollection).toContain(contratacao);
      expect(comp.formaCobrancasSharedCollection).toContain(formaCobranca);
      expect(comp.fatura).toEqual(fatura);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFatura>>();
      const fatura = { id: 123 };
      jest.spyOn(faturaFormService, 'getFatura').mockReturnValue(fatura);
      jest.spyOn(faturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fatura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fatura }));
      saveSubject.complete();

      // THEN
      expect(faturaFormService.getFatura).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(faturaService.update).toHaveBeenCalledWith(expect.objectContaining(fatura));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFatura>>();
      const fatura = { id: 123 };
      jest.spyOn(faturaFormService, 'getFatura').mockReturnValue({ id: null });
      jest.spyOn(faturaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fatura: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fatura }));
      saveSubject.complete();

      // THEN
      expect(faturaFormService.getFatura).toHaveBeenCalled();
      expect(faturaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFatura>>();
      const fatura = { id: 123 };
      jest.spyOn(faturaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fatura });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(faturaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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

    describe('compareContratacao', () => {
      it('Should forward to contratacaoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contratacaoService, 'compareContratacao');
        comp.compareContratacao(entity, entity2);
        expect(contratacaoService.compareContratacao).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFormaCobranca', () => {
      it('Should forward to formaCobrancaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(formaCobrancaService, 'compareFormaCobranca');
        comp.compareFormaCobranca(entity, entity2);
        expect(formaCobrancaService.compareFormaCobranca).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
