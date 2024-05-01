package cau.capstone2.tatoo.scar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestScarDto {

    private String styleKeyWord;
    private String styleDescription;
    public static RequestScarDto of(String styleKeyWord, String styleDescription) {
        return new RequestScarDto(styleKeyWord, styleDescription);
    }
}
