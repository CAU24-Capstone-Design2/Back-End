package cau.capstone2.tatoo.scar.service;

import cau.capstone2.tatoo.s3.dto.RequestTattooDto;
import cau.capstone2.tatoo.s3.dto.ResponseTattooDto;
import cau.capstone2.tatoo.scar.domain.Scar;
import cau.capstone2.tatoo.scar.repository.ScarRepository;
import cau.capstone2.tatoo.user.domain.User;
import cau.capstone2.tatoo.user.repository.UserRepository;
import cau.capstone2.tatoo.util.api.ResponseCode;
import cau.capstone2.tatoo.util.exception.ScarException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ScarService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3;
    private final ScarRepository scarRepository;
    private final UserRepository userRepository;

    @Transactional
    public void requestTattoo(RequestTattooDto requestTattooDto, Long userId) {

        //Scar 정보 저장
        Scar scar = Scar.createScar(requestTattooDto.getStyleDescription(), requestTattooDto.getStyleKeyWord());

        //scarImage를 aws s3 스토리지에 이미지 저장
        try {
            String scarImageUri = uploadImage(requestTattooDto.getScarImage());
            scar.setScarImage(scarImageUri);
        } catch (IOException e) {
            log.error("이미지 업로드 중 오류 발생");
        }

        String fileName = saveImage(requestTattooDto.getScarImage());
        String filePath = "준희 저장 경로" + fileName;
        scar.setScarUri(filePath); //scarImage 디렉토리를 저장

        scarRepository.save(scar);

        String newDirectoryPath = "/home/cvmlserver11/junhee/scart/images/경로 정확히 설정";
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

        //타투 도안을 s3 스토리지에 업로드
        try {
            String tattooImage = uploadImageFromFile("서버 스토리지 상의 타투 도안 이미지 경로");
            scar.setTattooImage(tattooImage); //s3 경로를 서버 스토리지에 저장
        } catch (IOException e) {
            log.error("이미지 업로드 중 오류 발생");
        }

        //segmentation 결과를 s3 스토리지에 업로드
        try {
            String scarSegImage = uploadImageFromFile("서버 스토리지 상의 segment 이미지 경로");
            scar.setScarSegImage(scarSegImage); //s3 경로를 서버 스토리지에 저장
        } catch (IOException e) {
            log.error("이미지 업로드 중 오류 발생");
        }
    }

    public ResponseTattooDto responseTattoo(Long scarId){
        Scar scar = findScarById(scarId);
        return ResponseTattooDto.of(scar.getScarImage(), scar.getScarSegImage(), scar.getTattooImage());
    }

    public List<ResponseTattooDto> getUserTattoo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ScarException(ResponseCode.USER_NOT_FOUND));
        List<Scar> scars = scarRepository.findAllByUserId(user.getId());
        List<ResponseTattooDto> userTattoos = new ArrayList<>();
        for(Scar scar : scars) {
            userTattoos.add(ResponseTattooDto.of(scar.getScarImage(), scar.getScarSegImage(), scar.getTattooImage()));
        }
        if(scars.isEmpty()) { //타투가 없는 경우
            return List.of();
        }
        return userTattoos;
    }

    //서버 디렉토리에 이미지 저장
    private String saveImage(MultipartFile imageFile) {
        String fileName = "scar" + imageFile.getOriginalFilename();
        try {
            log.info("\n\n파일 저장\n\n");
            log.info("fileName : " + fileName + "\n\n");
            imageFile.transferTo(new java.io.File("업로드 디렉토리 설정" + fileName));
        } catch (Exception e) {
            throw new ScarException(ResponseCode.FILE_UPLOAD_FAILED);
        }
        return fileName;
    }

    private String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString(); // Generate a unique filename
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public String uploadImageFromFile(String localFilePath) throws IOException {
        File file = new File(localFilePath);
        String fileName = file.getName();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());

        // Upload the file to Amazon S3
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file).withMetadata(metadata));

        // Get the URL of the uploaded file
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    private Scar findScarById(Long scarId) {
        return scarRepository.findById(scarId)
                .orElseThrow(() -> new ScarException(ResponseCode.SCAR_NOT_FOUND));
    }

}
