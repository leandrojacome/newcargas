import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITipoFrete } from '../tipo-frete.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tipo-frete.test-samples';

import { TipoFreteService, RestTipoFrete } from './tipo-frete.service';

const requireRestSample: RestTipoFrete = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('TipoFrete Service', () => {
  let service: TipoFreteService;
  let httpMock: HttpTestingController;
  let expectedResult: ITipoFrete | ITipoFrete[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TipoFreteService);
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

    it('should create a TipoFrete', () => {
      const tipoFrete = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tipoFrete).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TipoFrete', () => {
      const tipoFrete = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tipoFrete).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TipoFrete', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TipoFrete', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TipoFrete', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TipoFrete', () => {
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

    describe('addTipoFreteToCollectionIfMissing', () => {
      it('should add a TipoFrete to an empty array', () => {
        const tipoFrete: ITipoFrete = sampleWithRequiredData;
        expectedResult = service.addTipoFreteToCollectionIfMissing([], tipoFrete);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoFrete);
      });

      it('should not add a TipoFrete to an array that contains it', () => {
        const tipoFrete: ITipoFrete = sampleWithRequiredData;
        const tipoFreteCollection: ITipoFrete[] = [
          {
            ...tipoFrete,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTipoFreteToCollectionIfMissing(tipoFreteCollection, tipoFrete);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TipoFrete to an array that doesn't contain it", () => {
        const tipoFrete: ITipoFrete = sampleWithRequiredData;
        const tipoFreteCollection: ITipoFrete[] = [sampleWithPartialData];
        expectedResult = service.addTipoFreteToCollectionIfMissing(tipoFreteCollection, tipoFrete);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoFrete);
      });

      it('should add only unique TipoFrete to an array', () => {
        const tipoFreteArray: ITipoFrete[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tipoFreteCollection: ITipoFrete[] = [sampleWithRequiredData];
        expectedResult = service.addTipoFreteToCollectionIfMissing(tipoFreteCollection, ...tipoFreteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tipoFrete: ITipoFrete = sampleWithRequiredData;
        const tipoFrete2: ITipoFrete = sampleWithPartialData;
        expectedResult = service.addTipoFreteToCollectionIfMissing([], tipoFrete, tipoFrete2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tipoFrete);
        expect(expectedResult).toContain(tipoFrete2);
      });

      it('should accept null and undefined values', () => {
        const tipoFrete: ITipoFrete = sampleWithRequiredData;
        expectedResult = service.addTipoFreteToCollectionIfMissing([], null, tipoFrete, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tipoFrete);
      });

      it('should return initial array if no TipoFrete is added', () => {
        const tipoFreteCollection: ITipoFrete[] = [sampleWithRequiredData];
        expectedResult = service.addTipoFreteToCollectionIfMissing(tipoFreteCollection, undefined, null);
        expect(expectedResult).toEqual(tipoFreteCollection);
      });
    });

    describe('compareTipoFrete', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTipoFrete(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTipoFrete(entity1, entity2);
        const compareResult2 = service.compareTipoFrete(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTipoFrete(entity1, entity2);
        const compareResult2 = service.compareTipoFrete(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTipoFrete(entity1, entity2);
        const compareResult2 = service.compareTipoFrete(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
