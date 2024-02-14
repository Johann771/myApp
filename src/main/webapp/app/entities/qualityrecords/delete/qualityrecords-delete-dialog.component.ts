import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IQualityrecords } from '../qualityrecords.model';
import { QualityrecordsService } from '../service/qualityrecords.service';

@Component({
  standalone: true,
  templateUrl: './qualityrecords-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class QualityrecordsDeleteDialogComponent {
  qualityrecords?: IQualityrecords;

  constructor(
    protected qualityrecordsService: QualityrecordsService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.qualityrecordsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
