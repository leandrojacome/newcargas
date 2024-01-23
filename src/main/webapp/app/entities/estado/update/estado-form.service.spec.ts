import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../estado.test-samples';

import { EstadoFormService } from './estado-form.service';

describe('Estado Form Service', () => {
  let service: EstadoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EstadoFormService);
  });

  describe('Service methods', () => {
    describe('createEstadoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEstadoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            sigla: expect.any(Object),
            codigoIbge: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });

      it('passing IEstado should create a new form with FormGroup', () => {
        const formGroup = service.createEstadoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            sigla: expect.any(Object),
            codigoIbge: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getEstado', () => {
      it('should return NewEstado for default Estado initial value', () => {
        const formGroup = service.createEstadoFormGroup(sampleWithNewData);

        const estado = service.getEstado(formGroup) as any;

        expect(estado).toMatchObject(sampleWithNewData);
      });

      it('should return NewEstado for empty Estado initial value', () => {
        const formGroup = service.createEstadoFormGroup();

        const estado = service.getEstado(formGroup) as any;

        expect(estado).toMatchObject({});
      });

      it('should return IEstado', () => {
        const formGroup = service.createEstadoFormGroup(sampleWithRequiredData);

        const estado = service.getEstado(formGroup) as any;

        expect(estado).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEstado should not enable id FormControl', () => {
        const formGroup = service.createEstadoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEstado should disable id FormControl', () => {
        const formGroup = service.createEstadoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
