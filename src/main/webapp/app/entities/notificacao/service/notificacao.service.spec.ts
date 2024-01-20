import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INotificacao } from '../notificacao.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../notificacao.test-samples';

import { NotificacaoService, RestNotificacao } from './notificacao.service';

const requireRestSample: RestNotificacao = {
  ...sampleWithRequiredData,
  dataHoraEnvio: sampleWithRequiredData.dataHoraEnvio?.toJSON(),
  dataHoraLeitura: sampleWithRequiredData.dataHoraLeitura?.toJSON(),
  dataCadastro: sampleWithRequiredData.dataCadastro?.toJSON(),
  dataAtualizacao: sampleWithRequiredData.dataAtualizacao?.toJSON(),
  dataLeitura: sampleWithRequiredData.dataLeitura?.toJSON(),
  dataRemocao: sampleWithRequiredData.dataRemocao?.toJSON(),
};

describe('Notificacao Service', () => {
  let service: NotificacaoService;
  let httpMock: HttpTestingController;
  let expectedResult: INotificacao | INotificacao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NotificacaoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Notificacao', () => {
      const notificacao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(notificacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Notificacao', () => {
      const notificacao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(notificacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Notificacao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Notificacao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Notificacao', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Notificacao', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addNotificacaoToCollectionIfMissing', () => {
      it('should add a Notificacao to an empty array', () => {
        const notificacao: INotificacao = sampleWithRequiredData;
        expectedResult = service.addNotificacaoToCollectionIfMissing([], notificacao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificacao);
      });

      it('should not add a Notificacao to an array that contains it', () => {
        const notificacao: INotificacao = sampleWithRequiredData;
        const notificacaoCollection: INotificacao[] = [
          {
            ...notificacao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNotificacaoToCollectionIfMissing(notificacaoCollection, notificacao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Notificacao to an array that doesn't contain it", () => {
        const notificacao: INotificacao = sampleWithRequiredData;
        const notificacaoCollection: INotificacao[] = [sampleWithPartialData];
        expectedResult = service.addNotificacaoToCollectionIfMissing(notificacaoCollection, notificacao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificacao);
      });

      it('should add only unique Notificacao to an array', () => {
        const notificacaoArray: INotificacao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const notificacaoCollection: INotificacao[] = [sampleWithRequiredData];
        expectedResult = service.addNotificacaoToCollectionIfMissing(notificacaoCollection, ...notificacaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notificacao: INotificacao = sampleWithRequiredData;
        const notificacao2: INotificacao = sampleWithPartialData;
        expectedResult = service.addNotificacaoToCollectionIfMissing([], notificacao, notificacao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificacao);
        expect(expectedResult).toContain(notificacao2);
      });

      it('should accept null and undefined values', () => {
        const notificacao: INotificacao = sampleWithRequiredData;
        expectedResult = service.addNotificacaoToCollectionIfMissing([], null, notificacao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificacao);
      });

      it('should return initial array if no Notificacao is added', () => {
        const notificacaoCollection: INotificacao[] = [sampleWithRequiredData];
        expectedResult = service.addNotificacaoToCollectionIfMissing(notificacaoCollection, undefined, null);
        expect(expectedResult).toEqual(notificacaoCollection);
      });
    });

    describe('compareNotificacao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNotificacao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNotificacao(entity1, entity2);
        const compareResult2 = service.compareNotificacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNotificacao(entity1, entity2);
        const compareResult2 = service.compareNotificacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNotificacao(entity1, entity2);
        const compareResult2 = service.compareNotificacao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
