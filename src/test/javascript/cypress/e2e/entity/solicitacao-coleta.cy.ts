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

describe('SolicitacaoColeta e2e test', () => {
  const solicitacaoColetaPageUrl = '/solicitacao-coleta';
  const solicitacaoColetaPageUrlPattern = new RegExp('/solicitacao-coleta(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const solicitacaoColetaSample = {
    coletado: true,
    dataHoraColeta: '2024-01-20T04:43:30.320Z',
    entregue: false,
    dataCadastro: '2024-01-19T17:38:43.342Z',
  };

  let solicitacaoColeta;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/solicitacao-coletas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/solicitacao-coletas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/solicitacao-coletas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (solicitacaoColeta) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/solicitacao-coletas/${solicitacaoColeta.id}`,
      }).then(() => {
        solicitacaoColeta = undefined;
      });
    }
  });

  it('SolicitacaoColetas menu should load SolicitacaoColetas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('solicitacao-coleta');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SolicitacaoColeta').should('exist');
    cy.url().should('match', solicitacaoColetaPageUrlPattern);
  });

  describe('SolicitacaoColeta page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(solicitacaoColetaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SolicitacaoColeta page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/solicitacao-coleta/new$'));
        cy.getEntityCreateUpdateHeading('SolicitacaoColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', solicitacaoColetaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/solicitacao-coletas',
          body: solicitacaoColetaSample,
        }).then(({ body }) => {
          solicitacaoColeta = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/solicitacao-coletas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/solicitacao-coletas?page=0&size=20>; rel="last",<http://localhost/api/solicitacao-coletas?page=0&size=20>; rel="first"',
              },
              body: [solicitacaoColeta],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(solicitacaoColetaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SolicitacaoColeta page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('solicitacaoColeta');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', solicitacaoColetaPageUrlPattern);
      });

      it('edit button click should load edit SolicitacaoColeta page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SolicitacaoColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', solicitacaoColetaPageUrlPattern);
      });

      it('edit button click should load edit SolicitacaoColeta page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SolicitacaoColeta');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', solicitacaoColetaPageUrlPattern);
      });

      it('last delete button click should delete instance of SolicitacaoColeta', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('solicitacaoColeta').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', solicitacaoColetaPageUrlPattern);

        solicitacaoColeta = undefined;
      });
    });
  });

  describe('new SolicitacaoColeta page', () => {
    beforeEach(() => {
      cy.visit(`${solicitacaoColetaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SolicitacaoColeta');
    });

    it('should create an instance of SolicitacaoColeta', () => {
      cy.get(`[data-cy="coletado"]`).should('not.be.checked');
      cy.get(`[data-cy="coletado"]`).click();
      cy.get(`[data-cy="coletado"]`).should('be.checked');

      cy.get(`[data-cy="dataHoraColeta"]`).type('2024-01-19T19:16');
      cy.get(`[data-cy="dataHoraColeta"]`).blur();
      cy.get(`[data-cy="dataHoraColeta"]`).should('have.value', '2024-01-19T19:16');

      cy.get(`[data-cy="entregue"]`).should('not.be.checked');
      cy.get(`[data-cy="entregue"]`).click();
      cy.get(`[data-cy="entregue"]`).should('be.checked');

      cy.get(`[data-cy="dataHoraEntrega"]`).type('2024-01-20T12:28');
      cy.get(`[data-cy="dataHoraEntrega"]`).blur();
      cy.get(`[data-cy="dataHoraEntrega"]`).should('have.value', '2024-01-20T12:28');

      cy.get(`[data-cy="valorTotal"]`).type('4.67');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '4.67');

      cy.get(`[data-cy="observacao"]`).type('finally miserably commandment');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'finally miserably commandment');

      cy.get(`[data-cy="dataCadastro"]`).type('2024-01-19T22:37');
      cy.get(`[data-cy="dataCadastro"]`).blur();
      cy.get(`[data-cy="dataCadastro"]`).should('have.value', '2024-01-19T22:37');

      cy.get(`[data-cy="dataAtualizacao"]`).type('2024-01-20T14:10');
      cy.get(`[data-cy="dataAtualizacao"]`).blur();
      cy.get(`[data-cy="dataAtualizacao"]`).should('have.value', '2024-01-20T14:10');

      cy.get(`[data-cy="cancelado"]`).should('not.be.checked');
      cy.get(`[data-cy="cancelado"]`).click();
      cy.get(`[data-cy="cancelado"]`).should('be.checked');

      cy.get(`[data-cy="dataCancelamento"]`).type('2024-01-19T23:55');
      cy.get(`[data-cy="dataCancelamento"]`).blur();
      cy.get(`[data-cy="dataCancelamento"]`).should('have.value', '2024-01-19T23:55');

      cy.get(`[data-cy="usuarioCancelamento"]`).type('access oh');
      cy.get(`[data-cy="usuarioCancelamento"]`).should('have.value', 'access oh');

      cy.get(`[data-cy="removido"]`).should('not.be.checked');
      cy.get(`[data-cy="removido"]`).click();
      cy.get(`[data-cy="removido"]`).should('be.checked');

      cy.get(`[data-cy="dataRemocao"]`).type('2024-01-19T20:41');
      cy.get(`[data-cy="dataRemocao"]`).blur();
      cy.get(`[data-cy="dataRemocao"]`).should('have.value', '2024-01-19T20:41');

      cy.get(`[data-cy="usuarioRemocao"]`).type('fatigues ha ferociously');
      cy.get(`[data-cy="usuarioRemocao"]`).should('have.value', 'fatigues ha ferociously');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        solicitacaoColeta = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', solicitacaoColetaPageUrlPattern);
    });
  });
});
