import { DtoGeneratorService } from 'src/app/util/dto-generator.service';
import { ContainerDto, ContainerItemDto } from './../dto.d';
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

  public currentContainerId() : string {
    return this.currentMediaLibraryFolder().currentContainer.id;    
  }

  public currentContainer() : ContainerDto {
    return this.currentMediaLibraryFolder().currentContainer;
  }
}
