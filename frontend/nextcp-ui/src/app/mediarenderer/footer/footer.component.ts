import { SongOptionsServiceService } from './../../mediaserver/popup/song-options/song-options-service.service';
import { AvailableServerComponent } from './../../popup/available-server/available-server.component';
import { VolumeControlComponent } from './../../popup/volume-control/volume-control.component';
import { MatDialog } from '@angular/material/dialog';
import { AvailableRendererComponent } from './../../popup/available-renderer/available-renderer.component';
import { PlaylistService } from './../../service/playlist.service';
import { RendererService } from './../../service/renderer.service';
import { DeviceService } from './../../service/device.service';
import { TransportService as TransportService } from '../../service/transport.service';
import { ChangeDetectionStrategy, Component, ElementRef, OnInit, Optional, computed, signal } from '@angular/core';
import { QualityBadgeComponent } from '../../util/comp/quality-badge/quality-badge.component';
import { toObservable } from '@angular/core/rxjs-interop';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { ReactiveFormsModule, FormControl } from '@angular/forms';

@Component({
    selector: 'renderer-footer',
    templateUrl: './footer.component.html',
    styleUrls: ['./footer.component.scss'],
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [QualityBadgeComponent, ReactiveFormsModule]
})
export class FooterComponent implements OnInit{

  currentMediaRendererName = signal<string>('');
  sliderPos = computed(() => this.reverseLogVal(this.rendererService.deviceDriverState().volume));

  minVal = 0;
  maxVal = 100;
  expVal = 2;
  volControl = new FormControl(0);
  
  constructor(
    private dialog: MatDialog,
    public transportService: TransportService,
    public deviceService: DeviceService,
    public playlistService: PlaylistService,
    public songOptionsServiceService: SongOptionsServiceService,
    public rendererService: RendererService) {
      toObservable(this.deviceService.selectedMediaRendererDevice).subscribe(data => this.currentMediaRendererName.set(data.friendlyName));
  }

  ngOnInit(): void {
    this.volControl.valueChanges.pipe( debounceTime(300), distinctUntilChanged()).subscribe(val => {
      const norm = val / 100;    
      const logVal = Math.round(this.minVal + (this.maxVal - this.minVal) * Math.pow(norm, this.expVal));
      console.log("volume changed to " + logVal);
      this.rendererService.setVolume(logVal);
    });
  }

  // reverse from volume value to logarithmic slider position
  reverseLogVal(val) : number{
    const norm = (val - this.minVal) / (this.maxVal - this.minVal);
    const rev = Math.round(Math.pow(Math.max(0, norm), 1 / this.expVal) * 100);
    console.log("reverse log val " + val + " to " + rev);
    return rev;
  }

  trackTimePercent = computed(() => {
    if (this.rendererService?.trackTime().percent) {
      return this.streaming() ? 0 : this.rendererService.trackTime().percent;
    }
    return 0;
  });


  public rendererClicked(event: Event): void {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(AvailableRendererComponent, {
      data: { trigger: target },
      panelClass: 'popup'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log(_res);
    });
  }

  public serverClicked(event: Event): void {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(AvailableServerComponent, {
      data: { trigger: target },
      panelClass: 'popup'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log("server dialog closed");
    });
  }

  public volumeClicked(event: Event): void {
    const target = new ElementRef(event.currentTarget);
    const dialogRef = this.dialog.open(VolumeControlComponent, {
      data: { trigger: target },
      panelClass: 'popup'
    });
    dialogRef.afterClosed().subscribe(_res => {
      console.log(_res);
    });
  }

  public getCurrentSongTitle(): string {
    return this.rendererService.currentSongTitle();
  }

  //
  // Footer right : device audio and power control
  //

  hasDeviceDriver(): boolean {
    return this.rendererService.deviceDriverState().hasDeviceDriver;
  }

  powerClicked(): void {
    this.rendererService.powerPressed();
  }

  public getStandbyClass(): string {
    if (this.rendererService.deviceDriverState().standby) {
      return "standbyOn";
    }
    else {
      return "standbyOff";
    }
  }

  volChanged(event): void {
    const expVal = 2;
    const norm = event / 100;    
    const logVal = Math.round(this.minVal + (this.maxVal - this.minVal) * Math.pow(norm, expVal));
    console.log("volume changed to " + logVal);
    this.rendererService.setVolume(logVal);
  }

  streaming(): boolean {
    return this.rendererService.isStreaming();
  }

  getFinishTime(): string {
    return this.rendererService.finishTime();
  }

  public trackTimePercentSeek(value : any) {
    let timeAbs = Math.floor(this.rendererService.trackTime().duration * value/100);
    this.transportService.seek(timeAbs);
  }

  //
  // styling of elements depending on state information
  // =========================================================================================================

  get infoSongClass(): string {
    if (this.rendererService.trackInfoAvailable()) {
      return ""
    } else {
      return "disabled";
    }
  }

  get shuffleClass(): string {
    if (!this.rendererService.canShuffle()) {
      return "disabled";
    }
    if (this.rendererService.isShuffle()) {
      return "active"
    }
    return "";
  }

  get repeatClass(): string {
    if (!this.rendererService.canRepeat()) {
      return "disabled";
    }
    if (this.rendererService.isRepeat()) {
      return "active"
    }
    return "";
  }

  showSongPopup(event: MouseEvent): void {
    if (this.rendererService.trackInfo().currentTrack?.streamingURL) {
      this.songOptionsServiceService.openOptionsDialog(event, this.rendererService.trackInfo().currentTrack, null);
    }
  }

  currentInputSourceVisible(): boolean {
    return this.currentInputSource() != "";
  }

  currentInputSource(): string {
    if (this.deviceService.selectedMediaRendererDevice().currentSource?.Name) {
      return this.deviceService.selectedMediaRendererDevice().currentSource.Name;
    }
    return "";
  }
}
