import { ContentDirectoryService } from './../../service/content-directory.service';
import { MediaRendererDto, MediaServerDto } from './../../service/dto.d';
import { Component } from '@angular/core';

@Component({
  selector: 'music-library',
  templateUrl: './music-library.component.html',
  styleUrls: ['./music-library.component.scss'],
  providers: [ContentDirectoryService]
})

export class MusicLibraryComponent {
  constructor( ContentDirectoryService: ContentDirectoryService) {}
  
}
