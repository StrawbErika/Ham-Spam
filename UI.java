import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;

public class UI {
    private JFrame frame;

    public UI() {
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

        BagOfWords classify = new BagOfWords();

        s = String.format("all_data/classify/001");
        classifyFile(s);

        Probability p = new Probability();
        double pMessageSpam = 1;
        double pMessageHam = 1;
        double pSpam = p.pSpam(spam.numberOfWords, ham.numberOfWords);
        double pHam = p.pHam(pSpam);

        for (Map.Entry<String, Integer> entry : spam.dictionary.entrySet()) {
                double pWordSpam = p.pWordSpam(spam.numberOfWords, entry.getValue());
                pMessageSpam = pWordSpam * pMessageSpam;
        }

        for (Map.Entry<String, Integer> entry : ham.dictionary.entrySet()) {
                double pWordHam = p.pWordHam(ham.numberOfWords, entry.getValue());
                pMessageHam = pWordHam * pMessageHam;
        }


        double pMessage = p.pMessage(pMessageSpam, pSpam, pMessageHam, pHam);
        double pSpamMessage = p.pSpamMessage(pMessageSpam, pMessage);
        System.out.println(pSpamMessage);


        this.initializeUI();
    }


    public void loadFile(String filename) {
        try{
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line  = br.readLine()) != null)
            {
                String[] values = line.split(" "); //stores all the words from the line in values
                for (String str : values) {
                    str = str.toLowerCase();
                    str = str.replaceAll("[^a-zA-Z0-9]", ""); //checks if none alphanumeric


                }
            }
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }


    public void classifyFile(String number, String name, int probabilitySpam) { //writes file
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
