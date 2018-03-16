import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.*;

public class Classify {
    private JFrame frame;
    public HashMap<String, ArrayList> classifyDictionary;
    public int numberOfSpamMsgs;
    public int numberOfHamMsgs;
    public int numberOfClassifyMsgs;

    public Classify(){
        classifyDictionary = new HashMap<String, ArrayList>();
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

    }
}
