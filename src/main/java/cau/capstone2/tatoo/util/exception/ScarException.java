package cau.capstone2.tatoo.util.exception;

import cau.capstone2.tatoo.util.api.ResponseCode;

public class ScarException extends BaseException{
    public ScarException(ResponseCode responseCode) {
        super(responseCode);
    }
}
