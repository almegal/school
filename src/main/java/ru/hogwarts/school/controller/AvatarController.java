package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.implementation.AvatarService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("avatars")
public class AvatarController {
    private AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    // ---------------- GET REQUESts ----------- //
    @GetMapping("/{id}")
    @Operation(summary = "Получить оригинал аватара по id")
    public void getAvatar(
            @PathVariable(value = "id") long id,
            HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.getById(id);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping("/{id}/preview")
    @Operation(summary = "Получить превью автара по id")
    public ResponseEntity<byte[]> getPreview(@PathVariable(value = "id") long id) {
        Avatar avatar = avatarService.getById(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        httpHeaders.setContentLength(avatar.getPreview().length);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(avatar.getPreview());
    }

    @GetMapping("/paginate/")
    @Operation(summary = "Пагинация аватарок по page size")
    public ResponseEntity<List<Avatar>> getPaginateAvatar(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        List<Avatar> result = avatarService.getAvatarPaginate(page, size);
        return ResponseEntity.ok(result);
    }

    // ---------------- POST REQUESts ----------- //
    @PostMapping(value = "/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить аватар")
    public ResponseEntity<String> postAvatar(
            @PathVariable(value = "studentId") long studentId,
            @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }
}
