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

describe('FormaCobranca e2e test', () => {
  const formaCobrancaPageUrl = '/forma-cobranca';
  const formaCobrancaPageUrlPattern = new RegExp('/forma-cobranca(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const formaCobrancaSample = { nome: 'yin' };

  let formaCobranca;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/forma-cobrancas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/forma-cobrancas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/forma-cobrancas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (formaCobranca) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/forma-cobrancas/${formaCobranca.id}`,
      }).then(() => {
        formaCobranca = undefined;
      });
    }
  });

  it('FormaCobrancas menu should load FormaCobrancas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('forma-cobranca');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FormaCobranca').should('exist');
    cy.url().should('match', formaCobrancaPageUrlPattern);
  });

  describe('FormaCobranca page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formaCobrancaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FormaCobranca page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/forma-cobranca/new$'));
        cy.getEntityCreateUpdateHeading('FormaCobranca');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formaCobrancaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/forma-cobrancas',
          body: formaCobrancaSample,
        }).then(({ body }) => {
          formaCobranca = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/forma-cobrancas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/forma-cobrancas?page=0&size=20>; rel="last",<http://localhost/api/forma-cobrancas?page=0&size=20>; rel="first"',
              },
              body: [formaCobranca],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(formaCobrancaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FormaCobranca page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('formaCobranca');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formaCobrancaPageUrlPattern);
      });

      it('edit button click should load edit FormaCobranca page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormaCobranca');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formaCobrancaPageUrlPattern);
      });

      it('edit button click should load edit FormaCobranca page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormaCobranca');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formaCobrancaPageUrlPattern);
      });

      it('last delete button click should delete instance of FormaCobranca', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('formaCobranca').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formaCobrancaPageUrlPattern);

        formaCobranca = undefined;
      });
    });
  });

  describe('new FormaCobranca page', () => {
    beforeEach(() => {
      cy.visit(`${formaCobrancaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FormaCobranca');
    });

    it('should create an instance of FormaCobranca', () => {
      cy.get(`[data-cy="nome"]`).type('ew near better');
      cy.get(`[data-cy="nome"]`).should('have.value', 'ew near better');

      cy.get(`[data-cy="descricao"]`).type('since ick whether');
      cy.get(`[data-cy="descricao"]`).should('have.value', 'since ick whether');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        formaCobranca = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', formaCobrancaPageUrlPattern);
    });
  });
});
