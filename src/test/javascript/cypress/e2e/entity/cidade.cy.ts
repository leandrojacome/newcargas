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

describe('Cidade e2e test', () => {
  const cidadePageUrl = '/cidade';
  const cidadePageUrlPattern = new RegExp('/cidade(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cidadeSample = { nome: 'frank plump' };

  let cidade;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cidades+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cidades').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cidades/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cidade) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cidades/${cidade.id}`,
      }).then(() => {
        cidade = undefined;
      });
    }
  });

  it('Cidades menu should load Cidades page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cidade');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cidade').should('exist');
    cy.url().should('match', cidadePageUrlPattern);
  });

  describe('Cidade page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cidadePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cidade page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cidade/new$'));
        cy.getEntityCreateUpdateHeading('Cidade');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cidadePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cidades',
          body: cidadeSample,
        }).then(({ body }) => {
          cidade = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cidades+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cidades?page=0&size=20>; rel="last",<http://localhost/api/cidades?page=0&size=20>; rel="first"',
              },
              body: [cidade],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cidadePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Cidade page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cidade');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cidadePageUrlPattern);
      });

      it('edit button click should load edit Cidade page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cidade');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cidadePageUrlPattern);
      });

      it('edit button click should load edit Cidade page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cidade');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cidadePageUrlPattern);
      });

      it('last delete button click should delete instance of Cidade', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cidade').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cidadePageUrlPattern);

        cidade = undefined;
      });
    });
  });

  describe('new Cidade page', () => {
    beforeEach(() => {
      cy.visit(`${cidadePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cidade');
    });

    it('should create an instance of Cidade', () => {
      cy.get(`[data-cy="nome"]`).type('up yum');
      cy.get(`[data-cy="nome"]`).should('have.value', 'up yum');

      cy.get(`[data-cy="codigoIbge"]`).type('7');
      cy.get(`[data-cy="codigoIbge"]`).should('have.value', '7');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        cidade = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', cidadePageUrlPattern);
    });
  });
});
