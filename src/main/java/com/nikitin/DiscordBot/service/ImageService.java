//package com.nikitin.DiscordBot.service;
//
//import com.luciad.imageio.webp.WebPReadParam;
//import com.nikitin.DiscordBot.model.data.Image;
//import com.nikitin.DiscordBot.repository.ImageRepository;
//import com.nikitin.DiscordBot.utils.Constants;
//import com.pragone.jphash.image.radial.RadialHash;
//import com.pragone.jphash.image.radial.RadialHashAlgorithm;
//import com.pragone.jphash.jpHash;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.Message.Attachment;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import javax.imageio.ImageIO;
//import javax.imageio.ImageReader;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.UncheckedIOException;
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//@Slf4j
//public class ImageService {
//
//    private ImageRepository imageRepository;
//    private RestTemplate restTemplate;
//
//    public void saveImagesFromMessage(Message m) {
//
//        m.getAttachments()
//                .stream()
//                .filter(Attachment::isImage)
//                .forEach(a ->
//                        saveAttachment(a,
//                                m.getGuild().getIdLong(),
//                                m.getJumpUrl(),
//                                m.getAuthor().getIdLong()));
//
//    }
//
//    public Image saveAttachment(Attachment attachment, Long guildID, String getAsMention, Long authorID) {
//
//        if (attachment.getFileExtension().contains("webp")) {
//            log.warn("Can not save file: " + attachment.getProxyUrl());
//            return null;
//        }
//
//        Image image = findImageInDataBase(attachment, guildID);
//        if (image != null) {
//            return image;
//        }
//
//        String radialHash = getRadialHash(attachment);
//
//        image = new Image();
//
//        image.setGuildId(guildID);
//        image.setLinkAsMention(getAsMention);
//        image.setAuthorID(authorID);
//
//        image.setUrl(attachment.getUrl());
//        image.setProxyUrl(attachment.getProxyUrl());
//        image.setRadialHash(radialHash);
//
//        return imageRepository.save(image);
//    }
//
//    public Image findImageInDataBase(Attachment attachment, Long guildID) {
//        if (attachment == null || !attachment.isImage()) {
//            throw new IllegalArgumentException("Attachment is not an image");
//        }
//
//        Image savedImg = imageRepository.findByGuildIdAndUrl(guildID, attachment.getUrl()).orElse(null);
//
//        if (savedImg != null) {
//            return savedImg;
//        }
//
//        savedImg = imageRepository.findByGuildIdAndProxyUrl(guildID, attachment.getProxyUrl()).orElse(null);
//        if (savedImg != null) {
//            return savedImg;
//        }
//
//        String radialHash = getRadialHash(attachment);
//
//        savedImg = imageRepository.findByGuildIdAndRadialHash(guildID, radialHash).orElse(null);
//        return savedImg;
//    }
//
//    @NotNull
//    private String getRadialHash(Attachment attachment) {
//        String radialHash = getRadialHashFromImageUrl(attachment.getUrl());
//        if (radialHash == null) {
//            radialHash = getRadialHashFromImageUrl(attachment.getProxyUrl());
//        }
//        if (radialHash == null) {
//            throw new IllegalStateException("Can not get radial hash");
//        }
//        return radialHash;
//    }
//
//    private String getRadialHashFromImageUrl(String url) {
//        try {
//            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
//            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
//
//            if (url.trim().endsWith(".webp")) {
//                bufferedImage = readWebp(imageBytes);
//
//            }
//            bufferedImage = fillWhiteOnTransparent(bufferedImage);
//            bufferedImage = transformToType3ByteBGR(bufferedImage);
//
//
//            RadialHash radialHash = jpHash.getImageRadialHash(bufferedImage);
//            return radialHash.toString();
//        } catch (Exception e) {
//            log.error("Error during getting radial hash: " + url, e);
//            return null;
//        }
//
//    }
//
//    private BufferedImage readWebp(byte[] imageBytes) {
//
//        try {
//            ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
//
//            WebPReadParam readParam = new WebPReadParam();
//            readParam.setBypassFiltering(true);
//
//            reader.setInput(new ByteArrayInputStream(imageBytes));
//
//            return reader.read(0, readParam);
//        } catch (IOException e) {
//            throw new UncheckedIOException(e);
//        }
//
//    }
//
//    private BufferedImage transformToType3ByteBGR(BufferedImage bufferedImage) {
//        if (bufferedImage.getType() == BufferedImage.TYPE_3BYTE_BGR) {
//            return bufferedImage;
//        }
//
//        BufferedImage image = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
//        image.getGraphics().drawImage(bufferedImage, 0, 0, null);
//        return image;
//    }
//
//    private BufferedImage fillWhiteOnTransparent(BufferedImage bufferedImage) {
//        if (bufferedImage != null && bufferedImage.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
//            for (int y = 0; y < bufferedImage.getHeight(); ++y) {
//                for (int x = 0; x < bufferedImage.getWidth(); ++x) {
//                    int argb = bufferedImage.getRGB(x, y);
//                    if ((argb & 0x00FFFFFF) == 0x00FFFFFF) { //if the pixel is transparent
//                        bufferedImage.setRGB(x, y, 0xFFFFFFFF); // white color.
//                    }
//                }
//            }
//        }
//        return bufferedImage;
//    }
//
//    public List<String> findSimilarities(Attachment a, Long guildID, String jumpUrl) {
//        List<String> results = new ArrayList<>();
//        String imageHash = getRadialHash(a);
//
//        List<Image> guildImages = imageRepository.findByGuildId(guildID);
//
//        for (Image image : guildImages) {
//            double similarity = RadialHashAlgorithm.getSimilarity(
//                    RadialHash.fromString(image.getRadialHash()),
//                    RadialHash.fromString(imageHash));
//
//            if (similarity >= Constants.MIN_SIMILARITY_LEVEL && !image.getLinkAsMention().equals(jumpUrl)) {
//                String message = MessageFormat.format("Картинка {0} соответствие {1}%",
//                        image.getLinkAsMention(),
//                        similarity * 100d);
//                results.add(message);
//            }
//        }
//        return results;
//    }
//
//}
