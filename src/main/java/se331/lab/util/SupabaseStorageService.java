package se331.lab.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SupabaseStorageService {
    @Value("${supabase.storage.bucket}")
    String bucketName;
    @Value("${supabase.storage.endpoint}")
    String outputUrl;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddmmssSSS");
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    public String uploadFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        String saltFileName = LocalDateTime.now().format(formatter) + "-" + file.getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(saltFileName)
                .build();

        s3Client.putObject(putObjectRequest, tempFile);
        String key = String.format("%s", saltFileName);

        Files.delete(tempFile);
        return key;
    }
    public String getPresignedUrl(String keyName) {
        return getPresignedUrl(keyName, Duration.ofMinutes(60));
    }

    public String getPresignedUrl(String keyName, Duration expiration) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(expiration)
                .getObjectRequest(getObjectRequest)
                .build();
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
