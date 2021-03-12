package com.alr.products.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@Slf4j
public class FileHelper {

  private static String PATH_TEST = "src/main/resources/files";

  public String saveFile(final MultipartFile sourceFile) {
    File dest = new File(String.format("%s/%s", PATH_TEST, sourceFile.getOriginalFilename()));

    try (var fos = new FileOutputStream(dest)) {
      byte[] buffer = new byte[1024];
      int length;

      while ((length = sourceFile.getInputStream().read(buffer)) > 0) {
        fos.write(buffer, 0, length);
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }

    return dest.getAbsolutePath();
  }
}

