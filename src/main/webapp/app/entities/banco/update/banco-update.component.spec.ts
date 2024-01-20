import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BancoService } from '../service/banco.service';
import { IBanco } from '../banco.model';
import { BancoFormService } from './banco-form.service';

import { BancoUpdateComponent } from './banco-update.component';

describe('Banco Management Update Component', () => {
  let comp: BancoUpdateComponent;
  let fixture: ComponentFixture<BancoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bancoFormService: BancoFormService;
  let bancoService: BancoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BancoUpdateComponent],
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
      .overrideTemplate(BancoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BancoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bancoFormService = TestBed.inject(BancoFormService);
    bancoService = TestBed.inject(BancoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const banco: IBanco = { id: 456 };

      activatedRoute.data = of({ banco });
      comp.ngOnInit();

      expect(comp.banco).toEqual(banco);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBanco>>();
      const banco = { id: 123 };
      jest.spyOn(bancoFormService, 'getBanco').mockReturnValue(banco);
      jest.spyOn(bancoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ banco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: banco }));
      saveSubject.complete();

      // THEN
      expect(bancoFormService.getBanco).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bancoService.update).toHaveBeenCalledWith(expect.objectContaining(banco));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBanco>>();
      const banco = { id: 123 };
      jest.spyOn(bancoFormService, 'getBanco').mockReturnValue({ id: null });
      jest.spyOn(bancoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ banco: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: banco }));
      saveSubject.complete();

      // THEN
      expect(bancoFormService.getBanco).toHaveBeenCalled();
      expect(bancoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBanco>>();
      const banco = { id: 123 };
      jest.spyOn(bancoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ banco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bancoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
