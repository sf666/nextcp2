// Records animated documentation clips from the production nextCP/2 instance.
// A synthetic cursor is drawn into the page because Playwright videos do not
// capture the real mouse pointer.
// usage: node record.js <clipName>
const { chromium } = require('playwright');
const fs = require('fs');
const path = require('path');

const BASE = 'http://192.168.112.5:8085';
const PROFILE = path.join(__dirname, 'profile');
const W = 1456, H = 1210;

const CURSOR_SCRIPT = `
(() => {
  const install = () => {
    if (document.getElementById('__doc_cursor')) return;
    const c = document.createElement('div');
    c.id = '__doc_cursor';
    c.style.cssText = [
      'position:fixed', 'left:0', 'top:0', 'z-index:2147483647', 'pointer-events:none',
      'width:26px', 'height:26px', 'margin:-3px 0 0 -3px',
      'transition:transform 500ms cubic-bezier(.22,.61,.36,1)',
      'transform:translate(60px,60px)',
    ].join(';');
    c.innerHTML = '<svg viewBox="0 0 24 24" width="26" height="26">' +
      '<path d="M5 2 L5 19 L9.5 14.5 L12.5 21.5 L15.5 20 L12.5 13.5 L19 13.5 Z" ' +
      'fill="#ffffff" stroke="rgba(0,0,0,0.65)" stroke-width="1.2"/></svg>';
    const r = document.createElement('div');
    r.id = '__doc_ripple';
    r.style.cssText = [
      'position:fixed', 'left:0', 'top:0', 'z-index:2147483646', 'pointer-events:none',
      'width:34px', 'height:34px', 'margin:-17px 0 0 -17px', 'border-radius:50%',
      'border:2px solid rgba(34,211,238,0.95)', 'background:rgba(34,211,238,0.22)',
      'opacity:0', 'transform:translate(60px,60px) scale(0.3)',
      'transition:opacity 220ms ease, transform 220ms ease',
    ].join(';');
    document.documentElement.appendChild(c);
    document.documentElement.appendChild(r);
  };
  if (document.documentElement) install();
  document.addEventListener('DOMContentLoaded', install);
  window.__docCursor = {
    move(x, y) {
      install();
      document.getElementById('__doc_cursor').style.transform = 'translate(' + x + 'px,' + y + 'px)';
      document.getElementById('__doc_ripple').style.transform = 'translate(' + x + 'px,' + y + 'px) scale(0.3)';
    },
    click(x, y) {
      install();
      const r = document.getElementById('__doc_ripple');
      r.style.transition = 'opacity 120ms ease, transform 260ms ease';
      r.style.transform = 'translate(' + x + 'px,' + y + 'px) scale(1)';
      r.style.opacity = '1';
      setTimeout(() => { r.style.opacity = '0'; }, 300);
    },
  };
})();
`;

function makeHelpers(page) {
  const pos = { x: 60, y: 60 };
  const move = async (x, y, wait = 620) => {
    await page.evaluate(([a, b]) => window.__docCursor.move(a, b), [x, y]);
    await page.mouse.move(x, y);
    pos.x = x; pos.y = y;
    await page.waitForTimeout(wait);
  };
  const clickMark = async (x, y) => {
    await page.evaluate(([a, b]) => window.__docCursor.click(a, b), [x, y]);
    await page.waitForTimeout(260);
  };
  const moveToEl = async (locator, wait = 620) => {
    await locator.scrollIntoViewIfNeeded().catch(() => {});
    await page.waitForTimeout(120);
    const box = await locator.boundingBox();
    if (!box) throw new Error('element not visible');
    await move(Math.round(box.x + box.width / 2), Math.round(box.y + box.height / 2), wait);
    return box;
  };
  const click = async (locator, { pause = 500 } = {}) => {
    await moveToEl(locator);
    await clickMark(pos.x, pos.y);
    await locator.click();
    await page.waitForTimeout(pause);
  };
  const type = async (locator, text, delay = 130) => {
    await locator.type(text, { delay });
  };
  return { move, moveToEl, click, clickMark, type, pos };
}

async function record(name, fn) {
  const dir = path.join(__dirname, 'vid_' + name);
  fs.rmSync(dir, { recursive: true, force: true });
  const t0 = Date.now();
  const ctx = await chromium.launchPersistentContext(PROFILE, {
    channel: 'chrome',
    viewport: { width: W, height: H },
    deviceScaleFactor: 1,
    recordVideo: { dir, size: { width: W, height: H } },
    args: ['--hide-scrollbars'],
  });
  await ctx.addInitScript(CURSOR_SCRIPT);
  const page = ctx.pages()[0] || (await ctx.newPage());
  const h = makeHelpers(page);
  let startOffset = 0;
  const beginTake = () => { startOffset = (Date.now() - t0) / 1000; };
  await fn({ page, h, beginTake, goto: async (hash, wait = 4500) => {
    await page.goto('about:blank');
    await page.goto(BASE + hash);
    await page.waitForTimeout(wait);
    await page.evaluate(CURSOR_SCRIPT);
  } });
  await page.waitForTimeout(1200);
  await ctx.close();
  const file = fs.readdirSync(dir).find((f) => f.endsWith('.webm'));
  console.log(JSON.stringify({ video: path.join(dir, file), startOffset: startOffset.toFixed(2), total: ((Date.now() - t0) / 1000).toFixed(2) }));
}

const clips = {
  // Create a server side playlist inside the MY PLAYLISTS folder, add two tracks,
  // remove one again and finally delete the playlist.
  async playlist_create_add({ page, h, beginTake, goto }) {
    const NAME = 'docs demo';
    // Every dialog is a CDK overlay with a backdrop that swallows clicks while it is
    // still on screen, so wait for it to go away before aiming at the page again.
    const noBackdrop = async () => {
      await page.waitForFunction(() => !document.querySelector('.cdk-overlay-backdrop'), null, { timeout: 20000 }).catch(() => {});
      await page.waitForTimeout(500);
    };
    const step = (m) => console.log('  step: ' + m);
    await goto('/#/music-library/290', 5000);
    beginTake();
    await page.waitForTimeout(900);

    // 1) create the playlist in the folder that feeds the sidebar
    step('open sidebar dialog');
    await h.click(page.locator('mat-icon', { hasText: 'pending' }).first(), { pause: 1100 });
    await h.click(page.locator('button.tab', { hasText: 'create playlist' }).first(), { pause: 700 });
    const nameInput = page.locator('#newPlaylistName');
    await h.click(nameInput, { pause: 300 });
    await h.type(nameInput, NAME, 130);
    await page.waitForTimeout(700);
    await h.click(page.locator('button.glass-btn.primary', { hasText: 'create' }).first(), { pause: 1500 });
    await noBackdrop();
    const sidebarEntry = page.locator('.sidebar button', { hasText: NAME }).first();
    await sidebarEntry.waitFor({ state: 'visible', timeout: 20000 });
    await page.waitForTimeout(1800);

    step('open Charts');
    // 2) open a track list and add two tracks to the new playlist
    await h.click(page.locator('.sidebar button', { hasText: 'Charts' }).first(), { pause: 3500 });
    for (const idx of [1, 2]) {
      step('add track ' + idx);
      await h.click(page.locator('div[id^="OPT_OPTIONS_"]').nth(idx), { pause: 900 });
      await h.click(page.locator('.playlist-item', { hasText: 'Add to playlist' }).first(), { pause: 1500 });
      const row = page.locator('.playlist-row', { hasText: NAME }).first();
      await row.waitFor({ state: 'visible', timeout: 15000 });
      await h.click(row.locator('button', { hasText: 'add track' }), { pause: 1500 });
      await noBackdrop();
      await page.waitForTimeout(1200);
    }

    step('open new playlist');
    // 3) open the new playlist and remove one track again
    await h.click(page.locator('.sidebar button', { hasText: NAME }).first(), { pause: 3800 });
    await h.click(page.locator('div[id^="OPT_OPTIONS_"]').first(), { pause: 900 });
    await h.click(page.locator('.playlist-item', { hasText: 'Delete from playlist' }).first(), { pause: 1200 });
    // removing a track is confirmed first
    await h.click(page.locator('.confirm-actions button.danger'), { pause: 2200 });
    // the song options dialog can stay open behind the confirmation - close it
    await page.keyboard.press('Escape');
    await noBackdrop();
    await page.waitForTimeout(2000);

    step('delete playlist');
    // 4) delete the playlist again - the media server asks for confirmation first
    await h.click(page.locator('mat-icon', { hasText: 'pending' }).first(), { pause: 1100 });
    await h.click(page.locator('button.tab', { hasText: 'delete playlist' }).first(), { pause: 1300 });
    const plFilter = page.locator('input[placeholder="search for playlist"]');
    await h.click(plFilter, { pause: 300 });
    await h.type(plFilter, 'docs', 150);
    await page.waitForTimeout(1100);
    const delRow = page.locator('.playlist-row', { hasText: NAME }).first();
    await delRow.waitFor({ state: 'visible', timeout: 15000 });
    await h.click(delRow.locator('button', { hasText: 'delete' }), { pause: 1500 });
    await h.click(page.locator('.confirm-actions button.danger'), { pause: 2000 });
    await noBackdrop();
    await page.waitForTimeout(2500);
  },

  // Configure the MY PLAYLISTS sidebar: open the folder options of a folder holding
  // playlists and pick "Set MY PLAYLISTS". Recorded with the sidebar section empty.
  async playlist_sidebar({ page, h, beginTake, goto }) {
    await goto('/#/music-library/290', 5000);
    beginTake();
    await page.waitForTimeout(1100);
    await h.click(page.locator('#options_button'), { pause: 1100 });
    const item = page.locator('.playlist-item', { hasText: 'Set MY PLAYLISTS' }).first();
    await h.click(item, { pause: 4200 });
  },

  // UPnP / global search: type a query, browse the quick result panel, open an album
  async search({ page, h, beginTake, goto }) {
    await goto('/#/music-library/290', 5000);
    beginTake();
    await page.waitForTimeout(700);
    const input = page.locator('input.nav-search');
    await h.click(input, { pause: 300 });
    await h.type(input, 'kraftwerk', 140);
    // the quick search panel refreshes per keystroke - wait for the final result set
    await page.waitForTimeout(4200);
    // The panel groups the hits into Title / Album / Playlist / Artist - walk over them
    const artistRow = page.locator('.section-box').nth(3).locator('.search-row').first();
    await h.moveToEl(artistRow, 1500);
    await page.waitForTimeout(900);
    await h.click(page.locator('button.search-clear'), { pause: 1800 });
  },
};

(async () => {
  const name = process.argv[2];
  if (!clips[name]) { console.error('unknown clip: ' + name); process.exit(1); }
  await record(name, clips[name]);
})().catch((e) => { console.error(String(e && e.message || e).slice(0, 1500)); process.exit(1); });
