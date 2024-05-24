package cau.capstone2.tatoo.scar.service;

import cau.capstone2.tatoo.s3.dto.RequestTattooDto;
import cau.capstone2.tatoo.s3.dto.ResponseTattooDesignDto;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public void requestTattoo(RequestTattooDto requestTattooDto, Long userId) throws IOException {

        //Scar 정보 저장
        Scar scar = Scar.createScar(requestTattooDto.getStyleDescription(), requestTattooDto.getStyleKeyWord(), findUserById(userId));
        scarRepository.save(scar);
        System.out.println("\n\nKeyWord : " + requestTattooDto.getStyleKeyWord());
        System.out.println("\n\nDescription : " + requestTattooDto.getStyleDescription());
        System.out.println("\n\nScar : " + scar.getId());

        //scarImage를 aws s3 스토리지에 이미지 저장
        try {
            String scarImageUri = uploadImage(requestTattooDto.getScarImage(), userId, "scar");
            scar.setScarImage(scarImageUri);
            System.out.println("\n\n***이미지 s3에 업로드 성공\n");
        } catch (IOException e) {
            System.out.println("\n\n***이미지 s3에 업로드 중 오류 발생\n\n");
            log.error("\n***이미지 업로드 중 오류 발생\n");
        }

        //프론트한테 받은 multipartfile_scar를 서버 디렉토리에 저장 후 디렉토리 반환
        String filePath = saveImage(requestTattooDto.getScarImage(), userId, scar.getId());
        System.out.println("\n\n***서버에 이미지 저장 완료");
        System.out.println("\n***이미지 저장한 파일 경로 : " + filePath);
        log.info("\n***파일 경로 : " + filePath);
        scar.setScarUri(filePath); //scarImage 디렉토리를 디비에 세팅
        System.out.println("\n\n***디비에 이미지 경로 저장 완료");

        scarRepository.save(scar);

        String newDirectoryPath = "/home/cvmlserver11/junhee/scart";
        // Java(Spring)에서 현재 작업 디렉토리 변경
        System.setProperty("user.dir", newDirectoryPath);

        // 현재 작업 디렉토리 확인
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("\n\n현재 작업 디렉토리 변경 완료. 현재 작업 디렉토리: " + currentDirectory);
        log.info("\n\n현재 작업 디렉토리 변경 완료. 현재 작업 디렉토리: " + currentDirectory);

        String UPLOAD_DIRECTORY = "/home/cvmlserver11/junhee/scart/images/user"+userId+"/masks";

        // 디렉토리 생성
        File directory = new File(UPLOAD_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리 생성
            System.out.println("masks 디렉토리가 없습니다. -> 디렉토리 생성: " + UPLOAD_DIRECTORY);
        } else {
            System.out.println("masks 디렉토리 이미 존재합니다. 기존 경로에 저장 : " + UPLOAD_DIRECTORY);
        }

        String UPLOAD_DIRECTORY2 = "/home/cvmlserver11/junhee/scart/images/user"+userId+"/tattoos";

        // 디렉토리 생성
        File directory2 = new File(UPLOAD_DIRECTORY2);
        if (!directory2.exists()) {
            directory2.mkdirs(); // 디렉토리 생성
            System.out.println("tattoos 디렉토리가 없습니다. -> 디렉토리 생성: " + UPLOAD_DIRECTORY2);
        } else {
            System.out.println("tattoos 디렉토리 이미 존재합니다. 기존 경로에 저장 : " + UPLOAD_DIRECTORY2);
        }

        String[] command = {
                "/bin/bash",
                "start.sh",
                "user" + userId,
                "scar" + scar.getId() + ".png",
                requestTattooDto.getStyleDescription(),
                requestTattooDto.getStyleKeyWord()
        };

        // 프로세스 빌더 생성
        ProcessBuilder pb = new ProcessBuilder(command);
        System.out.println("\n\n프로세스 빌더 생성 완료");

        //String activeCommands = "./start.sh user" + userId + " scar" + scar.getId() + ".png " + "\"" + requestTattooDto.getStyleDescription() + "\" " + "\"" + requestTattooDto.getStyleKeyWord() + "\"";
        //System.out.println("\n\n실행한 명령어 : " + activeCommands);

        try {
            //Process process = Runtime.getRuntime().exec(activeCommands);
            Process process = pb.start();

            // 프로세스의 출력 스트림 처리
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("프로세스 출력: " + line);
            }

            // 프로세스의 오류 스트림 처리
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.out.println("프로세스 오류: " + line);
            }

            int exitCode = process.waitFor(); // 프로세스가 종료되기를 기다림
            if (exitCode == 0) {
                log.info("\n\nAI 명령어 command 실행 완료");
            } else {
                log.error("\n\nAI 명령어 command 실행 중 오류 발생. 종료 코드: " + exitCode);
            }
        } catch (Exception e) {
            log.error("\n\nAI 명령어 command 실행 중 오류 발생: " + e.getMessage());
        }

        //AI output 이미지 경로를 DB에 저장
        scar.setDesignUri("/home/cvmlserver11/junhee/scart/images/user"+userId+"/tattoos/scar"+scar.getId()+".png");
        scar.setScarSegUri("/home/cvmlserver11/junhee/scart/images/user"+userId+"/masks/scar"+scar.getId()+".png");

        //segmentation 결과를 s3 스토리지에 업로드
        try {
            String scarSegImage = uploadImageFromFile("/home/cvmlserver11/junhee/scart/images/user"+userId+"/masks/scar"+scar.getId()+".png", userId, "masks");
            scar.setScarSegImage(scarSegImage); //s3 경로를 서버 스토리지에 저장
        } catch (IOException e) {
            log.error("\n\n이미지 s3에 업로드 중 오류 발생");
        }

        //타투 도안을 s3 스토리지에 업로드
        try {
            String tattooImage = uploadImageFromFile("/home/cvmlserver11/junhee/scart/images/user"+userId+"/tattoos/scar"+scar.getId()+".png", userId, "tattoos");
            scar.setTattooImage(tattooImage); //s3 경로를 서버 스토리지에 저장
        } catch (IOException e) {
            log.error("\n\n이미지 s3에 업로드 중 오류 발생");
        }

        scarRepository.save(scar);
    }

    @Transactional(readOnly = true)
    public ResponseTattooDto responseTattoo(Long scarId){
        Scar scar = findScarById(scarId);
        return ResponseTattooDto.of(scar.getScarImage(), scar.getScarSegImage(), scar.getTattooImage());
    }

    @Transactional(readOnly = true)
    public List<ResponseTattooDesignDto> getUserTattoo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ScarException(ResponseCode.USER_NOT_FOUND));
        List<Scar> scars = scarRepository.findAllByUserId(user.getId());
        if(scars.isEmpty()) { //타투가 없는 경우
            System.out.println("\n\n***유저의 타투가 없습니다. 빈 리스트를 출력합니다.\n\n");
            return List.of();
        }
        return scars.stream()
                .map(scar -> ResponseTattooDesignDto.of(scar.getTattooImage(), scar.getId()))
                .toList();
    }

    //서버 디렉토리에 이미지 저장
    private String saveImage(MultipartFile file, Long userId, Long scarId) throws IOException {

        String UPLOAD_DIRECTORY = "/home/cvmlserver11/junhee/scart/images/user"+userId+"/inputs";

        // 디렉토리 생성
        File directory = new File(UPLOAD_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리 생성
            System.out.println("디렉토리가 없습니다. -> 디렉토리 생성: " + UPLOAD_DIRECTORY);
        } else {
            System.out.println("디렉토리 이미 존재합니다. 기존 경로에 저장 : " + UPLOAD_DIRECTORY);
        }

        // 파일을 저장할 경로를 설정합니다.
        Path uploadPath = Paths.get(UPLOAD_DIRECTORY + "/scar" + scarId + ".png");
        System.out.println("\n\n***이미지를 저장한 파일 경로 : " + uploadPath);

        try {
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadPath.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 저장된 파일의 경로를 반환합니다.
        return uploadPath.toString();
    }

    public String uploadImage(MultipartFile file, Long userId, String category) throws IOException {
        String fileName = "user" + userId + "_" + category + "_" + file.getName();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        String s3Url = amazonS3.getUrl(bucketName, fileName).toString();

        System.out.println("\n\n***이미지 s3에 업로드 성공");
        System.out.println("\n\n***이미지 s3에 업로드한 파일 이름 : " + s3Url);

        return s3Url;
    }

    public String uploadImageFromFile(String localFilePath, Long userId, String category) throws IOException {
        File file = new File(localFilePath);
        String fileName = "user" + userId + "_" + category + "_" + file.getName();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());

        // Upload the file to Amazon S3
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file).withMetadata(metadata));
        String s3Url = amazonS3.getUrl(bucketName, fileName).toString();
        System.out.println("\n\n***이미지 s3에 업로드 성공");
        System.out.println("\n\n***이미지 s3에 업로드한 경로 : " + s3Url);

        // Get the URL of the uploaded file
        return s3Url;
    }

    private Scar findScarById(Long scarId) {
        return scarRepository.findById(scarId)
                .orElseThrow(() -> new ScarException(ResponseCode.SCAR_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ScarException(ResponseCode.USER_NOT_FOUND));
    }

}
