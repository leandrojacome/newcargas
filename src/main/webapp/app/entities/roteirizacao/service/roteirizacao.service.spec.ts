import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoteirizacao } from '../roteirizacao.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../roteirizacao.test-samples';

import { RoteirizacaoService, RestRoteirizacao } from './roteirizacao.service';

const requireRestSample: RestRoteirizacao = {
  ...sampleWithRequiredData,
  dataHoraPrimeiraColeta: sampleWithRequiredData.dataHoraPrimeiraColeta?.toJSON(),
  dataHoraUltimaColeta: sampleWithRequiredData.dataHoraUltimaColeta?.toJSON(),
  dataHoraPrimeiraEntrega: sampleWithRequiredData.dataHoraPrimeiraEntrega?.toJSON(),
  dataHoraUltimaEntrega: sampleWithRequiredData.dataHoraUltimaEntrega?.toJSON(),
  dataCadastro: sampleWithRequiredData.dataCadastro?.toJSON(),
  dataAtualizacao: sampleWithRequiredData.dataAtualizacao?.toJSON(),
  dataCancelamento: sampleWithRequiredData.dataCancelamento?.toJSON(),
  dataRemocao: sampleWithRequiredData.dataRemocao?.toJSON(),
};

describe('Roteirizacao Service', () => {
  let service: RoteirizacaoService;
  let httpMock: HttpTestingController;
  let expectedResult: IRoteirizacao | IRoteirizacao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoteirizacaoService);
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

    it('should create a Roteirizacao', () => {
      const roteirizacao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(roteirizacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Roteirizacao', () => {
      const roteirizacao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(roteirizacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Roteirizacao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Roteirizacao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Roteirizacao', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Roteirizacao', () => {
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

    describe('addRoteirizacaoToCollectionIfMissing', () => {
      it('should add a Roteirizacao to an empty array', () => {
        const roteirizacao: IRoteirizacao = sampleWithRequiredData;
        expectedResult = service.addRoteirizacaoToCollectionIfMissing([], roteirizacao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roteirizacao);
      });

      it('should not add a Roteirizacao to an array that contains it', () => {
        const roteirizacao: IRoteirizacao = sampleWithRequiredData;
        const roteirizacaoCollection: IRoteirizacao[] = [
          {
            ...roteirizacao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRoteirizacaoToCollectionIfMissing(roteirizacaoCollection, roteirizacao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Roteirizacao to an array that doesn't contain it", () => {
        const roteirizacao: IRoteirizacao = sampleWithRequiredData;
        const roteirizacaoCollection: IRoteirizacao[] = [sampleWithPartialData];
        expectedResult = service.addRoteirizacaoToCollectionIfMissing(roteirizacaoCollection, roteirizacao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roteirizacao);
      });

      it('should add only unique Roteirizacao to an array', () => {
        const roteirizacaoArray: IRoteirizacao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const roteirizacaoCollection: IRoteirizacao[] = [sampleWithRequiredData];
        expectedResult = service.addRoteirizacaoToCollectionIfMissing(roteirizacaoCollection, ...roteirizacaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const roteirizacao: IRoteirizacao = sampleWithRequiredData;
        const roteirizacao2: IRoteirizacao = sampleWithPartialData;
        expectedResult = service.addRoteirizacaoToCollectionIfMissing([], roteirizacao, roteirizacao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roteirizacao);
        expect(expectedResult).toContain(roteirizacao2);
      });

      it('should accept null and undefined values', () => {
        const roteirizacao: IRoteirizacao = sampleWithRequiredData;
        expectedResult = service.addRoteirizacaoToCollectionIfMissing([], null, roteirizacao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roteirizacao);
      });

      it('should return initial array if no Roteirizacao is added', () => {
        const roteirizacaoCollection: IRoteirizacao[] = [sampleWithRequiredData];
        expectedResult = service.addRoteirizacaoToCollectionIfMissing(roteirizacaoCollection, undefined, null);
        expect(expectedResult).toEqual(roteirizacaoCollection);
      });
    });

    describe('compareRoteirizacao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRoteirizacao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRoteirizacao(entity1, entity2);
        const compareResult2 = service.compareRoteirizacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRoteirizacao(entity1, entity2);
        const compareResult2 = service.compareRoteirizacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRoteirizacao(entity1, entity2);
        const compareResult2 = service.compareRoteirizacao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
