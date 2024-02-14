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

describe('Qualityrecords e2e test', () => {
  const qualityrecordsPageUrl = '/qualityrecords';
  const qualityrecordsPageUrlPattern = new RegExp('/qualityrecords(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const qualityrecordsSample = { supplier: 'ouin diplomate' };

  let qualityrecords;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/qualityrecords+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/qualityrecords').as('postEntityRequest');
    cy.intercept('DELETE', '/api/qualityrecords/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (qualityrecords) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/qualityrecords/${qualityrecords.id}`,
      }).then(() => {
        qualityrecords = undefined;
      });
    }
  });

  it('Qualityrecords menu should load Qualityrecords page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('qualityrecords');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Qualityrecords').should('exist');
    cy.url().should('match', qualityrecordsPageUrlPattern);
  });

  describe('Qualityrecords page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(qualityrecordsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Qualityrecords page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/qualityrecords/new$'));
        cy.getEntityCreateUpdateHeading('Qualityrecords');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', qualityrecordsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/qualityrecords',
          body: qualityrecordsSample,
        }).then(({ body }) => {
          qualityrecords = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/qualityrecords+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/qualityrecords?page=0&size=20>; rel="last",<http://localhost/api/qualityrecords?page=0&size=20>; rel="first"',
              },
              body: [qualityrecords],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(qualityrecordsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Qualityrecords page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('qualityrecords');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', qualityrecordsPageUrlPattern);
      });

      it('edit button click should load edit Qualityrecords page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Qualityrecords');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', qualityrecordsPageUrlPattern);
      });

      it('edit button click should load edit Qualityrecords page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Qualityrecords');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', qualityrecordsPageUrlPattern);
      });

      it('last delete button click should delete instance of Qualityrecords', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('qualityrecords').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', qualityrecordsPageUrlPattern);

        qualityrecords = undefined;
      });
    });
  });

  describe('new Qualityrecords page', () => {
    beforeEach(() => {
      cy.visit(`${qualityrecordsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Qualityrecords');
    });

    it('should create an instance of Qualityrecords', () => {
      cy.get(`[data-cy="supplier"]`).type('extra désagréable');
      cy.get(`[data-cy="supplier"]`).should('have.value', 'extra désagréable');

      cy.get(`[data-cy="test2"]`).type('18658');
      cy.get(`[data-cy="test2"]`).should('have.value', '18658');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        qualityrecords = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', qualityrecordsPageUrlPattern);
    });
  });
});
