package hello.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import hello.common.ResponseStatus;

/**
 * @author tu.ta1 on 2019-03-04
 */
@Getter
@Setter
@AllArgsConstructor
public class RestException extends RuntimeException {

    private static final long serialVersionUID = -8756242290875950860L;

    private int code;
    private String message;
    
    public RestException(ResponseStatus stt) {
        this.code = stt.getCode();
        this.message = stt.getMessage();
    }
}