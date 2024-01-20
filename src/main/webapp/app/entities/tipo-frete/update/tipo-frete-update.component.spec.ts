import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoFreteService } from '../service/tipo-frete.service';
import { ITipoFrete } from '../tipo-frete.model';
import { TipoFreteFormService } from './tipo-frete-form.service';

import { TipoFreteUpdateComponent } from './tipo-frete-update.component';

describe('TipoFrete Management Update Component', () => {
  let comp: TipoFreteUpdateComponent;
  let fixture: ComponentFixture<TipoFreteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoFreteFormService: TipoFreteFormService;
  let tipoFreteService: TipoFreteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TipoFreteUpdateComponent],
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
      .overrideTemplate(TipoFreteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoFreteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoFreteFormService = TestBed.inject(TipoFreteFormService);
    tipoFreteService = TestBed.inject(TipoFreteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoFrete: ITipoFrete = { id: 456 };

      activatedRoute.data = of({ tipoFrete });
      comp.ngOnInit();

      expect(comp.tipoFrete).toEqual(tipoFrete);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoFrete>>();
      const tipoFrete = { id: 123 };
      jest.spyOn(tipoFreteFormService, 'getTipoFrete').mockReturnValue(tipoFrete);
      jest.spyOn(tipoFreteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoFrete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoFrete }));
      saveSubject.complete();

      // THEN
      expect(tipoFreteFormService.getTipoFrete).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoFreteService.update).toHaveBeenCalledWith(expect.objectContaining(tipoFrete));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoFrete>>();
      const tipoFrete = { id: 123 };
      jest.spyOn(tipoFreteFormService, 'getTipoFrete').mockReturnValue({ id: null });
      jest.spyOn(tipoFreteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoFrete: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoFrete }));
      saveSubject.complete();

      // THEN
      expect(tipoFreteFormService.getTipoFrete).toHaveBeenCalled();
      expect(tipoFreteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoFrete>>();
      const tipoFrete = { id: 123 };
      jest.spyOn(tipoFreteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoFrete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoFreteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
