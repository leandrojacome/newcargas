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
import { INotificacao } from '../notificacao.model';
import { NotificacaoService } from '../service/notificacao.service';
import { NotificacaoFormService } from './notificacao-form.service';

import { NotificacaoUpdateComponent } from './notificacao-update.component';

describe('Notificacao Management Update Component', () => {
  let comp: NotificacaoUpdateComponent;
  let fixture: ComponentFixture<NotificacaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificacaoFormService: NotificacaoFormService;
  let notificacaoService: NotificacaoService;
  let embarcadorService: EmbarcadorService;
  let transportadoraService: TransportadoraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), NotificacaoUpdateComponent],
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
      .overrideTemplate(NotificacaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificacaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificacaoFormService = TestBed.inject(NotificacaoFormService);
    notificacaoService = TestBed.inject(NotificacaoService);
    embarcadorService = TestBed.inject(EmbarcadorService);
    transportadoraService = TestBed.inject(TransportadoraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Embarcador query and add missing value', () => {
      const notificacao: INotificacao = { id: 456 };
      const embarcador: IEmbarcador = { id: 20068 };
      notificacao.embarcador = embarcador;

      const embarcadorCollection: IEmbarcador[] = [{ id: 21565 }];
      jest.spyOn(embarcadorService, 'query').mockReturnValue(of(new HttpResponse({ body: embarcadorCollection })));
      const additionalEmbarcadors = [embarcador];
      const expectedCollection: IEmbarcador[] = [...additionalEmbarcadors, ...embarcadorCollection];
      jest.spyOn(embarcadorService, 'addEmbarcadorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notificacao });
      comp.ngOnInit();

      expect(embarcadorService.query).toHaveBeenCalled();
      expect(embarcadorService.addEmbarcadorToCollectionIfMissing).toHaveBeenCalledWith(
        embarcadorCollection,
        ...additionalEmbarcadors.map(expect.objectContaining),
      );
      expect(comp.embarcadorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Transportadora query and add missing value', () => {
      const notificacao: INotificacao = { id: 456 };
      const transportadora: ITransportadora = { id: 20678 };
      notificacao.transportadora = transportadora;

      const transportadoraCollection: ITransportadora[] = [{ id: 21068 }];
      jest.spyOn(transportadoraService, 'query').mockReturnValue(of(new HttpResponse({ body: transportadoraCollection })));
      const additionalTransportadoras = [transportadora];
      const expectedCollection: ITransportadora[] = [...additionalTransportadoras, ...transportadoraCollection];
      jest.spyOn(transportadoraService, 'addTransportadoraToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ notificacao });
      comp.ngOnInit();

      expect(transportadoraService.query).toHaveBeenCalled();
      expect(transportadoraService.addTransportadoraToCollectionIfMissing).toHaveBeenCalledWith(
        transportadoraCollection,
        ...additionalTransportadoras.map(expect.objectContaining),
      );
      expect(comp.transportadorasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const notificacao: INotificacao = { id: 456 };
      const embarcador: IEmbarcador = { id: 16372 };
      notificacao.embarcador = embarcador;
      const transportadora: ITransportadora = { id: 26386 };
      notificacao.transportadora = transportadora;

      activatedRoute.data = of({ notificacao });
      comp.ngOnInit();

      expect(comp.embarcadorsSharedCollection).toContain(embarcador);
      expect(comp.transportadorasSharedCollection).toContain(transportadora);
      expect(comp.notificacao).toEqual(notificacao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificacao>>();
      const notificacao = { id: 123 };
      jest.spyOn(notificacaoFormService, 'getNotificacao').mockReturnValue(notificacao);
      jest.spyOn(notificacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificacao }));
      saveSubject.complete();

      // THEN
      expect(notificacaoFormService.getNotificacao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificacaoService.update).toHaveBeenCalledWith(expect.objectContaining(notificacao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificacao>>();
      const notificacao = { id: 123 };
      jest.spyOn(notificacaoFormService, 'getNotificacao').mockReturnValue({ id: null });
      jest.spyOn(notificacaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificacao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificacao }));
      saveSubject.complete();

      // THEN
      expect(notificacaoFormService.getNotificacao).toHaveBeenCalled();
      expect(notificacaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<INotificacao>>();
      const notificacao = { id: 123 };
      jest.spyOn(notificacaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificacao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificacaoService.update).toHaveBeenCalled();
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
  });
});
