import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { EmbarcadorService } from 'app/entities/embarcador/service/embarcador.service';
import { IStatusColeta } from 'app/entities/status-coleta/status-coleta.model';
import { StatusColetaService } from 'app/entities/status-coleta/service/status-coleta.service';
import { IRoteirizacao } from 'app/entities/roteirizacao/roteirizacao.model';
import { RoteirizacaoService } from 'app/entities/roteirizacao/service/roteirizacao.service';
import { ITipoVeiculo } from 'app/entities/tipo-veiculo/tipo-veiculo.model';
import { TipoVeiculoService } from 'app/entities/tipo-veiculo/service/tipo-veiculo.service';
import { ISolicitacaoColeta } from '../solicitacao-coleta.model';
import { SolicitacaoColetaService } from '../service/solicitacao-coleta.service';
import { SolicitacaoColetaFormService } from './solicitacao-coleta-form.service';

import { SolicitacaoColetaUpdateComponent } from './solicitacao-coleta-update.component';

describe('SolicitacaoColeta Management Update Component', () => {
  let comp: SolicitacaoColetaUpdateComponent;
  let fixture: ComponentFixture<SolicitacaoColetaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let solicitacaoColetaFormService: SolicitacaoColetaFormService;
  let solicitacaoColetaService: SolicitacaoColetaService;
  let embarcadorService: EmbarcadorService;
  let statusColetaService: StatusColetaService;
  let roteirizacaoService: RoteirizacaoService;
  let tipoVeiculoService: TipoVeiculoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SolicitacaoColetaUpdateComponent],
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
      .overrideTemplate(SolicitacaoColetaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SolicitacaoColetaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    solicitacaoColetaFormService = TestBed.inject(SolicitacaoColetaFormService);
    solicitacaoColetaService = TestBed.inject(SolicitacaoColetaService);
    embarcadorService = TestBed.inject(EmbarcadorService);
    statusColetaService = TestBed.inject(StatusColetaService);
    roteirizacaoService = TestBed.inject(RoteirizacaoService);
    tipoVeiculoService = TestBed.inject(TipoVeiculoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Embarcador query and add missing value', () => {
      const solicitacaoColeta: ISolicitacaoColeta = { id: 456 };
      const embarcador: IEmbarcador = { id: 6349 };
      solicitacaoColeta.embarcador = embarcador;

      const embarcadorCollection: IEmbarcador[] = [{ id: 19118 }];
      jest.spyOn(embarcadorService, 'query').mockReturnValue(of(new HttpResponse({ body: embarcadorCollection })));
      const additionalEmbarcadors = [embarcador];
      const expectedCollection: IEmbarcador[] = [...additionalEmbarcadors, ...embarcadorCollection];
      jest.spyOn(embarcadorService, 'addEmbarcadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ solicitacaoColeta });
      comp.ngOnInit();

      expect(embarcadorService.query).toHaveBeenCalled();
      expect(embarcadorService.addEmbarcadorToCollectionIfMissing).toHaveBeenCalledWith(
        embarcadorCollection,
        ...additionalEmbarcadors.map(expect.objectContaining),
      );
      expect(comp.embarcadorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call StatusColeta query and add missing value', () => {
      const solicitacaoColeta: ISolicitacaoColeta = { id: 456 };
      const statusColeta: IStatusColeta = { id: 1794 };
      solicitacaoColeta.statusColeta = statusColeta;

      const statusColetaCollection: IStatusColeta[] = [{ id: 5002 }];
      jest.spyOn(statusColetaService, 'query').mockReturnValue(of(new HttpResponse({ body: statusColetaCollection })));
      const additionalStatusColetas = [statusColeta];
      const expectedCollection: IStatusColeta[] = [...additionalStatusColetas, ...statusColetaCollection];
      jest.spyOn(statusColetaService, 'addStatusColetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ solicitacaoColeta });
      comp.ngOnInit();

      expect(statusColetaService.query).toHaveBeenCalled();
      expect(statusColetaService.addStatusColetaToCollectionIfMissing).toHaveBeenCalledWith(
        statusColetaCollection,
        ...additionalStatusColetas.map(expect.objectContaining),
      );
      expect(comp.statusColetasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Roteirizacao query and add missing value', () => {
      const solicitacaoColeta: ISolicitacaoColeta = { id: 456 };
      const roteirizacao: IRoteirizacao = { id: 28869 };
      solicitacaoColeta.roteirizacao = roteirizacao;

      const roteirizacaoCollection: IRoteirizacao[] = [{ id: 28176 }];
      jest.spyOn(roteirizacaoService, 'query').mockReturnValue(of(new HttpResponse({ body: roteirizacaoCollection })));
      const additionalRoteirizacaos = [roteirizacao];
      const expectedCollection: IRoteirizacao[] = [...additionalRoteirizacaos, ...roteirizacaoCollection];
      jest.spyOn(roteirizacaoService, 'addRoteirizacaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ solicitacaoColeta });
      comp.ngOnInit();

      expect(roteirizacaoService.query).toHaveBeenCalled();
      expect(roteirizacaoService.addRoteirizacaoToCollectionIfMissing).toHaveBeenCalledWith(
        roteirizacaoCollection,
        ...additionalRoteirizacaos.map(expect.objectContaining),
      );
      expect(comp.roteirizacaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TipoVeiculo query and add missing value', () => {
      const solicitacaoColeta: ISolicitacaoColeta = { id: 456 };
      const tipoVeiculo: ITipoVeiculo = { id: 26832 };
      solicitacaoColeta.tipoVeiculo = tipoVeiculo;

      const tipoVeiculoCollection: ITipoVeiculo[] = [{ id: 16737 }];
      jest.spyOn(tipoVeiculoService, 'query').mockReturnValue(of(new HttpResponse({ body: tipoVeiculoCollection })));
      const additionalTipoVeiculos = [tipoVeiculo];
      const expectedCollection: ITipoVeiculo[] = [...additionalTipoVeiculos, ...tipoVeiculoCollection];
      jest.spyOn(tipoVeiculoService, 'addTipoVeiculoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ solicitacaoColeta });
      comp.ngOnInit();

      expect(tipoVeiculoService.query).toHaveBeenCalled();
      expect(tipoVeiculoService.addTipoVeiculoToCollectionIfMissing).toHaveBeenCalledWith(
        tipoVeiculoCollection,
        ...additionalTipoVeiculos.map(expect.objectContaining),
      );
      expect(comp.tipoVeiculosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const solicitacaoColeta: ISolicitacaoColeta = { id: 456 };
      const embarcador: IEmbarcador = { id: 31347 };
      solicitacaoColeta.embarcador = embarcador;
      const statusColeta: IStatusColeta = { id: 19352 };
      solicitacaoColeta.statusColeta = statusColeta;
      const roteirizacao: IRoteirizacao = { id: 1001 };
      solicitacaoColeta.roteirizacao = roteirizacao;
      const tipoVeiculo: ITipoVeiculo = { id: 32538 };
      solicitacaoColeta.tipoVeiculo = tipoVeiculo;

      activatedRoute.data = of({ solicitacaoColeta });
      comp.ngOnInit();

      expect(comp.embarcadorsSharedCollection).toContain(embarcador);
      expect(comp.statusColetasSharedCollection).toContain(statusColeta);
      expect(comp.roteirizacaosSharedCollection).toContain(roteirizacao);
      expect(comp.tipoVeiculosSharedCollection).toContain(tipoVeiculo);
      expect(comp.solicitacaoColeta).toEqual(solicitacaoColeta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISolicitacaoColeta>>();
      const solicitacaoColeta = { id: 123 };
      jest.spyOn(solicitacaoColetaFormService, 'getSolicitacaoColeta').mockReturnValue(solicitacaoColeta);
      jest.spyOn(solicitacaoColetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ solicitacaoColeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: solicitacaoColeta }));
      saveSubject.complete();

      // THEN
      expect(solicitacaoColetaFormService.getSolicitacaoColeta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(solicitacaoColetaService.update).toHaveBeenCalledWith(expect.objectContaining(solicitacaoColeta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISolicitacaoColeta>>();
      const solicitacaoColeta = { id: 123 };
      jest.spyOn(solicitacaoColetaFormService, 'getSolicitacaoColeta').mockReturnValue({ id: null });
      jest.spyOn(solicitacaoColetaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ solicitacaoColeta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: solicitacaoColeta }));
      saveSubject.complete();

      // THEN
      expect(solicitacaoColetaFormService.getSolicitacaoColeta).toHaveBeenCalled();
      expect(solicitacaoColetaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISolicitacaoColeta>>();
      const solicitacaoColeta = { id: 123 };
      jest.spyOn(solicitacaoColetaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ solicitacaoColeta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(solicitacaoColetaService.update).toHaveBeenCalled();
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

    describe('compareStatusColeta', () => {
      it('Should forward to statusColetaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(statusColetaService, 'compareStatusColeta');
        comp.compareStatusColeta(entity, entity2);
        expect(statusColetaService.compareStatusColeta).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareTipoVeiculo', () => {
      it('Should forward to tipoVeiculoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tipoVeiculoService, 'compareTipoVeiculo');
        comp.compareTipoVeiculo(entity, entity2);
        expect(tipoVeiculoService.compareTipoVeiculo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
