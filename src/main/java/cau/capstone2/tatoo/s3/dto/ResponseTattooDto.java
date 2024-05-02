package cau.capstone2.tatoo.s3.dto;

import lombok.Getter;

@Getter
public class ResponseTattooDto {
    private String scarImage;
    private String segmentImage;
    private String tattooImage;

    public static ResponseTattooDto of(String scarImage, String segmentImage, String tattooImage) {
        ResponseTattooDto responseTattooDto = new ResponseTattooDto();
        responseTattooDto.scarImage = scarImage;
        responseTattooDto.segmentImage = segmentImage;
        responseTattooDto.tattooImage = tattooImage;
        return responseTattooDto;
    }
}
