import java.io.*;
import java.util.*;

public class Probability {
    public double x;
    public Probability(){
    }

//MESSAGE IS FILE
    public double pSpam(double numberOfSpamMsgs, double numberOfHamMsgs){
        x = numberOfSpamMsgs / (numberOfHamMsgs + numberOfSpamMsgs);
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
        return x;
    }

    public double pWordHam(double hamWords, double occurenceOfWord){
        x =  occurenceOfWord / hamWords;
        return x;
    }


    public double pSpamMessage(double pProductOfWordSpam, double pMessage, double pSpam){
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
