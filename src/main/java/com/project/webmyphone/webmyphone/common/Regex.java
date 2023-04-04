package com.project.webmyphone.webmyphone.common;

import java.util.regex.Pattern;

public class Regex {
    String regex_Email = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@(gmail.com)$";
    String regex_Sodienthoai = "^[0][1-9][0-9]{8}$";

    public boolean regexEmail(String string) {
        Pattern pattern = Pattern.compile(regex_Email);
        return pattern.matcher(string).matches();
    }

    public boolean regexSodienthoai(String string) {
        Pattern pattern = Pattern.compile(regex_Sodienthoai);
        return pattern.matcher(string).matches();
    }
}
