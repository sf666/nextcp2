import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-my-songs',
  templateUrl: './my-songs.component.html',
  styleUrls: ['./my-songs.component.scss'],
  changeDetection: ChangeDetectionStrategy.Eager,
  standalone: true,
})
export class MySongsComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
