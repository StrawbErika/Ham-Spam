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

    public UI() {
        classifyDictionary = new HashMap<String, ArrayList>();
        String s="";
        BagOfWords spam = new BagOfWords();
        for(int i = 1; i<300; i++){
            s = String.format("%03d", i);
            s = String.format("all_data/spam/" + s);
            spam.loadFile(s);
        }
        System.out.println("Spam lines " + spam.numberOfLines);
        spam.saveFile(spam.dictionarySize, spam.numberOfWords);

        BagOfWords ham = new BagOfWords();
        for(int i = 1; i<300; i++){
            s = String.format("%03d", i);
            s = String.format("all_data/ham/" + s);
            ham.loadFile(s);
        }
        System.out.println("ham lines " + ham.numberOfLines);
        ham.saveFile(ham.dictionarySize, ham.numberOfWords);

        s = String.format("all_data/classify/001");
        //loads all files in classify (atm 1 muna) and saves it in the dictionary
        classifyLoadFile(s, classifyDictionary);

        Probability p = new Probability();
        double pMessageSpam = 1;
        double pMessageHam = 1;
        double pSpam = p.pSpam(spam.numberOfWords, ham.numberOfWords);
        double pHam = p.pHam(pSpam);

        //gets pMessageSpam & pMessageHam by looping through the dictionary and saves each probability in a file
        for (Map.Entry<String, ArrayList> entry : classifyDictionary.entrySet()) {
          double sVal;
          double hVal;
          double pWordSpam;
          double pWordHam;
          //loops through the array of words of each file (which is saved as the value of each key <file>)
          for(int i = 0; i < entry.getValue().size(); i++){
            //gets MessageSpam & messageHam by getting the WordSpam (u get it by getting the #of occurences it is in Spam/Ham)
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

          System.out.println(pMessageHam);
          double pMessage = p.pMessage(pMessageSpam, pSpam, pMessageHam, pHam);
          System.out.println(pMessage);
          double pSpamMessage = p.pSpamMessage(pMessageSpam, pMessage);
          double pHamMessage = p.pHamMessage(pMessageHam, pMessage);
          System.out.println(pSpamMessage);
          System.out.println(pHamMessage);

          classifyFile(entry.getKey(), "Ham", pSpamMessage);
        }



        this.initializeUI();
    }


    public void classifyLoadFile(String filename, HashMap dictionary) {
        try{
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
                    list.add(str);
                }
            }
            dictionary.put(filename, list); //stores str in dictionary
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

    public void initializeUI() {
        frame = new JFrame("Sokoban");

        Container pane = frame.getContentPane();
        JPanel panel = new JPanel();
        frame.setSize(500, 500);        // "super" Frame sets its initial window size

        JButton button = new JButton();
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(200, 50)); //tile size
        button.setLabel("Select Spam Folder");
        button.setLocation(12, 371);
        panel.add(button);

        pane.add(panel);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.pack();
        frame.setVisible(true);
    }

}
