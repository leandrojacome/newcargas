import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EstadoService } from '../service/estado.service';
import { IEstado } from '../estado.model';
import { EstadoFormService } from './estado-form.service';

import { EstadoUpdateComponent } from './estado-update.component';

describe('Estado Management Update Component', () => {
  let comp: EstadoUpdateComponent;
  let fixture: ComponentFixture<EstadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let estadoFormService: EstadoFormService;
  let estadoService: EstadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EstadoUpdateComponent],
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
      .overrideTemplate(EstadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EstadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    estadoFormService = TestBed.inject(EstadoFormService);
    estadoService = TestBed.inject(EstadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const estado: IEstado = { id: 456 };

      activatedRoute.data = of({ estado });
      comp.ngOnInit();

      expect(comp.estado).toEqual(estado);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstado>>();
      const estado = { id: 123 };
      jest.spyOn(estadoFormService, 'getEstado').mockReturnValue(estado);
      jest.spyOn(estadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ estado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: estado }));
      saveSubject.complete();

      // THEN
      expect(estadoFormService.getEstado).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(estadoService.update).toHaveBeenCalledWith(expect.objectContaining(estado));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstado>>();
      const estado = { id: 123 };
      jest.spyOn(estadoFormService, 'getEstado').mockReturnValue({ id: null });
      jest.spyOn(estadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ estado: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: estado }));
      saveSubject.complete();

      // THEN
      expect(estadoFormService.getEstado).toHaveBeenCalled();
      expect(estadoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEstado>>();
      const estado = { id: 123 };
      jest.spyOn(estadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ estado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(estadoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
