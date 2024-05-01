package cau.capstone2.tatoo.scar.service;

import cau.capstone2.tatoo.scar.domain.Scar;
import cau.capstone2.tatoo.scar.dto.RequestScarDto;
import cau.capstone2.tatoo.scar.repository.ScarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class ScarService {

    private ScarRepository scarRepository;

    @Transactional
    public void requestTatoo(RequestScarDto requestScarDto, Long userId, MultipartFile scarImage) {

        //Scar 정보 저장
        Scar scar = Scar.createScar(requestScarDto.getStyleDescription(), requestScarDto.getStyleKeyWord());

        //aws s3로 이미지 저장 코드 추가 -> scarImage

        scar.setScarUri("경로 추가");
        scarRepository.save(scar);

        String newDirectoryPath = "/home/cvmlserver11/junhee/scart/images";
        // Java(Spring)에서 현재 작업 디렉토리 변경
        System.setProperty("user.dir", newDirectoryPath);

        // 현재 작업 디렉토리 확인
        String currentDirectory = System.getProperty("user.dir");
        log.info("\n\n현재 작업 디렉토리 변경 완료. 현재 작업 디렉토리: " + currentDirectory);

        String activeCommands = "command 실행 명령어";
        try {
            Process process = Runtime.getRuntime().exec(activeCommands);
            log.info("\n\ncommand 실행 완료");
        } catch (Exception e) {
            log.error("\n\ncommand 실행 중 오류 발생");
        }

        scar.setScarSegUri("경로 추가");
        scarRepository.save(scar);
    }

}
