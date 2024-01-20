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
  const notaFiscalColetaSample = { numero: 'boo geez', serie: 'but yahoo' };

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

      it('edit button click should load edit NotaFiscalColeta page and save', () => {
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
      cy.get(`[data-cy="numero"]`).type('uncomfortable frivol');
      cy.get(`[data-cy="numero"]`).should('have.value', 'uncomfortable frivol');

      cy.get(`[data-cy="serie"]`).type('um');
      cy.get(`[data-cy="serie"]`).should('have.value', 'um');

      cy.get(`[data-cy="remetente"]`).type('opposite consumption');
      cy.get(`[data-cy="remetente"]`).should('have.value', 'opposite consumption');

      cy.get(`[data-cy="destinatario"]`).type('save yet freely');
      cy.get(`[data-cy="destinatario"]`).should('have.value', 'save yet freely');

      cy.get(`[data-cy="metroCubico"]`).type('5.32');
      cy.get(`[data-cy="metroCubico"]`).should('have.value', '5.32');

      cy.get(`[data-cy="quantidade"]`).type('9.86');
      cy.get(`[data-cy="quantidade"]`).should('have.value', '9.86');

      cy.get(`[data-cy="peso"]`).type('1.5');
      cy.get(`[data-cy="peso"]`).should('have.value', '1.5');

      cy.get(`[data-cy="dataEmissao"]`).type('2024-01-20T08:56');
      cy.get(`[data-cy="dataEmissao"]`).blur();
      cy.get(`[data-cy="dataEmissao"]`).should('have.value', '2024-01-20T08:56');

      cy.get(`[data-cy="dataSaida"]`).type('2024-01-20T02:29');
      cy.get(`[data-cy="dataSaida"]`).blur();
      cy.get(`[data-cy="dataSaida"]`).should('have.value', '2024-01-20T02:29');

      cy.get(`[data-cy="valorTotal"]`).type('4.3');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '4.3');

      cy.get(`[data-cy="pesoTotal"]`).type('7.02');
      cy.get(`[data-cy="pesoTotal"]`).should('have.value', '7.02');

      cy.get(`[data-cy="quantidadeTotal"]`).type('2');
      cy.get(`[data-cy="quantidadeTotal"]`).should('have.value', '2');

      cy.get(`[data-cy="observacao"]`).type('wide elementary truthfully');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'wide elementary truthfully');

      cy.get(`[data-cy="dataCadastro"]`).type('2024-01-20T13:23');
      cy.get(`[data-cy="dataCadastro"]`).blur();
      cy.get(`[data-cy="dataCadastro"]`).should('have.value', '2024-01-20T13:23');

      cy.get(`[data-cy="dataAtualizacao"]`).type('2024-01-20T16:28');
      cy.get(`[data-cy="dataAtualizacao"]`).blur();
      cy.get(`[data-cy="dataAtualizacao"]`).should('have.value', '2024-01-20T16:28');

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
