import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-management',
    data: { pageTitle: 'UserManagements' },
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'influenceur',
    data: { pageTitle: 'Influenceurs' },
    loadChildren: () => import('./influenceur/influenceur.routes'),
  },
  {
    path: 'score-reach',
    data: { pageTitle: 'ScoreReaches' },
    loadChildren: () => import('./score-reach/score-reach.routes'),
  },
  {
    path: 'score-sentiment',
    data: { pageTitle: 'ScoreSentiments' },
    loadChildren: () => import('./score-sentiment/score-sentiment.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
