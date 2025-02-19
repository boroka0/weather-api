package com.weatherApp.util;

import com.deepl.api.TextResult;
import com.deepl.api.Translator;

public class TranslatorUtil {
    private static final String API_KEY = ConfigUtil.get("deepl.api.key");

    private static final Translator translator;

    static {
        translator = new Translator(API_KEY);
    }

    public static String translateTextToEnglish(String text) {
        try {
            TextResult result = translator.translateText(text, null, "en-GB");
            return result.getText();
        } catch (Exception e) {
            return text;
        }
    }
}
