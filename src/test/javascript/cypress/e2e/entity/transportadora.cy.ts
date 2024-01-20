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

describe('Transportadora e2e test', () => {
  const transportadoraPageUrl = '/transportadora';
  const transportadoraPageUrlPattern = new RegExp('/transportadora(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const transportadoraSample = { nome: 'past', cnpj: 'thunderous dre', dataCadastro: '2024-01-19T18:32:06.622Z' };

  let transportadora;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/transportadoras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transportadoras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transportadoras/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (transportadora) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transportadoras/${transportadora.id}`,
      }).then(() => {
        transportadora = undefined;
      });
    }
  });

  it('Transportadoras menu should load Transportadoras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transportadora');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Transportadora').should('exist');
    cy.url().should('match', transportadoraPageUrlPattern);
  });

  describe('Transportadora page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transportadoraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Transportadora page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/transportadora/new$'));
        cy.getEntityCreateUpdateHeading('Transportadora');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transportadoraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transportadoras',
          body: transportadoraSample,
        }).then(({ body }) => {
          transportadora = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transportadoras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/transportadoras?page=0&size=20>; rel="last",<http://localhost/api/transportadoras?page=0&size=20>; rel="first"',
              },
              body: [transportadora],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(transportadoraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Transportadora page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transportadora');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transportadoraPageUrlPattern);
      });

      it('edit button click should load edit Transportadora page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Transportadora');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transportadoraPageUrlPattern);
      });

      it('edit button click should load edit Transportadora page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Transportadora');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transportadoraPageUrlPattern);
      });

      it('last delete button click should delete instance of Transportadora', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transportadora').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', transportadoraPageUrlPattern);

        transportadora = undefined;
      });
    });
  });

  describe('new Transportadora page', () => {
    beforeEach(() => {
      cy.visit(`${transportadoraPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Transportadora');
    });

    it('should create an instance of Transportadora', () => {
      cy.get(`[data-cy="nome"]`).type('under');
      cy.get(`[data-cy="nome"]`).should('have.value', 'under');

      cy.get(`[data-cy="cnpj"]`).type('likewise relat');
      cy.get(`[data-cy="cnpj"]`).should('have.value', 'likewise relat');

      cy.get(`[data-cy="razaoSocial"]`).type('boot');
      cy.get(`[data-cy="razaoSocial"]`).should('have.value', 'boot');

      cy.get(`[data-cy="inscricaoEstadual"]`).type('despite amidst propa');
      cy.get(`[data-cy="inscricaoEstadual"]`).should('have.value', 'despite amidst propa');

      cy.get(`[data-cy="inscricaoMunicipal"]`).type('lazily');
      cy.get(`[data-cy="inscricaoMunicipal"]`).should('have.value', 'lazily');

      cy.get(`[data-cy="responsavel"]`).type('pro');
      cy.get(`[data-cy="responsavel"]`).should('have.value', 'pro');

      cy.get(`[data-cy="cep"]`).type('honestly');
      cy.get(`[data-cy="cep"]`).should('have.value', 'honestly');

      cy.get(`[data-cy="endereco"]`).type('mime');
      cy.get(`[data-cy="endereco"]`).should('have.value', 'mime');

      cy.get(`[data-cy="numero"]`).type('timer');
      cy.get(`[data-cy="numero"]`).should('have.value', 'timer');

      cy.get(`[data-cy="complemento"]`).type('deficient grizzled excepting');
      cy.get(`[data-cy="complemento"]`).should('have.value', 'deficient grizzled excepting');

      cy.get(`[data-cy="bairro"]`).type('while second-hand hence');
      cy.get(`[data-cy="bairro"]`).should('have.value', 'while second-hand hence');

      cy.get(`[data-cy="telefone"]`).type('amidst bah ');
      cy.get(`[data-cy="telefone"]`).should('have.value', 'amidst bah ');

      cy.get(`[data-cy="email"]`).type('Warley_Moreira@live.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Warley_Moreira@live.com');

      cy.get(`[data-cy="observacao"]`).type('of excluding fumbling');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'of excluding fumbling');

      cy.get(`[data-cy="dataCadastro"]`).type('2024-01-20T06:17');
      cy.get(`[data-cy="dataCadastro"]`).blur();
      cy.get(`[data-cy="dataCadastro"]`).should('have.value', '2024-01-20T06:17');

      cy.get(`[data-cy="usuarioCadastro"]`).type('since herald during');
      cy.get(`[data-cy="usuarioCadastro"]`).should('have.value', 'since herald during');

      cy.get(`[data-cy="dataAtualizacao"]`).type('2024-01-20T13:16');
      cy.get(`[data-cy="dataAtualizacao"]`).blur();
      cy.get(`[data-cy="dataAtualizacao"]`).should('have.value', '2024-01-20T13:16');

      cy.get(`[data-cy="usuarioAtualizacao"]`).type('woot toughen acquiesce');
      cy.get(`[data-cy="usuarioAtualizacao"]`).should('have.value', 'woot toughen acquiesce');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        transportadora = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', transportadoraPageUrlPattern);
    });
  });
});
