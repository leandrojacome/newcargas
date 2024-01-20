import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FormaCobrancaService } from '../service/forma-cobranca.service';
import { IFormaCobranca } from '../forma-cobranca.model';
import { FormaCobrancaFormService } from './forma-cobranca-form.service';

import { FormaCobrancaUpdateComponent } from './forma-cobranca-update.component';

describe('FormaCobranca Management Update Component', () => {
  let comp: FormaCobrancaUpdateComponent;
  let fixture: ComponentFixture<FormaCobrancaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formaCobrancaFormService: FormaCobrancaFormService;
  let formaCobrancaService: FormaCobrancaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FormaCobrancaUpdateComponent],
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
      .overrideTemplate(FormaCobrancaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormaCobrancaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formaCobrancaFormService = TestBed.inject(FormaCobrancaFormService);
    formaCobrancaService = TestBed.inject(FormaCobrancaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const formaCobranca: IFormaCobranca = { id: 456 };

      activatedRoute.data = of({ formaCobranca });
      comp.ngOnInit();

      expect(comp.formaCobranca).toEqual(formaCobranca);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormaCobranca>>();
      const formaCobranca = { id: 123 };
      jest.spyOn(formaCobrancaFormService, 'getFormaCobranca').mockReturnValue(formaCobranca);
      jest.spyOn(formaCobrancaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formaCobranca });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formaCobranca }));
      saveSubject.complete();

      // THEN
      expect(formaCobrancaFormService.getFormaCobranca).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formaCobrancaService.update).toHaveBeenCalledWith(expect.objectContaining(formaCobranca));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormaCobranca>>();
      const formaCobranca = { id: 123 };
      jest.spyOn(formaCobrancaFormService, 'getFormaCobranca').mockReturnValue({ id: null });
      jest.spyOn(formaCobrancaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formaCobranca: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formaCobranca }));
      saveSubject.complete();

      // THEN
      expect(formaCobrancaFormService.getFormaCobranca).toHaveBeenCalled();
      expect(formaCobrancaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormaCobranca>>();
      const formaCobranca = { id: 123 };
      jest.spyOn(formaCobrancaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formaCobranca });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formaCobrancaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
