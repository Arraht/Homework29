package ru.hogwarts.school.service.interfaces;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.util.List;

public interface AvatarService {
    void uploadAvatar(Long studentId, MultipartFile file) throws IOException;

    Avatar findStudentAvatar(Long studentId);

    void findAvatarFile(Long studentId, HttpServletResponse response);

    List<Avatar> findAvatarByPage(Integer pageNumber, Integer pageSize);
}