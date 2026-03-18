import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IScoreSentiment } from '../score-sentiment.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../score-sentiment.test-samples';

import { ScoreSentimentService } from './score-sentiment.service';

const requireRestSample: IScoreSentiment = {
  ...sampleWithRequiredData,
};

describe('ScoreSentiment Service', () => {
  let service: ScoreSentimentService;
  let httpMock: HttpTestingController;
  let expectedResult: IScoreSentiment | IScoreSentiment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ScoreSentimentService);
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

    it('should create a ScoreSentiment', () => {
      const scoreSentiment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(scoreSentiment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ScoreSentiment', () => {
      const scoreSentiment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(scoreSentiment).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ScoreSentiment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ScoreSentiment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ScoreSentiment', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests.length).toBe(1);
    });

    describe('addScoreSentimentToCollectionIfMissing', () => {
      it('should add a ScoreSentiment to an empty array', () => {
        const scoreSentiment: IScoreSentiment = sampleWithRequiredData;
        expectedResult = service.addScoreSentimentToCollectionIfMissing([], scoreSentiment);
        expect(expectedResult).toEqual([scoreSentiment]);
      });

      it('should not add a ScoreSentiment to an array that contains it', () => {
        const scoreSentiment: IScoreSentiment = sampleWithRequiredData;
        const scoreSentimentCollection: IScoreSentiment[] = [
          {
            ...scoreSentiment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addScoreSentimentToCollectionIfMissing(scoreSentimentCollection, scoreSentiment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ScoreSentiment to an array that doesn't contain it", () => {
        const scoreSentiment: IScoreSentiment = sampleWithRequiredData;
        const scoreSentimentCollection: IScoreSentiment[] = [sampleWithPartialData];
        expectedResult = service.addScoreSentimentToCollectionIfMissing(scoreSentimentCollection, scoreSentiment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(scoreSentiment);
      });

      it('should add only unique ScoreSentiment to an array', () => {
        const scoreSentimentArray: IScoreSentiment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const scoreSentimentCollection: IScoreSentiment[] = [sampleWithRequiredData];
        expectedResult = service.addScoreSentimentToCollectionIfMissing(scoreSentimentCollection, ...scoreSentimentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const scoreSentiment: IScoreSentiment = sampleWithRequiredData;
        const scoreSentiment2: IScoreSentiment = sampleWithPartialData;
        expectedResult = service.addScoreSentimentToCollectionIfMissing([], scoreSentiment, scoreSentiment2);
        expect(expectedResult).toEqual([scoreSentiment, scoreSentiment2]);
      });

      it('should accept null and undefined values', () => {
        const scoreSentiment: IScoreSentiment = sampleWithRequiredData;
        expectedResult = service.addScoreSentimentToCollectionIfMissing([], null, scoreSentiment, undefined);
        expect(expectedResult).toEqual([scoreSentiment]);
      });

      it('should return initial array if no ScoreSentiment is added', () => {
        const scoreSentimentCollection: IScoreSentiment[] = [sampleWithRequiredData];
        expectedResult = service.addScoreSentimentToCollectionIfMissing(scoreSentimentCollection, undefined, null);
        expect(expectedResult).toEqual(scoreSentimentCollection);
      });
    });

    describe('compareScoreSentiment', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareScoreSentiment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17421 };
        const entity2 = null;

        const compareResult1 = service.compareScoreSentiment(entity1, entity2);
        const compareResult2 = service.compareScoreSentiment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17421 };
        const entity2 = { id: 30345 };

        const compareResult1 = service.compareScoreSentiment(entity1, entity2);
        const compareResult2 = service.compareScoreSentiment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 17421 };
        const entity2 = { id: 17421 };

        const compareResult1 = service.compareScoreSentiment(entity1, entity2);
        const compareResult2 = service.compareScoreSentiment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
