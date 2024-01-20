import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICidade } from 'app/entities/cidade/cidade.model';
import { CidadeService } from 'app/entities/cidade/service/cidade.service';
import { IEmbarcador } from 'app/entities/embarcador/embarcador.model';
import { EmbarcadorService } from 'app/entities/embarcador/service/embarcador.service';
import { ITransportadora } from 'app/entities/transportadora/transportadora.model';
import { TransportadoraService } from 'app/entities/transportadora/service/transportadora.service';
import { INotaFiscalColeta } from 'app/entities/nota-fiscal-coleta/nota-fiscal-coleta.model';
import { NotaFiscalColetaService } from 'app/entities/nota-fiscal-coleta/service/nota-fiscal-coleta.service';
import { ISolicitacaoColeta } from 'app/entities/solicitacao-coleta/solicitacao-coleta.model';
import { SolicitacaoColetaService } from 'app/entities/solicitacao-coleta/service/solicitacao-coleta.service';
import { IEndereco } from '../endereco.model';
import { EnderecoService } from '../service/endereco.service';
import { EnderecoFormService } from './endereco-form.service';

import { EnderecoUpdateComponent } from './endereco-update.component';

describe('Endereco Management Update Component', () => {
  let comp: EnderecoUpdateComponent;
  let fixture: ComponentFixture<EnderecoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enderecoFormService: EnderecoFormService;
  let enderecoService: EnderecoService;
  let cidadeService: CidadeService;
  let embarcadorService: EmbarcadorService;
  let transportadoraService: TransportadoraService;
  let notaFiscalColetaService: NotaFiscalColetaService;
  let solicitacaoColetaService: SolicitacaoColetaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EnderecoUpdateComponent],
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
      .overrideTemplate(EnderecoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnderecoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enderecoFormService = TestBed.inject(EnderecoFormService);
    enderecoService = TestBed.inject(EnderecoService);
    cidadeService = TestBed.inject(CidadeService);
    embarcadorService = TestBed.inject(EmbarcadorService);
    transportadoraService = TestBed.inject(TransportadoraService);
    notaFiscalColetaService = TestBed.inject(NotaFiscalColetaService);
    solicitacaoColetaService = TestBed.inject(SolicitacaoColetaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cidade query and add missing value', () => {
      const endereco: IEndereco = { id: 456 };
      const cidade: ICidade = { id: 14890 };
      endereco.cidade = cidade;

      const cidadeCollection: ICidade[] = [{ id: 2138 }];
      jest.spyOn(cidadeService, 'query').mockReturnValue(of(new HttpResponse({ body: cidadeCollection })));
      const additionalCidades = [cidade];
      const expectedCollection: ICidade[] = [...additionalCidades, ...cidadeCollection];
      jest.spyOn(cidadeService, 'addCidadeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      expect(cidadeService.query).toHaveBeenCalled();
      expect(cidadeService.addCidadeToCollectionIfMissing).toHaveBeenCalledWith(
        cidadeCollection,
        ...additionalCidades.map(expect.objectContaining),
      );
      expect(comp.cidadesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Embarcador query and add missing value', () => {
      const endereco: IEndereco = { id: 456 };
      const embarcador: IEmbarcador = { id: 22281 };
      endereco.embarcador = embarcador;

      const embarcadorCollection: IEmbarcador[] = [{ id: 25835 }];
      jest.spyOn(embarcadorService, 'query').mockReturnValue(of(new HttpResponse({ body: embarcadorCollection })));
      const additionalEmbarcadors = [embarcador];
      const expectedCollection: IEmbarcador[] = [...additionalEmbarcadors, ...embarcadorCollection];
      jest.spyOn(embarcadorService, 'addEmbarcadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      expect(embarcadorService.query).toHaveBeenCalled();
      expect(embarcadorService.addEmbarcadorToCollectionIfMissing).toHaveBeenCalledWith(
        embarcadorCollection,
        ...additionalEmbarcadors.map(expect.objectContaining),
      );
      expect(comp.embarcadorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Transportadora query and add missing value', () => {
      const endereco: IEndereco = { id: 456 };
      const transportadora: ITransportadora = { id: 442 };
      endereco.transportadora = transportadora;

      const transportadoraCollection: ITransportadora[] = [{ id: 24684 }];
      jest.spyOn(transportadoraService, 'query').mockReturnValue(of(new HttpResponse({ body: transportadoraCollection })));
      const additionalTransportadoras = [transportadora];
      const expectedCollection: ITransportadora[] = [...additionalTransportadoras, ...transportadoraCollection];
      jest.spyOn(transportadoraService, 'addTransportadoraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      expect(transportadoraService.query).toHaveBeenCalled();
      expect(transportadoraService.addTransportadoraToCollectionIfMissing).toHaveBeenCalledWith(
        transportadoraCollection,
        ...additionalTransportadoras.map(expect.objectContaining),
      );
      expect(comp.transportadorasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call NotaFiscalColeta query and add missing value', () => {
      const endereco: IEndereco = { id: 456 };
      const notaFiscalColetaOrigem: INotaFiscalColeta = { id: 15567 };
      endereco.notaFiscalColetaOrigem = notaFiscalColetaOrigem;
      const notaFiscalColetaDestino: INotaFiscalColeta = { id: 22354 };
      endereco.notaFiscalColetaDestino = notaFiscalColetaDestino;

      const notaFiscalColetaCollection: INotaFiscalColeta[] = [{ id: 20476 }];
      jest.spyOn(notaFiscalColetaService, 'query').mockReturnValue(of(new HttpResponse({ body: notaFiscalColetaCollection })));
      const additionalNotaFiscalColetas = [notaFiscalColetaOrigem, notaFiscalColetaDestino];
      const expectedCollection: INotaFiscalColeta[] = [...additionalNotaFiscalColetas, ...notaFiscalColetaCollection];
      jest.spyOn(notaFiscalColetaService, 'addNotaFiscalColetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      expect(notaFiscalColetaService.query).toHaveBeenCalled();
      expect(notaFiscalColetaService.addNotaFiscalColetaToCollectionIfMissing).toHaveBeenCalledWith(
        notaFiscalColetaCollection,
        ...additionalNotaFiscalColetas.map(expect.objectContaining),
      );
      expect(comp.notaFiscalColetasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SolicitacaoColeta query and add missing value', () => {
      const endereco: IEndereco = { id: 456 };
      const solicitacaoColetaOrigem: ISolicitacaoColeta = { id: 4690 };
      endereco.solicitacaoColetaOrigem = solicitacaoColetaOrigem;
      const solicitacaoColetaDestino: ISolicitacaoColeta = { id: 29353 };
      endereco.solicitacaoColetaDestino = solicitacaoColetaDestino;

      const solicitacaoColetaCollection: ISolicitacaoColeta[] = [{ id: 3291 }];
      jest.spyOn(solicitacaoColetaService, 'query').mockReturnValue(of(new HttpResponse({ body: solicitacaoColetaCollection })));
      const additionalSolicitacaoColetas = [solicitacaoColetaOrigem, solicitacaoColetaDestino];
      const expectedCollection: ISolicitacaoColeta[] = [...additionalSolicitacaoColetas, ...solicitacaoColetaCollection];
      jest.spyOn(solicitacaoColetaService, 'addSolicitacaoColetaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      expect(solicitacaoColetaService.query).toHaveBeenCalled();
      expect(solicitacaoColetaService.addSolicitacaoColetaToCollectionIfMissing).toHaveBeenCalledWith(
        solicitacaoColetaCollection,
        ...additionalSolicitacaoColetas.map(expect.objectContaining),
      );
      expect(comp.solicitacaoColetasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const endereco: IEndereco = { id: 456 };
      const cidade: ICidade = { id: 11619 };
      endereco.cidade = cidade;
      const embarcador: IEmbarcador = { id: 7180 };
      endereco.embarcador = embarcador;
      const transportadora: ITransportadora = { id: 28466 };
      endereco.transportadora = transportadora;
      const notaFiscalColetaOrigem: INotaFiscalColeta = { id: 16678 };
      endereco.notaFiscalColetaOrigem = notaFiscalColetaOrigem;
      const notaFiscalColetaDestino: INotaFiscalColeta = { id: 4456 };
      endereco.notaFiscalColetaDestino = notaFiscalColetaDestino;
      const solicitacaoColetaOrigem: ISolicitacaoColeta = { id: 7802 };
      endereco.solicitacaoColetaOrigem = solicitacaoColetaOrigem;
      const solicitacaoColetaDestino: ISolicitacaoColeta = { id: 11264 };
      endereco.solicitacaoColetaDestino = solicitacaoColetaDestino;

      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      expect(comp.cidadesSharedCollection).toContain(cidade);
      expect(comp.embarcadorsSharedCollection).toContain(embarcador);
      expect(comp.transportadorasSharedCollection).toContain(transportadora);
      expect(comp.notaFiscalColetasSharedCollection).toContain(notaFiscalColetaOrigem);
      expect(comp.notaFiscalColetasSharedCollection).toContain(notaFiscalColetaDestino);
      expect(comp.solicitacaoColetasSharedCollection).toContain(solicitacaoColetaOrigem);
      expect(comp.solicitacaoColetasSharedCollection).toContain(solicitacaoColetaDestino);
      expect(comp.endereco).toEqual(endereco);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEndereco>>();
      const endereco = { id: 123 };
      jest.spyOn(enderecoFormService, 'getEndereco').mockReturnValue(endereco);
      jest.spyOn(enderecoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: endereco }));
      saveSubject.complete();

      // THEN
      expect(enderecoFormService.getEndereco).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enderecoService.update).toHaveBeenCalledWith(expect.objectContaining(endereco));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEndereco>>();
      const endereco = { id: 123 };
      jest.spyOn(enderecoFormService, 'getEndereco').mockReturnValue({ id: null });
      jest.spyOn(enderecoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ endereco: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: endereco }));
      saveSubject.complete();

      // THEN
      expect(enderecoFormService.getEndereco).toHaveBeenCalled();
      expect(enderecoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEndereco>>();
      const endereco = { id: 123 };
      jest.spyOn(enderecoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enderecoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCidade', () => {
      it('Should forward to cidadeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cidadeService, 'compareCidade');
        comp.compareCidade(entity, entity2);
        expect(cidadeService.compareCidade).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareNotaFiscalColeta', () => {
      it('Should forward to notaFiscalColetaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(notaFiscalColetaService, 'compareNotaFiscalColeta');
        comp.compareNotaFiscalColeta(entity, entity2);
        expect(notaFiscalColetaService.compareNotaFiscalColeta).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSolicitacaoColeta', () => {
      it('Should forward to solicitacaoColetaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(solicitacaoColetaService, 'compareSolicitacaoColeta');
        comp.compareSolicitacaoColeta(entity, entity2);
        expect(solicitacaoColetaService.compareSolicitacaoColeta).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
