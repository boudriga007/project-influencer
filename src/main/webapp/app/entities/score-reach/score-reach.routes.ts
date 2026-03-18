import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ScoreReachResolve from './route/score-reach-routing-resolve.service';

const scoreReachRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/score-reach').then(m => m.ScoreReach),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/score-reach-detail').then(m => m.ScoreReachDetail),
    resolve: {
      scoreReach: ScoreReachResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/score-reach-update').then(m => m.ScoreReachUpdate),
    resolve: {
      scoreReach: ScoreReachResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/score-reach-update').then(m => m.ScoreReachUpdate),
    resolve: {
      scoreReach: ScoreReachResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scoreReachRoute;
