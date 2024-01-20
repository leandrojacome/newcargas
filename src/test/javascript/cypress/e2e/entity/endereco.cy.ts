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

describe('Endereco e2e test', () => {
  const enderecoPageUrl = '/endereco';
  const enderecoPageUrlPattern = new RegExp('/endereco(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const enderecoSample = { tipo: 'EMBARCADOR', cep: 'repackag', endereco: 'gee weighty', bairro: 'hence' };

  let endereco;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/enderecos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/enderecos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/enderecos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (endereco) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/enderecos/${endereco.id}`,
      }).then(() => {
        endereco = undefined;
      });
    }
  });

  it('Enderecos menu should load Enderecos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('endereco');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Endereco').should('exist');
    cy.url().should('match', enderecoPageUrlPattern);
  });

  describe('Endereco page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(enderecoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Endereco page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/endereco/new$'));
        cy.getEntityCreateUpdateHeading('Endereco');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enderecoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/enderecos',
          body: enderecoSample,
        }).then(({ body }) => {
          endereco = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/enderecos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/enderecos?page=0&size=20>; rel="last",<http://localhost/api/enderecos?page=0&size=20>; rel="first"',
              },
              body: [endereco],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(enderecoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Endereco page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('endereco');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enderecoPageUrlPattern);
      });

      it('edit button click should load edit Endereco page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Endereco');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enderecoPageUrlPattern);
      });

      it('edit button click should load edit Endereco page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Endereco');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enderecoPageUrlPattern);
      });

      it('last delete button click should delete instance of Endereco', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('endereco').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enderecoPageUrlPattern);

        endereco = undefined;
      });
    });
  });

  describe('new Endereco page', () => {
    beforeEach(() => {
      cy.visit(`${enderecoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Endereco');
    });

    it('should create an instance of Endereco', () => {
      cy.get(`[data-cy="tipo"]`).select('EMBARCADOR');

      cy.get(`[data-cy="cep"]`).type('hatch ma');
      cy.get(`[data-cy="cep"]`).should('have.value', 'hatch ma');

      cy.get(`[data-cy="endereco"]`).type('mint acknowledge');
      cy.get(`[data-cy="endereco"]`).should('have.value', 'mint acknowledge');

      cy.get(`[data-cy="numero"]`).type('athwart vi');
      cy.get(`[data-cy="numero"]`).should('have.value', 'athwart vi');

      cy.get(`[data-cy="complemento"]`).type('shrill');
      cy.get(`[data-cy="complemento"]`).should('have.value', 'shrill');

      cy.get(`[data-cy="bairro"]`).type('to physically');
      cy.get(`[data-cy="bairro"]`).should('have.value', 'to physically');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        endereco = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', enderecoPageUrlPattern);
    });
  });
});
