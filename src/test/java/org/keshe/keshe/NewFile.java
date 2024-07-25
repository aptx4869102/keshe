package org.keshe.keshe;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewFile {

    @Test
    public void test() throws IOException {
        String str = "ipva.summary_day.20210621.20210622000205860.B20C41AB-0A17-46DB-9177-817533B8E551.1624291336921.txt";

        String pattern = "summary_day\\.\\d+[0-9]\\.\\d{17}";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        if (m.find()) {
            System.out.println(m.group());
        }

    }
}