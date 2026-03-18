import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ScoreSentimentResolve from './route/score-sentiment-routing-resolve.service';

const scoreSentimentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/score-sentiment').then(m => m.ScoreSentiment),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/score-sentiment-detail').then(m => m.ScoreSentimentDetail),
    resolve: {
      scoreSentiment: ScoreSentimentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/score-sentiment-update').then(m => m.ScoreSentimentUpdate),
    resolve: {
      scoreSentiment: ScoreSentimentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/score-sentiment-update').then(m => m.ScoreSentimentUpdate),
    resolve: {
      scoreSentiment: ScoreSentimentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default scoreSentimentRoute;
