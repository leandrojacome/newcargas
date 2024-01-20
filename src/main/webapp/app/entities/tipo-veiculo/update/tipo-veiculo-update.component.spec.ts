import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TipoVeiculoService } from '../service/tipo-veiculo.service';
import { ITipoVeiculo } from '../tipo-veiculo.model';
import { TipoVeiculoFormService } from './tipo-veiculo-form.service';

import { TipoVeiculoUpdateComponent } from './tipo-veiculo-update.component';

describe('TipoVeiculo Management Update Component', () => {
  let comp: TipoVeiculoUpdateComponent;
  let fixture: ComponentFixture<TipoVeiculoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tipoVeiculoFormService: TipoVeiculoFormService;
  let tipoVeiculoService: TipoVeiculoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TipoVeiculoUpdateComponent],
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
      .overrideTemplate(TipoVeiculoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TipoVeiculoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tipoVeiculoFormService = TestBed.inject(TipoVeiculoFormService);
    tipoVeiculoService = TestBed.inject(TipoVeiculoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tipoVeiculo: ITipoVeiculo = { id: 456 };

      activatedRoute.data = of({ tipoVeiculo });
      comp.ngOnInit();

      expect(comp.tipoVeiculo).toEqual(tipoVeiculo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoVeiculo>>();
      const tipoVeiculo = { id: 123 };
      jest.spyOn(tipoVeiculoFormService, 'getTipoVeiculo').mockReturnValue(tipoVeiculo);
      jest.spyOn(tipoVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoVeiculo }));
      saveSubject.complete();

      // THEN
      expect(tipoVeiculoFormService.getTipoVeiculo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tipoVeiculoService.update).toHaveBeenCalledWith(expect.objectContaining(tipoVeiculo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoVeiculo>>();
      const tipoVeiculo = { id: 123 };
      jest.spyOn(tipoVeiculoFormService, 'getTipoVeiculo').mockReturnValue({ id: null });
      jest.spyOn(tipoVeiculoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoVeiculo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tipoVeiculo }));
      saveSubject.complete();

      // THEN
      expect(tipoVeiculoFormService.getTipoVeiculo).toHaveBeenCalled();
      expect(tipoVeiculoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITipoVeiculo>>();
      const tipoVeiculo = { id: 123 };
      jest.spyOn(tipoVeiculoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tipoVeiculo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tipoVeiculoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
