import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IContratacao } from '../contratacao.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../contratacao.test-samples';

import { ContratacaoService, RestContratacao } from './contratacao.service';

const requireRestSample: RestContratacao = {
  ...sampleWithRequiredData,
  dataValidade: sampleWithRequiredData.dataValidade?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Contratacao Service', () => {
  let service: ContratacaoService;
  let httpMock: HttpTestingController;
  let expectedResult: IContratacao | IContratacao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContratacaoService);
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

    it('should create a Contratacao', () => {
      const contratacao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contratacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Contratacao', () => {
      const contratacao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contratacao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Contratacao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Contratacao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Contratacao', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Contratacao', () => {
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

    describe('addContratacaoToCollectionIfMissing', () => {
      it('should add a Contratacao to an empty array', () => {
        const contratacao: IContratacao = sampleWithRequiredData;
        expectedResult = service.addContratacaoToCollectionIfMissing([], contratacao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contratacao);
      });

      it('should not add a Contratacao to an array that contains it', () => {
        const contratacao: IContratacao = sampleWithRequiredData;
        const contratacaoCollection: IContratacao[] = [
          {
            ...contratacao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContratacaoToCollectionIfMissing(contratacaoCollection, contratacao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Contratacao to an array that doesn't contain it", () => {
        const contratacao: IContratacao = sampleWithRequiredData;
        const contratacaoCollection: IContratacao[] = [sampleWithPartialData];
        expectedResult = service.addContratacaoToCollectionIfMissing(contratacaoCollection, contratacao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contratacao);
      });

      it('should add only unique Contratacao to an array', () => {
        const contratacaoArray: IContratacao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contratacaoCollection: IContratacao[] = [sampleWithRequiredData];
        expectedResult = service.addContratacaoToCollectionIfMissing(contratacaoCollection, ...contratacaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contratacao: IContratacao = sampleWithRequiredData;
        const contratacao2: IContratacao = sampleWithPartialData;
        expectedResult = service.addContratacaoToCollectionIfMissing([], contratacao, contratacao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contratacao);
        expect(expectedResult).toContain(contratacao2);
      });

      it('should accept null and undefined values', () => {
        const contratacao: IContratacao = sampleWithRequiredData;
        expectedResult = service.addContratacaoToCollectionIfMissing([], null, contratacao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contratacao);
      });

      it('should return initial array if no Contratacao is added', () => {
        const contratacaoCollection: IContratacao[] = [sampleWithRequiredData];
        expectedResult = service.addContratacaoToCollectionIfMissing(contratacaoCollection, undefined, null);
        expect(expectedResult).toEqual(contratacaoCollection);
      });
    });

    describe('compareContratacao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContratacao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareContratacao(entity1, entity2);
        const compareResult2 = service.compareContratacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareContratacao(entity1, entity2);
        const compareResult2 = service.compareContratacao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareContratacao(entity1, entity2);
        const compareResult2 = service.compareContratacao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
