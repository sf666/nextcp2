import { DeviceService } from './device.service';
import { HttpService } from './http.service';
import { ContainerItemDto, BrowseRequestDto, MediaServerDto, ContainerDto } from './dto.d';
import { Injectable } from '@angular/core';
import Stack from "ts-data.stack";

@Injectable({
  providedIn: 'root'
})

export class ContentDirectoryService {

  baseUri = '/ContentDirectoryService';
  public currentContainerList: ContainerItemDto;

  constructor(
    private httpService: HttpService,
    private deviceService: DeviceService) {

    // Initialize empty result object
    this.currentContainerList = {
      currentContainer: this.generateEmptyContanerDto(),
      containerDto: [],
      musicItemDto: [],
      albumDto: [],
    }

    deviceService.mediaServerChanged$.subscribe(data => this.mediaServerChanged(data));
  }

  mediaServerChanged(data: MediaServerDto): void {
    // Update to root folder of media server
    this.browseChildrenByRequest(this.createBrowseRequest("0", "", data.udn));
  }

  public browseChildren(objectID: string, sortCriteria: string, mediaServerUdn: string): void {
    this.browseChildrenByRequest(this.createBrowseRequest(objectID, sortCriteria, mediaServerUdn));
  }

  public browseChildrenByRequest(browseRequestDto: BrowseRequestDto): void {
    const uri = '/browseChildren';
    this.httpService.post<ContainerItemDto>(this.baseUri, uri, browseRequestDto).subscribe(data => this.updateContainer(data));
  }

  updateContainer(data: ContainerItemDto): void {
    this.currentContainerList = data;
  }

  private createBrowseRequest(objectID: string, sortCriteria: string, mediaServerUdn: string): BrowseRequestDto {

    let br: BrowseRequestDto = {
      mediaServerUDN: mediaServerUdn,
      objectID: objectID,
      sortCriteria: sortCriteria
    }
    return br;
  }

  public generateEmptyContanerDto(): ContainerDto {
    return {
      albumartUri: '',
      artist: '',
      childCount: 0,
      createClass: '',
      creator: '',
      id: '',
      mediaServerUDN: '',
      objectClass: '',
      parentID: '',
      rating: 0,
      searchClass: '',
      searchable: false,
      title: ''
    };
  }
}
