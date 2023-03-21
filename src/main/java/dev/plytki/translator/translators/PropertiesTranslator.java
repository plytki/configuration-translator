package dev.plytki.translator.translators;

import dev.plytki.translator.model.DeepLLanguage;
import dev.plytki.translator.DeepLTranslatorAPI;
import dev.plytki.translator.model.Translator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

public class PropertiesTranslator extends Translator {

    public PropertiesTranslator(DeepLTranslatorAPI deepLTranslatorAPI, String inputPath, String outputPath) {
        super(deepLTranslatorAPI, inputPath, outputPath);
    }

    @Override
    public void translate(DeepLLanguage targetLanguage) {
        try {
            Properties langProperties = new Properties();
            langProperties.load(new FileInputStream(super.inputFile()));
            Properties translatedProperties = translateProperties(langProperties, targetLanguage);
            savePropertiesToFile(translatedProperties, super.translatedFile());
            if (!super.skipped().isEmpty()) {
                System.out.print("Skipped, matched preferences! [" + super.skipped().size() + "]");
            }
        } catch (IOException e) {
            System.err.println("Error reading or writing files: " + e.getMessage());
        }
    }

    private Properties translateProperties(Properties properties, DeepLLanguage deepLLanguage) {
        Properties translatedProperties = new Properties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            String translatedValue = translateString(value, deepLLanguage);
            translatedProperties.setProperty(key, translatedValue);
        }
        return translatedProperties;
    }

    private void savePropertiesToFile(Properties properties, String filePath) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(Paths.get(filePath).toFile())) {
            properties.store(fileOutputStream, "Translated Properties File");
        }
    }
}
