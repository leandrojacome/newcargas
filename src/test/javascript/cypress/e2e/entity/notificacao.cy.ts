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
    assunto: 'exotic drat frightfully',
    mensagem: 'hike dedication',
    dataHoraEnvio: '2024-01-20T17:04:11.395Z',
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

      it.skip('edit button click should load edit Notificacao page and save', () => {
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
      cy.get(`[data-cy="tipo"]`).select('EMBARCADOR');

      cy.get(`[data-cy="email"]`).type('Paulo10@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Paulo10@hotmail.com');

      cy.get(`[data-cy="telefone"]`).type('thorough fo');
      cy.get(`[data-cy="telefone"]`).should('have.value', 'thorough fo');

      cy.get(`[data-cy="assunto"]`).type('gadzooks yippee');
      cy.get(`[data-cy="assunto"]`).should('have.value', 'gadzooks yippee');

      cy.get(`[data-cy="mensagem"]`).type('silky minus');
      cy.get(`[data-cy="mensagem"]`).should('have.value', 'silky minus');

      cy.get(`[data-cy="dataHoraEnvio"]`).type('2024-01-20T13:46');
      cy.get(`[data-cy="dataHoraEnvio"]`).blur();
      cy.get(`[data-cy="dataHoraEnvio"]`).should('have.value', '2024-01-20T13:46');

      cy.get(`[data-cy="dataHoraLeitura"]`).type('2024-01-20T15:58');
      cy.get(`[data-cy="dataHoraLeitura"]`).blur();
      cy.get(`[data-cy="dataHoraLeitura"]`).should('have.value', '2024-01-20T15:58');

      cy.get(`[data-cy="lido"]`).should('not.be.checked');
      cy.get(`[data-cy="lido"]`).click();
      cy.get(`[data-cy="lido"]`).should('be.checked');

      cy.get(`[data-cy="dataLeitura"]`).type('2024-01-20T01:24');
      cy.get(`[data-cy="dataLeitura"]`).blur();
      cy.get(`[data-cy="dataLeitura"]`).should('have.value', '2024-01-20T01:24');

      cy.get(`[data-cy="removido"]`).should('not.be.checked');
      cy.get(`[data-cy="removido"]`).click();
      cy.get(`[data-cy="removido"]`).should('be.checked');

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
