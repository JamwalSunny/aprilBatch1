const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    specPattern: 'cypress/e2e/**/*.cy.js',
    supportFile: 'cypress/support/e2e.js',
    viewportWidth: 1440,
    viewportHeight: 900,
    defaultCommandTimeout: 15000,
    requestTimeout: 15000,
    video: false,
    chromeWebSecurity: false,
    retries: 1,
    env: {
      TESTMD_USERNAME: process.env.TESTMD_USERNAME,
      TESTMD_PASSWORD: process.env.TESTMD_PASSWORD,
    },
  },
});