import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tipo-carga.test-samples';

import { TipoCargaFormService } from './tipo-carga-form.service';

describe('TipoCarga Form Service', () => {
  let service: TipoCargaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoCargaFormService);
  });

  describe('Service methods', () => {
    describe('createTipoCargaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTipoCargaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
          }),
        );
      });

      it('passing ITipoCarga should create a new form with FormGroup', () => {
        const formGroup = service.createTipoCargaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
          }),
        );
      });
    });

    describe('getTipoCarga', () => {
      it('should return NewTipoCarga for default TipoCarga initial value', () => {
        const formGroup = service.createTipoCargaFormGroup(sampleWithNewData);

        const tipoCarga = service.getTipoCarga(formGroup) as any;

        expect(tipoCarga).toMatchObject(sampleWithNewData);
      });

      it('should return NewTipoCarga for empty TipoCarga initial value', () => {
        const formGroup = service.createTipoCargaFormGroup();

        const tipoCarga = service.getTipoCarga(formGroup) as any;

        expect(tipoCarga).toMatchObject({});
      });

      it('should return ITipoCarga', () => {
        const formGroup = service.createTipoCargaFormGroup(sampleWithRequiredData);

        const tipoCarga = service.getTipoCarga(formGroup) as any;

        expect(tipoCarga).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITipoCarga should not enable id FormControl', () => {
        const formGroup = service.createTipoCargaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTipoCarga should disable id FormControl', () => {
        const formGroup = service.createTipoCargaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
