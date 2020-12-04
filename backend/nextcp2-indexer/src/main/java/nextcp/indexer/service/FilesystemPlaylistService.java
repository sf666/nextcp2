package nextcp.indexer.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import nextcp.indexer.IndexerConfig;
import nextcp.indexer.IndexerException;
import nextcp.rating.domain.SongIndexed;
import nextcp.rating.repository.IndexerSessionFactory;
import nextcp.rating.repository.SongPersistenceService;

@Service
public class FilesystemPlaylistService
{
    private static final Logger log = LoggerFactory.getLogger(FilesystemPlaylistService.class.getName());

    private List<Path> availablePlaylists = new ArrayList<>();
    private List<String> playlistsNames = new ArrayList<>();

    @Autowired
    private SongPersistenceService songPersistenceService = null;

    @Autowired
    public FilesystemPlaylistService(IndexerConfig config, IndexerSessionFactory sessionFactory)
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
                Files.newDirectoryStream(dir , path -> isValidPlaylist(path.toString())).forEach(p -> addPlaylist(p));
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
        name = name.substring(0,name.indexOf('.'));
        playlistsNames.add(name);
    }

    private Path getPlaylistPathFromName(String playlistName)
    {
        return availablePlaylists.get(playlistsNames.indexOf(playlistName));
    }

    public List<String> addSongToPlaylist(String musicBrainzId, String playlistName)
    {
        Path playlistPath = getPlaylistPathFromName(playlistName);
        SongIndexed songIndex = songPersistenceService.getSongByMusicBrainzId(musicBrainzId);

        if (playlistPath == null || songIndex == null)
        {
            throw new IndexerException(IndexerException.PLAYLIST_PARAM_NOT_INITIALIZED, "cannnot add song to playlist. Playlist or song unavailable ... ");
        }

        String entry = calculateRelativeSongPath(Paths.get(songIndex.getFilePath()), playlistPath);
        List<String> playlistEntries = readCurrentPlaylist(playlistPath);
        if (playlistEntries.contains(entry))
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

    public List<String> removeSongFromPlaylist(String relativeSongPath, String playlistName)
    {
        Path playlistPath = getPlaylistPathFromName(playlistName);

        List<String> playlistEntries = readCurrentPlaylist(playlistPath);
        if (!playlistEntries.remove(relativeSongPath))
        {
            log.warn("Song not in playlist : " + relativeSongPath != null ? relativeSongPath : "NULL");
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
