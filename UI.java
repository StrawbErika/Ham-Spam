import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;

public class UI {
    private JFrame frame;
    public HashMap<String, ArrayList> classifyDictionary;
    public int numberOfSpamMsgs;
    public int numberOfHamMsgs;
    public int numberOfClassifyMsgs;

    public UI() {
        classifyDictionary = new HashMap<String, ArrayList>();
        String s="";
        String a="";
        BagOfWords spam = new BagOfWords();
        numberOfSpamMsgs = new File("all_data/spam/").listFiles().length;
        for(int i = 1; i<numberOfSpamMsgs; i++){
            a = String.format("%03d", i);
            s = String.format("all_data/spam/" + a);
            spam.loadFile(s);
        }
        spam.saveFile(spam.dictionarySize, spam.numberOfWords, "outputSpam.txt");

        BagOfWords ham = new BagOfWords();
        numberOfHamMsgs = new File("all_data/ham/").listFiles().length;
        for(int i = 1; i<300; i++){
            a = String.format("%03d", i);
            s = String.format("all_data/ham/" + a);
            ham.loadFile(s);
        }
        ham.saveFile(ham.dictionarySize, ham.numberOfWords, "outputHam.txt");

        numberOfClassifyMsgs = new File("all_data/classify/").listFiles().length;
        for(int i = 1; i<=numberOfClassifyMsgs; i++){
            a = String.format("%03d", i);
            s = String.format("all_data/classify/" + a);
            classifyLoadFile(a, classifyDictionary);
        }
        //loads all files in classify (atm 1 muna) and saves it in the dictionary

        Probability p = new Probability();
        double pMessageSpam = 1;
        double pMessageHam = 1;
        double pSpam = p.pSpam(numberOfSpamMsgs, numberOfHamMsgs);
        double pHam = p.pHam(pSpam);

        //gets pMessageSpam & pMessageHam by looping through the dictionary and saves each probability in a file
        for (Map.Entry<String, ArrayList> entry : classifyDictionary.entrySet()) {
          double sVal;
          double hVal;
          double pWordSpam = 0;
          double pWordHam = 0;

          for(int i = 0; i < entry.getValue().size(); i++){
            if(spam.dictionary.containsKey(entry.getValue().get(i))){
                sVal = spam.dictionary.get(entry.getValue().get(i));
            }else{
              sVal = 0;
            }
            pWordSpam = p.pWordSpam(spam.numberOfWords, sVal);

            pMessageSpam = pWordSpam * pMessageSpam;

            if(ham.dictionary.containsKey(entry.getValue().get(i))){
                hVal = ham.dictionary.get(entry.getValue().get(i));
            }else{
              hVal = 0;
            }
            pWordHam = p.pWordHam(spam.numberOfWords, hVal);
            pMessageHam = pWordHam * pMessageHam;
          }

          double pSpamMessage;
          double pMessage = p.pMessage(pMessageSpam, pSpam, pMessageHam, pHam);
          if(pMessage > 0){
            pSpamMessage = p.pSpamMessage(pMessageSpam, pMessage, pSpam);
          }
          else{
            pSpamMessage = 0;
          }

          if (pSpamMessage > 0.5){
            classifyFile(entry.getKey(), "Spam", pSpamMessage);
          }
          else{
            classifyFile(entry.getKey(), "Ham", pSpamMessage);
          }
        }

        this.initializeUI();
    }


    public void classifyLoadFile(String filename, HashMap dictionary) {
        try{
            String a = filename;
            filename = String.format("all_data/classify/" + a);
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            ArrayList<String> list = new ArrayList<String>();
            while((line  = br.readLine()) != null)
            {
                String[] values = line.split(" "); //stores all the words from the line in values
                for (String str : values) {
                    str = str.toLowerCase();
                    str = str.replaceAll("[^a-zA-Z0-9]", ""); //checks if none alphanumeric
                    if(!(str.equals("")) && !(list.contains(str))){ //catches the case of ""
                      list.add(str);
                    }
                }
            }
            dictionary.put(a, list); //stores str in dictionary
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }


    public void classifyFile(String number, String name, double probabilitySpam) { //writes file
        String filename = "classify.out";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(number+ " "+ name+ " "+ probabilitySpam +"\n");
            writer.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");
        }
        catch(IOException ex) {
            System.out.println("Error writing file '" + filename + "'");
        }
    }

    public void saveFile(String filename, HashMap<String, ArrayList> cDictionary) { //writes file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (Map.Entry<String, ArrayList> entry : cDictionary.entrySet()) {
              for(int i = 0; i < entry.getValue().size(); i++){
                    writer.write(i +" "+ entry.getValue().get(i) + "\n");
              }
            }

            writer.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");
        }
        catch(IOException ex) {
            System.out.println("Error writing file '" + filename + "'");
        }
    }

    public void initializeUI() {
    }
  }
