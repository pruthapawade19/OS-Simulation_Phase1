import java.util.*;
import java.io.*;

public class os{
    char[][] M = new char[100][4];
    char[] IR = new char[4];
    char[] R = new char[4];
    int IC;
    boolean C;
    int SI;
    char[] buffer = new char[40];
    
    public void printMemory(){
        System.out.println("Memory:");    
        for(int i=0;i<100;i++){
            for(int j=0;j<4;j++){
                System.out.print(M[i][j]);
            }
            System.out.println();
        }
    }
    public void start_exe() {
        IC = 0;
        while (true) {
            for (int i = 0; i < 4; i++) {
                IR[i] = M[IC][i];
            }
            IC++;

            if(IR[0] == 'G' && IR[1] == 'D'){
                SI = 1;
                System.out.println("GD, read from input device");
                MOS();
            }
            else if(IR[0] == 'P' && IR[1] == 'D'){
                SI = 2;
                System.out.println();
                System.out.println("***************************");
                MOS();
            }
            else if(IR[0] == 'L' && IR[1] == 'R'){
                int i = IR[2] - 48;
                i = i * 10;
                for(int j=0;j<4;j++){
                    R[j] = M[i][j];
                }
                System.out.println("LR, load register");
            }
            else if(IR[0] == 'S' && IR[1] == 'R'){
                int i = IR[2] - 48;
                i = i * 10;
                for(int j=0;j<4;j++){
                    M[i][j] = R[j];
                }
                System.out.println("SR, store register");
            }
            else if(IR[0] == 'C' && IR[1] == 'R'){
                int i = IR[2] - 48;
                i = i * 10;
                if(M[i].equals(R)){
                    C = true;
                }
                else{
                    C = false;
                }
                System.out.println("CR, compare register");
            }
            else if(IR[0] == 'B' && IR[1] == 'T'){
                if(C){
                    IC = (IR[2] - 48) * 10;
                    System.out.println("BT, branched-true");
                }
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
        if(SI == 1){
            // for(int i=0;i<40;i++)
            //     buffer[i] = '\0';
            for(int i=0;i<4;i++){
                IR[i] = 'ඞ';
            }
            System.out.println("Read from input device");
            int x = 0;
            int count = 4;
            int i = IR[2] - 48;
            i = i * 10;
            while (x < buffer.length) {
                for (int y = 0; x < count && x < buffer.length; y++) {
                    R[y] = buffer[x];
                    x++;
                }
                System.out.println("Data read: " + new String(R));
                for (int j = 0; j < 4 && i < M.length; j++) {
                    System.out.print(R[j]);
                    if (R[j] == 'ඞ') {
                        return;
                    }
                    M[i][j] = R[j];
                }
                i++;
                count = count + 4;
            }
        }
        else if(SI == 2){
            for(int i=0;i<4;i++){
                IR[i] = 'ඞ';
            }
            System.out.println("PD, Write to output device: ");
            int i = IR[2] - 48;
            i = i * 10;
            while (i < M.length) {
                for(int j=0;j<4;j++){
                    if (M[i][j] == 'ඞ') {
                        System.out.println();
                        System.out.println("***************************");
                        return;
                    }
                    System.out.print(M[i][j]);
                }
                i++;
            }
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
        for(int i=0;i<100;i++){
            for(int j=0;j<4;j++){
                M[i][j] = 'ඞ';
            }
        }
        for(int i=0;i<4;i++){
            IR[i] = 'ඞ';
            R[i] = 'ඞ';
        }
        IC = 0;
        C = false;
        SI = 0;
        System.out.println("Memory and registers initialised");
        // System.out.println("Memory:");
        // for(int i=0;i<100;i++){
        //     for(int j=0;j<4;j++){
        //         System.out.print(M[i][j]);
        //     }
        //     System.out.println();
        // }
    }
    
    public void load(){
        File f = new File("/home/aniket/Desktop/SY Module 4/OS/CP Phase1/input2.txt");
        int row = 0;
        try (Scanner sc = new Scanner(f)) {
            while(sc.hasNextLine()){
                int count = 0;
                String s = sc.nextLine();
                outer:
                while(true){
                    for(int j=0;j<4;j++){
                        buffer[j] = s.charAt(count);
                        System.out.println("Buffer: " + buffer[j]);
                        if (buffer[j]=='H') {
                            break;
                        }
                        count++;
                    }
                    System.out.println("We got buffer: " + new String(buffer));
                    
                    if (buffer[0]=='$' && buffer[1]=='A' && buffer[2]=='M' && buffer[3]=='J'){
                        SI = 1;
                        init();
                        System.out.println(buffer);
                        System.out.println("AMJ, initialised memory and registers");
                        break;
                    }
                    
                    else if (buffer[0]=='$' && buffer[1]=='D' && buffer[2]=='T' && buffer[3]=='A'){
                        count = 0;
                        s = sc.nextLine();
                        for(int j=0;j<40;j++){
                            if (count < s.length()) {
                                buffer[j] = s.charAt(count);
                                System.out.println("Buffer: " + buffer[j]);
                                count++;
                            } else {
                                buffer[j] = 'ඞ';
                                break;
                            }
                        }
                        
                        System.out.println("DTA, started execution");
                        start_exe();
                        break;
                    }
                    
                    else if (buffer[0]=='$' && buffer[1]=='E' && buffer[2]=='N' && buffer[3]=='D'){
                        SI = 3;
                        sc.close();
                        printMemory();
                        MOS();
                        System.out.println("END, ended execution");
                        break;
                    }
                    
                    else{
                        for(int j=0;j<4;j++){
                            System.out.print(buffer[j]);
                            M[row][j] = buffer[j];
                            if (buffer[j]=='H') {
                                row++;
                               break outer;
                               //    break;
                            }
                            if (buffer[j]=='ඞ') {
                               break outer;
                               //    break;
                            }
                        }
                        row++;
                        System.out.println();
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Error");
            System.out.println(e);
        }
        System.out.println("\nMemory loaded");
        printMemory();
    }
    
    public static void main(String[] args) {
        os job = new os();
        job.load();
    }
}