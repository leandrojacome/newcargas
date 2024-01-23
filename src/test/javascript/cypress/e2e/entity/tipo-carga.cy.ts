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

describe('TipoCarga e2e test', () => {
  const tipoCargaPageUrl = '/tipo-carga';
  const tipoCargaPageUrlPattern = new RegExp('/tipo-carga(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tipoCargaSample = { nome: 'opposite gee yowza' };

  let tipoCarga;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tipo-cargas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tipo-cargas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tipo-cargas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tipoCarga) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-cargas/${tipoCarga.id}`,
      }).then(() => {
        tipoCarga = undefined;
      });
    }
  });

  it('TipoCargas menu should load TipoCargas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tipo-carga');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TipoCarga').should('exist');
    cy.url().should('match', tipoCargaPageUrlPattern);
  });

  describe('TipoCarga page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tipoCargaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TipoCarga page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tipo-carga/new$'));
        cy.getEntityCreateUpdateHeading('TipoCarga');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoCargaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tipo-cargas',
          body: tipoCargaSample,
        }).then(({ body }) => {
          tipoCarga = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tipo-cargas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tipo-cargas?page=0&size=20>; rel="last",<http://localhost/api/tipo-cargas?page=0&size=20>; rel="first"',
              },
              body: [tipoCarga],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(tipoCargaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TipoCarga page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tipoCarga');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoCargaPageUrlPattern);
      });

      it('edit button click should load edit TipoCarga page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoCarga');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoCargaPageUrlPattern);
      });

      it.skip('edit button click should load edit TipoCarga page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoCarga');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoCargaPageUrlPattern);
      });

      it('last delete button click should delete instance of TipoCarga', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tipoCarga').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoCargaPageUrlPattern);

        tipoCarga = undefined;
      });
    });
  });

  describe('new TipoCarga page', () => {
    beforeEach(() => {
      cy.visit(`${tipoCargaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TipoCarga');
    });

    it('should create an instance of TipoCarga', () => {
      cy.get(`[data-cy="nome"]`).type('mostly furthermore');
      cy.get(`[data-cy="nome"]`).should('have.value', 'mostly furthermore');

      cy.get(`[data-cy="descricao"]`).type('legislation');
      cy.get(`[data-cy="descricao"]`).should('have.value', 'legislation');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        tipoCarga = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', tipoCargaPageUrlPattern);
    });
  });
});
