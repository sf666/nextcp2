package nextcp.indexer.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import nextcp.dto.ToastrMessage;
import nextcp.indexer.IndexerConfig;
import nextcp.indexer.IndexerException;
import nextcp.rating.domain.SongIndexed;
import nextcp.rating.repository.IndexerSessionFactory;
import nextcp.rating.repository.SongPersistenceService;

@Service
public class FilesystemIndexerService
{

    private static final Logger log = LoggerFactory.getLogger(FilesystemIndexerService.class.getName());

    private List<Path> availablePlaylists = new ArrayList<>();
    private List<String> playlistsNames = new ArrayList<>();
    private final int UUID_CHAR_LEN = 36;

    @Autowired
    private ApplicationEventPublisher publisher = null;
    
    @Autowired
    private SongPersistenceService songPersistenceService = null;

    @Autowired
    public FilesystemIndexerService(IndexerConfig config, IndexerSessionFactory sessionFactory)
    {
        if (StringUtils.isAllBlank(config.playlistDirectory))
        {
            log.info("'playlistPath' not configured. Filesystem playlist support is unavailable.");
            return;
        }

        try
        {
            Path dir = Paths.get(config.playlistDirectory);
            if (dir.toFile().isDirectory())
            {
                Files.newDirectoryStream(dir, path -> isValidPlaylist(path.toString())).forEach(p -> addPlaylist(p));
            }
            else
            {
                throw new IndexerException(IndexerException.PLAYLIST_DIRECTORY_NOT_EXISTS, "configured directory is not existent or not a directory.");
            }
        }
        catch (Exception e)
        {
            log.warn("Error while scanning Playlist files from disc location : " + config.playlistDirectory != null ? config.playlistDirectory : "NULL", e);
        }
    }

    public List<String> getAvailablePlaylistNames()
    {
        return playlistsNames;
    }

    private void addPlaylist(Path p)
    {
        availablePlaylists.add(p);
        String name = p.getName(p.getNameCount() - 1).toString();
        name = name.substring(0, name.indexOf('.'));
        playlistsNames.add(name.toLowerCase());
    }

    private Path getPlaylistPathFromName(String playlistName)
    {
        String baseName = FilenameUtils.getBaseName(playlistName);
        if (playlistsNames.indexOf(baseName.toLowerCase()) > -1)
        {
            return availablePlaylists.get(playlistsNames.indexOf(baseName.toLowerCase()));
        }
        throw new IndexerException(IndexerException.PLAYLIST_NOT_FOUND, "Playlist not managed : " + playlistName);
    }

    public List<String> addSongToPlaylist(String musicBrainzId, String playlistName)
    {
        Path playlistPath = getPlaylistPathFromName(playlistName);
        SongIndexed songIndex = getBestSongFromSongList(getIndexedSongFromMBID(musicBrainzId));

        if (songIndex == null)
        {
            throw new IndexerException(IndexerException.SONG_NOT_FOUND, "song was not found for MBID : " + musicBrainzId);
        }
        if (playlistName == null)
        {
            throw new IndexerException(IndexerException.PLAYLIST_NOT_FOUND, "playlist was not found. name : " + playlistName);
        }

        String entry = calculateRelativeSongPath(Paths.get(songIndex.getFilePath()), playlistPath);
        List<String> playlistEntries = readCurrentPlaylist(playlistPath);
        if (isSongAlreadyInPlaylist(songIndex.getFilePath(), entry, playlistEntries, playlistPath))
        {
            throw new IndexerException(IndexerException.PLAYLIST_FILE_EXISTS, "Song already exists in playlist");
        }
        else
        {
            playlistEntries.add(entry);
            writePlaylistToDisk(playlistEntries, playlistPath);
        }
        return playlistEntries;
    }

    /**
     * Checks if path is already in playlist or a song with the same musicBraint trackid.
     * 
     * @param entry
     * @param playlistEntries
     * @return
     */
    private boolean isSongAlreadyInPlaylist(String absolutePath, String entry, List<String> playlistEntries, Path playlistPath)
    {
        if (playlistEntries.contains(entry))
        {
            return true;
        }
        Set<String> mbIdSet = new HashSet<String>();

        String entryId = songPersistenceService.selectMusicBrainzIDFromPath(absolutePath);
        if (entryId == null)
        {
            throw new IndexerException(IndexerException.PLAYLIST_NOT_EXISTS, "songs MusicBrainzID dissapeared : " + absolutePath);
        }
        String base = FilenameUtils.getFullPath(playlistPath.toFile().getAbsolutePath());
        for (String aPath : playlistEntries)
        {
            String absoluteFile = FilenameUtils.concat(base, aPath);
            mbIdSet.add(songPersistenceService.selectMusicBrainzIDFromPath(absoluteFile));
        }
        if (mbIdSet.contains(entryId))
        {
            return true;
        }

        return false;
    }

    public SongIndexed getBestSong(String musicBrainzId)
    {
        return getBestSongFromSongList(getIndexedSongFromMBID(musicBrainzId));
    }

    private SongIndexed getBestSongFromSongList(List<SongIndexed> songs)
    {
        if (songs.size() == 0)
        {
            return null;
        }
        if (songs.size() == 1)
        {
            return songs.get(0);
        }
        // TODO analyse filetype and bitrate ...
        return songs.get(0);
    }

    public void checkValidUUID(String musicBrainzID)
    {
        if ("00000000-0000-0000-0000-000000000000".equals(musicBrainzID) || musicBrainzID == null || musicBrainzID.length() != UUID_CHAR_LEN)
        {
            throw new IndexerException(IndexerException.INVALID_UUID, String.format("Invalid UUID : %s", musicBrainzID));
        }
    }

    /**
     * 
     * @param musicBrainzId
     * @return
     */
    private List<SongIndexed> getIndexedSongFromMBID(String musicBrainzId)
    {
        checkValidUUID(musicBrainzId);
        List<SongIndexed> songIndexList = songPersistenceService.getSongByMusicBrainzId(musicBrainzId);
        return songIndexList;
    }

    public List<String> removeSongFromPlaylist(String musicBrainzId, String playlistName)
    {
        int numRemoved = 0;
        Path playlistPath = getPlaylistPathFromName(playlistName);
        List<SongIndexed> songIndexList = getIndexedSongFromMBID(musicBrainzId);
        List<String> playlistEntries = readCurrentPlaylist(playlistPath);

        for (SongIndexed songIndex : songIndexList)
        {
            String entry = calculateRelativeSongPath(Paths.get(songIndex.getFilePath()), playlistPath);
            if (playlistEntries.remove(entry))
            {
                numRemoved++;
            }
            else
            {
                log.debug("Song is not in playlist : " + entry != null ? entry : "NULL");
            }
        }
        if (numRemoved == 0)
        {
            throw new IndexerException(IndexerException.PLAYLIST_FILE_NOT_IN_PLAYLIST, "Cannot remove song. Song is not in playlist : ");
        }
        else
        {
            writePlaylistToDisk(playlistEntries, playlistPath);
        }

        return playlistEntries;
    }

    private List<String> readCurrentPlaylist(Path playlistFile)
    {
        if (!Files.exists(playlistFile))
        {
            throw new IndexerException(IndexerException.PLAYLIST_NOT_EXISTS, "Playlist does not exists: " + playlistFile.toString());
        }

        List<String> lines = new ArrayList<String>();
        try
        {
            lines = Files.readAllLines(playlistFile, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            log.error("readCurrentPlaylist", e);
        }
        return lines;
    }

    /**
     * 
     * @param event
     * @param absolutePlaylistFile
     * @return
     */
    private String calculateRelativeSongPath(Path absoluteSongPath, Path absolutePlaylistFile)
    {
        StringBuilder sb = new StringBuilder();

        if (absoluteSongPath.toString().startsWith(absolutePlaylistFile.getParent().toString()))
        {
            String relativePath = absoluteSongPath.toString().substring(absolutePlaylistFile.getParent().toString().length());
            sb.append(".");
            if (!relativePath.startsWith(File.separator))
            {
                sb.append(File.separator);
            }
            sb.append(relativePath);
        }
        else
        {
            Path commonRoot = absolutePlaylistFile.getParent();
            do
            {
                sb.append("..");
                sb.append(File.separator);
                commonRoot = commonRoot.getParent();
            }
            while (!absoluteSongPath.toString().startsWith(commonRoot.toString()));

            String relativePath = absoluteSongPath.toString().substring(commonRoot.toString().length() + 1);
            sb.append(relativePath);
            return sb.toString();
        }
        return sb.toString();
    }

    private void writePlaylistToDisk(List<String> lines, Path playlistFile)
    {
        try
        {
            Files.write(playlistFile, lines);
        }
        catch (IOException e)
        {
            log.warn("cannot write internal media renderer playlist file.", e);
        }
    }

    public boolean isValidPlaylist(String filename)
    {
        if (filename.endsWith(".m3u"))
        {
            return true;
        }
        if (filename.endsWith(".m3u8"))
        {
            return true;
        }
        if (filename.endsWith(".pls"))
        {
            return true;
        }
        return false;
    }
}
