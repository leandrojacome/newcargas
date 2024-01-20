import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoCargaService } from '../service/tipo-carga.service';
import { ITipoCarga } from '../tipo-carga.model';
import { TipoCargaFormService } from './tipo-carga-form.service';

import { TipoCargaUpdateComponent } from './tipo-carga-update.component';

describe('TipoCarga Management Update Component', () => {
  let comp: TipoCargaUpdateComponent;
  let fixture: ComponentFixture<TipoCargaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoCargaFormService: TipoCargaFormService;
  let tipoCargaService: TipoCargaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TipoCargaUpdateComponent],
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
      .overrideTemplate(TipoCargaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoCargaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoCargaFormService = TestBed.inject(TipoCargaFormService);
    tipoCargaService = TestBed.inject(TipoCargaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoCarga: ITipoCarga = { id: 456 };

      activatedRoute.data = of({ tipoCarga });
      comp.ngOnInit();

      expect(comp.tipoCarga).toEqual(tipoCarga);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoCarga>>();
      const tipoCarga = { id: 123 };
      jest.spyOn(tipoCargaFormService, 'getTipoCarga').mockReturnValue(tipoCarga);
      jest.spyOn(tipoCargaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoCarga });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoCarga }));
      saveSubject.complete();

      // THEN
      expect(tipoCargaFormService.getTipoCarga).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoCargaService.update).toHaveBeenCalledWith(expect.objectContaining(tipoCarga));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoCarga>>();
      const tipoCarga = { id: 123 };
      jest.spyOn(tipoCargaFormService, 'getTipoCarga').mockReturnValue({ id: null });
      jest.spyOn(tipoCargaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoCarga: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoCarga }));
      saveSubject.complete();

      // THEN
      expect(tipoCargaFormService.getTipoCarga).toHaveBeenCalled();
      expect(tipoCargaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoCarga>>();
      const tipoCarga = { id: 123 };
      jest.spyOn(tipoCargaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoCarga });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoCargaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
