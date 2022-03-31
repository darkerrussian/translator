package com.cage.translator;

import yandex.cloud.api.ai.translate.v2.TranslationServiceGrpc;
import yandex.cloud.api.ai.translate.v2.TranslationServiceGrpc.TranslationServiceBlockingStub;
import yandex.cloud.api.ai.translate.v2.TranslationServiceOuterClass.TranslateRequest;
import yandex.cloud.api.ai.translate.v2.TranslationServiceOuterClass.TranslateResponse;
import yandex.cloud.sdk.ServiceFactory;
import yandex.cloud.sdk.auth.Auth;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranslateYC {

    private static final String MY_YC_FOLDER_ID = "b1gd4v9h9o78f02ttbis";
    private String textToTranslate;
    private String textTranslated;
    private String wordTranslated;

    public String getWordTranslated() {
        return wordTranslated;
    }

    public void setWordTranslated(String wordTranslated) {
        this.wordTranslated = wordTranslated;
    }

    private String fromLang;
    private String toLang;
    Connection connection;
    PreparedStatement ps;


    public TranslateYC(String textToTranslate, String fromLang, String toLang){
        this.textToTranslate = textToTranslate;

        this.fromLang = fromLang;
        this.toLang = toLang;

    }




    public String getFromLang() {
        return fromLang;
    }

    public void setFromLang(String fromLang) {
        this.fromLang = fromLang;
    }

    public String getToLang() {
        return toLang;
    }

    public void setToLang(String toLang) {
        this.toLang = toLang;
    }

    public void TranslateText() throws SQLException {
        /*
        YC_OAUTH нужно добавить в Переменные среды (Windows) мое значение "AQAAAAAGzhZWAATuwfezFIjx2kU1jzD4MkKVK2k"
        */
        ServiceFactory factory = ServiceFactory.builder()
                .credentialProvider(Auth.oauthTokenBuilder().fromEnv("YC_OAUTH"))
                .requestTimeout(Duration.ofMinutes(1))
                .build();
        TranslationServiceBlockingStub translationService = factory.create(TranslationServiceBlockingStub.class, TranslationServiceGrpc::newBlockingStub);


        List<String> textArray = new ArrayList<>(Arrays.asList(textToTranslate.split(" ")));
        List<String> translatedList = new ArrayList<>();
        textTranslated = "";

        for(String wordToTranslate: textArray) {
            TranslateResponse response = translationService.translate(buildTranslateRequest(wordToTranslate, fromLang, toLang));

            wordTranslated = response.getTranslations(0).getText();
            addDataToNWords(textToTranslate, wordTranslated);
            textTranslated = textTranslated +" " + wordTranslated;
            addDataToWords(textToTranslate, wordToTranslate);



        }
        //System.out.println(String.format("%s -> %s", text, translation));


    }

    private void addDataToWords(String params, String word) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:base.db");
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        ps = connection.prepareStatement("INSERT INTO words(req_id, word) VALUES ((SELECT id FROM input_info WHERE params = ?), ?)");
        ps.setString(1, params);
        ps.setString(2, word);

        ps.addBatch();
        ps.executeBatch();
        connection.commit();
        ps.close();
    }

    private void addDataToNWords(String params, String word) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:base.db");
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        ps = connection.prepareStatement("INSERT INTO output(req_id, n_word) VALUES ((SELECT id FROM input_info WHERE params = ?), ?)");
        ps.setString(1, params);
        ps.setString(2, word);

        ps.addBatch();
        ps.executeBatch();
        connection.commit();
        ps.close();
    }

    public String getTextToTranslate() {
        return textToTranslate;
    }

    public String getTextTranslated() {
        return textTranslated;
    }

    private static TranslateRequest buildTranslateRequest(String text, String from, String to) {
        return TranslateRequest.newBuilder()
                .setSourceLanguageCode(from)
                .setTargetLanguageCode(to)
                .setFormat(TranslateRequest.Format.PLAIN_TEXT)
                .addTexts(text)
                .setFolderId(MY_YC_FOLDER_ID)
                .build();
    }

}
