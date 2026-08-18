// Helper for driving the production nextCP/2 UI and taking documentation screenshots.
const { chromium } = require('playwright');
const path = require('path');

const BASE = 'http://192.168.112.5:8085';
const PROFILE = path.join(__dirname, 'profile');

async function open({ w = 1456, h = 900, scale = 2, url = '/' } = {}) {
  const ctx = await chromium.launchPersistentContext(PROFILE, {
    channel: 'chrome',
    viewport: { width: w, height: h },
    deviceScaleFactor: scale,
    args: ['--hide-scrollbars', '--force-device-scale-factor=' + scale],
  });
  const page = ctx.pages()[0] || (await ctx.newPage());
  await page.goto(BASE + url, { waitUntil: 'domcontentloaded' });
  await page.waitForTimeout(3500);
  return { ctx, page };
}

module.exports = { open, BASE, PROFILE };
