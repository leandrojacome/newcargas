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

describe('HistoricoStatusColeta e2e test', () => {
  const historicoStatusColetaPageUrl = '/historico-status-coleta';
  const historicoStatusColetaPageUrlPattern = new RegExp('/historico-status-coleta(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const historicoStatusColetaSample = { dataCriacao: '2024-01-20T04:08:22.568Z' };

  let historicoStatusColeta;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/historico-status-coletas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/historico-status-coletas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/historico-status-coletas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (historicoStatusColeta) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/historico-status-coletas/${historicoStatusColeta.id}`,
      }).then(() => {
        historicoStatusColeta = undefined;
      });
    }
  });

  it('HistoricoStatusColetas menu should load HistoricoStatusColetas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('historico-status-coleta');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HistoricoStatusColeta').should('exist');
    cy.url().should('match', historicoStatusColetaPageUrlPattern);
  });

  describe('HistoricoStatusColeta page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(historicoStatusColetaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HistoricoStatusColeta page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/historico-status-coleta/new$'));
        cy.getEntityCreateUpdateHeading('HistoricoStatusColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoStatusColetaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/historico-status-coletas',
          body: historicoStatusColetaSample,
        }).then(({ body }) => {
          historicoStatusColeta = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/historico-status-coletas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/historico-status-coletas?page=0&size=20>; rel="last",<http://localhost/api/historico-status-coletas?page=0&size=20>; rel="first"',
              },
              body: [historicoStatusColeta],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(historicoStatusColetaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HistoricoStatusColeta page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('historicoStatusColeta');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoStatusColetaPageUrlPattern);
      });

      it('edit button click should load edit HistoricoStatusColeta page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HistoricoStatusColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoStatusColetaPageUrlPattern);
      });

      it.skip('edit button click should load edit HistoricoStatusColeta page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HistoricoStatusColeta');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoStatusColetaPageUrlPattern);
      });

      it('last delete button click should delete instance of HistoricoStatusColeta', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('historicoStatusColeta').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoStatusColetaPageUrlPattern);

        historicoStatusColeta = undefined;
      });
    });
  });

  describe('new HistoricoStatusColeta page', () => {
    beforeEach(() => {
      cy.visit(`${historicoStatusColetaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HistoricoStatusColeta');
    });

    it('should create an instance of HistoricoStatusColeta', () => {
      cy.get(`[data-cy="dataCriacao"]`).type('2024-01-20T06:19');
      cy.get(`[data-cy="dataCriacao"]`).blur();
      cy.get(`[data-cy="dataCriacao"]`).should('have.value', '2024-01-20T06:19');

      cy.get(`[data-cy="observacao"]`).type('plus');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'plus');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        historicoStatusColeta = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', historicoStatusColetaPageUrlPattern);
    });
  });
});
