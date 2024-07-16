package ru.hogwarts.school.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(AvatarServiceIml.class);
    private final AvatarRepository avatarRepository;
    private final StudentServiceImpl studentServiceImpl;

    public AvatarServiceIml(AvatarRepository avatarRepository,
                            StudentServiceImpl studentServiceImpl) {
        this.avatarRepository = avatarRepository;
        this.studentServiceImpl = studentServiceImpl;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Вызов метода сохранения аватара студента");
        checkStudent(studentId);
        byte[] data = file.getBytes();
        Student student = studentServiceImpl.findStudent(studentId);
        Avatar avatar = findAvatarByWrite(studentId);
        Path filePath = Path.of(avatarPath, student.getName() + "." + getFileExtension(file));
        setAvatarInfo(filePath, file, student, avatar, data);
        writeFile(avatar, filePath, data);
        logger.info("Сохранение файла аватара студента в БД");
        avatarRepository.save(avatar);
    }

    @Override
    public Avatar findStudentAvatar(Long studentId) {
        logger.info("Вызов метода поиска аватара студента из БД");
        if (!checkAvatarStudentById(studentId)) {
            logger.info("Аватар студента по id {} был найден", studentId);
            return avatarRepository.findByStudentId(studentId).orElseThrow();
        } else {
            logger.warn("Аватара студента с id {} нет в БД", studentId);
            return null;
        }
    }

    @Override
    public void findAvatarFile(Long studentId, HttpServletResponse response) {
        logger.info("Вызов метода поиска аватара студента по id {} в файловой системе",studentId);
        checkStudent(studentId);
        Avatar avatar = findStudentAvatar(studentId);
        if (avatar == null) {
            logger.error("Файл не найден при поиске из файловой системы!");
            throw new FileNotFoundException("Файл не найден при поиске из файловой системы!");
        }
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()
        ) {
            setResponseStatus(avatar, response);
            is.transferTo(os);
            logger.info("Чтение файла аватара из файловой системы");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Avatar> findAvatarByPage(Integer pageNumber, Integer pageSize) {
        logger.info("Вызов метода постраничного поиска аватаров из БД");
        PageRequest pageRequest = PageRequest.of((pageNumber - 1), pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    private Avatar findAvatarByWrite(Long studentId) {
        logger.debug("Вызов метода поиска файла аватара студента из БД для записи");
        if (!checkAvatarStudentById(studentId)) {
            return avatarRepository.findByStudentId(studentId).orElseThrow();
        } else {
            return new Avatar();
        }
    }

    private String getFileExtension(MultipartFile file) {
        logger.debug("Вызов метода для установки нудного расширения файлу аватара студента");
        return StringUtils.getFilenameExtension(file.getOriginalFilename());
    }

    private boolean checkAvatarStudentById(Long id) {
        logger.debug("Вызов метода проверки существования аватара студента в БД по id студента {}", id);
        return avatarRepository.findByStudentId(id).isEmpty();
    }

    private void checkNullAvatar(Avatar avatar) {
        logger.debug("Вызов метода для проверки на существования аватара");
        if (avatar == null) {
            avatar = new Avatar();
        }
    }

    private void checkStudent(Long studentId) {
        if (studentServiceImpl.checkStudentById(studentId)) {
            logger.error("Студента с id {} нет в БД", studentId);
            throw new StudentNotFoundException("Такого студента нет!");
        }
    }

    private void setResponseStatus(Avatar avatar, HttpServletResponse response) {
        logger.info("Вызов метода установки response статуса");
        response.setStatus(200);
        response.setContentType(avatar.getMediaType());
        response.setContentLength((int) avatar.getFileSize());
    }

    private void setAvatarInfo(Path path, MultipartFile file, Student student, Avatar avatar, byte[] data) {
        logger.debug("Вызов метода для установления полей аватара студента {}", student.getName());
        avatar.setStudent(student);
        avatar.setFilePath(path.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(data);
        logger.debug("Поля для аватара студента {} установлены", student.getName());
    }

    private void writeAvatarToFile(Path path, byte[] data) {
        logger.debug("Вызов метода для записи файла аватара студента в файловую систему");
        try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(path.toFile()))) {
            fos.write(data);
            logger.debug("Файл аватара студента записан в файловую систему");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFile(Avatar avatar, Path filePath, byte[] data) throws IOException {
        logger.debug("Вызов метода подготовки для записи файла аватара в файловую систему");
        try {
            checkNullAvatar(avatar);
            Files.createDirectory(filePath.getParent());
            logger.debug("Вызов метода для проверки существования директории");
            writeAvatarToFile(filePath, data);
        } catch (IOException e) {
            Files.deleteIfExists(filePath);
            logger.debug("Вызов метода удаления предыдущего файла");
            writeAvatarToFile(filePath, data);
        }
    }
}