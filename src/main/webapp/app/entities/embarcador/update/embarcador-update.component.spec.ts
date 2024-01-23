import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICidade } from 'app/entities/cidade/cidade.model';
import { CidadeService } from 'app/entities/cidade/service/cidade.service';
import { EmbarcadorService } from '../service/embarcador.service';
import { IEmbarcador } from '../embarcador.model';
import { EmbarcadorFormService } from './embarcador-form.service';

import { EmbarcadorUpdateComponent } from './embarcador-update.component';

describe('Embarcador Management Update Component', () => {
  let comp: EmbarcadorUpdateComponent;
  let fixture: ComponentFixture<EmbarcadorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let embarcadorFormService: EmbarcadorFormService;
  let embarcadorService: EmbarcadorService;
  let cidadeService: CidadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EmbarcadorUpdateComponent],
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
      .overrideTemplate(EmbarcadorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmbarcadorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    embarcadorFormService = TestBed.inject(EmbarcadorFormService);
    embarcadorService = TestBed.inject(EmbarcadorService);
    cidadeService = TestBed.inject(CidadeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cidade query and add missing value', () => {
      const embarcador: IEmbarcador = { id: 456 };
      const cidade: ICidade = { id: 30347 };
      embarcador.cidade = cidade;

      const cidadeCollection: ICidade[] = [{ id: 20813 }];
      jest.spyOn(cidadeService, 'query').mockReturnValue(of(new HttpResponse({ body: cidadeCollection })));
      const additionalCidades = [cidade];
      const expectedCollection: ICidade[] = [...additionalCidades, ...cidadeCollection];
      jest.spyOn(cidadeService, 'addCidadeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ embarcador });
      comp.ngOnInit();

      expect(cidadeService.query).toHaveBeenCalled();
      expect(cidadeService.addCidadeToCollectionIfMissing).toHaveBeenCalledWith(
        cidadeCollection,
        ...additionalCidades.map(expect.objectContaining),
      );
      expect(comp.cidadesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const embarcador: IEmbarcador = { id: 456 };
      const cidade: ICidade = { id: 16984 };
      embarcador.cidade = cidade;

      activatedRoute.data = of({ embarcador });
      comp.ngOnInit();

      expect(comp.cidadesSharedCollection).toContain(cidade);
      expect(comp.embarcador).toEqual(embarcador);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmbarcador>>();
      const embarcador = { id: 123 };
      jest.spyOn(embarcadorFormService, 'getEmbarcador').mockReturnValue(embarcador);
      jest.spyOn(embarcadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ embarcador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: embarcador }));
      saveSubject.complete();

      // THEN
      expect(embarcadorFormService.getEmbarcador).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(embarcadorService.update).toHaveBeenCalledWith(expect.objectContaining(embarcador));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmbarcador>>();
      const embarcador = { id: 123 };
      jest.spyOn(embarcadorFormService, 'getEmbarcador').mockReturnValue({ id: null });
      jest.spyOn(embarcadorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ embarcador: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: embarcador }));
      saveSubject.complete();

      // THEN
      expect(embarcadorFormService.getEmbarcador).toHaveBeenCalled();
      expect(embarcadorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmbarcador>>();
      const embarcador = { id: 123 };
      jest.spyOn(embarcadorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ embarcador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(embarcadorService.update).toHaveBeenCalled();
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
