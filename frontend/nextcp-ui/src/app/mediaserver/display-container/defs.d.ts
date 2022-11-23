import { ContentDirectoryService } from './../../service/content-directory.service';
import { PersistenceService } from './../../service/persistence/persistence.service';
import { CdsBrowsePathService } from './../../util/cds-browse-path.service';

export interface ScrollLoadHandler {
    cdsBrowsePathService : CdsBrowsePathService,
    persistenceService : PersistenceService,
    contentDirectoryService: ContentDirectoryService
}
