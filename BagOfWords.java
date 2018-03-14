import java.io.*;
import java.util.*;

public class BagOfWords {

    public HashMap<String, Integer> dictionary;
    public int numberOfWords;
    public int dictionarySize;
    public int numberOfLines;

    public BagOfWords(){
        this.dictionary = new HashMap<String, Integer>();
        this.numberOfWords = 0;
        this.dictionarySize = 0;
        this.numberOfLines = 0;
    }



    public void loadFile(String filename) {
        try{
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            //Read File Line By Line

            while((line  = br.readLine()) != null)
            {
                String[] values = line.split(" "); //stores all the words from the line in values
                for (String str : values) {
                        str = str.toLowerCase();
                        str = str.replaceAll("[^a-zA-Z0-9]", ""); //checks if none alphanumeric
                        if(dictionary.containsKey(str)){
                            int val = dictionary.get(str);
                            val ++;
                            dictionary.put(str, val); //stores str in dictionary
                        }else{
                            if(!(str.equals(""))){ //catches the case of ""
                                dictionarySize++;
                                dictionary.put(str, 1);
                            }
                        }
                        numberOfWords++;
                }
                numberOfLines++;
            }
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void saveFile(String filename, String string) { //writes file
          try {
              BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
              writer.write(string + "\n");

              writer.close();
          }
          catch(FileNotFoundException ex) {
              System.out.println("Unable to open file '" + filename + "'");
          }
          catch(IOException ex) {
              System.out.println("Error writing file '" + filename + "'");
          }

    }
    public void saveFile(int size, int num, String filename) { //writes file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write("Dictionary size: " + size + "\n" + "Total number of words: "  +num + "\n");
            for (Map.Entry<String, Integer> entry : dictionary.entrySet()) {
                    writer.write(entry.getKey()+" : "+entry.getValue() + "\n");
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
}
