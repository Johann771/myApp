import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IQualityrecords } from '../qualityrecords.model';

@Component({
  standalone: true,
  selector: 'jhi-qualityrecords-detail',
  templateUrl: './qualityrecords-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class QualityrecordsDetailComponent {
  @Input() qualityrecords: IQualityrecords | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
