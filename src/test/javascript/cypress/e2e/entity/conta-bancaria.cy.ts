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

describe('ContaBancaria e2e test', () => {
  const contaBancariaPageUrl = '/conta-bancaria';
  const contaBancariaPageUrlPattern = new RegExp('/conta-bancaria(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const contaBancariaSample = { agencia: 'less glidi', conta: 'tomorrow b', dataCadastro: '2024-01-20T13:49:47.598Z' };

  let contaBancaria;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/conta-bancarias+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/conta-bancarias').as('postEntityRequest');
    cy.intercept('DELETE', '/api/conta-bancarias/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contaBancaria) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/conta-bancarias/${contaBancaria.id}`,
      }).then(() => {
        contaBancaria = undefined;
      });
    }
  });

  it('ContaBancarias menu should load ContaBancarias page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('conta-bancaria');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContaBancaria').should('exist');
    cy.url().should('match', contaBancariaPageUrlPattern);
  });

  describe('ContaBancaria page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contaBancariaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContaBancaria page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/conta-bancaria/new$'));
        cy.getEntityCreateUpdateHeading('ContaBancaria');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contaBancariaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/conta-bancarias',
          body: contaBancariaSample,
        }).then(({ body }) => {
          contaBancaria = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/conta-bancarias+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/conta-bancarias?page=0&size=20>; rel="last",<http://localhost/api/conta-bancarias?page=0&size=20>; rel="first"',
              },
              body: [contaBancaria],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contaBancariaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContaBancaria page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contaBancaria');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contaBancariaPageUrlPattern);
      });

      it('edit button click should load edit ContaBancaria page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContaBancaria');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contaBancariaPageUrlPattern);
      });

      it('edit button click should load edit ContaBancaria page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContaBancaria');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contaBancariaPageUrlPattern);
      });

      it('last delete button click should delete instance of ContaBancaria', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contaBancaria').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contaBancariaPageUrlPattern);

        contaBancaria = undefined;
      });
    });
  });

  describe('new ContaBancaria page', () => {
    beforeEach(() => {
      cy.visit(`${contaBancariaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContaBancaria');
    });

    it('should create an instance of ContaBancaria', () => {
      cy.get(`[data-cy="agencia"]`).type('sleepily o');
      cy.get(`[data-cy="agencia"]`).should('have.value', 'sleepily o');

      cy.get(`[data-cy="conta"]`).type('absent');
      cy.get(`[data-cy="conta"]`).should('have.value', 'absent');

      cy.get(`[data-cy="observacao"]`).type('attainment memorable powerfully');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'attainment memorable powerfully');

      cy.get(`[data-cy="tipo"]`).type('eurocentrism vice rust');
      cy.get(`[data-cy="tipo"]`).should('have.value', 'eurocentrism vice rust');

      cy.get(`[data-cy="pix"]`).type('recklessly');
      cy.get(`[data-cy="pix"]`).should('have.value', 'recklessly');

      cy.get(`[data-cy="titular"]`).type('above');
      cy.get(`[data-cy="titular"]`).should('have.value', 'above');

      cy.get(`[data-cy="dataCadastro"]`).type('2024-01-20T14:10');
      cy.get(`[data-cy="dataCadastro"]`).blur();
      cy.get(`[data-cy="dataCadastro"]`).should('have.value', '2024-01-20T14:10');

      cy.get(`[data-cy="dataAtualizacao"]`).type('2024-01-20T06:44');
      cy.get(`[data-cy="dataAtualizacao"]`).blur();
      cy.get(`[data-cy="dataAtualizacao"]`).should('have.value', '2024-01-20T06:44');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        contaBancaria = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', contaBancariaPageUrlPattern);
    });
  });
});
