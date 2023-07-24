package com.bifrostsmp.heimdall.html;
import com.velocitypowered.api.plugin.annotation.DataDirectory;

import javax.swing.text.html.HTML;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.bifrostsmp.heimdall.HeimdallVelocity.logger;

public class CreateHTML {
    public static Object loadHTML(@DataDirectory final Path dataDirectory, String sourceAndDestDir, String fileName) {
        logger.warn(String.valueOf(dataDirectory));
        File file = new File(dataDirectory + "/" + sourceAndDestDir, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try (InputStream input = CreateHTML.class.getResourceAsStream("/" + sourceAndDestDir + "/" + file.getName())) {
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
        return new Object();
    }
}
