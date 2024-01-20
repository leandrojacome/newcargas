import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'estado',
    data: { pageTitle: 'Estados' },
    loadChildren: () => import('./estado/estado.routes'),
  },
  {
    path: 'cidade',
    data: { pageTitle: 'Cidades' },
    loadChildren: () => import('./cidade/cidade.routes'),
  },
  {
    path: 'embarcador',
    data: { pageTitle: 'Embarcadors' },
    loadChildren: () => import('./embarcador/embarcador.routes'),
  },
  {
    path: 'transportadora',
    data: { pageTitle: 'Transportadoras' },
    loadChildren: () => import('./transportadora/transportadora.routes'),
  },
  {
    path: 'banco',
    data: { pageTitle: 'Bancos' },
    loadChildren: () => import('./banco/banco.routes'),
  },
  {
    path: 'conta-bancaria',
    data: { pageTitle: 'ContaBancarias' },
    loadChildren: () => import('./conta-bancaria/conta-bancaria.routes'),
  },
  {
    path: 'tabela-frete',
    data: { pageTitle: 'TabelaFretes' },
    loadChildren: () => import('./tabela-frete/tabela-frete.routes'),
  },
  {
    path: 'endereco',
    data: { pageTitle: 'Enderecos' },
    loadChildren: () => import('./endereco/endereco.routes'),
  },
  {
    path: 'forma-cobranca',
    data: { pageTitle: 'FormaCobrancas' },
    loadChildren: () => import('./forma-cobranca/forma-cobranca.routes'),
  },
  {
    path: 'tipo-frete',
    data: { pageTitle: 'TipoFretes' },
    loadChildren: () => import('./tipo-frete/tipo-frete.routes'),
  },
  {
    path: 'tipo-veiculo',
    data: { pageTitle: 'TipoVeiculos' },
    loadChildren: () => import('./tipo-veiculo/tipo-veiculo.routes'),
  },
  {
    path: 'tipo-carga',
    data: { pageTitle: 'TipoCargas' },
    loadChildren: () => import('./tipo-carga/tipo-carga.routes'),
  },
  {
    path: 'solicitacao-coleta',
    data: { pageTitle: 'SolicitacaoColetas' },
    loadChildren: () => import('./solicitacao-coleta/solicitacao-coleta.routes'),
  },
  {
    path: 'nota-fiscal-coleta',
    data: { pageTitle: 'NotaFiscalColetas' },
    loadChildren: () => import('./nota-fiscal-coleta/nota-fiscal-coleta.routes'),
  },
  {
    path: 'status-coleta',
    data: { pageTitle: 'StatusColetas' },
    loadChildren: () => import('./status-coleta/status-coleta.routes'),
  },
  {
    path: 'historico-status-coleta',
    data: { pageTitle: 'HistoricoStatusColetas' },
    loadChildren: () => import('./historico-status-coleta/historico-status-coleta.routes'),
  },
  {
    path: 'roteirizacao',
    data: { pageTitle: 'Roteirizacaos' },
    loadChildren: () => import('./roteirizacao/roteirizacao.routes'),
  },
  {
    path: 'tomada-preco',
    data: { pageTitle: 'TomadaPrecos' },
    loadChildren: () => import('./tomada-preco/tomada-preco.routes'),
  },
  {
    path: 'notificacao',
    data: { pageTitle: 'Notificacaos' },
    loadChildren: () => import('./notificacao/notificacao.routes'),
  },
  {
    path: 'contratacao',
    data: { pageTitle: 'Contratacaos' },
    loadChildren: () => import('./contratacao/contratacao.routes'),
  },
  {
    path: 'fatura',
    data: { pageTitle: 'Faturas' },
    loadChildren: () => import('./fatura/fatura.routes'),
  },
  {
    path: 'regiao',
    data: { pageTitle: 'Regiaos' },
    loadChildren: () => import('./regiao/regiao.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
