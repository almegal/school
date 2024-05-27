package ru.hogwarts.school.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
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
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentSchoolServiceImpl studentSchoolService;
    Logger logger = LoggerFactory.getLogger(Avatar.class);
    // получаем путь до аватаров из свойтсв приложения
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarService(AvatarRepository avatarRepository, StudentSchoolServiceImpl studentSchoolService) {
        this.avatarRepository = avatarRepository;
        this.studentSchoolService = studentSchoolService;
    }

    public List<Avatar> getAvatarPaginate(Integer page, Integer size) {
        logger.info("Получить список аватарок в виде пагинации, метод getAvatarPaginate");
        PageRequest pageable = PageRequest.of(page - 1, size);
        return avatarRepository.findAll(pageable).getContent();
    }

    public Avatar getById(long id) {
        logger.info("Получить аватарку по ID {}, метод getById", id);
        return avatarRepository.findById(id)
                .orElseGet(() -> {
                    logger.error("Нет аватарки по ID {}", id);
                    return new Avatar();
                });
    }

    public void uploadAvatar(long id, MultipartFile avatarContent) throws IOException {
        logger.info("Загрузить аватар. ID: {}, fileName: {}, метод uploadAvatar", id, avatarContent.getOriginalFilename());
        if (avatarContent.getOriginalFilename() == null) {
            logger.error("Ошибка загрузки автарки. Имя файла отсутсвует. fileName: {}", avatarContent.getOriginalFilename());
            throw new RuntimeException("");
        }
        // получить студента по айди
        Student student = studentSchoolService.get(id);
        // создаем путь где будут храниться автары
        Path filePath = Path.of(avatarsDir, student.getId() + "." + getExtension(avatarContent.getOriginalFilename()));
        // создаем директорию
        Files.createDirectories(filePath.getParent());
        // если такой файл есть - удалить
        Files.deleteIfExists(filePath);
        // создаем стримы на запись и чтение
        try (InputStream is = avatarContent.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            // пераем поток чтения в поток на запись
            bis.transferTo(bos);
        }
        // присваиваем своствам аватара значения и сохраняем в бд
        Avatar avatar = getById(id);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarContent.getSize());
        avatar.setMediaType(avatarContent.getContentType());
        avatar.setPreview(resizeAvatar(filePath));
        avatarRepository.save(avatar);
    }

    private String getExtension(String fileName) {
        logger.debug("Получить расширение файла, метод getExtension");
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private byte[] resizeAvatar(Path path) throws IOException {
        logger.debug("Урезать размер аватара, метод resizeAvatar");
        //создаем стримы на чтение и запись
        try (InputStream is = Files.newInputStream(path);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // получаем буффер изображения
            BufferedImage image = ImageIO.read(bis);
            int height = image.getHeight() / (image.getWidth() / 100);
            // создаем новый буффер изображения
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            //  создаем поверхность для рисования
            Graphics2D graphics = preview.createGraphics();
            // рисуем новое превью
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(path.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }
}
