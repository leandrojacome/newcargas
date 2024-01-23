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

describe('StatusColeta e2e test', () => {
  const statusColetaPageUrl = '/status-coleta';
  const statusColetaPageUrlPattern = new RegExp('/status-coleta(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const statusColetaSample = { nome: 'wrought round zowie' };

  let statusColeta;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/status-coletas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/status-coletas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/status-coletas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (statusColeta) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/status-coletas/${statusColeta.id}`,
      }).then(() => {
        statusColeta = undefined;
      });
    }
  });

  it('StatusColetas menu should load StatusColetas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('status-coleta');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StatusColeta').should('exist');
    cy.url().should('match', statusColetaPageUrlPattern);
  });

  describe('StatusColeta page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(statusColetaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StatusColeta page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/status-coleta/new$'));
        cy.getEntityCreateUpdateHeading('StatusColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/status-coletas',
          body: statusColetaSample,
        }).then(({ body }) => {
          statusColeta = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/status-coletas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/status-coletas?page=0&size=20>; rel="last",<http://localhost/api/status-coletas?page=0&size=20>; rel="first"',
              },
              body: [statusColeta],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(statusColetaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StatusColeta page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('statusColeta');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);
      });

      it('edit button click should load edit StatusColeta page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StatusColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);
      });

      it.skip('edit button click should load edit StatusColeta page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StatusColeta');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);
      });

      it('last delete button click should delete instance of StatusColeta', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('statusColeta').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);

        statusColeta = undefined;
      });
    });
  });

  describe('new StatusColeta page', () => {
    beforeEach(() => {
      cy.visit(`${statusColetaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StatusColeta');
    });

    it('should create an instance of StatusColeta', () => {
      cy.get(`[data-cy="nome"]`).type('roar');
      cy.get(`[data-cy="nome"]`).should('have.value', 'roar');

      cy.get(`[data-cy="cor"]`).type('dry veri');
      cy.get(`[data-cy="cor"]`).should('have.value', 'dry veri');

      cy.get(`[data-cy="ordem"]`).type('3');
      cy.get(`[data-cy="ordem"]`).should('have.value', '3');

      cy.get(`[data-cy="estadoInicial"]`).should('not.be.checked');
      cy.get(`[data-cy="estadoInicial"]`).click();
      cy.get(`[data-cy="estadoInicial"]`).should('be.checked');

      cy.get(`[data-cy="estadoFinal"]`).should('not.be.checked');
      cy.get(`[data-cy="estadoFinal"]`).click();
      cy.get(`[data-cy="estadoFinal"]`).should('be.checked');

      cy.get(`[data-cy="permiteCancelar"]`).should('not.be.checked');
      cy.get(`[data-cy="permiteCancelar"]`).click();
      cy.get(`[data-cy="permiteCancelar"]`).should('be.checked');

      cy.get(`[data-cy="permiteEditar"]`).should('not.be.checked');
      cy.get(`[data-cy="permiteEditar"]`).click();
      cy.get(`[data-cy="permiteEditar"]`).should('be.checked');

      cy.get(`[data-cy="permiteExcluir"]`).should('not.be.checked');
      cy.get(`[data-cy="permiteExcluir"]`).click();
      cy.get(`[data-cy="permiteExcluir"]`).should('be.checked');

      cy.get(`[data-cy="descricao"]`).type('freelance fooey underneath');
      cy.get(`[data-cy="descricao"]`).should('have.value', 'freelance fooey underneath');

      cy.get(`[data-cy="ativo"]`).should('not.be.checked');
      cy.get(`[data-cy="ativo"]`).click();
      cy.get(`[data-cy="ativo"]`).should('be.checked');

      cy.get(`[data-cy="removido"]`).should('not.be.checked');
      cy.get(`[data-cy="removido"]`).click();
      cy.get(`[data-cy="removido"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        statusColeta = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', statusColetaPageUrlPattern);
    });
  });
});
