import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TransportadoraService } from '../service/transportadora.service';
import { ITransportadora } from '../transportadora.model';
import { TransportadoraFormService } from './transportadora-form.service';

import { TransportadoraUpdateComponent } from './transportadora-update.component';

describe('Transportadora Management Update Component', () => {
  let comp: TransportadoraUpdateComponent;
  let fixture: ComponentFixture<TransportadoraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transportadoraFormService: TransportadoraFormService;
  let transportadoraService: TransportadoraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TransportadoraUpdateComponent],
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
      .overrideTemplate(TransportadoraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransportadoraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transportadoraFormService = TestBed.inject(TransportadoraFormService);
    transportadoraService = TestBed.inject(TransportadoraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const transportadora: ITransportadora = { id: 456 };

      activatedRoute.data = of({ transportadora });
      comp.ngOnInit();

      expect(comp.transportadora).toEqual(transportadora);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransportadora>>();
      const transportadora = { id: 123 };
      jest.spyOn(transportadoraFormService, 'getTransportadora').mockReturnValue(transportadora);
      jest.spyOn(transportadoraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transportadora });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transportadora }));
      saveSubject.complete();

      // THEN
      expect(transportadoraFormService.getTransportadora).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transportadoraService.update).toHaveBeenCalledWith(expect.objectContaining(transportadora));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransportadora>>();
      const transportadora = { id: 123 };
      jest.spyOn(transportadoraFormService, 'getTransportadora').mockReturnValue({ id: null });
      jest.spyOn(transportadoraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transportadora: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transportadora }));
      saveSubject.complete();

      // THEN
      expect(transportadoraFormService.getTransportadora).toHaveBeenCalled();
      expect(transportadoraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransportadora>>();
      const transportadora = { id: 123 };
      jest.spyOn(transportadoraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transportadora });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transportadoraService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
