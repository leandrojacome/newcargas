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

describe('TabelaFrete e2e test', () => {
  const tabelaFretePageUrl = '/tabela-frete';
  const tabelaFretePageUrlPattern = new RegExp('/tabela-frete(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const tabelaFreteSample = { tipo: 'EMBARCADOR', nome: 'milky drat back' };

  let tabelaFrete;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tabela-fretes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tabela-fretes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tabela-fretes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (tabelaFrete) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tabela-fretes/${tabelaFrete.id}`,
      }).then(() => {
        tabelaFrete = undefined;
      });
    }
  });

  it('TabelaFretes menu should load TabelaFretes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tabela-frete');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TabelaFrete').should('exist');
    cy.url().should('match', tabelaFretePageUrlPattern);
  });

  describe('TabelaFrete page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(tabelaFretePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TabelaFrete page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tabela-frete/new$'));
        cy.getEntityCreateUpdateHeading('TabelaFrete');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tabelaFretePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tabela-fretes',
          body: tabelaFreteSample,
        }).then(({ body }) => {
          tabelaFrete = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tabela-fretes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/tabela-fretes?page=0&size=20>; rel="last",<http://localhost/api/tabela-fretes?page=0&size=20>; rel="first"',
              },
              body: [tabelaFrete],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(tabelaFretePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TabelaFrete page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('tabelaFrete');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tabelaFretePageUrlPattern);
      });

      it('edit button click should load edit TabelaFrete page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TabelaFrete');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tabelaFretePageUrlPattern);
      });

      it.skip('edit button click should load edit TabelaFrete page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TabelaFrete');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tabelaFretePageUrlPattern);
      });

      it('last delete button click should delete instance of TabelaFrete', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('tabelaFrete').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', tabelaFretePageUrlPattern);

        tabelaFrete = undefined;
      });
    });
  });

  describe('new TabelaFrete page', () => {
    beforeEach(() => {
      cy.visit(`${tabelaFretePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TabelaFrete');
    });

    it('should create an instance of TabelaFrete', () => {
      cy.get(`[data-cy="tipo"]`).select('EMBARCADOR');

      cy.get(`[data-cy="nome"]`).type('guitar assassinate');
      cy.get(`[data-cy="nome"]`).should('have.value', 'guitar assassinate');

      cy.get(`[data-cy="descricao"]`).type('ear strident');
      cy.get(`[data-cy="descricao"]`).should('have.value', 'ear strident');

      cy.get(`[data-cy="leadTime"]`).type('2');
      cy.get(`[data-cy="leadTime"]`).should('have.value', '2');

      cy.get(`[data-cy="freteMinimo"]`).type('7.16');
      cy.get(`[data-cy="freteMinimo"]`).should('have.value', '7.16');

      cy.get(`[data-cy="valorTonelada"]`).type('2.62');
      cy.get(`[data-cy="valorTonelada"]`).should('have.value', '2.62');

      cy.get(`[data-cy="valorMetroCubico"]`).type('1.28');
      cy.get(`[data-cy="valorMetroCubico"]`).should('have.value', '1.28');

      cy.get(`[data-cy="valorUnidade"]`).type('5.61');
      cy.get(`[data-cy="valorUnidade"]`).should('have.value', '5.61');

      cy.get(`[data-cy="valorKm"]`).type('1.64');
      cy.get(`[data-cy="valorKm"]`).should('have.value', '1.64');

      cy.get(`[data-cy="valorAdicional"]`).type('4.38');
      cy.get(`[data-cy="valorAdicional"]`).should('have.value', '4.38');

      cy.get(`[data-cy="valorColeta"]`).type('7.26');
      cy.get(`[data-cy="valorColeta"]`).should('have.value', '7.26');

      cy.get(`[data-cy="valorEntrega"]`).type('7.51');
      cy.get(`[data-cy="valorEntrega"]`).should('have.value', '7.51');

      cy.get(`[data-cy="valorTotal"]`).type('1.81');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '1.81');

      cy.get(`[data-cy="valorKmAdicional"]`).type('4.46');
      cy.get(`[data-cy="valorKmAdicional"]`).should('have.value', '4.46');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        tabelaFrete = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', tabelaFretePageUrlPattern);
    });
  });
});
