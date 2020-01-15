package hello.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.common.ResponseStatus;
import hello.exception.RestException;

/**
 * @author tu.ta1 on 2019-04-20
 */
@RestController
@RequestMapping("/api/lyric")
public class LyricController {

    @Autowired
    private Environment env;

    @GetMapping(value = "", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getLyricByFileName(@RequestParam("file") List<String> file) {
        String folderString = env.getProperty("lyric_folder");

        for (int i = 0; i < file.size(); i++) {
            StringBuilder sb = new StringBuilder();
            try (FileReader reader = new FileReader(folderString + File.separator + file.get(i));
                    BufferedReader br = new BufferedReader(reader)) {

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new RestException(ResponseStatus.LYRIC_NOT_FOUND);
    }

    @GetMapping(value = "/update/offset")
    public String updateOffset(@RequestParam("file") List<String> file, @RequestParam("offset") Integer offset) {
        String folderString = env.getProperty("lyric_folder");
        for (int i = 0; i < file.size(); i++) {
            File fileToBeModified = new File(folderString + File.separator + file.get(i));
            StringBuilder builder = new StringBuilder();
            String content;
            BufferedReader reader = null;
            FileWriter writer = null;

            try {
                reader = new BufferedReader(new FileReader(fileToBeModified));

                // Reading all the lines of input text file into oldContent
                String line = reader.readLine();
                while (line != null) {
                    if (line.contains("[offset:")) {
                        line = "[offset:" + offset + "]";
                    }
                    builder.append(line).append(System.lineSeparator());
                    line = reader.readLine();
                }

                content = builder.toString();
                if (!content.contains("[offset:")) {
                    content = "[offset:" + offset + "]" + System.lineSeparator() + content;
                }

                // Rewriting the input text file with newContent
                writer = new FileWriter(fileToBeModified);
                writer.write(content);
                return "Offset updated!";
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null)
                        reader.close();
                    if (writer != null)
                        writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new RestException(ResponseStatus.LYRIC_NOT_FOUND);
    }
}