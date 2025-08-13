# Cypress E2E for TestMD

## Run

- Install deps: `npm ci` (or `npm install`)
- Headless: `TESTMD_USERNAME=... TESTMD_PASSWORD=... TESTMD_BASE_URL=https://testappa.collaboratemd.com npm test`
- Interactive: `npm run cypress:open`

Credentials are optional; if omitted, the test will proceed only if already authenticated in the browser session.