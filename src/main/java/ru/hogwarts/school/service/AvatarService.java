package ru.hogwarts.school.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceprion.FileNotFoundException;
import ru.hogwarts.school.exceprion.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarPath;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    public AvatarService(AvatarRepository avatarRepository,
                         StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        byte[] data = file.getBytes();
        Student student = studentService.findStudent(studentId);
        Avatar avatar = findStudentAvatar(studentId);
        Path filePath = Path.of(avatarPath, student.getName() + "." + getFileExtension(file));
        try {
            if (avatar == null) {
                avatar = new Avatar();
            }
            Files.createDirectory(filePath.getParent());
            writeAvatarToFile(filePath, data);
        } catch (IOException e) {
            Files.deleteIfExists(filePath);
            writeAvatarToFile(filePath, data);
        }
        setAvatarInfo(filePath, file, student, avatar, data);
        avatarRepository.save(avatar);
    }

    public Avatar findStudentAvatar(Long studentId) {
        if (!checkAvatarStudentById(studentId)) {
            return avatarRepository.findByStudentId(studentId).orElseThrow();
        } else {
            return null;
        }
    }

    public void findAvatarFile(Long studentId, HttpServletResponse response) {
        checkStudent(studentId);
        Avatar avatar = findStudentAvatar(studentId);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()
        ) {
            setResponseStatus(avatar, response);
            is.transferTo(os);
        } catch (IOException e) {
            FileNotFoundException f = new FileNotFoundException("Файл не найден при поиске из файловой системы!");
            f.printStackTrace();
            throw f;
        }
    }

    private void setResponseStatus(Avatar avatar, HttpServletResponse response) {
        response.setStatus(200);
        response.setContentType(avatar.getMediaType());
        response.setContentLength((int) avatar.getFileSize());
    }

    private boolean checkAvatarStudentById(Long id) {
        return avatarRepository.findByStudentId(id).isEmpty();
    }

    private void checkStudent(Long studentId) {
        if (studentService.checkStudentById(studentId)) {
            StudentNotFoundException s = new StudentNotFoundException("Такого студента нет!");
            s.printStackTrace();
            throw s;
        }
    }

    private void setAvatarInfo(Path path, MultipartFile file, Student student, Avatar avatar, byte[] data) {
        avatar.setStudent(student);
        avatar.setFilePath(path.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(data);
    }

    private String getFileExtension(MultipartFile file) {
        return StringUtils.getFilenameExtension(file.getOriginalFilename());
    }

    private void writeAvatarToFile(Path path, byte[] data) {
        try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(path.toFile()))) {
            fos.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}