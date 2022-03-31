package com.cage.translator;

import java.util.List;



    public class MyText {
        private int code;
        private String text;
        private String fromLang;
        private String toLang;
        private String translatedText;

        public String getTranslatedText() {
            return translatedText;
        }

        public void setTranslatedText(String translatedText) {
            this.translatedText = translatedText;
        }

        public MyText(String text, String fromLang, String toLang, String translatedText){
            this.text = text;
            this.fromLang = fromLang;
            this.toLang = toLang;
            this.translatedText = translatedText;

        }

        public String getToLang() {
            return toLang;
        }

        public void setToLang(String toLang) {
            this.toLang = toLang;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getFromLang() {
            return fromLang;
        }

        public void setFromLang(String fromLang) {
            this.fromLang = fromLang;
        }
    }


