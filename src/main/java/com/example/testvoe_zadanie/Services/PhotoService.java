package com.example.testvoe_zadanie.Services;

import com.example.testvoe_zadanie.Repositories.EmployeeRepository;
import com.example.testvoe_zadanie.Repositories.PhotoRepository;
import com.example.testvoe_zadanie.models.Employee;
import com.example.testvoe_zadanie.models.Photo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final EmployeeRepository employeeRepository;
    private final String UPLOAD_DIR = "D:/photos/";

    public PhotoService(PhotoRepository photoRepository, EmployeeRepository employeeRepository) {
        this.photoRepository = photoRepository;
        this.employeeRepository = employeeRepository;
    }

    public Photo uploadPhoto(Long employeeId, MultipartFile file) throws IOException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        // Создаём директорию, если её нет
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Сохраняем файл на диск
        String fileName = employeeId + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // Удаляем старую фотографию, если существует
        photoRepository.findByEmployeeId(employeeId).ifPresent(photoRepository::delete);

        // Сохраняем метаинформацию о фото
        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setFilePath(filePath.toString());
        photo.setEmployee(employee);
        return photoRepository.save(photo);
    }

    public byte[] getPhoto(Long employeeId) throws IOException {
        Photo photo = photoRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        Path filePath = Paths.get(photo.getFilePath());
        return Files.readAllBytes(filePath);
    }

    public void deletePhoto(Long employeeId) {
        photoRepository.findByEmployeeId(employeeId).ifPresent(photo -> {
            try {
                Files.deleteIfExists(Paths.get(photo.getFilePath()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete photo", e);
            }
            photoRepository.delete(photo);
        });
    }
}
