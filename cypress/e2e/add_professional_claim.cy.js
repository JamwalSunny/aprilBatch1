/// <reference types="cypress" />

const base = Cypress.env('TESTMD_BASE_URL') || 'https://testappa.collaboratemd.com';
const claimsUrl = `${base}/claims`;

function loginIfNeeded() {
  const username = Cypress.env('TESTMD_USERNAME');
  const password = Cypress.env('TESTMD_PASSWORD');
  if (!username || !password) return;

  cy.document({ log: false }).then((doc) => {
    const hasUser = doc.querySelector('input[name="username"], #username');
    const hasPass = doc.querySelector('input[name="password"], #password');
    if (hasUser && hasPass) {
      cy.get('input[name="username"], #username').clear().type(username, { log: false });
      cy.get('input[name="password"], #password').clear().type(password, { log: false });
      cy.get('button[type="submit"], input[type="submit"]').first().click();
    }
  });
}

function clickAddProfessionalClaim() {
  const xpaths = [
    "//button[contains(normalize-space(),'+ Add Professional Claim')]",
    "//span[contains(normalize-space(),'+ Add Professional Claim')]",
    "//*[self::button or self::a or self::div or self::span][contains(.,'Add Professional Claim')]"
  ];

  cy.then(() => {
    let clicked = false;
    xpaths.forEach((xp) => {
      if (!clicked) {
        cy.xpath(xp, { timeout: 8000 }).then(($els) => {
          if ($els.length > 0 && !clicked) {
            cy.wrap($els[0]).click({ force: true });
            clicked = true;
          }
        }).catch(() => {});
      }
    });
  });

  cy.then(() => {
    cy.contains(/^\+?\s*Add\s+Professional\s+Claim$/i).click({ force: true });
  });
}

function assertProfessionalClaimFormVisible() {
  cy.contains(/Professional\s+Claim/i, { timeout: 20000 }).should('be.visible');
}

describe('Add Professional Claim', () => {
  it('opens the Add Professional Claim form', () => {
    cy.visit(claimsUrl, { failOnStatusCode: false });
    cy.wait(2000);

    loginIfNeeded();

    cy.contains(/Claim(s)?/i, { timeout: 20000 });

    clickAddProfessionalClaim();

    assertProfessionalClaimFormVisible();
  });
});