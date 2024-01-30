import { MusicItemDto } from './../../service/dto.d';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TrackDetailsData } from './track-details-config.d';
import { FormGroup } from '@angular/forms';
import { Component, Inject, OnInit } from '@angular/core';
import { StarRatingComponent } from '../../view/star-rating/star-rating.component';

@Component({
    selector: 'app-track-details',
    templateUrl: './track-details.component.html',
    styleUrls: ['./track-details.component.scss'],
    standalone: true,
    imports: [StarRatingComponent]
})

/**
 * Experimental
 * =========================
 * Current state : not used 
 * Alternative: Use link to "Now Listening"
 */
export class TrackDetailsComponent implements OnInit {

  form: FormGroup;

  data : TrackDetailsData = {albumartUri: '', inputSource: '', currentTrack : null}

  constructor(@Inject(MAT_DIALOG_DATA) data: TrackDetailsData,
  private dialogRef: MatDialogRef<TrackDetailsComponent>) { 
    if (data) {
      this.data = data;
    }
  }

  ngOnInit(): void {
  }

  close(): void {
    this.dialogRef.close();
  }
}
