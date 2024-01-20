import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../transportadora.test-samples';

import { TransportadoraFormService } from './transportadora-form.service';

describe('Transportadora Form Service', () => {
  let service: TransportadoraFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TransportadoraFormService);
  });

  describe('Service methods', () => {
    describe('createTransportadoraFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransportadoraFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cnpj: expect.any(Object),
            razaoSocial: expect.any(Object),
            inscricaoEstadual: expect.any(Object),
            inscricaoMunicipal: expect.any(Object),
            responsavel: expect.any(Object),
            cep: expect.any(Object),
            endereco: expect.any(Object),
            numero: expect.any(Object),
            complemento: expect.any(Object),
            bairro: expect.any(Object),
            telefone: expect.any(Object),
            email: expect.any(Object),
            observacao: expect.any(Object),
            dataCadastro: expect.any(Object),
            usuarioCadastro: expect.any(Object),
            dataAtualizacao: expect.any(Object),
            usuarioAtualizacao: expect.any(Object),
          }),
        );
      });

      it('passing ITransportadora should create a new form with FormGroup', () => {
        const formGroup = service.createTransportadoraFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cnpj: expect.any(Object),
            razaoSocial: expect.any(Object),
            inscricaoEstadual: expect.any(Object),
            inscricaoMunicipal: expect.any(Object),
            responsavel: expect.any(Object),
            cep: expect.any(Object),
            endereco: expect.any(Object),
            numero: expect.any(Object),
            complemento: expect.any(Object),
            bairro: expect.any(Object),
            telefone: expect.any(Object),
            email: expect.any(Object),
            observacao: expect.any(Object),
            dataCadastro: expect.any(Object),
            usuarioCadastro: expect.any(Object),
            dataAtualizacao: expect.any(Object),
            usuarioAtualizacao: expect.any(Object),
          }),
        );
      });
    });

    describe('getTransportadora', () => {
      it('should return NewTransportadora for default Transportadora initial value', () => {
        const formGroup = service.createTransportadoraFormGroup(sampleWithNewData);

        const transportadora = service.getTransportadora(formGroup) as any;

        expect(transportadora).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransportadora for empty Transportadora initial value', () => {
        const formGroup = service.createTransportadoraFormGroup();

        const transportadora = service.getTransportadora(formGroup) as any;

        expect(transportadora).toMatchObject({});
      });

      it('should return ITransportadora', () => {
        const formGroup = service.createTransportadoraFormGroup(sampleWithRequiredData);

        const transportadora = service.getTransportadora(formGroup) as any;

        expect(transportadora).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransportadora should not enable id FormControl', () => {
        const formGroup = service.createTransportadoraFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransportadora should disable id FormControl', () => {
        const formGroup = service.createTransportadoraFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
