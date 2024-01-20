import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cidade.test-samples';

import { CidadeFormService } from './cidade-form.service';

describe('Cidade Form Service', () => {
  let service: CidadeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CidadeFormService);
  });

  describe('Service methods', () => {
    describe('createCidadeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCidadeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            codigoIbge: expect.any(Object),
            estado: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
          }),
        );
      });

      it('passing ICidade should create a new form with FormGroup', () => {
        const formGroup = service.createCidadeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            codigoIbge: expect.any(Object),
            estado: expect.any(Object),
            embarcador: expect.any(Object),
            transportadora: expect.any(Object),
          }),
        );
      });
    });

    describe('getCidade', () => {
      it('should return NewCidade for default Cidade initial value', () => {
        const formGroup = service.createCidadeFormGroup(sampleWithNewData);

        const cidade = service.getCidade(formGroup) as any;

        expect(cidade).toMatchObject(sampleWithNewData);
      });

      it('should return NewCidade for empty Cidade initial value', () => {
        const formGroup = service.createCidadeFormGroup();

        const cidade = service.getCidade(formGroup) as any;

        expect(cidade).toMatchObject({});
      });

      it('should return ICidade', () => {
        const formGroup = service.createCidadeFormGroup(sampleWithRequiredData);

        const cidade = service.getCidade(formGroup) as any;

        expect(cidade).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICidade should not enable id FormControl', () => {
        const formGroup = service.createCidadeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCidade should disable id FormControl', () => {
        const formGroup = service.createCidadeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
