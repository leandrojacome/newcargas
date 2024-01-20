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

describe('Banco e2e test', () => {
  const bancoPageUrl = '/banco';
  const bancoPageUrlPattern = new RegExp('/banco(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const bancoSample = { nome: 'busk secret yippee' };

  let banco;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bancos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bancos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bancos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (banco) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bancos/${banco.id}`,
      }).then(() => {
        banco = undefined;
      });
    }
  });

  it('Bancos menu should load Bancos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('banco');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Banco').should('exist');
    cy.url().should('match', bancoPageUrlPattern);
  });

  describe('Banco page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bancoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Banco page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/banco/new$'));
        cy.getEntityCreateUpdateHeading('Banco');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bancoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bancos',
          body: bancoSample,
        }).then(({ body }) => {
          banco = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bancos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/bancos?page=0&size=20>; rel="last",<http://localhost/api/bancos?page=0&size=20>; rel="first"',
              },
              body: [banco],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(bancoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Banco page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('banco');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bancoPageUrlPattern);
      });

      it('edit button click should load edit Banco page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Banco');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bancoPageUrlPattern);
      });

      it('edit button click should load edit Banco page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Banco');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bancoPageUrlPattern);
      });

      it('last delete button click should delete instance of Banco', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('banco').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', bancoPageUrlPattern);

        banco = undefined;
      });
    });
  });

  describe('new Banco page', () => {
    beforeEach(() => {
      cy.visit(`${bancoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Banco');
    });

    it('should create an instance of Banco', () => {
      cy.get(`[data-cy="nome"]`).type('colorless');
      cy.get(`[data-cy="nome"]`).should('have.value', 'colorless');

      cy.get(`[data-cy="codigo"]`).type('pag');
      cy.get(`[data-cy="codigo"]`).should('have.value', 'pag');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        banco = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', bancoPageUrlPattern);
    });
  });
});
