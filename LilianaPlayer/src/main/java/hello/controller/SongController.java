package hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import hello.common.ResponseStatus;
import hello.entity.Song;
import hello.exception.RestException;
import hello.model.Category;
import hello.service.SongService;

/**
 * @author tu.ta1 on 2019-04-20
 */
@RestController
@RequestMapping("/api/song")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping(value = "/all")
    public List<Song> getAllSongs(
            @RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") String sortOrder)
            throws IOException {
        return songService.getAllSongs(sortBy, sortOrder);
    }

    @GetMapping(value = "/all/by/folder")
    public List<Category> getAllSongsByFolders() throws IOException {
        return songService.getAllSongsByFolders();
    }

    /**
     * Return theo kiểu byte[] thì sẽ ko tua được file audio, do đó phải dùng
     * FileSystemResource
     *
     * API này return về audio, thỉnh thoảng chạy nó có dấu hiệu bị xước! Hiện tại
     * chưa khắc phục được
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = { "audio/mpeg" })
    public FileSystemResource getSong(@RequestParam("file") String file) throws IOException {
        return songService.getSong(file);
    }

    @GetMapping(value = "/import")
    public String importSongData() throws IOException {
        songService.importSongData();
        return "Import done!";
    }

    @GetMapping(value = "/album", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public byte[] getAlbum(@RequestParam("file") String file) throws IOException {
        byte[] arr = songService.getAlbum(file);
        if (arr.length > 0)
            return arr;
        else
            throw new RestException(ResponseStatus.ALBUM_NOT_FOUND);
    }

    @PutMapping(value = "/listens")
    public String updateListens(@RequestParam("file") String file) throws IOException {
        Song s = songService.updateListens(file);
        return "Updated listens: " + s.getTitle() + " (" + s.getArtist() + "): " + s.getListens();
    }
}