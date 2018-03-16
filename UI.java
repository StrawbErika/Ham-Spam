import java.util.Scanner;
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
    public ArrayList<ArrayList<String>> outputDictionary;
    public int numberOfSpamMsgs;
    public int numberOfHamMsgs;
    public int numberOfClassifyMsgs;
    public BagOfWords spam;
    public BagOfWords ham;
    public UI() {
        this.classifyDictionary = new HashMap<String, ArrayList>();
        this.outputDictionary = new ArrayList<ArrayList<String>>();
        this.spam = new BagOfWords();
        this.ham = new BagOfWords();
        String s="";
        String a="";
        // numberOfSpamMsgs = new File("all_data/spam/").listFiles().length;
        // for(int i = 1; i<numberOfSpamMsgs; i++){
        //     a = String.format("%03d", i);
        //     s = String.format("all_data/spam/" + a);
        //     spam.loadFile(s);
        // }
        // spam.saveFile(spam.dictionarySize, spam.numberOfWords, "outputSpam.txt");

        // numberOfHamMsgs = new File("all_data/ham/").listFiles().length;
        // for(int i = 1; i<300; i++){
        //     a = String.format("%03d", i);
        //     s = String.format("all_data/ham/" + a);
        //     ham.loadFile(s);
        // }
        // ham.saveFile(ham.dictionarySize, ham.numberOfWords, "outputHam.txt");

        // numberOfClassifyMsgs = new File("all_data/classify/").listFiles().length;
        // for(int i = 1; i<=numberOfClassifyMsgs; i++){
        //     a = String.format("%03d", i);
        //     s = String.format("all_data/classify/" + a);
        //     classifyLoadFile(a, classifyDictionary);
        // }
        // //loads all files in classify (atm 1 muna) and saves it in the dictionary
        this.initializeUI();
    }

    public ArrayList<String> findSpam(String name, ArrayList<String> words, double k){
        Probability p = new Probability();
        double pMessageSpam = 1;
        double pMessageHam = 1;
        numberOfHamMsgs = 1;
        numberOfSpamMsgs = 1;
        double pSpam = p.pSpam(numberOfSpamMsgs, numberOfHamMsgs, k);
        double pHam = p.pHam(pSpam);

        double newSpam = p.findNewWords(this.spam, words);
        double newHam = p.findNewWords(this.ham, words);

        double sVal;
        double hVal;
        double pWordSpam = 0;
        double pWordHam = 0;

        for(int i = 0; i < words.size(); i++){
          if(this.spam.dictionary.containsKey(words.get(i))){
              sVal = this.spam.dictionary.get(words.get(i));
          }else{
            sVal = 0;
          }
          pWordSpam = p.pWordSpam(this.spam.numberOfWords, sVal, k, this.spam.dictionarySize, newSpam);

          pMessageSpam = pWordSpam * pMessageSpam;

          if(this.ham.dictionary.containsKey(words.get(i))){
              hVal = this.ham.dictionary.get(words.get(i));
          }else{
            hVal = 0;
          }
          pWordHam = p.pWordHam(this.ham.numberOfWords, hVal, k, this.ham.dictionarySize, newHam);
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

        String classify = " ";
        if (pSpamMessage > 0.5){
          classify = "Spam";
        }
        else{
          classify = "Ham";
        }
        ArrayList<String> output = new ArrayList<String>();
        output.add(name);
        output.add(classify);
        output.add(Double.toString(pSpamMessage));

        return output;
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
            // writer.write("# of files: " + dictionary.size +"\n");
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
      JLabel stats02= new JLabel();
      JFrame frame = new JFrame("Ham & Spam");//creates a frame/window; javax.swing.JFrame
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setPreferredSize(new Dimension(1200, 700));//sets initial size resolution of the frame...; java.awt.Dimensio

      Container con = frame.getContentPane();
      JPanel contentPanel= new JPanel();

      JPanel tabPanel= new JPanel();
      tabPanel.setLayout(new FlowLayout());
      tabPanel.setPreferredSize(new Dimension(250,500));
      // tabPanel.setBackground(Color.WHITE);

      JTextArea wordsSpam = new JTextArea();
      JTextArea frequenciesSpam = new JTextArea();
      JButton information= new JButton("Select Spam Folder here");
      JPanel totalWordSpam = new JPanel();
      totalWordSpam.setLayout(new GridLayout(1,2));
      JLabel totalSpam = new JLabel("Total words in Spam: ");
      JTextArea totalS = new JTextArea();
      totalWordSpam.add(totalSpam);
      totalWordSpam.add(totalS);

      information.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          JFileChooser fileChooser = new JFileChooser();
          int result = fileChooser.showOpenDialog(frame);
          if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String a = "";
            String s = "";
            String fileDirectory = (fileChooser.getCurrentDirectory()).getPath();
            int numberOfSpamMsgs = new File(fileDirectory).listFiles().length;
            for(int i = 1; i<=numberOfSpamMsgs; i++){
                a = String.format("%03d", i);
                s = String.format(fileDirectory+"/" + a);
                spam.loadFile(s);
            }
            for (Map.Entry<String, Integer> entry : spam.dictionary.entrySet()) {
              wordsSpam.append(entry.getKey()+ '\n');
              frequenciesSpam.append("             "+ entry.getValue()+ '\n');
            }
            totalS.append(Integer.toString(spam.numberOfWords));
          }
          frame.requestFocus();
          }
      });

      information.setPreferredSize(new Dimension(200,50));

      JPanel wordFrequencySpamPanel= new JPanel();
      wordFrequencySpamPanel.setLayout(new GridLayout(1,2));
      wordFrequencySpamPanel.setPreferredSize(new Dimension(300,550));

      JPanel wordSpamPanel= new JPanel();
      wordSpamPanel.setBackground(Color.WHITE);
      JPanel textWSpamPanel= new JPanel();
      textWSpamPanel.setPreferredSize(new Dimension(200,30));
      textWSpamPanel.setBackground(Color.LIGHT_GRAY);
      JLabel wordSpam = new JLabel("Word");
      textWSpamPanel.add(wordSpam);
      wordSpamPanel.add(textWSpamPanel);
      wordSpamPanel.add(wordsSpam);

      JPanel frequencySpamPanel= new JPanel();
      frequencySpamPanel.setBackground(Color.WHITE);
      JPanel textFSpamPanel= new JPanel();
      textFSpamPanel.setPreferredSize(new Dimension(200,30));
      textFSpamPanel.setBackground(Color.LIGHT_GRAY);
      JLabel frequencySpam = new JLabel("Frequency");
      textFSpamPanel.add(frequencySpam);
      frequencySpamPanel.add(textFSpamPanel);
      frequencySpamPanel.add(frequenciesSpam);

      wordFrequencySpamPanel.add(wordSpamPanel);
      wordFrequencySpamPanel.add(frequencySpamPanel);


      tabPanel.add(information);
      tabPanel.add(wordFrequencySpamPanel);
      tabPanel.add(totalWordSpam);


      contentPanel.setLayout(new CardLayout());
      CardLayout cardLayout= (CardLayout) contentPanel.getLayout();

      //panel 1
      JPanel hamPanel= new JPanel();
      hamPanel.setLayout(new FlowLayout());
      hamPanel.setPreferredSize(new Dimension(250,550));

      JButton panel1= new JButton("Select Ham Folder here");
      JTextArea words = new JTextArea();
      JTextArea frequencies = new JTextArea();
      JPanel totalWordHam = new JPanel();
      totalWordHam.setLayout(new GridLayout(1,2));
      JLabel totalHam = new JLabel("Total words in Ham: ");
      JTextArea totalH = new JTextArea();
      totalWordHam.add(totalHam);
      totalWordHam.add(totalH);
      panel1.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          JFileChooser fileChooser = new JFileChooser();
          int result = fileChooser.showOpenDialog(frame);
          if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            System.out.println(fileChooser.getCurrentDirectory());
            String a = "";
            String s = "";
            String fileDirectory = (fileChooser.getCurrentDirectory()).getPath();
            int numberOfHamMsgs = new File(fileDirectory).listFiles().length;
            for(int i = 1; i<=numberOfHamMsgs; i++){
                a = String.format("%03d", i);
                s = String.format(fileDirectory+"/" + a);
                ham.loadFile(s);
            }
            for (Map.Entry<String, Integer> entry : ham.dictionary.entrySet()) {
              words.append("                                                                                                                 "+ entry.getKey()+ '\n');
              frequencies.append("             "+ entry.getValue()+ '\n');
            }
            totalH.append(Integer.toString(ham.numberOfWords));
          }
          frame.requestFocus();
        }
      });

      panel1.setPreferredSize(new Dimension(200,50));

      JPanel wordFrequencyPanel= new JPanel();
      wordFrequencyPanel.setLayout(new GridLayout(1,2));
      wordFrequencyPanel.setPreferredSize(new Dimension(300,550));
      wordFrequencyPanel.setBackground(Color.GRAY);
      JPanel wordPanel= new JPanel();
      wordPanel.setBackground(Color.WHITE);
      JPanel textWPanel= new JPanel();
      textWPanel.setPreferredSize(new Dimension(200,30));
      textWPanel.setBackground(Color.LIGHT_GRAY);
      JLabel word = new JLabel("Word");
      textWPanel.add(word);
      wordPanel.add(textWPanel);
      wordPanel.add(words);

      JPanel frequencyPanel= new JPanel();
      frequencyPanel.setBackground(Color.WHITE);
      JPanel textFPanel= new JPanel();
      textFPanel.setPreferredSize(new Dimension(200,30));
      textFPanel.setBackground(Color.LIGHT_GRAY);
      JLabel frequency = new JLabel("Frequency");
      textFPanel.add(frequency);
      frequencyPanel.add(textFPanel);
      frequencyPanel.add(frequencies);

      wordFrequencyPanel.add(wordPanel);
      wordFrequencyPanel.add(frequencyPanel);

      hamPanel.add(panel1);
      hamPanel.add(wordFrequencyPanel);
      hamPanel.add(totalWordHam);


  //panel 2
      JPanel panel02= new JPanel();
      panel02.setPreferredSize(new Dimension(600,60));
      JPanel dictionaryTotal = new JPanel();
      dictionaryTotal.setPreferredSize(new Dimension(500,60));
      dictionaryTotal.setBackground(Color.WHITE);
      dictionaryTotal.setLayout(new GridLayout(1, 4));
      JTextArea dSize = new JTextArea();
      dSize.setPreferredSize(new Dimension(10,60));
      JTextArea tWords = new JTextArea();
      JLabel dictionarySize = new JLabel("               D_Size:");
      JLabel totalWords = new JLabel("Total Words: ");
      dictionaryTotal.add(dictionarySize);
      dictionaryTotal.add(dSize);
      dictionaryTotal.add(totalWords);
      dictionaryTotal.add(tWords);

      JPanel buttonsPanel = new JPanel();
      buttonsPanel.setPreferredSize(new Dimension(350,100));
      // buttonsPanel.setBackground(Color.GRAY);

      JButton classify= new JButton("Select Classify Folder");
      JButton filter= new JButton("Filter");
      JLabel output = new JLabel ("Output: ");
      JPanel outputPanel= new JPanel();
      outputPanel.setPreferredSize(new Dimension(350,500));
      outputPanel.setLayout(new GridLayout(1,3));

      JPanel filePanel= new JPanel();
      JPanel fileName = new JPanel();
      fileName.setPreferredSize(new Dimension(300, 30));
      fileName.setBackground(Color.LIGHT_GRAY);
      JLabel file = new JLabel("Filename");
      JPanel fileContent = new JPanel();
      fileContent.setPreferredSize(new Dimension(200,500));
      fileContent.setBackground(Color.WHITE);
      JTextArea fileC = new JTextArea();
      fileName.add(file);
      fileContent.add(fileC);
      filePanel.add(fileName);
      filePanel.add(fileContent);

      JPanel classPanel= new JPanel();
      JPanel className = new JPanel();
      className.setPreferredSize(new Dimension(300, 30));
      className.setBackground(Color.LIGHT_GRAY);
      JLabel classN = new JLabel("Class");
      JPanel classContent = new JPanel();
      classContent.setPreferredSize(new Dimension(200,500));
      classContent.setBackground(Color.WHITE);
      JTextArea classC = new JTextArea();
      classContent.add(classC);
      className.add(classN);
      classPanel.add(className);
      classPanel.add(classContent);

      JPanel pSpamPanel= new JPanel();
      JPanel spamName = new JPanel();
      spamName.setPreferredSize(new Dimension(300, 30));
      spamName.setBackground(Color.LIGHT_GRAY);

      JLabel spamN = new JLabel("pSpam");
      JPanel spamContent = new JPanel();
      spamContent.setPreferredSize(new Dimension(200,500));
      spamContent.setBackground(Color.WHITE);
      JTextArea spamC = new JTextArea();
      spamName.add(spamN);
      spamContent.add(spamC);
      pSpamPanel.add(spamName);
      pSpamPanel.add(spamContent);

      classify.setPreferredSize(new Dimension(200,50));
      filter.setPreferredSize(new Dimension(100,30));

      classify.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          JFileChooser fileChooser = new JFileChooser();
          int result = fileChooser.showOpenDialog(frame);
          if (result == JFileChooser.APPROVE_OPTION) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            System.out.println(fileChooser.getCurrentDirectory());
            String a = "";
            String s = "";
            String fileDirectory = (fileChooser.getCurrentDirectory()).getPath();
            int numberOfClassifyMsgs = new File(fileDirectory).listFiles().length;
            for(int i = 1; i<=numberOfClassifyMsgs; i++){
                a = String.format("%03d", i);
                s = String.format("all_data/classify/" + a);
                classifyLoadFile(a, classifyDictionary);
            }
            dSize.append(Integer.toString(ham.dictionarySize + spam.dictionarySize));
            tWords.append(Integer.toString(ham.numberOfWords + spam.dictionarySize));
          }
          frame.requestFocus();
        }
      });

      filter.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){

          Scanner reader = new Scanner(System.in);  // Reading from System.in
          System.out.println("Enter k: ");
          double k =reader.nextDouble();

          System.out.println("Enter H0I: ");
          for (Map.Entry<String, ArrayList> entry : classifyDictionary.entrySet()) {
            System.out.println(entry.getKey());
            outputDictionary = findSpam(entry.getKey(),entry.getValue(), k);
          }
          System.out.println("Enter H0III: ");
          for(int i = 0; i < outputDictionary.size(); i+=2){
            // ArrayList<String> list = outputDictionary.get(i);
            System.out.println(outputDictionary);
            // for(int j = 0; j < list.size(); j+=2){
              // System.out.println("Enter H0III???????????/: ");
              // System.out.println(list);
              // String j0 = (list.get(j));
              // String j1 = (list.get(j+1));
              // String j2 = (list.get(j+2));
              // fileC.append(j0+ '\n');
              // classC.append(j1+ '\n');
              // spamC.append("                            "+j2+ '\n');
            // }
          }
        }
      });

      buttonsPanel.setLayout(new GridLayout(2,1));
      buttonsPanel.add(classify);
      buttonsPanel.add(filter);
      panel02.add(dictionaryTotal);
      panel02.add(buttonsPanel);
      panel02.add(output);
      outputPanel.add(filePanel);
      outputPanel.add(classPanel);
      outputPanel.add(pSpamPanel);
      panel02.add(outputPanel);
  //--

  //--

      con.setLayout(new GridLayout(1,3));
      con.add(tabPanel);
      con.add(hamPanel);
      con.add(panel02);

      frame.pack();
  		frame.setVisible(true);
    }
  }
