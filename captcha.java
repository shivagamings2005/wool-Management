package project;

import java.util.Random;
public class captcha {
    String captchatext="";
    private char random(){
        Random r=new Random();
        return (char)((r.nextInt(93))+33);
    }
    public String captchaGenerate(){
        captchatext="";
        for(int i=0;i<5;i++){
            captchatext+=random();
        }
        return captchatext;
    }
    public boolean captchaCheck(String txt){
        if(txt.equals(captchatext)) return true;
        return false;
    }
}
