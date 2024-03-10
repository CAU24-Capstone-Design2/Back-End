package cau.capstone2.tatoo.util.exception;

import cau.capstone2.tatoo.util.api.ResponseCode;

public class UserException extends BaseException {

    public UserException(ResponseCode responseCode) {
        super(responseCode);
    }
}
