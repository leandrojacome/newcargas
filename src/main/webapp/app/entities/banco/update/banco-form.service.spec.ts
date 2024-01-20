import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../banco.test-samples';

import { BancoFormService } from './banco-form.service';

describe('Banco Form Service', () => {
  let service: BancoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BancoFormService);
  });

  describe('Service methods', () => {
    describe('createBancoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBancoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            codigo: expect.any(Object),
          }),
        );
      });

      it('passing IBanco should create a new form with FormGroup', () => {
        const formGroup = service.createBancoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            codigo: expect.any(Object),
          }),
        );
      });
    });

    describe('getBanco', () => {
      it('should return NewBanco for default Banco initial value', () => {
        const formGroup = service.createBancoFormGroup(sampleWithNewData);

        const banco = service.getBanco(formGroup) as any;

        expect(banco).toMatchObject(sampleWithNewData);
      });

      it('should return NewBanco for empty Banco initial value', () => {
        const formGroup = service.createBancoFormGroup();

        const banco = service.getBanco(formGroup) as any;

        expect(banco).toMatchObject({});
      });

      it('should return IBanco', () => {
        const formGroup = service.createBancoFormGroup(sampleWithRequiredData);

        const banco = service.getBanco(formGroup) as any;

        expect(banco).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBanco should not enable id FormControl', () => {
        const formGroup = service.createBancoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBanco should disable id FormControl', () => {
        const formGroup = service.createBancoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
