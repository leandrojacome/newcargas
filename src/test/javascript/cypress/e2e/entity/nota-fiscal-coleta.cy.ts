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

describe('NotaFiscalColeta e2e test', () => {
  const notaFiscalColetaPageUrl = '/nota-fiscal-coleta';
  const notaFiscalColetaPageUrlPattern = new RegExp('/nota-fiscal-coleta(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const notaFiscalColetaSample = { numero: 'whether across thoug', serie: 'offensively while' };

  let notaFiscalColeta;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/nota-fiscal-coletas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/nota-fiscal-coletas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/nota-fiscal-coletas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (notaFiscalColeta) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/nota-fiscal-coletas/${notaFiscalColeta.id}`,
      }).then(() => {
        notaFiscalColeta = undefined;
      });
    }
  });

  it('NotaFiscalColetas menu should load NotaFiscalColetas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('nota-fiscal-coleta');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NotaFiscalColeta').should('exist');
    cy.url().should('match', notaFiscalColetaPageUrlPattern);
  });

  describe('NotaFiscalColeta page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(notaFiscalColetaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NotaFiscalColeta page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/nota-fiscal-coleta/new$'));
        cy.getEntityCreateUpdateHeading('NotaFiscalColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notaFiscalColetaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/nota-fiscal-coletas',
          body: notaFiscalColetaSample,
        }).then(({ body }) => {
          notaFiscalColeta = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/nota-fiscal-coletas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/nota-fiscal-coletas?page=0&size=20>; rel="last",<http://localhost/api/nota-fiscal-coletas?page=0&size=20>; rel="first"',
              },
              body: [notaFiscalColeta],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(notaFiscalColetaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details NotaFiscalColeta page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('notaFiscalColeta');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notaFiscalColetaPageUrlPattern);
      });

      it('edit button click should load edit NotaFiscalColeta page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NotaFiscalColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notaFiscalColetaPageUrlPattern);
      });

      it.skip('edit button click should load edit NotaFiscalColeta page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NotaFiscalColeta');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notaFiscalColetaPageUrlPattern);
      });

      it('last delete button click should delete instance of NotaFiscalColeta', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('notaFiscalColeta').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notaFiscalColetaPageUrlPattern);

        notaFiscalColeta = undefined;
      });
    });
  });

  describe('new NotaFiscalColeta page', () => {
    beforeEach(() => {
      cy.visit(`${notaFiscalColetaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('NotaFiscalColeta');
    });

    it('should create an instance of NotaFiscalColeta', () => {
      cy.get(`[data-cy="numero"]`).type('plush muted');
      cy.get(`[data-cy="numero"]`).should('have.value', 'plush muted');

      cy.get(`[data-cy="serie"]`).type('truthfully');
      cy.get(`[data-cy="serie"]`).should('have.value', 'truthfully');

      cy.get(`[data-cy="remetente"]`).type('provided');
      cy.get(`[data-cy="remetente"]`).should('have.value', 'provided');

      cy.get(`[data-cy="destinatario"]`).type('whereas impugn');
      cy.get(`[data-cy="destinatario"]`).should('have.value', 'whereas impugn');

      cy.get(`[data-cy="metroCubico"]`).type('1.1');
      cy.get(`[data-cy="metroCubico"]`).should('have.value', '1.1');

      cy.get(`[data-cy="quantidade"]`).type('2.11');
      cy.get(`[data-cy="quantidade"]`).should('have.value', '2.11');

      cy.get(`[data-cy="peso"]`).type('8.44');
      cy.get(`[data-cy="peso"]`).should('have.value', '8.44');

      cy.get(`[data-cy="dataEmissao"]`).type('2024-01-20T06:51');
      cy.get(`[data-cy="dataEmissao"]`).blur();
      cy.get(`[data-cy="dataEmissao"]`).should('have.value', '2024-01-20T06:51');

      cy.get(`[data-cy="dataSaida"]`).type('2024-01-19T21:09');
      cy.get(`[data-cy="dataSaida"]`).blur();
      cy.get(`[data-cy="dataSaida"]`).should('have.value', '2024-01-19T21:09');

      cy.get(`[data-cy="valorTotal"]`).type('3.77');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '3.77');

      cy.get(`[data-cy="pesoTotal"]`).type('2.65');
      cy.get(`[data-cy="pesoTotal"]`).should('have.value', '2.65');

      cy.get(`[data-cy="quantidadeTotal"]`).type('1');
      cy.get(`[data-cy="quantidadeTotal"]`).should('have.value', '1');

      cy.get(`[data-cy="observacao"]`).type('ripe ouch');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'ripe ouch');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        notaFiscalColeta = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', notaFiscalColetaPageUrlPattern);
    });
  });
});
