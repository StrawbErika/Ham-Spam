import java.io.*;
import java.util.*;

public class Probability {
    public double x;
    public Probability(){
    }

    public double pSpam(double spamLines, double hamLines){
        x = spamLines / (hamLines + spamLines);
        return x;
    }

    public double pHam(double spam){
        x = 1 - spam;
        return x;
    }

    // public double pMessageSpam(){
        // multiply all pWordSpam
    // }

    public double pWordSpam(double spamWords, double occurenceOfWord){
        x =  occurenceOfWord / spamWords;
        System.out.println("# OF SPAM WORDS: " + spamWords + " OCCURENCE OF WORD: " + occurenceOfWord);
        return x;
    }

    public double pWordHam(double hamWords, double occurenceOfWord){
        x =  occurenceOfWord / hamWords;
        return x;
    }


    public double pSpamMessage(double pProductOfWordSpam, double pMessage, double pSpam){
        x = pProductOfWordSpam*pSpam / pMessage;
        return x;
    }

    public double pHamMessage(double pProductOfWordHam, double pMessage){
        x = pProductOfWordHam / pMessage;
        return x;
    }


    public double pMessage(double pMessageSpam, double pSpam, double pMessageHam, double pHam){
        x = (pMessageSpam*pSpam) + (pMessageHam*pHam);
        return x;
    }
}
