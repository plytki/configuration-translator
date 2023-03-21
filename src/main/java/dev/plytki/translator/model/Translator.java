package dev.plytki.translator.model;

import dev.plytki.translator.DeepLTranslatorAPI;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public abstract class Translator implements ITranslator {

    private final String inputFile;
    private final String translatedFile;
    private final DeepLTranslatorAPI deepLTranslatorAPI;
    private final List<String> patternPreferences = new ArrayList<>();
    private final Set<String> skipped = new LinkedHashSet<>();
    private final Set<Function<String, Boolean>> handlers = new HashSet<>();

    public Translator(DeepLTranslatorAPI deepLTranslatorAPI, String inputPath, String outputPath) {
        this.deepLTranslatorAPI = deepLTranslatorAPI;
        this.inputFile = inputPath;
        this.translatedFile = outputPath;
    }

    public void addPatternPreference(String... skipped) {
        this.patternPreferences.addAll(Arrays.asList(skipped));
    }

    public void skip(Function<String, Boolean> function) {
        this.handlers.add(function);
    }

    protected boolean shouldSkipLine(String line) {
        for (String pattern : this.patternPreferences) {
            if (Pattern.compile(pattern).matcher(line).find()) {
                this.skipped.add(line);
                return true;
            }
        }
        return false;
    }

    protected String translateString(String original, DeepLLanguage deepLLanguage) {
        for (Function<String, Boolean> handler : this.handlers) {
            if (handler.apply(original)) {
                return original;
            }
        }
        if (shouldSkipLine(original)) return original;
        try {
            return this.deepLTranslatorAPI.translate(original, deepLLanguage);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return original;
    }

    protected String inputFile() {
        return inputFile;
    }

    protected String translatedFile() {
        return translatedFile;
    }

    protected DeepLTranslatorAPI deepLTranslatorAPI() {
        return deepLTranslatorAPI;
    }

    protected List<String> userPreferences() {
        return patternPreferences;
    }

    public Set<String> skipped() {
        return skipped;
    }

    public Set<Function<String, Boolean>> handlers() {
        return handlers;
    }

}
