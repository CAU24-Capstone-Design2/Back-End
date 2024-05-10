package cau.capstone2.tatoo.s3.dto;

import lombok.Getter;

@Getter
public class ResponseTattooDesignDto {
    private String tattooImage;
    private Long scarId;

    public static ResponseTattooDesignDto of(String tattooImage, Long scarId) {
        ResponseTattooDesignDto responseTattooDesignDto = new ResponseTattooDesignDto();
        responseTattooDesignDto.tattooImage = tattooImage;
        responseTattooDesignDto.scarId = scarId;
        return responseTattooDesignDto;
    }
}
