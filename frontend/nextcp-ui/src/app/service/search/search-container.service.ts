import { ContainerItemDto, ContainerDto } from './../dto.d';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SearchContainerService {

  public containerItem: ContainerDto;

  constructor() { }
}
