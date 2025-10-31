package SDD.smash.Exception.Exception;

import SDD.smash.Exception.Code.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode code;

    public BusinessException(ErrorCode errorCode, String message)
    {
        super(message);
        this.code = errorCode;
    }
}
