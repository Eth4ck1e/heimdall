package com.bifrostsmp.heimdall.common;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

public class YamlParser {

  private static InputStream inputStream;

  public static Map<String, Object> parse(Path path) {
    // yaml parser for BifrostSMPApplication.yml
    try {
      inputStream = new FileInputStream(new File(String.valueOf(path)));
    } catch (FileNotFoundException | SecurityException e) {
      e.printStackTrace();
    }

    Yaml yaml = new Yaml();

    return yaml.load(inputStream);
  }
}
