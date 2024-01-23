import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../contratacao.test-samples';

import { ContratacaoFormService } from './contratacao-form.service';

describe('Contratacao Form Service', () => {
  let service: ContratacaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContratacaoFormService);
  });

  describe('Service methods', () => {
    describe('createContratacaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createContratacaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valorTotal: expect.any(Object),
            validadeEmDias: expect.any(Object),
            dataValidade: expect.any(Object),
            observacao: expect.any(Object),
            cancelado: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            transportadora: expect.any(Object),
          }),
        );
      });

      it('passing IContratacao should create a new form with FormGroup', () => {
        const formGroup = service.createContratacaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valorTotal: expect.any(Object),
            validadeEmDias: expect.any(Object),
            dataValidade: expect.any(Object),
            observacao: expect.any(Object),
            cancelado: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            transportadora: expect.any(Object),
          }),
        );
      });
    });

    describe('getContratacao', () => {
      it('should return NewContratacao for default Contratacao initial value', () => {
        const formGroup = service.createContratacaoFormGroup(sampleWithNewData);

        const contratacao = service.getContratacao(formGroup) as any;

        expect(contratacao).toMatchObject(sampleWithNewData);
      });

      it('should return NewContratacao for empty Contratacao initial value', () => {
        const formGroup = service.createContratacaoFormGroup();

        const contratacao = service.getContratacao(formGroup) as any;

        expect(contratacao).toMatchObject({});
      });

      it('should return IContratacao', () => {
        const formGroup = service.createContratacaoFormGroup(sampleWithRequiredData);

        const contratacao = service.getContratacao(formGroup) as any;

        expect(contratacao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IContratacao should not enable id FormControl', () => {
        const formGroup = service.createContratacaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewContratacao should disable id FormControl', () => {
        const formGroup = service.createContratacaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
