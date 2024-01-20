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

describe('StatusColeta e2e test', () => {
  const statusColetaPageUrl = '/status-coleta';
  const statusColetaPageUrlPattern = new RegExp('/status-coleta(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const statusColetaSample = { nome: 'before yet vaguely', dataCadastro: '2024-01-20T13:31:37.165Z' };

  let statusColeta;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/status-coletas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/status-coletas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/status-coletas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (statusColeta) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/status-coletas/${statusColeta.id}`,
      }).then(() => {
        statusColeta = undefined;
      });
    }
  });

  it('StatusColetas menu should load StatusColetas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('status-coleta');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StatusColeta').should('exist');
    cy.url().should('match', statusColetaPageUrlPattern);
  });

  describe('StatusColeta page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(statusColetaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StatusColeta page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/status-coleta/new$'));
        cy.getEntityCreateUpdateHeading('StatusColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/status-coletas',
          body: statusColetaSample,
        }).then(({ body }) => {
          statusColeta = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/status-coletas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/status-coletas?page=0&size=20>; rel="last",<http://localhost/api/status-coletas?page=0&size=20>; rel="first"',
              },
              body: [statusColeta],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(statusColetaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StatusColeta page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('statusColeta');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);
      });

      it('edit button click should load edit StatusColeta page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StatusColeta');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);
      });

      it('edit button click should load edit StatusColeta page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StatusColeta');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);
      });

      it('last delete button click should delete instance of StatusColeta', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('statusColeta').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', statusColetaPageUrlPattern);

        statusColeta = undefined;
      });
    });
  });

  describe('new StatusColeta page', () => {
    beforeEach(() => {
      cy.visit(`${statusColetaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StatusColeta');
    });

    it('should create an instance of StatusColeta', () => {
      cy.get(`[data-cy="nome"]`).type('readily landscape ruddy');
      cy.get(`[data-cy="nome"]`).should('have.value', 'readily landscape ruddy');

      cy.get(`[data-cy="cor"]`).type('who pfft');
      cy.get(`[data-cy="cor"]`).should('have.value', 'who pfft');

      cy.get(`[data-cy="ordem"]`).type('4');
      cy.get(`[data-cy="ordem"]`).should('have.value', '4');

      cy.get(`[data-cy="estadoInicial"]`).should('not.be.checked');
      cy.get(`[data-cy="estadoInicial"]`).click();
      cy.get(`[data-cy="estadoInicial"]`).should('be.checked');

      cy.get(`[data-cy="estadoFinal"]`).should('not.be.checked');
      cy.get(`[data-cy="estadoFinal"]`).click();
      cy.get(`[data-cy="estadoFinal"]`).should('be.checked');

      cy.get(`[data-cy="permiteCancelar"]`).should('not.be.checked');
      cy.get(`[data-cy="permiteCancelar"]`).click();
      cy.get(`[data-cy="permiteCancelar"]`).should('be.checked');

      cy.get(`[data-cy="permiteEditar"]`).should('not.be.checked');
      cy.get(`[data-cy="permiteEditar"]`).click();
      cy.get(`[data-cy="permiteEditar"]`).should('be.checked');

      cy.get(`[data-cy="permiteExcluir"]`).should('not.be.checked');
      cy.get(`[data-cy="permiteExcluir"]`).click();
      cy.get(`[data-cy="permiteExcluir"]`).should('be.checked');

      cy.get(`[data-cy="descricao"]`).type('round');
      cy.get(`[data-cy="descricao"]`).should('have.value', 'round');

      cy.get(`[data-cy="dataCadastro"]`).type('2024-01-19T23:11');
      cy.get(`[data-cy="dataCadastro"]`).blur();
      cy.get(`[data-cy="dataCadastro"]`).should('have.value', '2024-01-19T23:11');

      cy.get(`[data-cy="usuarioCadastro"]`).type('our liberalise');
      cy.get(`[data-cy="usuarioCadastro"]`).should('have.value', 'our liberalise');

      cy.get(`[data-cy="dataAtualizacao"]`).type('2024-01-20T06:18');
      cy.get(`[data-cy="dataAtualizacao"]`).blur();
      cy.get(`[data-cy="dataAtualizacao"]`).should('have.value', '2024-01-20T06:18');

      cy.get(`[data-cy="usuarioAtualizacao"]`).type('dearest give');
      cy.get(`[data-cy="usuarioAtualizacao"]`).should('have.value', 'dearest give');

      cy.get(`[data-cy="ativo"]`).should('not.be.checked');
      cy.get(`[data-cy="ativo"]`).click();
      cy.get(`[data-cy="ativo"]`).should('be.checked');

      cy.get(`[data-cy="removido"]`).should('not.be.checked');
      cy.get(`[data-cy="removido"]`).click();
      cy.get(`[data-cy="removido"]`).should('be.checked');

      cy.get(`[data-cy="dataRemocao"]`).type('2024-01-19T20:38');
      cy.get(`[data-cy="dataRemocao"]`).blur();
      cy.get(`[data-cy="dataRemocao"]`).should('have.value', '2024-01-19T20:38');

      cy.get(`[data-cy="usuarioRemocao"]`).type('oof');
      cy.get(`[data-cy="usuarioRemocao"]`).should('have.value', 'oof');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        statusColeta = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', statusColetaPageUrlPattern);
    });
  });
});
