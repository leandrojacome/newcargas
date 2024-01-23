import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICidade } from '../cidade.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cidade.test-samples';

import { CidadeService, RestCidade } from './cidade.service';

const requireRestSample: RestCidade = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Cidade Service', () => {
  let service: CidadeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICidade | ICidade[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CidadeService);
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

    it('should create a Cidade', () => {
      const cidade = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cidade).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cidade', () => {
      const cidade = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cidade).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cidade', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cidade', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cidade', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Cidade', () => {
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

    describe('addCidadeToCollectionIfMissing', () => {
      it('should add a Cidade to an empty array', () => {
        const cidade: ICidade = sampleWithRequiredData;
        expectedResult = service.addCidadeToCollectionIfMissing([], cidade);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cidade);
      });

      it('should not add a Cidade to an array that contains it', () => {
        const cidade: ICidade = sampleWithRequiredData;
        const cidadeCollection: ICidade[] = [
          {
            ...cidade,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCidadeToCollectionIfMissing(cidadeCollection, cidade);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cidade to an array that doesn't contain it", () => {
        const cidade: ICidade = sampleWithRequiredData;
        const cidadeCollection: ICidade[] = [sampleWithPartialData];
        expectedResult = service.addCidadeToCollectionIfMissing(cidadeCollection, cidade);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cidade);
      });

      it('should add only unique Cidade to an array', () => {
        const cidadeArray: ICidade[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cidadeCollection: ICidade[] = [sampleWithRequiredData];
        expectedResult = service.addCidadeToCollectionIfMissing(cidadeCollection, ...cidadeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cidade: ICidade = sampleWithRequiredData;
        const cidade2: ICidade = sampleWithPartialData;
        expectedResult = service.addCidadeToCollectionIfMissing([], cidade, cidade2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cidade);
        expect(expectedResult).toContain(cidade2);
      });

      it('should accept null and undefined values', () => {
        const cidade: ICidade = sampleWithRequiredData;
        expectedResult = service.addCidadeToCollectionIfMissing([], null, cidade, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cidade);
      });

      it('should return initial array if no Cidade is added', () => {
        const cidadeCollection: ICidade[] = [sampleWithRequiredData];
        expectedResult = service.addCidadeToCollectionIfMissing(cidadeCollection, undefined, null);
        expect(expectedResult).toEqual(cidadeCollection);
      });
    });

    describe('compareCidade', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCidade(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCidade(entity1, entity2);
        const compareResult2 = service.compareCidade(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCidade(entity1, entity2);
        const compareResult2 = service.compareCidade(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCidade(entity1, entity2);
        const compareResult2 = service.compareCidade(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
