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

describe('Estado e2e test', () => {
  const estadoPageUrl = '/estado';
  const estadoPageUrlPattern = new RegExp('/estado(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const estadoSample = { nome: 'mobile', sigla: 'yu' };

  let estado;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/estados+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/estados').as('postEntityRequest');
    cy.intercept('DELETE', '/api/estados/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (estado) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/estados/${estado.id}`,
      }).then(() => {
        estado = undefined;
      });
    }
  });

  it('Estados menu should load Estados page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('estado');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Estado').should('exist');
    cy.url().should('match', estadoPageUrlPattern);
  });

  describe('Estado page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(estadoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Estado page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/estado/new$'));
        cy.getEntityCreateUpdateHeading('Estado');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estadoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/estados',
          body: estadoSample,
        }).then(({ body }) => {
          estado = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/estados+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/estados?page=0&size=20>; rel="last",<http://localhost/api/estados?page=0&size=20>; rel="first"',
              },
              body: [estado],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(estadoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Estado page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('estado');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estadoPageUrlPattern);
      });

      it('edit button click should load edit Estado page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Estado');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estadoPageUrlPattern);
      });

      it.skip('edit button click should load edit Estado page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Estado');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estadoPageUrlPattern);
      });

      it('last delete button click should delete instance of Estado', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('estado').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', estadoPageUrlPattern);

        estado = undefined;
      });
    });
  });

  describe('new Estado page', () => {
    beforeEach(() => {
      cy.visit(`${estadoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Estado');
    });

    it('should create an instance of Estado', () => {
      cy.get(`[data-cy="nome"]`).type('plastic');
      cy.get(`[data-cy="nome"]`).should('have.value', 'plastic');

      cy.get(`[data-cy="sigla"]`).type('li');
      cy.get(`[data-cy="sigla"]`).should('have.value', 'li');

      cy.get(`[data-cy="codigoIbge"]`).type('7');
      cy.get(`[data-cy="codigoIbge"]`).should('have.value', '7');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        estado = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', estadoPageUrlPattern);
    });
  });
});
