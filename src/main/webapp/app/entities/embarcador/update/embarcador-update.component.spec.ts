import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const embarcador: IEmbarcador = { id: 456 };

      activatedRoute.data = of({ embarcador });
      comp.ngOnInit();

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
});
