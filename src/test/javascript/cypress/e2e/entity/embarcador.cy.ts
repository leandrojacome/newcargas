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

describe('Embarcador e2e test', () => {
  const embarcadorPageUrl = '/embarcador';
  const embarcadorPageUrlPattern = new RegExp('/embarcador(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const embarcadorSample = { nome: 'embellished striking', cnpj: 'likely overwor' };

  let embarcador;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/embarcadors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/embarcadors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/embarcadors/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (embarcador) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/embarcadors/${embarcador.id}`,
      }).then(() => {
        embarcador = undefined;
      });
    }
  });

  it('Embarcadors menu should load Embarcadors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('embarcador');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Embarcador').should('exist');
    cy.url().should('match', embarcadorPageUrlPattern);
  });

  describe('Embarcador page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(embarcadorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Embarcador page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/embarcador/new$'));
        cy.getEntityCreateUpdateHeading('Embarcador');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', embarcadorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/embarcadors',
          body: embarcadorSample,
        }).then(({ body }) => {
          embarcador = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/embarcadors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/embarcadors?page=0&size=20>; rel="last",<http://localhost/api/embarcadors?page=0&size=20>; rel="first"',
              },
              body: [embarcador],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(embarcadorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Embarcador page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('embarcador');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', embarcadorPageUrlPattern);
      });

      it('edit button click should load edit Embarcador page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Embarcador');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', embarcadorPageUrlPattern);
      });

      it.skip('edit button click should load edit Embarcador page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Embarcador');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', embarcadorPageUrlPattern);
      });

      it('last delete button click should delete instance of Embarcador', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('embarcador').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', embarcadorPageUrlPattern);

        embarcador = undefined;
      });
    });
  });

  describe('new Embarcador page', () => {
    beforeEach(() => {
      cy.visit(`${embarcadorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Embarcador');
    });

    it('should create an instance of Embarcador', () => {
      cy.get(`[data-cy="nome"]`).type('owlishly');
      cy.get(`[data-cy="nome"]`).should('have.value', 'owlishly');

      cy.get(`[data-cy="cnpj"]`).type('or drab viciou');
      cy.get(`[data-cy="cnpj"]`).should('have.value', 'or drab viciou');

      cy.get(`[data-cy="razaoSocial"]`).type('before');
      cy.get(`[data-cy="razaoSocial"]`).should('have.value', 'before');

      cy.get(`[data-cy="inscricaoEstadual"]`).type('quizzically around a');
      cy.get(`[data-cy="inscricaoEstadual"]`).should('have.value', 'quizzically around a');

      cy.get(`[data-cy="inscricaoMunicipal"]`).type('brr pointless');
      cy.get(`[data-cy="inscricaoMunicipal"]`).should('have.value', 'brr pointless');

      cy.get(`[data-cy="responsavel"]`).type('whenever following');
      cy.get(`[data-cy="responsavel"]`).should('have.value', 'whenever following');

      cy.get(`[data-cy="cep"]`).type('swab dem');
      cy.get(`[data-cy="cep"]`).should('have.value', 'swab dem');

      cy.get(`[data-cy="endereco"]`).type('reasonable');
      cy.get(`[data-cy="endereco"]`).should('have.value', 'reasonable');

      cy.get(`[data-cy="numero"]`).type('clothing p');
      cy.get(`[data-cy="numero"]`).should('have.value', 'clothing p');

      cy.get(`[data-cy="complemento"]`).type('cloth');
      cy.get(`[data-cy="complemento"]`).should('have.value', 'cloth');

      cy.get(`[data-cy="bairro"]`).type('likewise reappear');
      cy.get(`[data-cy="bairro"]`).should('have.value', 'likewise reappear');

      cy.get(`[data-cy="telefone"]`).type('pish assaul');
      cy.get(`[data-cy="telefone"]`).should('have.value', 'pish assaul');

      cy.get(`[data-cy="email"]`).type('EnzoGabriel_Carvalho@yahoo.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'EnzoGabriel_Carvalho@yahoo.com');

      cy.get(`[data-cy="observacao"]`).type('tussle nippy');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'tussle nippy');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        embarcador = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', embarcadorPageUrlPattern);
    });
  });
});
