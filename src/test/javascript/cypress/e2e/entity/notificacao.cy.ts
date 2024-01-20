import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Notificacao e2e test', () => {
  const notificacaoPageUrl = '/notificacao';
  const notificacaoPageUrlPattern = new RegExp('/notificacao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const notificacaoSample = {
    tipo: 'TRANSPORTADORA',
    assunto: 'pharmacopoeia centre',
    mensagem: 'when',
    dataHoraEnvio: '2024-01-20T12:15:54.293Z',
    dataCadastro: '2024-01-20T09:44:41.816Z',
  };

  let notificacao;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/notificacaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/notificacaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/notificacaos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (notificacao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/notificacaos/${notificacao.id}`,
      }).then(() => {
        notificacao = undefined;
      });
    }
  });

  it('Notificacaos menu should load Notificacaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('notificacao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Notificacao').should('exist');
    cy.url().should('match', notificacaoPageUrlPattern);
  });

  describe('Notificacao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(notificacaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Notificacao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/notificacao/new$'));
        cy.getEntityCreateUpdateHeading('Notificacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificacaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/notificacaos',
          body: notificacaoSample,
        }).then(({ body }) => {
          notificacao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/notificacaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/notificacaos?page=0&size=20>; rel="last",<http://localhost/api/notificacaos?page=0&size=20>; rel="first"',
              },
              body: [notificacao],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(notificacaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Notificacao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('notificacao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificacaoPageUrlPattern);
      });

      it('edit button click should load edit Notificacao page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Notificacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificacaoPageUrlPattern);
      });

      it('edit button click should load edit Notificacao page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Notificacao');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificacaoPageUrlPattern);
      });

      it('last delete button click should delete instance of Notificacao', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('notificacao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificacaoPageUrlPattern);

        notificacao = undefined;
      });
    });
  });

  describe('new Notificacao page', () => {
    beforeEach(() => {
      cy.visit(`${notificacaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Notificacao');
    });

    it('should create an instance of Notificacao', () => {
      cy.get(`[data-cy="tipo"]`).select('TRANSPORTADORA');

      cy.get(`[data-cy="email"]`).type('Aline_Batista80@live.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Aline_Batista80@live.com');

      cy.get(`[data-cy="telefone"]`).type('evenXXXXXX');
      cy.get(`[data-cy="telefone"]`).should('have.value', 'evenXXXXXX');

      cy.get(`[data-cy="assunto"]`).type('aftershock ew');
      cy.get(`[data-cy="assunto"]`).should('have.value', 'aftershock ew');

      cy.get(`[data-cy="mensagem"]`).type('hence sans plain');
      cy.get(`[data-cy="mensagem"]`).should('have.value', 'hence sans plain');

      cy.get(`[data-cy="dataHoraEnvio"]`).type('2024-01-20T16:41');
      cy.get(`[data-cy="dataHoraEnvio"]`).blur();
      cy.get(`[data-cy="dataHoraEnvio"]`).should('have.value', '2024-01-20T16:41');

      cy.get(`[data-cy="dataHoraLeitura"]`).type('2024-01-20T12:50');
      cy.get(`[data-cy="dataHoraLeitura"]`).blur();
      cy.get(`[data-cy="dataHoraLeitura"]`).should('have.value', '2024-01-20T12:50');

      cy.get(`[data-cy="dataCadastro"]`).type('2024-01-20T03:14');
      cy.get(`[data-cy="dataCadastro"]`).blur();
      cy.get(`[data-cy="dataCadastro"]`).should('have.value', '2024-01-20T03:14');

      cy.get(`[data-cy="dataAtualizacao"]`).type('2024-01-20T01:43');
      cy.get(`[data-cy="dataAtualizacao"]`).blur();
      cy.get(`[data-cy="dataAtualizacao"]`).should('have.value', '2024-01-20T01:43');

      cy.get(`[data-cy="lido"]`).should('not.be.checked');
      cy.get(`[data-cy="lido"]`).click();
      cy.get(`[data-cy="lido"]`).should('be.checked');

      cy.get(`[data-cy="dataLeitura"]`).type('2024-01-20T02:38');
      cy.get(`[data-cy="dataLeitura"]`).blur();
      cy.get(`[data-cy="dataLeitura"]`).should('have.value', '2024-01-20T02:38');

      cy.get(`[data-cy="removido"]`).should('not.be.checked');
      cy.get(`[data-cy="removido"]`).click();
      cy.get(`[data-cy="removido"]`).should('be.checked');

      cy.get(`[data-cy="dataRemocao"]`).type('2024-01-20T14:42');
      cy.get(`[data-cy="dataRemocao"]`).blur();
      cy.get(`[data-cy="dataRemocao"]`).should('have.value', '2024-01-20T14:42');

      cy.get(`[data-cy="usuarioRemocao"]`).type('elaborate notarise moral');
      cy.get(`[data-cy="usuarioRemocao"]`).should('have.value', 'elaborate notarise moral');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        notificacao = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', notificacaoPageUrlPattern);
    });
  });
});
