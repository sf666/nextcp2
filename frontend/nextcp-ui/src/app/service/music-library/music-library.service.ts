import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { ContainerItemDto } from './../dto.d';
import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MusicLibraryService {

  constructor(public dtoGeneratorService: DtoGeneratorService) { }

  public currentMediaLibraryFolder = signal<ContainerItemDto>(this.dtoGeneratorService.generateEmptyContainerItemDto());

  public updateCurrentContainer(container: ContainerItemDto) {
    this.currentMediaLibraryFolder.set(container);
  }
}
