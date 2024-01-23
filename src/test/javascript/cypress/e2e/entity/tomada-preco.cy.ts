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

describe('TomadaPreco e2e test', () => {
  const tomadaPrecoPageUrl = '/tomada-preco';
  const tomadaPrecoPageUrlPattern = new RegExp('/tomada-preco(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tomadaPrecoSample = { dataHoraEnvio: '2024-01-20T16:10:09.201Z' };

  let tomadaPreco;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tomada-precos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tomada-precos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tomada-precos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tomadaPreco) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tomada-precos/${tomadaPreco.id}`,
      }).then(() => {
        tomadaPreco = undefined;
      });
    }
  });

  it('TomadaPrecos menu should load TomadaPrecos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tomada-preco');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TomadaPreco').should('exist');
    cy.url().should('match', tomadaPrecoPageUrlPattern);
  });

  describe('TomadaPreco page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tomadaPrecoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TomadaPreco page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tomada-preco/new$'));
        cy.getEntityCreateUpdateHeading('TomadaPreco');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tomadaPrecoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tomada-precos',
          body: tomadaPrecoSample,
        }).then(({ body }) => {
          tomadaPreco = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tomada-precos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tomada-precos?page=0&size=20>; rel="last",<http://localhost/api/tomada-precos?page=0&size=20>; rel="first"',
              },
              body: [tomadaPreco],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(tomadaPrecoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TomadaPreco page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tomadaPreco');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tomadaPrecoPageUrlPattern);
      });

      it('edit button click should load edit TomadaPreco page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TomadaPreco');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tomadaPrecoPageUrlPattern);
      });

      it.skip('edit button click should load edit TomadaPreco page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TomadaPreco');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tomadaPrecoPageUrlPattern);
      });

      it('last delete button click should delete instance of TomadaPreco', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tomadaPreco').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tomadaPrecoPageUrlPattern);

        tomadaPreco = undefined;
      });
    });
  });

  describe('new TomadaPreco page', () => {
    beforeEach(() => {
      cy.visit(`${tomadaPrecoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TomadaPreco');
    });

    it('should create an instance of TomadaPreco', () => {
      cy.get(`[data-cy="dataHoraEnvio"]`).type('2024-01-20T13:40');
      cy.get(`[data-cy="dataHoraEnvio"]`).blur();
      cy.get(`[data-cy="dataHoraEnvio"]`).should('have.value', '2024-01-20T13:40');

      cy.get(`[data-cy="prazoResposta"]`).type('4');
      cy.get(`[data-cy="prazoResposta"]`).should('have.value', '4');

      cy.get(`[data-cy="valorTotal"]`).type('4.09');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '4.09');

      cy.get(`[data-cy="observacao"]`).type('alarmed aw');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'alarmed aw');

      cy.get(`[data-cy="aprovado"]`).should('not.be.checked');
      cy.get(`[data-cy="aprovado"]`).click();
      cy.get(`[data-cy="aprovado"]`).should('be.checked');

      cy.get(`[data-cy="cancelado"]`).should('not.be.checked');
      cy.get(`[data-cy="cancelado"]`).click();
      cy.get(`[data-cy="cancelado"]`).should('be.checked');

      cy.get(`[data-cy="removido"]`).should('not.be.checked');
      cy.get(`[data-cy="removido"]`).click();
      cy.get(`[data-cy="removido"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        tomadaPreco = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', tomadaPrecoPageUrlPattern);
    });
  });
});
