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

describe('Regiao e2e test', () => {
  const regiaoPageUrl = '/regiao';
  const regiaoPageUrlPattern = new RegExp('/regiao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const regiaoSample = { nome: 'euphoric' };

  let regiao;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/regiaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/regiaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/regiaos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (regiao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/regiaos/${regiao.id}`,
      }).then(() => {
        regiao = undefined;
      });
    }
  });

  it('Regiaos menu should load Regiaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('regiao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Regiao').should('exist');
    cy.url().should('match', regiaoPageUrlPattern);
  });

  describe('Regiao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(regiaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Regiao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/regiao/new$'));
        cy.getEntityCreateUpdateHeading('Regiao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regiaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/regiaos',
          body: regiaoSample,
        }).then(({ body }) => {
          regiao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/regiaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/regiaos?page=0&size=20>; rel="last",<http://localhost/api/regiaos?page=0&size=20>; rel="first"',
              },
              body: [regiao],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(regiaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Regiao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('regiao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regiaoPageUrlPattern);
      });

      it('edit button click should load edit Regiao page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Regiao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regiaoPageUrlPattern);
      });

      it.skip('edit button click should load edit Regiao page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Regiao');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regiaoPageUrlPattern);
      });

      it('last delete button click should delete instance of Regiao', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('regiao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regiaoPageUrlPattern);

        regiao = undefined;
      });
    });
  });

  describe('new Regiao page', () => {
    beforeEach(() => {
      cy.visit(`${regiaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Regiao');
    });

    it('should create an instance of Regiao', () => {
      cy.get(`[data-cy="nome"]`).type('certainly electric');
      cy.get(`[data-cy="nome"]`).should('have.value', 'certainly electric');

      cy.get(`[data-cy="sigla"]`).type('amid');
      cy.get(`[data-cy="sigla"]`).should('have.value', 'amid');

      cy.get(`[data-cy="descricao"]`).type('than');
      cy.get(`[data-cy="descricao"]`).should('have.value', 'than');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        regiao = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', regiaoPageUrlPattern);
    });
  });
});
