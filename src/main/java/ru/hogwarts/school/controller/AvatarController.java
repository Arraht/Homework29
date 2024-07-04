package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.interfaces.AvatarService;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "{id}/student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id,
                                               @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/repository")
    public ResponseEntity<byte[]> downloadAvatarByRepository(@PathVariable Long id, HttpServletResponse response) {
        Avatar avatar = avatarService.findStudentAvatar(id);
        if (avatar != null) {
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(avatar.getMediaType()))
                    .contentLength(avatar.getFileSize())
                    .body(avatar.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) {
        avatarService.findAvatarFile(id, response);
    }

    @GetMapping("/page")
    public ResponseEntity<List<Avatar>> getAvatarByPage(@RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size) {
        List<Avatar> avatars = avatarService.findAvatarByPage(page, size);
        return ResponseEntity.ok(avatars);
    }
}