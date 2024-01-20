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

describe('Fatura e2e test', () => {
  const faturaPageUrl = '/fatura';
  const faturaPageUrlPattern = new RegExp('/fatura(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const faturaSample = {
    tipo: 'TRANSPORTADORA',
    dataFatura: '2024-01-20T09:59:06.236Z',
    dataVencimento: '2024-01-19T18:09:54.923Z',
    valorTotal: 2.27,
    dataCadastro: '2024-01-20T01:53:26.220Z',
  };

  let fatura;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/faturas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/faturas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/faturas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fatura) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/faturas/${fatura.id}`,
      }).then(() => {
        fatura = undefined;
      });
    }
  });

  it('Faturas menu should load Faturas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fatura');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Fatura').should('exist');
    cy.url().should('match', faturaPageUrlPattern);
  });

  describe('Fatura page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(faturaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Fatura page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/fatura/new$'));
        cy.getEntityCreateUpdateHeading('Fatura');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', faturaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/faturas',
          body: faturaSample,
        }).then(({ body }) => {
          fatura = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/faturas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/faturas?page=0&size=20>; rel="last",<http://localhost/api/faturas?page=0&size=20>; rel="first"',
              },
              body: [fatura],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(faturaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Fatura page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fatura');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', faturaPageUrlPattern);
      });

      it('edit button click should load edit Fatura page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fatura');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', faturaPageUrlPattern);
      });

      it('edit button click should load edit Fatura page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fatura');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', faturaPageUrlPattern);
      });

      it('last delete button click should delete instance of Fatura', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fatura').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', faturaPageUrlPattern);

        fatura = undefined;
      });
    });
  });

  describe('new Fatura page', () => {
    beforeEach(() => {
      cy.visit(`${faturaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Fatura');
    });

    it('should create an instance of Fatura', () => {
      cy.get(`[data-cy="tipo"]`).select('TRANSPORTADORA');

      cy.get(`[data-cy="dataFatura"]`).type('2024-01-20T03:03');
      cy.get(`[data-cy="dataFatura"]`).blur();
      cy.get(`[data-cy="dataFatura"]`).should('have.value', '2024-01-20T03:03');

      cy.get(`[data-cy="dataVencimento"]`).type('2024-01-20T00:29');
      cy.get(`[data-cy="dataVencimento"]`).blur();
      cy.get(`[data-cy="dataVencimento"]`).should('have.value', '2024-01-20T00:29');

      cy.get(`[data-cy="dataPagamento"]`).type('2024-01-19T22:16');
      cy.get(`[data-cy="dataPagamento"]`).blur();
      cy.get(`[data-cy="dataPagamento"]`).should('have.value', '2024-01-19T22:16');

      cy.get(`[data-cy="numeroParcela"]`).type('4');
      cy.get(`[data-cy="numeroParcela"]`).should('have.value', '4');

      cy.get(`[data-cy="valorTotal"]`).type('6.54');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '6.54');

      cy.get(`[data-cy="observacao"]`).type('why till mantua');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'why till mantua');

      cy.get(`[data-cy="dataCadastro"]`).type('2024-01-20T02:44');
      cy.get(`[data-cy="dataCadastro"]`).blur();
      cy.get(`[data-cy="dataCadastro"]`).should('have.value', '2024-01-20T02:44');

      cy.get(`[data-cy="usuarioCadastro"]`).type('glimmering even');
      cy.get(`[data-cy="usuarioCadastro"]`).should('have.value', 'glimmering even');

      cy.get(`[data-cy="dataAtualizacao"]`).type('2024-01-19T23:14');
      cy.get(`[data-cy="dataAtualizacao"]`).blur();
      cy.get(`[data-cy="dataAtualizacao"]`).should('have.value', '2024-01-19T23:14');

      cy.get(`[data-cy="usuarioAtualizacao"]`).type('homeland until');
      cy.get(`[data-cy="usuarioAtualizacao"]`).should('have.value', 'homeland until');

      cy.get(`[data-cy="cancelado"]`).should('not.be.checked');
      cy.get(`[data-cy="cancelado"]`).click();
      cy.get(`[data-cy="cancelado"]`).should('be.checked');

      cy.get(`[data-cy="dataCancelamento"]`).type('2024-01-20T08:51');
      cy.get(`[data-cy="dataCancelamento"]`).blur();
      cy.get(`[data-cy="dataCancelamento"]`).should('have.value', '2024-01-20T08:51');

      cy.get(`[data-cy="usuarioCancelamento"]`).type('unethically pooh upright');
      cy.get(`[data-cy="usuarioCancelamento"]`).should('have.value', 'unethically pooh upright');

      cy.get(`[data-cy="removido"]`).should('not.be.checked');
      cy.get(`[data-cy="removido"]`).click();
      cy.get(`[data-cy="removido"]`).should('be.checked');

      cy.get(`[data-cy="dataRemocao"]`).type('2024-01-19T20:18');
      cy.get(`[data-cy="dataRemocao"]`).blur();
      cy.get(`[data-cy="dataRemocao"]`).should('have.value', '2024-01-19T20:18');

      cy.get(`[data-cy="usuarioRemocao"]`).type('since whereas');
      cy.get(`[data-cy="usuarioRemocao"]`).should('have.value', 'since whereas');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        fatura = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', faturaPageUrlPattern);
    });
  });
});
