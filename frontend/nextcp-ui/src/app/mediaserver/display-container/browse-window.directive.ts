import {
  Directive,
  ElementRef,
  computed,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { DOCUMENT } from '@angular/common';

const BUFFER_ROWS = 3;

export function browseRowRange(
  totalRows: number,
  rowHeight: number,
  viewportHeight: number,
  offset: number,
): { first: number; last: number } {
  const current = Math.min(
    Math.floor(Math.max(0, offset) / rowHeight),
    Math.max(0, totalRows - 1),
  );
  const first = Math.max(0, current - BUFFER_ROWS);
  const last = Math.min(
    totalRows,
    first + Math.ceil(viewportHeight / rowHeight) + 2 * BUFFER_ROWS,
  );
  return { first, last };
}

/**
 * Windows evenly spaced rows inside the shared browse scroller. The marked grid holds only
 * rendered items, with spacers outside it so its grid gaps do not change the scroll height.
 */
@Directive({ selector: '[browseWindow]', exportAs: 'browseWindow' })
export class BrowseWindowDirective<T> {
  private readonly host = inject<ElementRef<HTMLElement>>(ElementRef).nativeElement;
  // The embedded view can be detached when its directives are constructed.
  private readonly parent = inject(DOCUMENT).getElementById('mainContent');
  private readonly range = signal({ start: 0, end: 20, before: 0, after: 0 }, {
    equal: (a, b) => a.start === b.start && a.end === b.end &&
      a.before === b.before && a.after === b.after,
  });
  private schedule?: (measure: boolean) => void;

  items = input.required<readonly T[]>({ alias: 'browseWindow' });
  enabled = input(false, { alias: 'browseWindowEnabled' });
  active = computed(() => this.enabled() && this.parent !== null);
  visibleItems = computed(() => {
    const items = this.items();
    const { start, end } = this.range();
    return this.active() ? items.slice(start, end) : items;
  });
  before = computed(() => this.active() ? this.range().before : 0);
  after = computed(() => this.active() ? this.range().after : 0);

  constructor() {
    effect((onCleanup) => {
      const parent = this.parent;
      if (!parent || !this.active()) {
        return;
      }

      let frame: number | undefined;
      let rowHeight = 0;
      let columns = 1;
      let needsMeasure = true;
      this.schedule = (measure) => {
        needsMeasure ||= measure;
        if (frame !== undefined) {
          return;
        }
        frame = requestAnimationFrame(() => {
          frame = undefined;
          const count = this.items().length;
          if (needsMeasure) {
            const grid = this.host.querySelector<HTMLElement>('[data-window-rows]');
            const row = grid?.firstElementChild;
            if (!grid || !row) {
              this.range.set({
                start: 0, end: 20, before: 0, after: 0,
              });
              return;
            }
            const style = getComputedStyle(grid);
            columns = style.gridTemplateColumns === 'none'
              ? 1 : Math.max(1, style.gridTemplateColumns.split(' ').length);
            rowHeight = row.getBoundingClientRect().height + (parseFloat(style.rowGap) || 0);
            needsMeasure = rowHeight <= 0;
          }
          if (needsMeasure) {
            return;
          }

          const totalRows = Math.ceil(count / columns);
          const offset = parent.getBoundingClientRect().top - this.host.getBoundingClientRect().top;
          const { first, last } = browseRowRange(totalRows, rowHeight, parent.clientHeight, offset);
          this.range.set({
            start: first * columns,
            end: last * columns,
            before: first * rowHeight,
            after: (totalRows - last) * rowHeight,
          });
        });
      };

      const onScroll = () => this.schedule?.(false);
      parent.addEventListener('scroll', onScroll, { passive: true });
      const observer = new ResizeObserver(() => this.schedule?.(true));
      observer.observe(this.host);
      observer.observe(parent);
      this.schedule(true);
      onCleanup(() => {
        parent.removeEventListener('scroll', onScroll);
        observer.disconnect();
        if (frame !== undefined) {
          cancelAnimationFrame(frame);
        }
        this.schedule = undefined;
      });
    });
    effect(() => {
      this.items();
      this.schedule?.(true);
    });
  }
}
