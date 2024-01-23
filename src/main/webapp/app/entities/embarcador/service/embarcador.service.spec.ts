import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmbarcador } from '../embarcador.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../embarcador.test-samples';

import { EmbarcadorService, RestEmbarcador } from './embarcador.service';

const requireRestSample: RestEmbarcador = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Embarcador Service', () => {
  let service: EmbarcadorService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmbarcador | IEmbarcador[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmbarcadorService);
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

    it('should create a Embarcador', () => {
      const embarcador = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(embarcador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Embarcador', () => {
      const embarcador = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(embarcador).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Embarcador', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Embarcador', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Embarcador', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Embarcador', () => {
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

    describe('addEmbarcadorToCollectionIfMissing', () => {
      it('should add a Embarcador to an empty array', () => {
        const embarcador: IEmbarcador = sampleWithRequiredData;
        expectedResult = service.addEmbarcadorToCollectionIfMissing([], embarcador);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(embarcador);
      });

      it('should not add a Embarcador to an array that contains it', () => {
        const embarcador: IEmbarcador = sampleWithRequiredData;
        const embarcadorCollection: IEmbarcador[] = [
          {
            ...embarcador,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmbarcadorToCollectionIfMissing(embarcadorCollection, embarcador);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Embarcador to an array that doesn't contain it", () => {
        const embarcador: IEmbarcador = sampleWithRequiredData;
        const embarcadorCollection: IEmbarcador[] = [sampleWithPartialData];
        expectedResult = service.addEmbarcadorToCollectionIfMissing(embarcadorCollection, embarcador);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(embarcador);
      });

      it('should add only unique Embarcador to an array', () => {
        const embarcadorArray: IEmbarcador[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const embarcadorCollection: IEmbarcador[] = [sampleWithRequiredData];
        expectedResult = service.addEmbarcadorToCollectionIfMissing(embarcadorCollection, ...embarcadorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const embarcador: IEmbarcador = sampleWithRequiredData;
        const embarcador2: IEmbarcador = sampleWithPartialData;
        expectedResult = service.addEmbarcadorToCollectionIfMissing([], embarcador, embarcador2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(embarcador);
        expect(expectedResult).toContain(embarcador2);
      });

      it('should accept null and undefined values', () => {
        const embarcador: IEmbarcador = sampleWithRequiredData;
        expectedResult = service.addEmbarcadorToCollectionIfMissing([], null, embarcador, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(embarcador);
      });

      it('should return initial array if no Embarcador is added', () => {
        const embarcadorCollection: IEmbarcador[] = [sampleWithRequiredData];
        expectedResult = service.addEmbarcadorToCollectionIfMissing(embarcadorCollection, undefined, null);
        expect(expectedResult).toEqual(embarcadorCollection);
      });
    });

    describe('compareEmbarcador', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmbarcador(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmbarcador(entity1, entity2);
        const compareResult2 = service.compareEmbarcador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmbarcador(entity1, entity2);
        const compareResult2 = service.compareEmbarcador(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmbarcador(entity1, entity2);
        const compareResult2 = service.compareEmbarcador(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
