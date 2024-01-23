import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../status-coleta.test-samples';

import { StatusColetaFormService } from './status-coleta-form.service';

describe('StatusColeta Form Service', () => {
  let service: StatusColetaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StatusColetaFormService);
  });

  describe('Service methods', () => {
    describe('createStatusColetaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStatusColetaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cor: expect.any(Object),
            ordem: expect.any(Object),
            estadoInicial: expect.any(Object),
            estadoFinal: expect.any(Object),
            permiteCancelar: expect.any(Object),
            permiteEditar: expect.any(Object),
            permiteExcluir: expect.any(Object),
            descricao: expect.any(Object),
            ativo: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            statusColetaOrigems: expect.any(Object),
          }),
        );
      });

      it('passing IStatusColeta should create a new form with FormGroup', () => {
        const formGroup = service.createStatusColetaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cor: expect.any(Object),
            ordem: expect.any(Object),
            estadoInicial: expect.any(Object),
            estadoFinal: expect.any(Object),
            permiteCancelar: expect.any(Object),
            permiteEditar: expect.any(Object),
            permiteExcluir: expect.any(Object),
            descricao: expect.any(Object),
            ativo: expect.any(Object),
            removido: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            statusColetaOrigems: expect.any(Object),
          }),
        );
      });
    });

    describe('getStatusColeta', () => {
      it('should return NewStatusColeta for default StatusColeta initial value', () => {
        const formGroup = service.createStatusColetaFormGroup(sampleWithNewData);

        const statusColeta = service.getStatusColeta(formGroup) as any;

        expect(statusColeta).toMatchObject(sampleWithNewData);
      });

      it('should return NewStatusColeta for empty StatusColeta initial value', () => {
        const formGroup = service.createStatusColetaFormGroup();

        const statusColeta = service.getStatusColeta(formGroup) as any;

        expect(statusColeta).toMatchObject({});
      });

      it('should return IStatusColeta', () => {
        const formGroup = service.createStatusColetaFormGroup(sampleWithRequiredData);

        const statusColeta = service.getStatusColeta(formGroup) as any;

        expect(statusColeta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStatusColeta should not enable id FormControl', () => {
        const formGroup = service.createStatusColetaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStatusColeta should disable id FormControl', () => {
        const formGroup = service.createStatusColetaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
