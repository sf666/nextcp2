import { PopupService } from './../../../util/popup.service';
import { MatDialogRef, MatDialogConfig, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ContentDirectoryService } from './../../../service/content-directory.service';
import { ContainerDto } from './../../../service/dto.d';
import { Component, ElementRef, Inject, OnInit } from '@angular/core';



@Component({
  selector: 'app-minim-tag',
  templateUrl: './minim-tag.component.html',
  styleUrls: ['./minim-tag.component.scss']
})

export class MinimTagComponent implements OnInit {

  private readonly _matDialogRef: MatDialogRef<MinimTagComponent>;
  private readonly triggerElementRef: ElementRef;

  constructor(
    private contentDirectoryService: ContentDirectoryService,
    private popupService: PopupService,
    _matDialogRef: MatDialogRef<MinimTagComponent>,
    @Inject(MAT_DIALOG_DATA) data: { trigger: ElementRef }) {
    this._matDialogRef = _matDialogRef;
    this.triggerElementRef = data.trigger;
  }

  ngOnInit(): void {
    this.popupService.configurePopupPosition(this._matDialogRef, this.triggerElementRef, 300, 400);
  }

  cancel(): void {
    this._matDialogRef.close(null);
  }

  // 
  // content specific stuff starts here
  // 
  public get minimTagsList(): ContainerDto[] {
    return this.contentDirectoryService.currentContainerList.minimServerSupportTags;
  }

  public browseTo(containerDto: ContainerDto) {
    this.contentDirectoryService.browseChildrenByContiner(containerDto);
    this.cancel();
  }
}
