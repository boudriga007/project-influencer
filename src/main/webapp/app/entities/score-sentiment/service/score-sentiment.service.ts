import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IScoreSentiment, NewScoreSentiment } from '../score-sentiment.model';

export type PartialUpdateScoreSentiment = Partial<IScoreSentiment> & Pick<IScoreSentiment, 'id'>;

@Injectable()
export class ScoreSentimentsService {
  readonly scoreSentimentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly scoreSentimentsResource = httpResource<IScoreSentiment[]>(() => {
    const params = this.scoreSentimentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of scoreSentiment that have been fetched. It is updated when the scoreSentimentsResource emits a new value.
   * In case of error while fetching the scoreSentiments, the signal is set to an empty array.
   */
  readonly scoreSentiments = computed(() => (this.scoreSentimentsResource.hasValue() ? this.scoreSentimentsResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/score-sentiments');
}

@Injectable({ providedIn: 'root' })
export class ScoreSentimentService extends ScoreSentimentsService {
  protected readonly http = inject(HttpClient);

  create(scoreSentiment: NewScoreSentiment): Observable<IScoreSentiment> {
    return this.http.post<IScoreSentiment>(this.resourceUrl, scoreSentiment);
  }

  update(scoreSentiment: IScoreSentiment): Observable<IScoreSentiment> {
    return this.http.put<IScoreSentiment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getScoreSentimentIdentifier(scoreSentiment))}`,
      scoreSentiment,
    );
  }

  partialUpdate(scoreSentiment: PartialUpdateScoreSentiment): Observable<IScoreSentiment> {
    return this.http.patch<IScoreSentiment>(
      `${this.resourceUrl}/${encodeURIComponent(this.getScoreSentimentIdentifier(scoreSentiment))}`,
      scoreSentiment,
    );
  }

  find(id: number): Observable<IScoreSentiment> {
    return this.http.get<IScoreSentiment>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IScoreSentiment[]>> {
    const options = createRequestOption(req);
    return this.http.get<IScoreSentiment[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getScoreSentimentIdentifier(scoreSentiment: Pick<IScoreSentiment, 'id'>): number {
    return scoreSentiment.id;
  }

  compareScoreSentiment(o1: Pick<IScoreSentiment, 'id'> | null, o2: Pick<IScoreSentiment, 'id'> | null): boolean {
    return o1 && o2 ? this.getScoreSentimentIdentifier(o1) === this.getScoreSentimentIdentifier(o2) : o1 === o2;
  }

  addScoreSentimentToCollectionIfMissing<Type extends Pick<IScoreSentiment, 'id'>>(
    scoreSentimentCollection: Type[],
    ...scoreSentimentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scoreSentiments: Type[] = scoreSentimentsToCheck.filter(isPresent);
    if (scoreSentiments.length > 0) {
      const scoreSentimentCollectionIdentifiers = scoreSentimentCollection.map(scoreSentimentItem =>
        this.getScoreSentimentIdentifier(scoreSentimentItem),
      );
      const scoreSentimentsToAdd = scoreSentiments.filter(scoreSentimentItem => {
        const scoreSentimentIdentifier = this.getScoreSentimentIdentifier(scoreSentimentItem);
        if (scoreSentimentCollectionIdentifiers.includes(scoreSentimentIdentifier)) {
          return false;
        }
        scoreSentimentCollectionIdentifiers.push(scoreSentimentIdentifier);
        return true;
      });
      return [...scoreSentimentsToAdd, ...scoreSentimentCollection];
    }
    return scoreSentimentCollection;
  }
}
