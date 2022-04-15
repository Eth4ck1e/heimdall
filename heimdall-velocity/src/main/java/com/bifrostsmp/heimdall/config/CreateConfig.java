package com.bifrostsmp.heimdall.config;

import com.velocitypowered.api.plugin.annotation.DataDirectory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateConfig {
  // START config.yml generation
  public static Yaml loadConfig(@DataDirectory final Path dataDirectory) {
    File file = new File(String.valueOf(dataDirectory), "config.yml");
    if (!file.getParentFile().exists()) { // Create directory if not exists
      file.getParentFile().mkdirs();
    }

    if (!file.exists()) { // check for config.yml
      try (InputStream input = CreateConfig.class.getResourceAsStream("/" + file.getName())) {
        if (input != null) {
          Files.copy(input, file.toPath());
        } else {
          file.createNewFile();
        }
      } catch (IOException exception) {
        exception.printStackTrace();
        return null;
      }
    }
    return new Yaml();
  }
  // END config.yml generation
}
