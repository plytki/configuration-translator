package dev.plytki.translator;

import dev.plytki.translator.model.DeepLLanguage;
import dev.plytki.translator.translators.HoconTranslator;

public class Main {

    public static void main(String[] args) {
        // Put your https://www.deepl.com/ API key for translations.
        DeepLTranslatorAPI deepLTranslatorAPI = new DeepLTranslatorAPI("your api key");
        // Set input file (file to translate) and output file (translated file)
        HoconTranslator hoconTranslator = new HoconTranslator(deepLTranslatorAPI, "to-translate.conf", "translated.conf");
        // Add your own handler to prevent lines from being translated
        hoconTranslator.skip(s -> s.contains("@players"));
        // Set your desired language, translate and save translated file to the output file
        hoconTranslator.translate(DeepLLanguage.PL);
    }

}