import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEstado } from '../estado.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../estado.test-samples';

import { EstadoService, RestEstado } from './estado.service';

const requireRestSample: RestEstado = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Estado Service', () => {
  let service: EstadoService;
  let httpMock: HttpTestingController;
  let expectedResult: IEstado | IEstado[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EstadoService);
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

    it('should create a Estado', () => {
      const estado = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(estado).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Estado', () => {
      const estado = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(estado).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Estado', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Estado', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Estado', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Estado', () => {
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

    describe('addEstadoToCollectionIfMissing', () => {
      it('should add a Estado to an empty array', () => {
        const estado: IEstado = sampleWithRequiredData;
        expectedResult = service.addEstadoToCollectionIfMissing([], estado);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(estado);
      });

      it('should not add a Estado to an array that contains it', () => {
        const estado: IEstado = sampleWithRequiredData;
        const estadoCollection: IEstado[] = [
          {
            ...estado,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEstadoToCollectionIfMissing(estadoCollection, estado);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Estado to an array that doesn't contain it", () => {
        const estado: IEstado = sampleWithRequiredData;
        const estadoCollection: IEstado[] = [sampleWithPartialData];
        expectedResult = service.addEstadoToCollectionIfMissing(estadoCollection, estado);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(estado);
      });

      it('should add only unique Estado to an array', () => {
        const estadoArray: IEstado[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const estadoCollection: IEstado[] = [sampleWithRequiredData];
        expectedResult = service.addEstadoToCollectionIfMissing(estadoCollection, ...estadoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const estado: IEstado = sampleWithRequiredData;
        const estado2: IEstado = sampleWithPartialData;
        expectedResult = service.addEstadoToCollectionIfMissing([], estado, estado2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(estado);
        expect(expectedResult).toContain(estado2);
      });

      it('should accept null and undefined values', () => {
        const estado: IEstado = sampleWithRequiredData;
        expectedResult = service.addEstadoToCollectionIfMissing([], null, estado, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(estado);
      });

      it('should return initial array if no Estado is added', () => {
        const estadoCollection: IEstado[] = [sampleWithRequiredData];
        expectedResult = service.addEstadoToCollectionIfMissing(estadoCollection, undefined, null);
        expect(expectedResult).toEqual(estadoCollection);
      });
    });

    describe('compareEstado', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEstado(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEstado(entity1, entity2);
        const compareResult2 = service.compareEstado(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEstado(entity1, entity2);
        const compareResult2 = service.compareEstado(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEstado(entity1, entity2);
        const compareResult2 = service.compareEstado(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
