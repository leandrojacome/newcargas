jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { StatusColetaService } from '../service/status-coleta.service';

import { StatusColetaDeleteDialogComponent } from './status-coleta-delete-dialog.component';

describe('StatusColeta Management Delete Component', () => {
  let comp: StatusColetaDeleteDialogComponent;
  let fixture: ComponentFixture<StatusColetaDeleteDialogComponent>;
  let service: StatusColetaService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, StatusColetaDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(StatusColetaDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StatusColetaDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(StatusColetaService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
