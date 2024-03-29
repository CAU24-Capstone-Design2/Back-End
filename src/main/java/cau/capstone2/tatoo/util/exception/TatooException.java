package cau.capstone2.tatoo.util.exception;

import cau.capstone2.tatoo.util.api.ResponseCode;

public class TatooException extends BaseException{

    public TatooException(ResponseCode responseCode) {
        super(responseCode);
    }
}
