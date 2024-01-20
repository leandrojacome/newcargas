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

describe('TipoVeiculo e2e test', () => {
  const tipoVeiculoPageUrl = '/tipo-veiculo';
  const tipoVeiculoPageUrlPattern = new RegExp('/tipo-veiculo(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tipoVeiculoSample = { nome: 'hence' };

  let tipoVeiculo;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tipo-veiculos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tipo-veiculos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tipo-veiculos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tipoVeiculo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tipo-veiculos/${tipoVeiculo.id}`,
      }).then(() => {
        tipoVeiculo = undefined;
      });
    }
  });

  it('TipoVeiculos menu should load TipoVeiculos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tipo-veiculo');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TipoVeiculo').should('exist');
    cy.url().should('match', tipoVeiculoPageUrlPattern);
  });

  describe('TipoVeiculo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tipoVeiculoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TipoVeiculo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tipo-veiculo/new$'));
        cy.getEntityCreateUpdateHeading('TipoVeiculo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoVeiculoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tipo-veiculos',
          body: tipoVeiculoSample,
        }).then(({ body }) => {
          tipoVeiculo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tipo-veiculos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tipo-veiculos?page=0&size=20>; rel="last",<http://localhost/api/tipo-veiculos?page=0&size=20>; rel="first"',
              },
              body: [tipoVeiculo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(tipoVeiculoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TipoVeiculo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tipoVeiculo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoVeiculoPageUrlPattern);
      });

      it('edit button click should load edit TipoVeiculo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoVeiculo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoVeiculoPageUrlPattern);
      });

      it('edit button click should load edit TipoVeiculo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TipoVeiculo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoVeiculoPageUrlPattern);
      });

      it('last delete button click should delete instance of TipoVeiculo', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tipoVeiculo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tipoVeiculoPageUrlPattern);

        tipoVeiculo = undefined;
      });
    });
  });

  describe('new TipoVeiculo page', () => {
    beforeEach(() => {
      cy.visit(`${tipoVeiculoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TipoVeiculo');
    });

    it('should create an instance of TipoVeiculo', () => {
      cy.get(`[data-cy="nome"]`).type('until lest');
      cy.get(`[data-cy="nome"]`).should('have.value', 'until lest');

      cy.get(`[data-cy="descricao"]`).type('klutzy');
      cy.get(`[data-cy="descricao"]`).should('have.value', 'klutzy');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        tipoVeiculo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', tipoVeiculoPageUrlPattern);
    });
  });
});
