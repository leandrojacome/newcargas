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
  const embarcadorSample = { nome: 'saucer quaintly well-made', cnpj: 'behindXXXXXXXX', dataCadastro: '2024-01-19T20:24:17.789Z' };

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

      it('edit button click should load edit Embarcador page and save', () => {
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
      cy.get(`[data-cy="nome"]`).type('unto gosh knuckle');
      cy.get(`[data-cy="nome"]`).should('have.value', 'unto gosh knuckle');

      cy.get(`[data-cy="cnpj"]`).type('or insideXXXXX');
      cy.get(`[data-cy="cnpj"]`).should('have.value', 'or insideXXXXX');

      cy.get(`[data-cy="razaoSocial"]`).type('plan who foolishly');
      cy.get(`[data-cy="razaoSocial"]`).should('have.value', 'plan who foolishly');

      cy.get(`[data-cy="inscricaoEstadual"]`).type('kindheartedly');
      cy.get(`[data-cy="inscricaoEstadual"]`).should('have.value', 'kindheartedly');

      cy.get(`[data-cy="inscricaoMunicipal"]`).type('exfoliate library ou');
      cy.get(`[data-cy="inscricaoMunicipal"]`).should('have.value', 'exfoliate library ou');

      cy.get(`[data-cy="responsavel"]`).type('physically');
      cy.get(`[data-cy="responsavel"]`).should('have.value', 'physically');

      cy.get(`[data-cy="cep"]`).type('oddlyXXX');
      cy.get(`[data-cy="cep"]`).should('have.value', 'oddlyXXX');

      cy.get(`[data-cy="endereco"]`).type('throughout vane');
      cy.get(`[data-cy="endereco"]`).should('have.value', 'throughout vane');

      cy.get(`[data-cy="numero"]`).type('whoever');
      cy.get(`[data-cy="numero"]`).should('have.value', 'whoever');

      cy.get(`[data-cy="complemento"]`).type('considering blah');
      cy.get(`[data-cy="complemento"]`).should('have.value', 'considering blah');

      cy.get(`[data-cy="bairro"]`).type('wash afterwards without');
      cy.get(`[data-cy="bairro"]`).should('have.value', 'wash afterwards without');

      cy.get(`[data-cy="telefone"]`).type('blah butXX');
      cy.get(`[data-cy="telefone"]`).should('have.value', 'blah butXX');

      cy.get(`[data-cy="email"]`).type('Elisa.Albuquerque@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Elisa.Albuquerque@hotmail.com');

      cy.get(`[data-cy="observacao"]`).type('enchant destabilise ultimately');
      cy.get(`[data-cy="observacao"]`).should('have.value', 'enchant destabilise ultimately');

      cy.get(`[data-cy="dataCadastro"]`).type('2024-01-19T19:23');
      cy.get(`[data-cy="dataCadastro"]`).blur();
      cy.get(`[data-cy="dataCadastro"]`).should('have.value', '2024-01-19T19:23');

      cy.get(`[data-cy="usuarioCadastro"]`).type('enormously');
      cy.get(`[data-cy="usuarioCadastro"]`).should('have.value', 'enormously');

      cy.get(`[data-cy="dataAtualizacao"]`).type('2024-01-19T16:52');
      cy.get(`[data-cy="dataAtualizacao"]`).blur();
      cy.get(`[data-cy="dataAtualizacao"]`).should('have.value', '2024-01-19T16:52');

      cy.get(`[data-cy="usuarioAtualizacao"]`).type('baboon while');
      cy.get(`[data-cy="usuarioAtualizacao"]`).should('have.value', 'baboon while');

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
