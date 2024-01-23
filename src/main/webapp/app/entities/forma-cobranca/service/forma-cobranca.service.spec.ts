import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFormaCobranca } from '../forma-cobranca.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../forma-cobranca.test-samples';

import { FormaCobrancaService, RestFormaCobranca } from './forma-cobranca.service';

const requireRestSample: RestFormaCobranca = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('FormaCobranca Service', () => {
  let service: FormaCobrancaService;
  let httpMock: HttpTestingController;
  let expectedResult: IFormaCobranca | IFormaCobranca[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormaCobrancaService);
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

    it('should create a FormaCobranca', () => {
      const formaCobranca = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(formaCobranca).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FormaCobranca', () => {
      const formaCobranca = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(formaCobranca).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FormaCobranca', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FormaCobranca', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FormaCobranca', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FormaCobranca', () => {
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

    describe('addFormaCobrancaToCollectionIfMissing', () => {
      it('should add a FormaCobranca to an empty array', () => {
        const formaCobranca: IFormaCobranca = sampleWithRequiredData;
        expectedResult = service.addFormaCobrancaToCollectionIfMissing([], formaCobranca);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formaCobranca);
      });

      it('should not add a FormaCobranca to an array that contains it', () => {
        const formaCobranca: IFormaCobranca = sampleWithRequiredData;
        const formaCobrancaCollection: IFormaCobranca[] = [
          {
            ...formaCobranca,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFormaCobrancaToCollectionIfMissing(formaCobrancaCollection, formaCobranca);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FormaCobranca to an array that doesn't contain it", () => {
        const formaCobranca: IFormaCobranca = sampleWithRequiredData;
        const formaCobrancaCollection: IFormaCobranca[] = [sampleWithPartialData];
        expectedResult = service.addFormaCobrancaToCollectionIfMissing(formaCobrancaCollection, formaCobranca);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formaCobranca);
      });

      it('should add only unique FormaCobranca to an array', () => {
        const formaCobrancaArray: IFormaCobranca[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const formaCobrancaCollection: IFormaCobranca[] = [sampleWithRequiredData];
        expectedResult = service.addFormaCobrancaToCollectionIfMissing(formaCobrancaCollection, ...formaCobrancaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formaCobranca: IFormaCobranca = sampleWithRequiredData;
        const formaCobranca2: IFormaCobranca = sampleWithPartialData;
        expectedResult = service.addFormaCobrancaToCollectionIfMissing([], formaCobranca, formaCobranca2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formaCobranca);
        expect(expectedResult).toContain(formaCobranca2);
      });

      it('should accept null and undefined values', () => {
        const formaCobranca: IFormaCobranca = sampleWithRequiredData;
        expectedResult = service.addFormaCobrancaToCollectionIfMissing([], null, formaCobranca, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formaCobranca);
      });

      it('should return initial array if no FormaCobranca is added', () => {
        const formaCobrancaCollection: IFormaCobranca[] = [sampleWithRequiredData];
        expectedResult = service.addFormaCobrancaToCollectionIfMissing(formaCobrancaCollection, undefined, null);
        expect(expectedResult).toEqual(formaCobrancaCollection);
      });
    });

    describe('compareFormaCobranca', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFormaCobranca(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFormaCobranca(entity1, entity2);
        const compareResult2 = service.compareFormaCobranca(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFormaCobranca(entity1, entity2);
        const compareResult2 = service.compareFormaCobranca(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFormaCobranca(entity1, entity2);
        const compareResult2 = service.compareFormaCobranca(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
