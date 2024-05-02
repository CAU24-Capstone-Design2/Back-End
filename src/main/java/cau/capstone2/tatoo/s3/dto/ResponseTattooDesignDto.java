package cau.capstone2.tatoo.s3.dto;

import lombok.Getter;

@Getter
public class ResponseTattooDesignDto {
    private String tattooImage;

    public static ResponseTattooDesignDto of(String tattooImage) {
        ResponseTattooDesignDto responseTattooDesignDto = new ResponseTattooDesignDto();
        responseTattooDesignDto.tattooImage = tattooImage;
        return responseTattooDesignDto;
    }
}
