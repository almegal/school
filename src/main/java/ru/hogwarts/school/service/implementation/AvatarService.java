package ru.hogwarts.school.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private AvatarRepository avatarRepository;
    private StudentSchoolServiceImpl studentSchoolService;

    public AvatarService(AvatarRepository avatarRepository, StudentSchoolServiceImpl studentSchoolService) {
        this.avatarRepository = avatarRepository;
        this.studentSchoolService = studentSchoolService;
    }

    public Avatar getById(long id) {
        return avatarRepository.findById(id).orElse(new Avatar());
    }

    public void uploadAvatar(long id, MultipartFile avatarContent) throws IOException {
        Student student = studentSchoolService.get(id);
        Path filePath = Path.of(avatarsDir, student.getId() + "." + getExtension(avatarContent.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = avatarContent.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = getById(id);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarContent.getSize());
        avatar.setMediaType(avatarContent.getContentType());
        avatar.setPreview(resizeAvatar(filePath));
        avatarRepository.save(avatar);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private byte[] resizeAvatar(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            BufferedImage image = ImageIO.read(bis);
            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(path.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }
}
