package hr.kristiankliskovic.ProjectGolf1.zavrsni16052021;


import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import static android.provider.Settings.System.getString;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class Location_data {

    double X;
    double Y;
    int code;
    String timestamp;
    boolean show_marker;


    double getX(){
        return X;
    }

    double getY(){
        return Y;
    }

    public int getCode() {
        return code;
    }

    void setX(double mX) {
        X=mX;
    }
    void setY(double mY) {
        Y=mY;
    }

    public boolean get_Show_marker(){
        return show_marker;
    }


    public void setShow_marker(boolean show_marker) {
        this.show_marker = show_marker;
    }

    public void setCode(int code) {
        this.code = code;
    }

    String getTimestamp(){
        return timestamp;
    }

    void setTimestamp(String mTimestamp){
        timestamp=mTimestamp;
    }

    Location_data(){
        X=0;
        Y=0;
        code=-1;
        timestamp="";
        show_marker = false;
        return;
    }

    Location_data(double mX, double mY, int mCode, String mTimestamp){
        X=mX;
        Y=mY;
        code=mCode;
        timestamp=mTimestamp;
        show_marker  =false;
    }

    Location_data(String input, String mTimestamp){
        timestamp=mTimestamp;
        String base= "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.:";
        this.show_marker = false;
        //velicina paketa 11 charova, ponekad 10
        //zadnji char je onaj dodatni character

        if((input.length()==11 || input.length()==10 || input.length()==1)==false){
            X=183;
            Y=93;
            code=-2;
            return;
        }
        if(input.length()==1){
            setX(182);
            setY(92);
            switch (input.charAt(0)) {
                case ',':
                    setCode(0);
                    break;
                case '@':
                    setCode(1);
                    break;
                case '-':
                    setCode(2);
                    break;
                case '_':
                    setCode(3);
                    break;
                default:
                    setCode(-1);
                    break;
            }
            setX(182);
            setY(92);
            return;
        }

        String binary="";
        for(int i=0;i<10;i++){
            int value=base.indexOf(""+input.charAt(i));
            String value_bin=Integer.toBinaryString(value);
            while(value_bin.length()<6){
                value_bin="0"+value_bin;
            }
            binary+=value_bin;
        }

        //60 znakova
        int sum=0;
        for(int i=0;i<57;i++){
            if(binary.charAt(i)=='1') sum++;
        }
        sum=sum%8;
        String Checksum="";
        for(int i=57;i<60;i++) Checksum+=binary.charAt(i);
        if(Integer.parseInt(Checksum,2)!=sum) {
            Log.i("tag_binary", "neispravno dekodiranje");
        }

        //X
        int X_sign=binary.charAt(0)=='0'? 1:-1;

        String X_int_str="";
        for(int i=1;i<9;i++) X_int_str+=binary.charAt(i);
        int X_int_int=Integer.parseInt(X_int_str,2);


        String X_dec_str="";
        for(int i=9;i<29;i++) X_dec_str+=binary.charAt(i);
        double X_dec_dec=Integer.parseInt(X_dec_str,2);
        X_dec_dec/=1000000;


        setX(X_sign*(X_int_int+X_dec_dec));

        //Y
        int Y_sign=binary.charAt(29)=='0'? 1:-1;

        String Y_int_str="";
        for(int i=30;i<37;i++) Y_int_str+=binary.charAt(i);
        int Y_int_int=Integer.parseInt(Y_int_str,2);

        Log.i("tag_binary", ""+Y_int_str);

        String Y_dec_str="";
        for(int i=37;i<57;i++) Y_dec_str+=binary.charAt(i);
        double Y_dec_dec=Integer.parseInt(Y_dec_str,2);
        Y_dec_dec/=1000000;


        setY(Y_sign*(Y_int_int+Y_dec_dec));

        if(input.length()==11) {
            switch (input.charAt(10)) {
                case ',':
                    setCode(0);
                    break;
                case '@':
                    setCode(1);
                    break;
                case '-':
                    setCode(2);
                    break;
                case '_':
                    setCode(3);
                    break;
                default:
                    setCode(-1);
                    break;
            }
        }
        else if(input.length()==10){
            setCode(4);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    long Timestamp_history_calc(String input){
        //vraca vrijeme u sekundama od argumenta
        //LocalDate NOW = LocalDate.now();
        //LocalDate parsedDate = LocalDate.parse(time, ISO_INSTANT);

        //LocalDateTime time = LocalDateTime.parse("04.02.2014  19:51:01", DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm:ss"));


        //String text = date.format(ISO_INSTANT);
        //LocalDate parsedDate = LocalDate.parse(text, ISO_INSTANT);

        input = input.replace("T"," ");
        input = input.replace("Z","");
        Log.i("tagg","|"+input +"|");
        //LocalDateTime now=now();
        LocalDateTime nowUTC=LocalDateTime.now(ZoneId.of("Z"));


        LocalDateTime input_parsed = LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        long UNIX_now=nowUTC.getLong(ChronoField.EPOCH_DAY)*24*3600 + nowUTC.getLong(ChronoField.SECOND_OF_DAY);
        long unix_input=input_parsed.getLong(ChronoField.EPOCH_DAY)*24*3600+input_parsed.getLong(ChronoField.SECOND_OF_DAY);

        return UNIX_now-unix_input;
    }
}