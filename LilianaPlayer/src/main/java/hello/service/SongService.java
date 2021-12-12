package hello.service;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hello.common.ResponseStatus;
import hello.entity.Song;
import hello.exception.RestException;
import hello.model.Category;
import hello.repository.SongRepository;

/**
 * @author tu.ta1 on 2019-09-09
 */
@Service
public class SongService {

    @Autowired
    private Environment env;

    @Autowired
    private SongRepository songRepository;

    private String[] INVALID_CHARACTERS = { "\\", "/", "*", "?", "\"", "<", ">", "|" };

    public List<Song> getAllSongs(String sortBy, String sortOrder) {
        Direction direction = null;
        if ("ASC".equals(sortOrder)) {
            direction = Direction.ASC;
        } else {
            direction = Direction.DESC;
        }

        Sort sort;
        String DEFAULT_FILED_SORT = "title";
        if (!DEFAULT_FILED_SORT.equals(sortBy)) {
            sort = Sort.by(direction, sortBy).and(Sort.by(Direction.ASC, DEFAULT_FILED_SORT));
        } else {
            sort = Sort.by(direction, sortBy);
        }
        PageRequest pageRequest = PageRequest.of(0, 99999, sort);
        Page<Song> songPage = songRepository.findAll(pageRequest);

        return songPage.getContent();
    }

    public List<Category> getAllSongsByFolders() throws IOException {
        List<Category> res = new ArrayList<>();
        String folderString = env.getProperty("mp3_folder");
        String name;
        Category category;
        List<String> mp3List;

        File[] listOfFolders = new File(folderString).listFiles();

        for (int i = 0; i < listOfFolders.length; i++) {
            if (listOfFolders[i].isDirectory()) {
                mp3List = new ArrayList<>();
                File[] listOfFiles = new File(listOfFolders[i].getAbsolutePath()).listFiles();
                for (int j = 0; j < listOfFiles.length; j++) {
                    if (listOfFiles[j].isFile()) {
                        name = listOfFiles[j].getName();
                        if (name.toLowerCase().endsWith(".mp3")) {
                            mp3List.add(listOfFiles[j].getName());
                        }
                    }
                }

                if (mp3List.size() > 0) {
                    category = new Category();
                    category.setName(listOfFolders[i].getName());
                    category.setFileList(mp3List);
                    res.add(category);
                }
            }
        }

        return res;
    }

    public FileSystemResource getSong(String file) throws IOException {
        String folderString = env.getProperty("mp3_folder");
        File[] listOfFolders = new File(folderString).listFiles();
        for (int i = 0; i < listOfFolders.length; i++) {
            if (listOfFolders[i].isDirectory()) {
                File serverFile = new File(listOfFolders[i].getAbsolutePath() + File.separator + file);
                if (serverFile.exists())
//return Files.readAllBytes(serverFile.toPath());
                    return new FileSystemResource(serverFile);
            }
        }

// có thể trong database vẫn còn lưu bài này nhưng ở ổ cứng ko còn,
// nên sẽ remove bài này trong database
        Song doomedSong = songRepository.findByFileName(file);
        if (doomedSong != null)
            songRepository.delete(doomedSong);
        throw new RestException(ResponseStatus.MP3_NOT_FOUND);
    }

    public Song getSongInfoByTitleAndArtist(String title, String artist) {
        return songRepository.findByTitleAndArtist(title, artist);
    }

    /**
     * Reference: https://stackoverflow.com/a/21746415/7688028
     */
    private Song getSongInfoByFilePath(String file) throws IOException {
        String folderString = env.getProperty("mp3_folder");
        File[] listOfFolders = new File(folderString).listFiles();
        for (int i = 0; i < listOfFolders.length; i++) {
            if (listOfFolders[i].isDirectory()) {
                File serverFile = new File(listOfFolders[i].getAbsolutePath() + File.separator + file);
                if (serverFile.exists()) {
                    return getSongInfoByFile(serverFile);
                }
            }
        }

        return null;
    }

    private Song getSongInfoByFile(File file) {
        String title, artist, album;
        try {
            InputStream input = new FileInputStream(file);
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

            title = metadata.get("title");
            artist = metadata.get("xmpDM:artist");
            album = metadata.get("xmpDM:album");

            if (title != null)
                title = title.trim();
            if (artist != null)
                artist = artist.trim();
            if (album != null)
                album = album.trim();

            Song song = new Song();
            song.setTitle(title);
            song.setArtist(artist);
            song.setAlbum(album);

            return song;
        } catch (FileNotFoundException e) {
            throw new RestException(ResponseStatus.MP3_NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void importSongData() throws IOException {
        System.out.println("========> ^^ Start importing data for songs...");

        String folderString = env.getProperty("mp3_folder");
        String name;

        File[] listOfFolders = new File(folderString).listFiles();

        for (int i = 0; i < listOfFolders.length; i++) {
            if (listOfFolders[i].isDirectory()) {
                File[] listOfFiles = new File(listOfFolders[i].getAbsolutePath()).listFiles();
                for (int j = 0; j < listOfFiles.length; j++) {
                    if (listOfFiles[j].isFile()) {
                        name = listOfFiles[j].getName();
                        if (name.toLowerCase().endsWith(".mp3")) {
                            // mp3List.add(listOfFiles[j].getName());
                            Song song = getSongInfoByFilePath(name);
                            song.setType(listOfFolders[i].getName());
                            song.setFileName(name);
                            upsertSong(song);
                        }
                    }
                }
            }
        }

        System.out.println("========> ^^ End of importing data for songs");
    }

    /**
     * Check xem song đã tồn tại trong database, nếu có, update những field sau:
     * album, type, fileName. Nếu chưa có: tạo mới 1 song trong database
     * 
     * @param songReq
     */
    private void upsertSong(Song songReq) {
        Song songInDb = null;
        if (songReq.getTitle() != null && !songReq.getTitle().trim().equals("") && songReq.getArtist() != null
                && !songReq.getArtist().trim().equals("")) {
            songInDb = songRepository.findByTitleAndArtist(songReq.getTitle(), songReq.getArtist());
        } else {
            songInDb = songRepository.findByFileName(songReq.getFileName());
        }
        if (songInDb == null) {
            songInDb = new Song();
            BeanUtils.copyProperties(songReq, songInDb);
            songInDb.setListens(0);
            songInDb.setCreatedDate(new Date());
        }
        songInDb.setAlbum(songReq.getAlbum());
        songInDb.setType(songReq.getType());
        songInDb.setFileName(songReq.getFileName());

        // get Album name
        String imageName = songReq.getArtist() + " - " + songReq.getTitle() + ".jpg";
        if (!checkValidFileName(imageName) || getAlbum(imageName).length == 0) {
            imageName = songReq.getFileName().replace(".mp3", ".jpg");
            if (getAlbum(imageName).length == 0) {
                imageName = null;
            }
        }
        if (imageName != null) {
            songInDb.setImageName(imageName);
            songInDb.setImageUrl("/api/song/album?file=" + imageName);
        }

        songRepository.save(songInDb);
    }

    private boolean checkValidFileName(String file) {
        for (int i = 0; i < INVALID_CHARACTERS.length; i++) {
            if (file.contains(INVALID_CHARACTERS[i]))
                return false;
        }
        return true;
    }

    public Song updateListens(String file) {
        Song song = songRepository.findByFileName(file);
        if (song == null) {
            throw new RestException(ResponseStatus.MP3_NOT_FOUND);
        }
        song.setListens(song.getListens() + 1);
        songRepository.save(song);
        return song;
    }

    public byte[] getAlbum(String file) {
        String albumFolder = env.getProperty("album_folder");
        File serverFile = new File(albumFolder + File.separator + file);

        try {
            return Files.readAllBytes(serverFile.toPath());
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return new byte[] {};
    }

}