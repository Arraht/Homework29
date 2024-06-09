package ru.hogwarts.school.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@Transactional
public class AvatarService {
    private String avatarPath = new File("").getAbsolutePath();;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    public AvatarService(AvatarRepository avatarRepository,
                         StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) {
        Student student = studentService.findStudent(studentId);
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path filePath = Path.of(avatarPath, studentId + "." + fileExtension);
        Avatar avatar = findStudentAvatarByWrite(studentId);
        try {
            byte[] data = file.getBytes();
            writeAvatarToFile(filePath, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatarRepository.save(avatar);
    }

    public Avatar findStudentAvatar(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    public void findAvatarFile(Long studentId, HttpServletResponse response) {
        Avatar avatar = findStudentAvatar(studentId);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()
        ) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Avatar findStudentAvatarByWrite(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private void writeAvatarToFile(Path path, byte[] data) {
        try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(path.toFile()))) {
            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            fos.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Avatar getAvatarForStudent(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }
}