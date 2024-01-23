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
  const transportadoraSample = { nome: 'quarrelsomely luxurious', cnpj: 'pew noisyXXXXX' };

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

      it.skip('edit button click should load edit Transportadora page and save', () => {
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
      cy.get(`[data-cy="nome"]`).type('ecclesia');
      cy.get(`[data-cy="nome"]`).should('have.value', 'ecclesia');

      cy.get(`[data-cy="cnpj"]`).type('hungrilyXXXXXX');
      cy.get(`[data-cy="cnpj"]`).should('have.value', 'hungrilyXXXXXX');

      cy.get(`[data-cy="razaoSocial"]`).type('that');
      cy.get(`[data-cy="razaoSocial"]`).should('have.value', 'that');

      cy.get(`[data-cy="inscricaoEstadual"]`).type('angel under ack');
      cy.get(`[data-cy="inscricaoEstadual"]`).should('have.value', 'angel under ack');

      cy.get(`[data-cy="inscricaoMunicipal"]`).type('relative inasmuch');
      cy.get(`[data-cy="inscricaoMunicipal"]`).should('have.value', 'relative inasmuch');

      cy.get(`[data-cy="responsavel"]`).type('boot');
      cy.get(`[data-cy="responsavel"]`).should('have.value', 'boot');

      cy.get(`[data-cy="cep"]`).type('despite ');
      cy.get(`[data-cy="cep"]`).should('have.value', 'despite ');

      cy.get(`[data-cy="endereco"]`).type('lazily');
      cy.get(`[data-cy="endereco"]`).should('have.value', 'lazily');

      cy.get(`[data-cy="numero"]`).type('pro');
      cy.get(`[data-cy="numero"]`).should('have.value', 'pro');

      cy.get(`[data-cy="complemento"]`).type('honestly disclose');
      cy.get(`[data-cy="complemento"]`).should('have.value', 'honestly disclose');

      cy.get(`[data-cy="bairro"]`).type('mime');
      cy.get(`[data-cy="bairro"]`).should('have.value', 'mime');

      cy.get(`[data-cy="telefone"]`).type('timerXXXXX');
      cy.get(`[data-cy="telefone"]`).should('have.value', 'timerXXXXX');

      cy.get(`[data-cy="email"]`).type('Vitor_Moreira@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Vitor_Moreira@hotmail.com');

      cy.get(`[data-cy="observacao"]`).type('old-fashioned always likewise');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'old-fashioned always likewise');

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
