import { Component } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { InlineDiffComponent } from 'ngx-diff';

import SharedModule from 'app/shared/shared.module';
import { EntityAuditService } from './entity-audit.service';
import { EntityAuditEvent } from './entity-audit-event.model';

@Component({
  standalone: true,
  selector: 'jhi-entity-audit-modal',
  templateUrl: './entity-audit-modal.component.html',
  imports: [SharedModule, InlineDiffComponent],
  styles: [
    `
      /* NOTE: for now the ::ng-deep shadow-piercing descendant combinator is
         * required because Angular defaults to emulated view encapsulation and
         * preprocesses all component styles to approximate shadow scoping
         * rules. This means these styles wouldn't apply to the HTML generated
         * by ng-diff-match-patch.
         *
         * This shouldn't be required when browsers support native
         * encapsulation, at which point ::ng-deep will also be deprecated/removed
         * see https://angular.io/guide/component-styles
         */

      :host ::ng-deep ins {
        color: black;
        background: #bbffbb;
      }

      :host ::ng-deep del {
        color: black;
        background: #ffbbbb;
      }

      .code {
        background: #dcdada;
        padding: 10px;
      }
    `,
  ],
})
export default class EntityAuditModalComponent {
  action?: string;
  left?: string;
  right?: string;

  constructor(
    private service: EntityAuditService,
    public activeModal: NgbActiveModal,
  ) {}

  openChange(audit: EntityAuditEvent): void {
    this.service.getPrevVersion(audit.entityType, audit.entityId, audit.commitVersion!).subscribe((res: HttpResponse<EntityAuditEvent>) => {
      const data: EntityAuditEvent = res.body!;
      const previousVersion = JSON.stringify(JSON.parse(data.entityValue ?? '{}'), null, 2);
      const currentVersion = JSON.stringify(audit.entityValue, null, 2);

      this.action = audit.action;
      this.left = previousVersion;
      this.right = currentVersion;
    });
  }
}
