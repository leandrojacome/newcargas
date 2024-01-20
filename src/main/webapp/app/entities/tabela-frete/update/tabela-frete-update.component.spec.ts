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
import { ITipoCarga } from 'app/entities/tipo-carga/tipo-carga.model';
import { TipoCargaService } from 'app/entities/tipo-carga/service/tipo-carga.service';
import { ITipoFrete } from 'app/entities/tipo-frete/tipo-frete.model';
import { TipoFreteService } from 'app/entities/tipo-frete/service/tipo-frete.service';
import { IFormaCobranca } from 'app/entities/forma-cobranca/forma-cobranca.model';
import { FormaCobrancaService } from 'app/entities/forma-cobranca/service/forma-cobranca.service';
import { IRegiao } from 'app/entities/regiao/regiao.model';
import { RegiaoService } from 'app/entities/regiao/service/regiao.service';
import { ITabelaFrete } from '../tabela-frete.model';
import { TabelaFreteService } from '../service/tabela-frete.service';
import { TabelaFreteFormService } from './tabela-frete-form.service';

import { TabelaFreteUpdateComponent } from './tabela-frete-update.component';

describe('TabelaFrete Management Update Component', () => {
  let comp: TabelaFreteUpdateComponent;
  let fixture: ComponentFixture<TabelaFreteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tabelaFreteFormService: TabelaFreteFormService;
  let tabelaFreteService: TabelaFreteService;
  let embarcadorService: EmbarcadorService;
  let transportadoraService: TransportadoraService;
  let tipoCargaService: TipoCargaService;
  let tipoFreteService: TipoFreteService;
  let formaCobrancaService: FormaCobrancaService;
  let regiaoService: RegiaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TabelaFreteUpdateComponent],
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
      .overrideTemplate(TabelaFreteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TabelaFreteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tabelaFreteFormService = TestBed.inject(TabelaFreteFormService);
    tabelaFreteService = TestBed.inject(TabelaFreteService);
    embarcadorService = TestBed.inject(EmbarcadorService);
    transportadoraService = TestBed.inject(TransportadoraService);
    tipoCargaService = TestBed.inject(TipoCargaService);
    tipoFreteService = TestBed.inject(TipoFreteService);
    formaCobrancaService = TestBed.inject(FormaCobrancaService);
    regiaoService = TestBed.inject(RegiaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Embarcador query and add missing value', () => {
      const tabelaFrete: ITabelaFrete = { id: 456 };
      const embarcador: IEmbarcador = { id: 13764 };
      tabelaFrete.embarcador = embarcador;

      const embarcadorCollection: IEmbarcador[] = [{ id: 31464 }];
      jest.spyOn(embarcadorService, 'query').mockReturnValue(of(new HttpResponse({ body: embarcadorCollection })));
      const additionalEmbarcadors = [embarcador];
      const expectedCollection: IEmbarcador[] = [...additionalEmbarcadors, ...embarcadorCollection];
      jest.spyOn(embarcadorService, 'addEmbarcadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tabelaFrete });
      comp.ngOnInit();

      expect(embarcadorService.query).toHaveBeenCalled();
      expect(embarcadorService.addEmbarcadorToCollectionIfMissing).toHaveBeenCalledWith(
        embarcadorCollection,
        ...additionalEmbarcadors.map(expect.objectContaining),
      );
      expect(comp.embarcadorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Transportadora query and add missing value', () => {
      const tabelaFrete: ITabelaFrete = { id: 456 };
      const transportadora: ITransportadora = { id: 7763 };
      tabelaFrete.transportadora = transportadora;

      const transportadoraCollection: ITransportadora[] = [{ id: 4841 }];
      jest.spyOn(transportadoraService, 'query').mockReturnValue(of(new HttpResponse({ body: transportadoraCollection })));
      const additionalTransportadoras = [transportadora];
      const expectedCollection: ITransportadora[] = [...additionalTransportadoras, ...transportadoraCollection];
      jest.spyOn(transportadoraService, 'addTransportadoraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tabelaFrete });
      comp.ngOnInit();

      expect(transportadoraService.query).toHaveBeenCalled();
      expect(transportadoraService.addTransportadoraToCollectionIfMissing).toHaveBeenCalledWith(
        transportadoraCollection,
        ...additionalTransportadoras.map(expect.objectContaining),
      );
      expect(comp.transportadorasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TipoCarga query and add missing value', () => {
      const tabelaFrete: ITabelaFrete = { id: 456 };
      const tipoCarga: ITipoCarga = { id: 20077 };
      tabelaFrete.tipoCarga = tipoCarga;

      const tipoCargaCollection: ITipoCarga[] = [{ id: 18953 }];
      jest.spyOn(tipoCargaService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoCargaCollection })));
      const additionalTipoCargas = [tipoCarga];
      const expectedCollection: ITipoCarga[] = [...additionalTipoCargas, ...tipoCargaCollection];
      jest.spyOn(tipoCargaService, 'addTipoCargaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tabelaFrete });
      comp.ngOnInit();

      expect(tipoCargaService.query).toHaveBeenCalled();
      expect(tipoCargaService.addTipoCargaToCollectionIfMissing).toHaveBeenCalledWith(
        tipoCargaCollection,
        ...additionalTipoCargas.map(expect.objectContaining),
      );
      expect(comp.tipoCargasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TipoFrete query and add missing value', () => {
      const tabelaFrete: ITabelaFrete = { id: 456 };
      const tipoFrete: ITipoFrete = { id: 16435 };
      tabelaFrete.tipoFrete = tipoFrete;

      const tipoFreteCollection: ITipoFrete[] = [{ id: 23330 }];
      jest.spyOn(tipoFreteService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoFreteCollection })));
      const additionalTipoFretes = [tipoFrete];
      const expectedCollection: ITipoFrete[] = [...additionalTipoFretes, ...tipoFreteCollection];
      jest.spyOn(tipoFreteService, 'addTipoFreteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tabelaFrete });
      comp.ngOnInit();

      expect(tipoFreteService.query).toHaveBeenCalled();
      expect(tipoFreteService.addTipoFreteToCollectionIfMissing).toHaveBeenCalledWith(
        tipoFreteCollection,
        ...additionalTipoFretes.map(expect.objectContaining),
      );
      expect(comp.tipoFretesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FormaCobranca query and add missing value', () => {
      const tabelaFrete: ITabelaFrete = { id: 456 };
      const formaCobranca: IFormaCobranca = { id: 23728 };
      tabelaFrete.formaCobranca = formaCobranca;

      const formaCobrancaCollection: IFormaCobranca[] = [{ id: 6448 }];
      jest.spyOn(formaCobrancaService, 'query').mockReturnValue(of(new HttpResponse({ body: formaCobrancaCollection })));
      const additionalFormaCobrancas = [formaCobranca];
      const expectedCollection: IFormaCobranca[] = [...additionalFormaCobrancas, ...formaCobrancaCollection];
      jest.spyOn(formaCobrancaService, 'addFormaCobrancaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tabelaFrete });
      comp.ngOnInit();

      expect(formaCobrancaService.query).toHaveBeenCalled();
      expect(formaCobrancaService.addFormaCobrancaToCollectionIfMissing).toHaveBeenCalledWith(
        formaCobrancaCollection,
        ...additionalFormaCobrancas.map(expect.objectContaining),
      );
      expect(comp.formaCobrancasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Regiao query and add missing value', () => {
      const tabelaFrete: ITabelaFrete = { id: 456 };
      const regiaoOrigem: IRegiao = { id: 29227 };
      tabelaFrete.regiaoOrigem = regiaoOrigem;
      const regiaoDestino: IRegiao = { id: 18163 };
      tabelaFrete.regiaoDestino = regiaoDestino;

      const regiaoCollection: IRegiao[] = [{ id: 18849 }];
      jest.spyOn(regiaoService, 'query').mockReturnValue(of(new HttpResponse({ body: regiaoCollection })));
      const additionalRegiaos = [regiaoOrigem, regiaoDestino];
      const expectedCollection: IRegiao[] = [...additionalRegiaos, ...regiaoCollection];
      jest.spyOn(regiaoService, 'addRegiaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tabelaFrete });
      comp.ngOnInit();

      expect(regiaoService.query).toHaveBeenCalled();
      expect(regiaoService.addRegiaoToCollectionIfMissing).toHaveBeenCalledWith(
        regiaoCollection,
        ...additionalRegiaos.map(expect.objectContaining),
      );
      expect(comp.regiaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tabelaFrete: ITabelaFrete = { id: 456 };
      const embarcador: IEmbarcador = { id: 13384 };
      tabelaFrete.embarcador = embarcador;
      const transportadora: ITransportadora = { id: 11202 };
      tabelaFrete.transportadora = transportadora;
      const tipoCarga: ITipoCarga = { id: 22346 };
      tabelaFrete.tipoCarga = tipoCarga;
      const tipoFrete: ITipoFrete = { id: 18709 };
      tabelaFrete.tipoFrete = tipoFrete;
      const formaCobranca: IFormaCobranca = { id: 26079 };
      tabelaFrete.formaCobranca = formaCobranca;
      const regiaoOrigem: IRegiao = { id: 24056 };
      tabelaFrete.regiaoOrigem = regiaoOrigem;
      const regiaoDestino: IRegiao = { id: 26327 };
      tabelaFrete.regiaoDestino = regiaoDestino;

      activatedRoute.data = of({ tabelaFrete });
      comp.ngOnInit();

      expect(comp.embarcadorsSharedCollection).toContain(embarcador);
      expect(comp.transportadorasSharedCollection).toContain(transportadora);
      expect(comp.tipoCargasSharedCollection).toContain(tipoCarga);
      expect(comp.tipoFretesSharedCollection).toContain(tipoFrete);
      expect(comp.formaCobrancasSharedCollection).toContain(formaCobranca);
      expect(comp.regiaosSharedCollection).toContain(regiaoOrigem);
      expect(comp.regiaosSharedCollection).toContain(regiaoDestino);
      expect(comp.tabelaFrete).toEqual(tabelaFrete);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITabelaFrete>>();
      const tabelaFrete = { id: 123 };
      jest.spyOn(tabelaFreteFormService, 'getTabelaFrete').mockReturnValue(tabelaFrete);
      jest.spyOn(tabelaFreteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tabelaFrete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tabelaFrete }));
      saveSubject.complete();

      // THEN
      expect(tabelaFreteFormService.getTabelaFrete).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tabelaFreteService.update).toHaveBeenCalledWith(expect.objectContaining(tabelaFrete));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITabelaFrete>>();
      const tabelaFrete = { id: 123 };
      jest.spyOn(tabelaFreteFormService, 'getTabelaFrete').mockReturnValue({ id: null });
      jest.spyOn(tabelaFreteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tabelaFrete: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tabelaFrete }));
      saveSubject.complete();

      // THEN
      expect(tabelaFreteFormService.getTabelaFrete).toHaveBeenCalled();
      expect(tabelaFreteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITabelaFrete>>();
      const tabelaFrete = { id: 123 };
      jest.spyOn(tabelaFreteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tabelaFrete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tabelaFreteService.update).toHaveBeenCalled();
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

    describe('compareTipoCarga', () => {
      it('Should forward to tipoCargaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tipoCargaService, 'compareTipoCarga');
        comp.compareTipoCarga(entity, entity2);
        expect(tipoCargaService.compareTipoCarga).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTipoFrete', () => {
      it('Should forward to tipoFreteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tipoFreteService, 'compareTipoFrete');
        comp.compareTipoFrete(entity, entity2);
        expect(tipoFreteService.compareTipoFrete).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareRegiao', () => {
      it('Should forward to regiaoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(regiaoService, 'compareRegiao');
        comp.compareRegiao(entity, entity2);
        expect(regiaoService.compareRegiao).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
