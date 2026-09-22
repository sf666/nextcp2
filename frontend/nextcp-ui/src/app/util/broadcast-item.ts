import { ItemDto } from 'src/app/service/dto';

export function isBroadcastItem(item: ItemDto): boolean {
  return (
    item?.objectClass?.startsWith('object.item.audioItem.audioBroadcast') ===
      true || item?.audioFormat?.isStreaming === true
  );
}
