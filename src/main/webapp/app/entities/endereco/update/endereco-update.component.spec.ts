import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { from, of, Subject } from 'rxjs';

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
      const cidade: ICidade = { id: 26319 };
      endereco.cidade = cidade;

      const cidadeCollection: ICidade[] = [{ id: 12484 }];
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

    it('Should update editForm', () => {
      const endereco: IEndereco = { id: 456 };
      const cidade: ICidade = { id: 15056 };
      endereco.cidade = cidade;
      const embarcador: IEmbarcador = { id: 13384 };
      endereco.embarcador = embarcador;
      const transportadora: ITransportadora = { id: 14813 };
      endereco.transportadora = transportadora;
      const notaFiscalColetaOrigem: INotaFiscalColeta = { id: 22354 };
      endereco.notaFiscalColetaOrigem = notaFiscalColetaOrigem;
      const notaFiscalColetaDestino: INotaFiscalColeta = { id: 20476 };
      endereco.notaFiscalColetaDestino = notaFiscalColetaDestino;
      const solicitacaoColetaOrigem: ISolicitacaoColeta = { id: 7268 };
      endereco.solicitacaoColetaOrigem = solicitacaoColetaOrigem;
      const solicitacaoColetaDestino: ISolicitacaoColeta = { id: 24717 };
      endereco.solicitacaoColetaDestino = solicitacaoColetaDestino;

      activatedRoute.data = of({ endereco });
      comp.ngOnInit();

      expect(comp.cidadesSharedCollection).toContain(cidade);

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
  });
});
