package me.alphar.core.ex;

/**
 * 系统业务异常
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

}
