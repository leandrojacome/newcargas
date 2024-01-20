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

describe('Roteirizacao e2e test', () => {
  const roteirizacaoPageUrl = '/roteirizacao';
  const roteirizacaoPageUrlPattern = new RegExp('/roteirizacao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const roteirizacaoSample = { dataHoraPrimeiraColeta: '2024-01-20T04:34:23.698Z', dataCadastro: '2024-01-20T11:53:11.850Z' };

  let roteirizacao;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/roteirizacaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/roteirizacaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/roteirizacaos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (roteirizacao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/roteirizacaos/${roteirizacao.id}`,
      }).then(() => {
        roteirizacao = undefined;
      });
    }
  });

  it('Roteirizacaos menu should load Roteirizacaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('roteirizacao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Roteirizacao').should('exist');
    cy.url().should('match', roteirizacaoPageUrlPattern);
  });

  describe('Roteirizacao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(roteirizacaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Roteirizacao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/roteirizacao/new$'));
        cy.getEntityCreateUpdateHeading('Roteirizacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roteirizacaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/roteirizacaos',
          body: roteirizacaoSample,
        }).then(({ body }) => {
          roteirizacao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/roteirizacaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/roteirizacaos?page=0&size=20>; rel="last",<http://localhost/api/roteirizacaos?page=0&size=20>; rel="first"',
              },
              body: [roteirizacao],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(roteirizacaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Roteirizacao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('roteirizacao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roteirizacaoPageUrlPattern);
      });

      it('edit button click should load edit Roteirizacao page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Roteirizacao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roteirizacaoPageUrlPattern);
      });

      it('edit button click should load edit Roteirizacao page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Roteirizacao');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roteirizacaoPageUrlPattern);
      });

      it('last delete button click should delete instance of Roteirizacao', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('roteirizacao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roteirizacaoPageUrlPattern);

        roteirizacao = undefined;
      });
    });
  });

  describe('new Roteirizacao page', () => {
    beforeEach(() => {
      cy.visit(`${roteirizacaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Roteirizacao');
    });

    it('should create an instance of Roteirizacao', () => {
      cy.get(`[data-cy="dataHoraPrimeiraColeta"]`).type('2024-01-20T16:57');
      cy.get(`[data-cy="dataHoraPrimeiraColeta"]`).blur();
      cy.get(`[data-cy="dataHoraPrimeiraColeta"]`).should('have.value', '2024-01-20T16:57');

      cy.get(`[data-cy="dataHoraUltimaColeta"]`).type('2024-01-19T18:27');
      cy.get(`[data-cy="dataHoraUltimaColeta"]`).blur();
      cy.get(`[data-cy="dataHoraUltimaColeta"]`).should('have.value', '2024-01-19T18:27');

      cy.get(`[data-cy="dataHoraPrimeiraEntrega"]`).type('2024-01-20T03:47');
      cy.get(`[data-cy="dataHoraPrimeiraEntrega"]`).blur();
      cy.get(`[data-cy="dataHoraPrimeiraEntrega"]`).should('have.value', '2024-01-20T03:47');

      cy.get(`[data-cy="dataHoraUltimaEntrega"]`).type('2024-01-20T16:21');
      cy.get(`[data-cy="dataHoraUltimaEntrega"]`).blur();
      cy.get(`[data-cy="dataHoraUltimaEntrega"]`).should('have.value', '2024-01-20T16:21');

      cy.get(`[data-cy="valorTotal"]`).type('8.33');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '8.33');

      cy.get(`[data-cy="observacao"]`).type('kiddingly');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'kiddingly');

      cy.get(`[data-cy="dataCadastro"]`).type('2024-01-19T21:32');
      cy.get(`[data-cy="dataCadastro"]`).blur();
      cy.get(`[data-cy="dataCadastro"]`).should('have.value', '2024-01-19T21:32');

      cy.get(`[data-cy="usuarioCadastro"]`).type('lady');
      cy.get(`[data-cy="usuarioCadastro"]`).should('have.value', 'lady');

      cy.get(`[data-cy="dataAtualizacao"]`).type('2024-01-19T20:29');
      cy.get(`[data-cy="dataAtualizacao"]`).blur();
      cy.get(`[data-cy="dataAtualizacao"]`).should('have.value', '2024-01-19T20:29');

      cy.get(`[data-cy="usuarioAtualizacao"]`).type('nor within ack');
      cy.get(`[data-cy="usuarioAtualizacao"]`).should('have.value', 'nor within ack');

      cy.get(`[data-cy="cancelado"]`).should('not.be.checked');
      cy.get(`[data-cy="cancelado"]`).click();
      cy.get(`[data-cy="cancelado"]`).should('be.checked');

      cy.get(`[data-cy="dataCancelamento"]`).type('2024-01-20T10:30');
      cy.get(`[data-cy="dataCancelamento"]`).blur();
      cy.get(`[data-cy="dataCancelamento"]`).should('have.value', '2024-01-20T10:30');

      cy.get(`[data-cy="usuarioCancelamento"]`).type('considering into whenever');
      cy.get(`[data-cy="usuarioCancelamento"]`).should('have.value', 'considering into whenever');

      cy.get(`[data-cy="removido"]`).should('not.be.checked');
      cy.get(`[data-cy="removido"]`).click();
      cy.get(`[data-cy="removido"]`).should('be.checked');

      cy.get(`[data-cy="dataRemocao"]`).type('2024-01-20T08:42');
      cy.get(`[data-cy="dataRemocao"]`).blur();
      cy.get(`[data-cy="dataRemocao"]`).should('have.value', '2024-01-20T08:42');

      cy.get(`[data-cy="usuarioRemocao"]`).type('digress');
      cy.get(`[data-cy="usuarioRemocao"]`).should('have.value', 'digress');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        roteirizacao = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', roteirizacaoPageUrlPattern);
    });
  });
});
