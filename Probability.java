import java.io.*;
import java.util.*;

public class Probability {
    public double x;
    public Probability(){
    }

//MESSAGE IS FILE
    public double pSpam(double numberOfSpamMsgs, double numberOfHamMsgs, double k){
        x = numberOfSpamMsgs+k / (numberOfHamMsgs + numberOfSpamMsgs) + 2*k;
        return x;
    }

    public double pHam(double spam){
        x = 1 - spam;
        return x;
    }

    public double findNewWords(BagOfWords hamSpam, ArrayList<String> words){
      x = 0;
      for(int i = 0; i < words.size(); i++){
        for (Map.Entry<String, Integer> entry : hamSpam.dictionary.entrySet()) {
          if(!(entry.getKey().equals(words.get(i)))){
            x++;
          }
        }
      }
      return x;
    }

    // public double pMessageSpam(){
        // multiply all pWordSpam
    // }

    public double pWordSpam(double spamWords, double occurenceOfWord, double k, double size, double newWords){
        x =  occurenceOfWord+k / spamWords + (k*(size+newWords));
        return x;
    }

    public double pWordHam(double hamWords, double occurenceOfWord, double k, double size, double newWords){
        x =  occurenceOfWord+k / hamWords + (k*(size+newWords));
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
