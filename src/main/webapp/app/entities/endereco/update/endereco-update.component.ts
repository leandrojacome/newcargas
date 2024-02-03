import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICidade } from 'app/entities/cidade/cidade.model';
import { CidadeService } from 'app/entities/cidade/service/cidade.service';
import { TipoEndereco } from 'app/entities/enumerations/tipo-endereco.model';
import { EnderecoService } from '../service/endereco.service';
import { IEndereco, NewEndereco } from '../endereco.model';
import { EnderecoFormGroup, EnderecoFormService } from './endereco-form.service';
import { EstadoService } from '../../estado/service/estado.service';
import { IEstado } from '../../estado/estado.model';
import { TransportadoraCommunicationService } from '../../transportadora/service/transportadora-communication-service';

@Component({
  standalone: true,
  selector: 'jhi-endereco-update',
  templateUrl: './endereco-update.component.html',
  styleUrl: './endereco-update.component.scss',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnderecoUpdateComponent implements OnInit {
  @Input('tipoEndereco') tipoEndereco: TipoEndereco | null = null;
  @Input('idParent') idParent?: number = 0;

  isSaving = false;
  endereco: IEndereco | null = null;

  enderecos: IEndereco[] = [];
  estadosSharedCollection: IEstado[] = [];
  cidadesSharedCollection: ICidade[] = [];

  editEnderecoForm: EnderecoFormGroup = this.enderecoFormService.createEnderecoFormGroup();

  constructor(
    protected enderecoService: EnderecoService,
    protected enderecoFormService: EnderecoFormService,
    protected estadoService: EstadoService,
    protected cidadeService: CidadeService,
    protected transpotadoraCommunicationService: TransportadoraCommunicationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareEstado = (o1: IEstado | null, o2: IEstado | null): boolean => this.estadoService.compareEstado(o1, o2);

  compareCidade = (o1: ICidade | null, o2: ICidade | null): boolean => this.cidadeService.compareCidade(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ endereco }) => {
      this.endereco = endereco;
      if (endereco) {
        this.updateForm(endereco);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    let endereco = this.enderecoFormService.getEndereco(this.editEnderecoForm);
    const enderecoConvertido: IEndereco = this.convertToIEndereco(endereco);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEndereco>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(endereco: IEndereco): void {
    this.endereco = endereco;
    this.enderecoFormService.resetForm(this.editEnderecoForm, endereco);

    this.estadosSharedCollection = this.estadoService.addEstadoToCollectionIfMissing<IEstado>(
      this.estadosSharedCollection,
      endereco.estado,
    );

    this.cidadesSharedCollection = this.cidadeService.addCidadeToCollectionIfMissing<ICidade>(
      this.cidadesSharedCollection,
      endereco.cidade,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.estadoService
      .query()
      .pipe(map((res: HttpResponse<IEstado[]>) => res.body ?? []))
      .pipe(map((estados: IEstado[]) => this.estadoService.addEstadoToCollectionIfMissing<IEstado>(estados, this.endereco?.estado)))
      .subscribe((estados: IEstado[]) => (this.estadosSharedCollection = estados));
  }

  buscarCep() {}

  addEndereco() {
    let endereco = this.enderecoFormService.getEndereco(this.editEnderecoForm);
    endereco.tipo = this.tipoEndereco;
    const enderecoConvertido = this.convertToIEndereco(endereco);
    this.getCompletedEndereco(endereco, enderecoConvertido);
    this.selectStackHolderId(enderecoConvertido);
    this.addToEnderecoList(enderecoConvertido);
  }

  private addToEnderecoList(enderecoConvertido: IEndereco) {
    this.enderecos.push(enderecoConvertido);
    this.transpotadoraCommunicationService.addEndereco(enderecoConvertido);
    this.endereco = null;
    this.editEnderecoForm = this.enderecoFormService.createEnderecoFormGroup();
  }

  private getCompletedEndereco(
    endereco:
      | IEndereco
      | (Omit<IEndereco, 'id'> & {
          id?: number | null;
        }),
    enderecoConvertido: IEndereco,
  ) {
    this.cidadeService.find(Number(endereco.cidade)).subscribe(cidadeData => {
      enderecoConvertido.cidade = { id: cidadeData.body?.id as number, nome: cidadeData.body?.nome };
    });
    this.estadoService.find(Number(endereco.estado)).subscribe(estadoData => {
      enderecoConvertido.estado = { id: estadoData.body?.id as number, nome: estadoData.body?.nome };
    });
  }

  private selectStackHolderId(enderecoConvertido: IEndereco) {
    if (this.idParent !== undefined && this.idParent !== null && this.idParent !== 0) {
      switch (this.tipoEndereco) {
        case TipoEndereco.EMBARCADOR:
          enderecoConvertido.embarcador = { id: this.idParent };
          break;
        case TipoEndereco.TRANSPORTADORA:
          enderecoConvertido.transportadora = { id: this.idParent };
          break;
        case TipoEndereco.NOTA_FISCAL_COLETA_ORIGEM:
          enderecoConvertido.notaFiscalColetaOrigem = { id: this.idParent };
          break;
        case TipoEndereco.NOTA_FISCAL_COLETA_DESTINO:
          enderecoConvertido.notaFiscalColetaDestino = { id: this.idParent };
          break;
        case TipoEndereco.SOLICITACAO_COLETA_ORIGEM:
          enderecoConvertido.solicitacaoColetaOrigem = { id: this.idParent };
          break;
        case TipoEndereco.SOLICITACAO_COLETA_DESTINO:
          enderecoConvertido.solicitacaoColetaDestino = { id: this.idParent };
          break;
      }
    }
  }

  private convertToIEndereco(endereco: IEndereco | NewEndereco): IEndereco {
    const id = endereco.id === undefined || endereco.id === null ? 0 : endereco.id;
    return { ...endereco, id };
  }

  loadCidades() {
    this.cidadeService
      .query({ 'estadoId.equals': this.editEnderecoForm.get('estado')?.value ?? 0 })
      .pipe(map((res: HttpResponse<ICidade[]>) => res.body ?? []))
      .pipe(map((cidades: ICidade[]) => this.cidadeService.addCidadeToCollectionIfMissing<ICidade>(cidades, this.endereco?.cidade)))
      .subscribe((cidades: ICidade[]) => (this.cidadesSharedCollection = cidades));
  }

  delete(endereco: any) {
    this.transpotadoraCommunicationService.removeEndereco(endereco);
    this.enderecos.splice(this.enderecos.indexOf(endereco), 1);
  }

  edit(endereco: any) {
    this.endereco = endereco;
    this.transpotadoraCommunicationService.updateEndereco(endereco);
    this.updateForm(endereco);
  }
}
