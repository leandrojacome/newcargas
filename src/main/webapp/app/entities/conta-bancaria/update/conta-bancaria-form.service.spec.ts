import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../conta-bancaria.test-samples';

import { ContaBancariaFormService } from './conta-bancaria-form.service';

describe('ContaBancaria Form Service', () => {
  let service: ContaBancariaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContaBancariaFormService);
  });

  describe('Service methods', () => {
    describe('createContaBancariaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContaBancariaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            agencia: expect.any(Object),
            conta: expect.any(Object),
            observacao: expect.any(Object),
            tipo: expect.any(Object),
            pix: expect.any(Object),
            titular: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            banco: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
          }),
        );
      });

      it('passing IContaBancaria should create a new form with FormGroup', () => {
        const formGroup = service.createContaBancariaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            agencia: expect.any(Object),
            conta: expect.any(Object),
            observacao: expect.any(Object),
            tipo: expect.any(Object),
            pix: expect.any(Object),
            titular: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            banco: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
          }),
        );
      });
    });

    describe('getContaBancaria', () => {
      it('should return NewContaBancaria for default ContaBancaria initial value', () => {
        const formGroup = service.createContaBancariaFormGroup(sampleWithNewData);

        const contaBancaria = service.getContaBancaria(formGroup) as any;

        expect(contaBancaria).toMatchObject(sampleWithNewData);
      });

      it('should return NewContaBancaria for empty ContaBancaria initial value', () => {
        const formGroup = service.createContaBancariaFormGroup();

        const contaBancaria = service.getContaBancaria(formGroup) as any;

        expect(contaBancaria).toMatchObject({});
      });

      it('should return IContaBancaria', () => {
        const formGroup = service.createContaBancariaFormGroup(sampleWithRequiredData);

        const contaBancaria = service.getContaBancaria(formGroup) as any;

        expect(contaBancaria).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContaBancaria should not enable id FormControl', () => {
        const formGroup = service.createContaBancariaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContaBancaria should disable id FormControl', () => {
        const formGroup = service.createContaBancariaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
