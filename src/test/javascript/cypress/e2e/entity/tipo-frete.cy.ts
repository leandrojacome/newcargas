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

describe('TipoFrete e2e test', () => {
  const tipoFretePageUrl = '/tipo-frete';
  const tipoFretePageUrlPattern = new RegExp('/tipo-frete(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tipoFreteSample = { nome: 'sling stimulating substitution' };

  let tipoFrete;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tipo-fretes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tipo-fretes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tipo-fretes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tipoFrete) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-fretes/${tipoFrete.id}`,
      }).then(() => {
        tipoFrete = undefined;
      });
    }
  });

  it('TipoFretes menu should load TipoFretes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tipo-frete');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TipoFrete').should('exist');
    cy.url().should('match', tipoFretePageUrlPattern);
  });

  describe('TipoFrete page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tipoFretePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TipoFrete page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tipo-frete/new$'));
        cy.getEntityCreateUpdateHeading('TipoFrete');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoFretePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tipo-fretes',
          body: tipoFreteSample,
        }).then(({ body }) => {
          tipoFrete = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tipo-fretes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tipo-fretes?page=0&size=20>; rel="last",<http://localhost/api/tipo-fretes?page=0&size=20>; rel="first"',
              },
              body: [tipoFrete],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(tipoFretePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TipoFrete page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tipoFrete');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoFretePageUrlPattern);
      });

      it('edit button click should load edit TipoFrete page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoFrete');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoFretePageUrlPattern);
      });

      it.skip('edit button click should load edit TipoFrete page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoFrete');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoFretePageUrlPattern);
      });

      it('last delete button click should delete instance of TipoFrete', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tipoFrete').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoFretePageUrlPattern);

        tipoFrete = undefined;
      });
    });
  });

  describe('new TipoFrete page', () => {
    beforeEach(() => {
      cy.visit(`${tipoFretePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TipoFrete');
    });

    it('should create an instance of TipoFrete', () => {
      cy.get(`[data-cy="nome"]`).type('barbeque penalise');
      cy.get(`[data-cy="nome"]`).should('have.value', 'barbeque penalise');

      cy.get(`[data-cy="descricao"]`).type('however');
      cy.get(`[data-cy="descricao"]`).should('have.value', 'however');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        tipoFrete = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', tipoFretePageUrlPattern);
    });
  });
});
