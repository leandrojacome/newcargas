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

describe('Contratacao e2e test', () => {
  const contratacaoPageUrl = '/contratacao';
  const contratacaoPageUrlPattern = new RegExp('/contratacao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const contratacaoSample = { valorTotal: 5.91, validadeEmDias: 4, dataValidade: '2024-01-20' };

  let contratacao;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contratacaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contratacaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contratacaos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contratacao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contratacaos/${contratacao.id}`,
      }).then(() => {
        contratacao = undefined;
      });
    }
  });

  it('Contratacaos menu should load Contratacaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contratacao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Contratacao').should('exist');
    cy.url().should('match', contratacaoPageUrlPattern);
  });

  describe('Contratacao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contratacaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Contratacao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/contratacao/new$'));
        cy.getEntityCreateUpdateHeading('Contratacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contratacaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contratacaos',
          body: contratacaoSample,
        }).then(({ body }) => {
          contratacao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contratacaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/contratacaos?page=0&size=20>; rel="last",<http://localhost/api/contratacaos?page=0&size=20>; rel="first"',
              },
              body: [contratacao],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contratacaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Contratacao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contratacao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contratacaoPageUrlPattern);
      });

      it('edit button click should load edit Contratacao page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Contratacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contratacaoPageUrlPattern);
      });

      it.skip('edit button click should load edit Contratacao page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Contratacao');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contratacaoPageUrlPattern);
      });

      it('last delete button click should delete instance of Contratacao', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contratacao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contratacaoPageUrlPattern);

        contratacao = undefined;
      });
    });
  });

  describe('new Contratacao page', () => {
    beforeEach(() => {
      cy.visit(`${contratacaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Contratacao');
    });

    it('should create an instance of Contratacao', () => {
      cy.get(`[data-cy="valorTotal"]`).type('5.47');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '5.47');

      cy.get(`[data-cy="validadeEmDias"]`).type('3');
      cy.get(`[data-cy="validadeEmDias"]`).should('have.value', '3');

      cy.get(`[data-cy="dataValidade"]`).type('2024-01-20');
      cy.get(`[data-cy="dataValidade"]`).blur();
      cy.get(`[data-cy="dataValidade"]`).should('have.value', '2024-01-20');

      cy.get(`[data-cy="observacao"]`).type('outside');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'outside');

      cy.get(`[data-cy="cancelado"]`).should('not.be.checked');
      cy.get(`[data-cy="cancelado"]`).click();
      cy.get(`[data-cy="cancelado"]`).should('be.checked');

      cy.get(`[data-cy="removido"]`).should('not.be.checked');
      cy.get(`[data-cy="removido"]`).click();
      cy.get(`[data-cy="removido"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        contratacao = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', contratacaoPageUrlPattern);
    });
  });
});
