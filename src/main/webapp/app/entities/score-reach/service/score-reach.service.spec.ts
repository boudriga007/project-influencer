import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IScoreReach } from '../score-reach.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../score-reach.test-samples';

import { ScoreReachService } from './score-reach.service';

const requireRestSample: IScoreReach = {
  ...sampleWithRequiredData,
};

describe('ScoreReach Service', () => {
  let service: ScoreReachService;
  let httpMock: HttpTestingController;
  let expectedResult: IScoreReach | IScoreReach[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ScoreReachService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ScoreReach', () => {
      const scoreReach = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scoreReach).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ScoreReach', () => {
      const scoreReach = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scoreReach).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ScoreReach', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ScoreReach', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ScoreReach', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests.length).toBe(1);
    });

    describe('addScoreReachToCollectionIfMissing', () => {
      it('should add a ScoreReach to an empty array', () => {
        const scoreReach: IScoreReach = sampleWithRequiredData;
        expectedResult = service.addScoreReachToCollectionIfMissing([], scoreReach);
        expect(expectedResult).toEqual([scoreReach]);
      });

      it('should not add a ScoreReach to an array that contains it', () => {
        const scoreReach: IScoreReach = sampleWithRequiredData;
        const scoreReachCollection: IScoreReach[] = [
          {
            ...scoreReach,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScoreReachToCollectionIfMissing(scoreReachCollection, scoreReach);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ScoreReach to an array that doesn't contain it", () => {
        const scoreReach: IScoreReach = sampleWithRequiredData;
        const scoreReachCollection: IScoreReach[] = [sampleWithPartialData];
        expectedResult = service.addScoreReachToCollectionIfMissing(scoreReachCollection, scoreReach);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scoreReach);
      });

      it('should add only unique ScoreReach to an array', () => {
        const scoreReachArray: IScoreReach[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scoreReachCollection: IScoreReach[] = [sampleWithRequiredData];
        expectedResult = service.addScoreReachToCollectionIfMissing(scoreReachCollection, ...scoreReachArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scoreReach: IScoreReach = sampleWithRequiredData;
        const scoreReach2: IScoreReach = sampleWithPartialData;
        expectedResult = service.addScoreReachToCollectionIfMissing([], scoreReach, scoreReach2);
        expect(expectedResult).toEqual([scoreReach, scoreReach2]);
      });

      it('should accept null and undefined values', () => {
        const scoreReach: IScoreReach = sampleWithRequiredData;
        expectedResult = service.addScoreReachToCollectionIfMissing([], null, scoreReach, undefined);
        expect(expectedResult).toEqual([scoreReach]);
      });

      it('should return initial array if no ScoreReach is added', () => {
        const scoreReachCollection: IScoreReach[] = [sampleWithRequiredData];
        expectedResult = service.addScoreReachToCollectionIfMissing(scoreReachCollection, undefined, null);
        expect(expectedResult).toEqual(scoreReachCollection);
      });
    });

    describe('compareScoreReach', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScoreReach(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6334 };
        const entity2 = null;

        const compareResult1 = service.compareScoreReach(entity1, entity2);
        const compareResult2 = service.compareScoreReach(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6334 };
        const entity2 = { id: 27588 };

        const compareResult1 = service.compareScoreReach(entity1, entity2);
        const compareResult2 = service.compareScoreReach(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6334 };
        const entity2 = { id: 6334 };

        const compareResult1 = service.compareScoreReach(entity1, entity2);
        const compareResult2 = service.compareScoreReach(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
