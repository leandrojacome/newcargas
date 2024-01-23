import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITomadaPreco } from '../tomada-preco.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tomada-preco.test-samples';

import { TomadaPrecoService, RestTomadaPreco } from './tomada-preco.service';

const requireRestSample: RestTomadaPreco = {
  ...sampleWithRequiredData,
  dataHoraEnvio: sampleWithRequiredData.dataHoraEnvio?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('TomadaPreco Service', () => {
  let service: TomadaPrecoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITomadaPreco | ITomadaPreco[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TomadaPrecoService);
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

    it('should create a TomadaPreco', () => {
      const tomadaPreco = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tomadaPreco).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TomadaPreco', () => {
      const tomadaPreco = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tomadaPreco).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TomadaPreco', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TomadaPreco', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TomadaPreco', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TomadaPreco', () => {
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

    describe('addTomadaPrecoToCollectionIfMissing', () => {
      it('should add a TomadaPreco to an empty array', () => {
        const tomadaPreco: ITomadaPreco = sampleWithRequiredData;
        expectedResult = service.addTomadaPrecoToCollectionIfMissing([], tomadaPreco);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tomadaPreco);
      });

      it('should not add a TomadaPreco to an array that contains it', () => {
        const tomadaPreco: ITomadaPreco = sampleWithRequiredData;
        const tomadaPrecoCollection: ITomadaPreco[] = [
          {
            ...tomadaPreco,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTomadaPrecoToCollectionIfMissing(tomadaPrecoCollection, tomadaPreco);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TomadaPreco to an array that doesn't contain it", () => {
        const tomadaPreco: ITomadaPreco = sampleWithRequiredData;
        const tomadaPrecoCollection: ITomadaPreco[] = [sampleWithPartialData];
        expectedResult = service.addTomadaPrecoToCollectionIfMissing(tomadaPrecoCollection, tomadaPreco);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tomadaPreco);
      });

      it('should add only unique TomadaPreco to an array', () => {
        const tomadaPrecoArray: ITomadaPreco[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tomadaPrecoCollection: ITomadaPreco[] = [sampleWithRequiredData];
        expectedResult = service.addTomadaPrecoToCollectionIfMissing(tomadaPrecoCollection, ...tomadaPrecoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tomadaPreco: ITomadaPreco = sampleWithRequiredData;
        const tomadaPreco2: ITomadaPreco = sampleWithPartialData;
        expectedResult = service.addTomadaPrecoToCollectionIfMissing([], tomadaPreco, tomadaPreco2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tomadaPreco);
        expect(expectedResult).toContain(tomadaPreco2);
      });

      it('should accept null and undefined values', () => {
        const tomadaPreco: ITomadaPreco = sampleWithRequiredData;
        expectedResult = service.addTomadaPrecoToCollectionIfMissing([], null, tomadaPreco, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tomadaPreco);
      });

      it('should return initial array if no TomadaPreco is added', () => {
        const tomadaPrecoCollection: ITomadaPreco[] = [sampleWithRequiredData];
        expectedResult = service.addTomadaPrecoToCollectionIfMissing(tomadaPrecoCollection, undefined, null);
        expect(expectedResult).toEqual(tomadaPrecoCollection);
      });
    });

    describe('compareTomadaPreco', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTomadaPreco(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTomadaPreco(entity1, entity2);
        const compareResult2 = service.compareTomadaPreco(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTomadaPreco(entity1, entity2);
        const compareResult2 = service.compareTomadaPreco(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTomadaPreco(entity1, entity2);
        const compareResult2 = service.compareTomadaPreco(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
