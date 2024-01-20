import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../embarcador.test-samples';

import { EmbarcadorFormService } from './embarcador-form.service';

describe('Embarcador Form Service', () => {
  let service: EmbarcadorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmbarcadorFormService);
  });

  describe('Service methods', () => {
    describe('createEmbarcadorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmbarcadorFormGroup();

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

      it('passing IEmbarcador should create a new form with FormGroup', () => {
        const formGroup = service.createEmbarcadorFormGroup(sampleWithRequiredData);

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

    describe('getEmbarcador', () => {
      it('should return NewEmbarcador for default Embarcador initial value', () => {
        const formGroup = service.createEmbarcadorFormGroup(sampleWithNewData);

        const embarcador = service.getEmbarcador(formGroup) as any;

        expect(embarcador).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmbarcador for empty Embarcador initial value', () => {
        const formGroup = service.createEmbarcadorFormGroup();

        const embarcador = service.getEmbarcador(formGroup) as any;

        expect(embarcador).toMatchObject({});
      });

      it('should return IEmbarcador', () => {
        const formGroup = service.createEmbarcadorFormGroup(sampleWithRequiredData);

        const embarcador = service.getEmbarcador(formGroup) as any;

        expect(embarcador).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmbarcador should not enable id FormControl', () => {
        const formGroup = service.createEmbarcadorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmbarcador should disable id FormControl', () => {
        const formGroup = service.createEmbarcadorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
