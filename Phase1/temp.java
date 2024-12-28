import java.io.*;

class FileReaderHelper{
    public static final String filename = "/home/aniket/Desktop/SY Module 4/OS/CP Phase1/input2.txt";
    public static BufferedReader br;
    static {
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static String readLine() throws IOException{
        return br.readLine();
    }
}

public class temp extends FileReaderHelper{
    // public static BufferedReader br = new BufferedReader(new FileReader(filename));
    public static final int MAX_MEMORY = 100;
    char[][] M = new char[MAX_MEMORY][4];
    char[] IR = new char[4];
    char[] R = new char[4];
    int IC;
    boolean C;
    int SI;
    StringBuilder buffer = new StringBuilder(40);
    
    public void printMemory(){
        System.out.println("Memory:");    
        for(int i=0;i<MAX_MEMORY;i++){
            for(int j=0;j<4;j++)
                System.out.print(M[i][j]);
            System.out.println();
        }
    }
    public void start_exe() {
        IC = 0;
        while (true) {
            for (int i = 0; i < 4; i++)
                IR[i] = M[IC][i];
            IC++;

            if(IR[0] == 'G' && IR[1] == 'D'){
                SI = 1;
                System.out.println();
                MOS();
            }
            else if(IR[0] == 'P' && IR[1] == 'D'){
                SI = 2;
                System.out.println();
                MOS();
            }
            else if(IR[0] == 'L' && IR[1] == 'R'){
                int i = (IR[2] - 48)*10 + (IR[3] - 48);
                for(int j=0;j<4;j++)
                    R[j] = M[i][j];
                System.out.println("LR, load register");
            }
            else if(IR[0] == 'S' && IR[1] == 'R'){
                int i = (IR[2] - 48)*10 + (IR[3] - 48);
                for(int j=0;j<4;j++)
                M[i][j] = R[j];
                System.out.println("SR, store register");
            }
            else if(IR[0] == 'C' && IR[1] == 'R'){
                int i = (IR[2] - 48)*10 + (IR[3] - 48);
                if(M[i].equals(R))
                C = true;
                else
                C = false;
                System.out.println("CR, compare register");
            }
            else if(IR[0] == 'B' && IR[1] == 'T'){
                if(C){
                    IC = (IR[2] - 48)*10 + (IR[3] - 48);
                    System.out.println("BT, branched-true");
                }
                else
                    System.out.println("BT, not branched-true");
            }
            else if(IR[0] == 'H'){
                SI = 3;
                System.out.println("H, halt");
                MOS();
                break;
            }
            else{
                System.out.println("Invalid instruction");
            }
        }
    }
    
    public void MOS() {
        buffer.setLength(0);
        if(SI == 1){
            System.out.println("GD, Read from input device");
            try {
            String data = readLine();
            System.out.println("**********Data:"+data);
            buffer.append(data);
            int i = (IR[2] - 48) * 10 + (IR[3] - 48);
            int k = 0;
            for (int j = 0; j < buffer.length() && j < 40; j++){
                M[i][k++] = buffer.charAt(j);
                if (k == 4){
                    k = 0;
                    i++;
                }
            }}catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(SI == 2){
            System.out.println("PD, Write to output device: ");
            int i = (IR[2] - 48) * 10 + (IR[3] - 48);
            String output = "";
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 4; k++){
                    if (M[i][k] == 'ඞ') {
                        break;
                    }
                    output += M[i][k];
                }
                i++;
            }
            System.out.println(output);
        }
        else if(SI == 3){
            System.out.println("Halt");
            System.out.println("Execution ended");
        }
        else{
            System.out.println("Invalid interrupt");
        }
    }
    
    public void init(){
        System.out.println("Initialising memory and registers.....");
        for(int i=0;i<MAX_MEMORY;i++){
            for(int j=0;j<4;j++){
                M[i][j] = 'ඞ';
            }
        }
        for(int i=0;i<4;i++){
            IR[i] = 'ඞ';
            R[i] = 'ඞ';
        }
        System.out.println("Memory and registers initialised");
    }
    
    public void load(){
        int block_index = 0;
        // String filename = "input2.txt";
        // File f = new File("/home/aniket/Desktop/SY Module 4/OS/CP Phase1/input2.txt");
        // try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        try{
            String line;
            // int i = 0;
            while ((line = readLine()) != null) {
                System.out.println();
                System.out.println("Line: " + line);
                
                buffer.append(line);
                System.out.println("Buffer:"+ buffer);
                if (buffer.substring(0, 4).equals("$AMJ")) {
                    SI = 1;
                    buffer.setLength(0);
                    init();
                    System.out.println("AMJ, initialised memory and registers");
                } else if (buffer.substring(0, 4).equals("$DTA")) {
                    System.out.println("DTA, started execution");
                    buffer.setLength(0);
                    // br.mark(100);
                    start_exe();
                } else if (buffer.substring(0, 4).equals("$END")) {
                    SI = 3;
                    buffer.setLength(0);
                    // br.close();
                    printMemory();
                    System.out.println("END, ended execution");
                    break;
                } else {
                    if (block_index > MAX_MEMORY){
                        System.out.println("Memory full");
                        break;
                    }
                    int k = 0;
                    for (int i = 0; k < buffer.length(); i++)
                        for (int j = 0; j < 4; j++)
                        {
                            if(buffer.charAt(k) == 'H')
                            {
                                M[i][j] = buffer.charAt(k++);
                                break;
                            }
                            else
                            M[i][j] = buffer.charAt(k++);
                        }   
                        // block_index += 10;
                        buffer.setLength(0);
                    }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        finally{
            System.out.println("Memory loaded");
            printMemory();
        }
    }
    
    public static void main(String[] args) {
        temp job = new temp();
        job.load();
    }
}
