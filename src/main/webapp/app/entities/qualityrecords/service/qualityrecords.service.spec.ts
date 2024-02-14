import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQualityrecords } from '../qualityrecords.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../qualityrecords.test-samples';

import { QualityrecordsService } from './qualityrecords.service';

const requireRestSample: IQualityrecords = {
  ...sampleWithRequiredData,
};

describe('Qualityrecords Service', () => {
  let service: QualityrecordsService;
  let httpMock: HttpTestingController;
  let expectedResult: IQualityrecords | IQualityrecords[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QualityrecordsService);
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

    it('should create a Qualityrecords', () => {
      const qualityrecords = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(qualityrecords).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Qualityrecords', () => {
      const qualityrecords = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(qualityrecords).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Qualityrecords', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Qualityrecords', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Qualityrecords', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addQualityrecordsToCollectionIfMissing', () => {
      it('should add a Qualityrecords to an empty array', () => {
        const qualityrecords: IQualityrecords = sampleWithRequiredData;
        expectedResult = service.addQualityrecordsToCollectionIfMissing([], qualityrecords);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(qualityrecords);
      });

      it('should not add a Qualityrecords to an array that contains it', () => {
        const qualityrecords: IQualityrecords = sampleWithRequiredData;
        const qualityrecordsCollection: IQualityrecords[] = [
          {
            ...qualityrecords,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addQualityrecordsToCollectionIfMissing(qualityrecordsCollection, qualityrecords);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Qualityrecords to an array that doesn't contain it", () => {
        const qualityrecords: IQualityrecords = sampleWithRequiredData;
        const qualityrecordsCollection: IQualityrecords[] = [sampleWithPartialData];
        expectedResult = service.addQualityrecordsToCollectionIfMissing(qualityrecordsCollection, qualityrecords);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(qualityrecords);
      });

      it('should add only unique Qualityrecords to an array', () => {
        const qualityrecordsArray: IQualityrecords[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const qualityrecordsCollection: IQualityrecords[] = [sampleWithRequiredData];
        expectedResult = service.addQualityrecordsToCollectionIfMissing(qualityrecordsCollection, ...qualityrecordsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const qualityrecords: IQualityrecords = sampleWithRequiredData;
        const qualityrecords2: IQualityrecords = sampleWithPartialData;
        expectedResult = service.addQualityrecordsToCollectionIfMissing([], qualityrecords, qualityrecords2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(qualityrecords);
        expect(expectedResult).toContain(qualityrecords2);
      });

      it('should accept null and undefined values', () => {
        const qualityrecords: IQualityrecords = sampleWithRequiredData;
        expectedResult = service.addQualityrecordsToCollectionIfMissing([], null, qualityrecords, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(qualityrecords);
      });

      it('should return initial array if no Qualityrecords is added', () => {
        const qualityrecordsCollection: IQualityrecords[] = [sampleWithRequiredData];
        expectedResult = service.addQualityrecordsToCollectionIfMissing(qualityrecordsCollection, undefined, null);
        expect(expectedResult).toEqual(qualityrecordsCollection);
      });
    });

    describe('compareQualityrecords', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareQualityrecords(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareQualityrecords(entity1, entity2);
        const compareResult2 = service.compareQualityrecords(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareQualityrecords(entity1, entity2);
        const compareResult2 = service.compareQualityrecords(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareQualityrecords(entity1, entity2);
        const compareResult2 = service.compareQualityrecords(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
