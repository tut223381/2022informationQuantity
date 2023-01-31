package s4.B223312;

import java.util.Arrays;
import s4.specification.*;

/*
interface FrequencerInterface {  // This interface provides the design for frequency counter.
    void setTarget(byte[] target);  // set the data to search.
    void setSpace(byte[] space);  // set the data to be searched target from.
    int frequency(); // It return -1, when TARGET is not set or TARGET's length is zero
                     // Otherwise, it return 0, when SPACE is not set or Space's length is zero
                     // Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e. target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
}
*/

public class Frequencer implements FrequencerInterface {
    static boolean debugMode = false;
    byte[] myTarget;
    byte[] mySpace;

    SuffixArray suffixArray;

    public void setSpace(byte[] space) {
        mySpace = space;
        suffixArray = new SuffixArray(space);
    }
    public void setTarget(byte[] target) {
        myTarget = target;
    }

    private void showVariables() {
        for (int i = 0; i < mySpace.length; i++) {
            System.out.write(mySpace[i]);
        }
        System.out.write(' ');
        for (int i = 0; i < myTarget.length; i++) {
            System.out.write(myTarget[i]);
        }
        System.out.write(' ');
    }
    public void printSuffixArray() {
        suffixArray.printSuffixArray();
    }

    public int frequency() {
        if (myTarget == null || myTarget.length == 0) return -1;
        if (mySpace == null) return 0;
        
        if (debugMode) {
            showVariables();
        }

        return subByteFrequency(0, myTarget.length);
    }

    public int subByteFrequency(int start, int length) {
        byte[] subByte = Arrays.copyOfRange(myTarget, start, length);
        int first = suffixArray.searchLowerBound(subByte);
        int last = suffixArray.searchUpperBound(subByte);
        return last - first;
    }

    public static void main(String[] args) {
        Frequencer frequencerObject;
        try { // テストに使うのに推奨するmySpaceの文字は、"ABC", "CBA", "HHH", "Hi Ho Hi Ho".
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();
            /* Example from "Hi Ho Hi Ho"    
               0: Hi Ho                      
               1: Ho                         
               2: Ho Hi Ho                   
               3:Hi Ho                       
               4:Hi Ho Hi Ho                 
               5:Ho                          
               6:Ho Hi Ho
               7:i Ho                        
               8:i Ho Hi Ho                  
               9:o                           
              10:o Hi Ho                     
            */

            frequencerObject.setTarget("H".getBytes());
            int first = frequencerObject.suffixArray.searchLowerBound(frequencerObject.myTarget);
            int last = frequencerObject.suffixArray.searchUpperBound(frequencerObject.myTarget);
            System.out.print("Search[" + first + ", " + last + ") ");
            if (4 == first && 8 == last) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}
