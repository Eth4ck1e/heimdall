package com.bifrostsmp.heimdall.discord.applications;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

public class Parser {

  private static InputStream inputStream;

  public static Map<String, Object> parse(Path appPath) {
    // yaml parser for BifrostSMPApplication.yml
    try {
      inputStream = new FileInputStream(new File(String.valueOf(appPath)));
    } catch (FileNotFoundException | SecurityException e) {
      e.printStackTrace();
    }

    Yaml yaml = new Yaml();

    return yaml.load(inputStream);
  }
}
