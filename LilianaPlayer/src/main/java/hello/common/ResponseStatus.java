package hello.common;

import lombok.Getter;

@Getter
public enum ResponseStatus {
    
    UNAUTHORIZED(401001, "Oops! Unauthorized!!!"),

    MP3_NOT_FOUND(404001, "Mp3 not found!"),
    LYRIC_NOT_FOUND(404002, "Lyric not found!"),
    ALBUM_NOT_FOUND(404003, "Album not found!");
    
    private int code;
    private String message;

    private ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

}