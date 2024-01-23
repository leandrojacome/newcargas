import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../forma-cobranca.test-samples';

import { FormaCobrancaFormService } from './forma-cobranca-form.service';

describe('FormaCobranca Form Service', () => {
  let service: FormaCobrancaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormaCobrancaFormService);
  });

  describe('Service methods', () => {
    describe('createFormaCobrancaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFormaCobrancaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IFormaCobranca should create a new form with FormGroup', () => {
        const formGroup = service.createFormaCobrancaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            descricao: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getFormaCobranca', () => {
      it('should return NewFormaCobranca for default FormaCobranca initial value', () => {
        const formGroup = service.createFormaCobrancaFormGroup(sampleWithNewData);

        const formaCobranca = service.getFormaCobranca(formGroup) as any;

        expect(formaCobranca).toMatchObject(sampleWithNewData);
      });

      it('should return NewFormaCobranca for empty FormaCobranca initial value', () => {
        const formGroup = service.createFormaCobrancaFormGroup();

        const formaCobranca = service.getFormaCobranca(formGroup) as any;

        expect(formaCobranca).toMatchObject({});
      });

      it('should return IFormaCobranca', () => {
        const formGroup = service.createFormaCobrancaFormGroup(sampleWithRequiredData);

        const formaCobranca = service.getFormaCobranca(formGroup) as any;

        expect(formaCobranca).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFormaCobranca should not enable id FormControl', () => {
        const formGroup = service.createFormaCobrancaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFormaCobranca should disable id FormControl', () => {
        const formGroup = service.createFormaCobrancaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
