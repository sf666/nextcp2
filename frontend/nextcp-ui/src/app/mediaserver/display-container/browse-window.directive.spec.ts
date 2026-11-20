import { browseRowRange } from './browse-window.directive';

describe('browse row range', () => {
  it('buffers a window of rows around the viewport', () => {
    expect(browseRowRange(415, 50, 750, -100)).toEqual({ first: 0, last: 21 });
    expect(browseRowRange(415, 50, 750, 5000)).toEqual({ first: 97, last: 118 });
  });

  it('keeps the last row reachable and clamps the window after a shorter result', () => {
    expect(browseRowRange(415, 50, 750, 20000)).toEqual({ first: 397, last: 415 });
    expect(browseRowRange(5, 50, 750, 20000)).toEqual({ first: 1, last: 5 });
    expect(browseRowRange(0, 50, 750, 20000)).toEqual({ first: 0, last: 0 });
  });
});
