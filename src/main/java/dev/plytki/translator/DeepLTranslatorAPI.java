package dev.plytki.translator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.plytki.translator.model.DeepLLanguage;
import okhttp3.*;

import java.io.*;

public class DeepLTranslatorAPI {
    private final String deepLApiKey;

    public DeepLTranslatorAPI(String deepLApiKey) {
        this.deepLApiKey = deepLApiKey;
    }

    public String translate(String text, DeepLLanguage targetLanguage) throws IOException {
        if (text == null)
            return null;
        if (text.isEmpty())
            return "";

        OkHttpClient client = new OkHttpClient();
        String url = "https://api-free.deepl.com/v2/translate";
        // Build request body
        RequestBody requestBody = new FormBody.Builder()
                .add("text", text)
                .add("target_lang", targetLanguage.name())
                .add("formality", "less")
                .add("split_sentences", "nonewlines")
                .add("preserve_formatting", "1")
                .build();
        // Build request with headers and body
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "DeepL-Auth-Key " + this.deepLApiKey)
                .post(requestBody)
                .build();
        // Send request and get response
        Response response = client.newCall(request).execute();
        // Parse JSON response
        String responseBody = response.body().string();
        JsonObject jsonData = JsonParser.parseString(responseBody).getAsJsonObject();
        // Print translation text
        String translationText = jsonData.get("translations").getAsJsonArray()
                .get(0).getAsJsonObject().get("text").getAsString();
        // Close response and client
        response.close();
        client.dispatcher().executorService().shutdown();
        // Display translated line
        System.out.println("Translated: " + text + " -> " + translationText);
        return translationText;
    }

}
