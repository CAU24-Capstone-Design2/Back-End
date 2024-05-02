package cau.capstone2.tatoo.s3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestTattooDto {
    private MultipartFile scarImage;
    private String styleKeyWord;
    private String styleDescription;

}
