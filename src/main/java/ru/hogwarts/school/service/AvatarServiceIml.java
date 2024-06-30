package ru.hogwarts.school.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceprion.FileNotFoundException;
import ru.hogwarts.school.exceprion.StudentNotFoundException;
import ru.hogwarts.school.service.interfaces.AvatarService;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@Transactional
public class AvatarServiceIml implements AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarPath;
    private final AvatarRepository avatarRepository;
    private final StudentServiceImpl studentServiceImpl;

    public AvatarServiceIml(AvatarRepository avatarRepository,
                            StudentServiceImpl studentServiceImpl) {
        this.avatarRepository = avatarRepository;
        this.studentServiceImpl = studentServiceImpl;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        checkStudent(studentId);
        byte[] data = file.getBytes();
        Student student = studentServiceImpl.findStudent(studentId);
        Avatar avatar = findAvatarByWrite(studentId);
        Path filePath = Path.of(avatarPath, student.getName() + "." + getFileExtension(file));
        setAvatarInfo(filePath, file, student, avatar, data);
        writeFile(avatar, filePath, data);
        avatarRepository.save(avatar);
    }

    @Override
    public Avatar findStudentAvatar(Long studentId) {
        if (!checkAvatarStudentById(studentId)) {
            return avatarRepository.findByStudentId(studentId).orElseThrow();
        } else {
            return null;
        }
    }

    @Override
    public void findAvatarFile(Long studentId, HttpServletResponse response) {
        checkStudent(studentId);
        Avatar avatar = findStudentAvatar(studentId);
        if (avatar == null) {
            FileNotFoundException f = new FileNotFoundException("Файл не найден при поиске из файловой системы!");
            f.printStackTrace();
            throw f;
        }
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()
        ) {
            setResponseStatus(avatar, response);
            is.transferTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Avatar> findAvatarByPage(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of((pageNumber - 1), pageSize);
        return avatarRepository.getAvatar(pageRequest);
    }

    private Avatar findAvatarByWrite(Long studentId) {
        if (!checkAvatarStudentById(studentId)) {
            return avatarRepository.findByStudentId(studentId).orElseThrow();
        } else {
            return new Avatar();
        }
    }

    private String getFileExtension(MultipartFile file) {
        return StringUtils.getFilenameExtension(file.getOriginalFilename());
    }

    private boolean checkAvatarStudentById(Long id) {
        return avatarRepository.findByStudentId(id).isEmpty();
    }

    private void checkNullAvatar(Avatar avatar) {
        if (avatar == null) {
            avatar = new Avatar();
        }
    }

    private void checkStudent(Long studentId) {
        if (studentServiceImpl.checkStudentById(studentId)) {
            StudentNotFoundException s = new StudentNotFoundException("Такого студента нет!");
            s.printStackTrace();
            throw s;
        }
    }

    private void setResponseStatus(Avatar avatar, HttpServletResponse response) {
        response.setStatus(200);
        response.setContentType(avatar.getMediaType());
        response.setContentLength((int) avatar.getFileSize());
    }

    private void setAvatarInfo(Path path, MultipartFile file, Student student, Avatar avatar, byte[] data) {
        avatar.setStudent(student);
        avatar.setFilePath(path.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(data);
    }

    private void writeAvatarToFile(Path path, byte[] data) {
        try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(path.toFile()))) {
            fos.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFile(Avatar avatar, Path filePath, byte[] data) throws IOException {
        try {
            checkNullAvatar(avatar);
            Files.createDirectory(filePath.getParent());
            writeAvatarToFile(filePath, data);
        } catch (IOException e) {
            Files.deleteIfExists(filePath);
            writeAvatarToFile(filePath, data);
        }
    }
}