import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IInfluenceur, NewInfluenceur } from '../influenceur.model';

export type PartialUpdateInfluenceur = Partial<IInfluenceur> & Pick<IInfluenceur, 'id'>;

@Injectable()
export class InfluenceursService {
  readonly influenceursParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly influenceursResource = httpResource<IInfluenceur[]>(() => {
    const params = this.influenceursParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of influenceur that have been fetched. It is updated when the influenceursResource emits a new value.
   * In case of error while fetching the influenceurs, the signal is set to an empty array.
   */
  readonly influenceurs = computed(() => (this.influenceursResource.hasValue() ? this.influenceursResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/influenceurs');
}

@Injectable({ providedIn: 'root' })
export class InfluenceurService extends InfluenceursService {
  protected readonly http = inject(HttpClient);

  create(influenceur: NewInfluenceur): Observable<IInfluenceur> {
    return this.http.post<IInfluenceur>(this.resourceUrl, influenceur);
  }

  update(influenceur: IInfluenceur): Observable<IInfluenceur> {
    return this.http.put<IInfluenceur>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInfluenceurIdentifier(influenceur))}`,
      influenceur,
    );
  }

  partialUpdate(influenceur: PartialUpdateInfluenceur): Observable<IInfluenceur> {
    return this.http.patch<IInfluenceur>(
      `${this.resourceUrl}/${encodeURIComponent(this.getInfluenceurIdentifier(influenceur))}`,
      influenceur,
    );
  }

  find(id: number): Observable<IInfluenceur> {
    return this.http.get<IInfluenceur>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IInfluenceur[]>> {
    const options = createRequestOption(req);
    return this.http.get<IInfluenceur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInfluenceurIdentifier(influenceur: Pick<IInfluenceur, 'id'>): number {
    return influenceur.id;
  }

  compareInfluenceur(o1: Pick<IInfluenceur, 'id'> | null, o2: Pick<IInfluenceur, 'id'> | null): boolean {
    return o1 && o2 ? this.getInfluenceurIdentifier(o1) === this.getInfluenceurIdentifier(o2) : o1 === o2;
  }

  addInfluenceurToCollectionIfMissing<Type extends Pick<IInfluenceur, 'id'>>(
    influenceurCollection: Type[],
    ...influenceursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const influenceurs: Type[] = influenceursToCheck.filter(isPresent);
    if (influenceurs.length > 0) {
      const influenceurCollectionIdentifiers = influenceurCollection.map(influenceurItem => this.getInfluenceurIdentifier(influenceurItem));
      const influenceursToAdd = influenceurs.filter(influenceurItem => {
        const influenceurIdentifier = this.getInfluenceurIdentifier(influenceurItem);
        if (influenceurCollectionIdentifiers.includes(influenceurIdentifier)) {
          return false;
        }
        influenceurCollectionIdentifiers.push(influenceurIdentifier);
        return true;
      });
      return [...influenceursToAdd, ...influenceurCollection];
    }
    return influenceurCollection;
  }
}
