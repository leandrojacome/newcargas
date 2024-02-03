import { Injectable } from '@angular/core';
import { IEndereco } from '../../endereco/endereco.model';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TransportadoraCommunicationService {
  private _enderecos = new BehaviorSubject<IEndereco[] | []>([]);

  enderecosUpdated$ = this._enderecos.asObservable();

  get enderecos(): IEndereco[] {
    return this._enderecos.getValue();
  }

  addEndereco(endereco: IEndereco): void {
    if (!endereco.id) {
      endereco.id = this.getLastId();
    }
    const enderecos = this._enderecos.getValue();
    this._enderecos.next([...enderecos, endereco]);
  }

  private getLastId(): number {
    const enderecos = this._enderecos.getValue();
    return enderecos[enderecos.length + 1].id as number;
  }

  removeEndereco(endereco: IEndereco): void {
    const enderecos = this._enderecos.getValue();
    const index = enderecos.findIndex(e => e.id === endereco.id);
    if (index > -1) {
      enderecos.splice(index, 1);
      this._enderecos.next(enderecos);
    }
  }

  updateEndereco(endereco: IEndereco): void {
    const enderecos = this._enderecos.getValue();
    const index = enderecos.findIndex(e => e.id === endereco.id);
    if (index > -1) {
      enderecos[index] = endereco;
      this._enderecos.next(enderecos);
    }
  }
}
