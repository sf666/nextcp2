import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  inject,
  signal,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { firstValueFrom } from 'rxjs';
import { CdsUpdateService } from 'src/app/service/cds-update.service';

export interface UpdateAlbumArtDialogData {
  objectId: string;
  previousAlbumArtUri: string;
  containerId?: string;
}

const IMAGE_TYPES = ['image/jpeg', 'image/png'];
// Base64 adds a third to the file size before the image is sent in the JSON request.
const MAX_FILE_SIZE = 10 * 1024 * 1024;

@Component({
  selector: 'app-update-album-art',
  imports: [
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
  ],
  templateUrl: './update-album-art.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UpdateAlbumArtComponent {
  private readonly dialogRef = inject(
    MatDialogRef<UpdateAlbumArtComponent, boolean>,
  );
  private readonly data = inject<UpdateAlbumArtDialogData>(MAT_DIALOG_DATA);
  private readonly cdsUpdateService = inject(CdsUpdateService);
  private readonly destroyRef = inject(DestroyRef);

  readonly url = signal('');
  readonly file = signal<File | null>(null);
  readonly fileError = signal('');
  readonly saving = signal(false);
  readonly acceptedTypes = IMAGE_TYPES.join(',');
  readonly canSave = computed(
    () => !this.saving() && (!!this.url().trim() || !!this.file()),
  );

  setUrl(value: string): void {
    this.url.set(value);
    this.file.set(null);
    this.fileError.set('');
  }

  selectFile(input: HTMLInputElement): void {
    const file = input.files?.[0];
    if (!file) {
      return;
    }
    input.value = '';
    this.file.set(null);
    this.fileError.set('');
    if (!IMAGE_TYPES.includes(file.type) || !file.size) {
      this.fileError.set('Choose a JPEG or PNG image.');
    } else if (file.size > MAX_FILE_SIZE) {
      this.fileError.set('Choose an image no larger than 10 MiB.');
    } else {
      this.url.set('');
      this.file.set(file);
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }

  async save(): Promise<void> {
    if (!this.canSave()) {
      return;
    }
    this.saving.set(true);
    this.dialogRef.disableClose = true;
    this.fileError.set('');
    try {
      const file = this.file();
      const albumArtUri = file ? await this.readImage(file) : this.url().trim();
      if (!albumArtUri || this.destroyRef.destroyed) {
        return;
      }
      await firstValueFrom(
        this.cdsUpdateService.setNewAlbumArtUri(
          {
            objectID: this.data.objectId,
            acoustID: '',
            musicBrainzIdTrackId: '',
          },
          this.data.previousAlbumArtUri,
          albumArtUri,
          this.data.containerId,
        ),
      );
      this.dialogRef.close(true);
    } catch {
      // HttpService displays the server error. Keep the selection available for retry.
    } finally {
      this.saving.set(false);
      this.dialogRef.disableClose = false;
    }
  }

  private async readImage(file: File): Promise<string | null> {
    try {
      return await new Promise<string>((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result as string);
        reader.onerror = () => reject(reader.error);
        reader.readAsDataURL(file);
      });
    } catch {
      this.fileError.set('The image file could not be read.');
      return null;
    }
  }
}
