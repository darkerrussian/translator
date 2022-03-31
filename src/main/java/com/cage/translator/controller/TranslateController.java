package com.cage.translator.controller;

import com.cage.translator.MyText;
import com.cage.translator.TranslateYC;
import com.cage.translator.connection.SQLiteDB;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping("/translate")
public class TranslateController {

    SQLiteDB sqLiteDB = new SQLiteDB();
    TranslateYC translateYC;


    @GetMapping
    public ResponseEntity get(){
       translateYC = new TranslateYC("Hello mom" , "en", "ru");
        //translateYC.TranslateText();

       /* try {
            sqLiteDB.connect();
            sqLiteDB.addDataToTable("hallo", "en", "ru");
            sqLiteDB.toString();
            return ResponseEntity.ok("Salam " + sqLiteDB.toString());
            //return  ResponseEntity.ok(translateYC.getTextToTranslate() + " -> " + translateYC.getTextTranslated());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("error");
        }*/
        return ResponseEntity.ok("Hello, go to /translate/text and add params");
    }
    @GetMapping("/text")
    public MyText text(@RequestParam(value = "text") String text, @RequestParam(value = "from") String from,
    @RequestParam(value = "to") String to) throws SQLException {


        sqLiteDB.connect();
        sqLiteDB.addDataToTable(text, from, to);
        sqLiteDB.toString();

        translateYC = new TranslateYC(text , from, to);
        translateYC.TranslateText();


        return new MyText(text,to,from,translateYC.getTextTranslated());

    }


}
