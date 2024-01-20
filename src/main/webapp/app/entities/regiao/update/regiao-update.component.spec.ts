import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RegiaoService } from '../service/regiao.service';
import { IRegiao } from '../regiao.model';
import { RegiaoFormService } from './regiao-form.service';

import { RegiaoUpdateComponent } from './regiao-update.component';

describe('Regiao Management Update Component', () => {
  let comp: RegiaoUpdateComponent;
  let fixture: ComponentFixture<RegiaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let regiaoFormService: RegiaoFormService;
  let regiaoService: RegiaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RegiaoUpdateComponent],
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
      .overrideTemplate(RegiaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegiaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    regiaoFormService = TestBed.inject(RegiaoFormService);
    regiaoService = TestBed.inject(RegiaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const regiao: IRegiao = { id: 456 };

      activatedRoute.data = of({ regiao });
      comp.ngOnInit();

      expect(comp.regiao).toEqual(regiao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegiao>>();
      const regiao = { id: 123 };
      jest.spyOn(regiaoFormService, 'getRegiao').mockReturnValue(regiao);
      jest.spyOn(regiaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ regiao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: regiao }));
      saveSubject.complete();

      // THEN
      expect(regiaoFormService.getRegiao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(regiaoService.update).toHaveBeenCalledWith(expect.objectContaining(regiao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegiao>>();
      const regiao = { id: 123 };
      jest.spyOn(regiaoFormService, 'getRegiao').mockReturnValue({ id: null });
      jest.spyOn(regiaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ regiao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: regiao }));
      saveSubject.complete();

      // THEN
      expect(regiaoFormService.getRegiao).toHaveBeenCalled();
      expect(regiaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRegiao>>();
      const regiao = { id: 123 };
      jest.spyOn(regiaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ regiao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(regiaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
