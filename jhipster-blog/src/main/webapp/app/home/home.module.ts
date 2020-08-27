import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DossierSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [DossierSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class DossierHomeModule {}
