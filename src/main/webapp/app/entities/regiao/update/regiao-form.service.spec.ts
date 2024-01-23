import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../regiao.test-samples';

import { RegiaoFormService } from './regiao-form.service';

describe('Regiao Form Service', () => {
  let service: RegiaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegiaoFormService);
  });

  describe('Service methods', () => {
    describe('createRegiaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRegiaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            sigla: expect.any(Object),
            descricao: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IRegiao should create a new form with FormGroup', () => {
        const formGroup = service.createRegiaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            sigla: expect.any(Object),
            descricao: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getRegiao', () => {
      it('should return NewRegiao for default Regiao initial value', () => {
        const formGroup = service.createRegiaoFormGroup(sampleWithNewData);

        const regiao = service.getRegiao(formGroup) as any;

        expect(regiao).toMatchObject(sampleWithNewData);
      });

      it('should return NewRegiao for empty Regiao initial value', () => {
        const formGroup = service.createRegiaoFormGroup();

        const regiao = service.getRegiao(formGroup) as any;

        expect(regiao).toMatchObject({});
      });

      it('should return IRegiao', () => {
        const formGroup = service.createRegiaoFormGroup(sampleWithRequiredData);

        const regiao = service.getRegiao(formGroup) as any;

        expect(regiao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRegiao should not enable id FormControl', () => {
        const formGroup = service.createRegiaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRegiao should disable id FormControl', () => {
        const formGroup = service.createRegiaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
