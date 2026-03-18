import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IScoreReach, NewScoreReach } from '../score-reach.model';

export type PartialUpdateScoreReach = Partial<IScoreReach> & Pick<IScoreReach, 'id'>;

@Injectable()
export class ScoreReachesService {
  readonly scoreReachesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly scoreReachesResource = httpResource<IScoreReach[]>(() => {
    const params = this.scoreReachesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of scoreReach that have been fetched. It is updated when the scoreReachesResource emits a new value.
   * In case of error while fetching the scoreReaches, the signal is set to an empty array.
   */
  readonly scoreReaches = computed(() => (this.scoreReachesResource.hasValue() ? this.scoreReachesResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/score-reaches');
}

@Injectable({ providedIn: 'root' })
export class ScoreReachService extends ScoreReachesService {
  protected readonly http = inject(HttpClient);

  create(scoreReach: NewScoreReach): Observable<IScoreReach> {
    return this.http.post<IScoreReach>(this.resourceUrl, scoreReach);
  }

  update(scoreReach: IScoreReach): Observable<IScoreReach> {
    return this.http.put<IScoreReach>(`${this.resourceUrl}/${encodeURIComponent(this.getScoreReachIdentifier(scoreReach))}`, scoreReach);
  }

  partialUpdate(scoreReach: PartialUpdateScoreReach): Observable<IScoreReach> {
    return this.http.patch<IScoreReach>(`${this.resourceUrl}/${encodeURIComponent(this.getScoreReachIdentifier(scoreReach))}`, scoreReach);
  }

  find(id: number): Observable<IScoreReach> {
    return this.http.get<IScoreReach>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IScoreReach[]>> {
    const options = createRequestOption(req);
    return this.http.get<IScoreReach[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getScoreReachIdentifier(scoreReach: Pick<IScoreReach, 'id'>): number {
    return scoreReach.id;
  }

  compareScoreReach(o1: Pick<IScoreReach, 'id'> | null, o2: Pick<IScoreReach, 'id'> | null): boolean {
    return o1 && o2 ? this.getScoreReachIdentifier(o1) === this.getScoreReachIdentifier(o2) : o1 === o2;
  }

  addScoreReachToCollectionIfMissing<Type extends Pick<IScoreReach, 'id'>>(
    scoreReachCollection: Type[],
    ...scoreReachesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scoreReaches: Type[] = scoreReachesToCheck.filter(isPresent);
    if (scoreReaches.length > 0) {
      const scoreReachCollectionIdentifiers = scoreReachCollection.map(scoreReachItem => this.getScoreReachIdentifier(scoreReachItem));
      const scoreReachesToAdd = scoreReaches.filter(scoreReachItem => {
        const scoreReachIdentifier = this.getScoreReachIdentifier(scoreReachItem);
        if (scoreReachCollectionIdentifiers.includes(scoreReachIdentifier)) {
          return false;
        }
        scoreReachCollectionIdentifiers.push(scoreReachIdentifier);
        return true;
      });
      return [...scoreReachesToAdd, ...scoreReachCollection];
    }
    return scoreReachCollection;
  }
}
