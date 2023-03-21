package dev.plytki.translator.translators;

import com.typesafe.config.*;
import com.typesafe.config.parser.ConfigDocument;
import com.typesafe.config.parser.ConfigDocumentFactory;
import dev.plytki.translator.model.DeepLLanguage;
import dev.plytki.translator.DeepLTranslatorAPI;
import dev.plytki.translator.model.ITranslator;
import dev.plytki.translator.model.Translator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class HoconTranslator extends Translator implements ITranslator {

    public HoconTranslator(DeepLTranslatorAPI deepLTranslatorAPI, String inputPath, String outputPath) {
        super(deepLTranslatorAPI, inputPath, outputPath);
    }

    @Override
    public void translate(DeepLLanguage targetLanguage) {
        try {
            String hoconContent = new String(Files.readAllBytes(Paths.get(super.inputFile())));
            ConfigDocument configDocument = ConfigDocumentFactory.parseString(hoconContent);
            Config config = ConfigFactory.parseString(hoconContent);
            String translatedConfigStr = translateConfig(config, configDocument, targetLanguage);
            saveConfigToFile(translatedConfigStr, super.translatedFile());
            if (!super.skipped().isEmpty()) {
                System.out.print("Skipped, matched preferences! [" + super.skipped().size() + "]");
            }
        } catch (IOException e) {
            System.err.println("Error reading or writing files: " + e.getMessage());
        }
    }

    private String translateConfig(Config config, ConfigDocument configDocument, DeepLLanguage deepLLanguage) {
        for (Map.Entry<String, ConfigValue> entry : config.entrySet()) {
            ConfigValue value = entry.getValue();
            if (value.valueType() == ConfigValueType.STRING) {
                String translatedString = translateString(value.unwrapped().toString(), deepLLanguage).trim();
                configDocument = configDocument.withValue(entry.getKey(), ConfigValueFactory.fromAnyRef(translatedString));
            }
        }
        return configDocument.render();
    }

    private void saveConfigToFile(String configStr, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(configStr);
        }
    }
}
