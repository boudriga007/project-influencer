import { Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ScoreReachDeleteDialog } from '../delete/score-reach-delete-dialog';
import { IScoreReach } from '../score-reach.model';
import { ScoreReachService } from '../service/score-reach.service';

@Component({
  selector: 'jhi-score-reach',
  templateUrl: './score-reach.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective],
})
export class ScoreReach implements OnInit {
  subscription: Subscription | null = null;
  readonly scoreReaches = signal<IScoreReach[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly scoreReachService = inject(ScoreReachService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.scoreReachService.scoreReachesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.scoreReaches.set(this.fillComponentAttributesFromResponseBody([...this.scoreReachService.scoreReaches()]));
    });
  }

  trackId = (item: IScoreReach): number => this.scoreReachService.getScoreReachIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.scoreReaches().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(scoreReach: IScoreReach): void {
    const modalRef = this.modalService.open(ScoreReachDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.scoreReach = scoreReach;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected refineData(data: IScoreReach[]): IScoreReach[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IScoreReach[]): IScoreReach[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.scoreReachService.scoreReachesParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
