import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import InfluenceurResolve from './route/influenceur-routing-resolve.service';

const influenceurRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/influenceur').then(m => m.Influenceur),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/influenceur-detail').then(m => m.InfluenceurDetail),
    resolve: {
      influenceur: InfluenceurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/influenceur-update').then(m => m.InfluenceurUpdate),
    resolve: {
      influenceur: InfluenceurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/influenceur-update').then(m => m.InfluenceurUpdate),
    resolve: {
      influenceur: InfluenceurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default influenceurRoute;
