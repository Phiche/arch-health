import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { DossierSharedModule } from 'app/shared/shared.module';
import { DossierCoreModule } from 'app/core/core.module';
import { DossierAppRoutingModule } from './app-routing.module';
import { DossierHomeModule } from './home/home.module';
import { DossierEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    DossierSharedModule,
    DossierCoreModule,
    DossierHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    DossierEntityModule,
    DossierAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class DossierAppModule {}
