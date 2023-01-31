package s4.B223365;  // ここは、かならず、自分の名前に変えよ。 import java.lang.*;
import s4.specification.*;


/*package s4.specification;
  ここは、１回、２回と変更のない外部仕様である。
  public interface FrequencerInterface {     // This interface provides the design for frequency counter.
  void setTarget(byte  target[]); // set the data to search.
  void setSpace(byte  space[]);  // set the data to be searched target from.
  int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
  //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
  //Otherwise, get the frequency of TAGET in SPACE
  int subByteFrequency(int start, int end);
  // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
  // For the incorrect value of START or END, the behavior is undefined.
  }
*/



public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。


    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a integer, which is the starting position in mySpace.

    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.

    // この関数は、デバッグに使ってもよい。mainから実行するときにも使ってよい。
    // リポジトリにpushするときには、mainメッソド以外からは呼ばれないようにせよ。
    //
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
                System.out.printf("suffixArray[%2d]=%2d:", i, s);
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }

    private int suffixCompare(int i, int j) {
        // suffixCompareはソートのための比較メソッドである。
        // 次のように定義せよ。
        //
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD",
        // suffix_2 is "CD", and sufffix_3 is "D".
        // Each i and j denote suffix_i, and suffix_j.
        // Example of dictionary order
        // "i"      <  "o"        : compare by code
        // "Hi"     <  "Ho"       ; if head is same, compare the next element
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big
        //
        //The return value of "int suffixCompare" is as follows.
        // if suffix_i > suffix_j, it returns 1
        // if suffix_i < suffix_j, it returns -1
        // if suffix_i = suffix_j, it returns 0;

        // ここにコードを記述せよ
        int suffix_i = i;
        int suffix_j = j;
        byte s_i,s_j;
        int comp_pos;
        for(comp_pos = 0; comp_pos < mySpace.length - suffix_i; comp_pos++){
            if ( (comp_pos+suffix_j) >= mySpace.length ) return 1;
            s_i = mySpace[ suffix_i + comp_pos ];
            s_j = mySpace[ suffix_j + comp_pos ];
            if( s_i > s_j ) return 1;
            else if( s_i < s_j ) return -1;
        }
        if( ( comp_pos + suffix_j ) < mySpace.length ) return -1;
        return 0;
    }

    public void setSpace(byte []space) {
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.
        }
        //
        // ここに、int suffixArrayをソートするコードを書け。
        // もし、mySpace が"ABC"ならば、
        // suffixArray = { 0, 1, 2} となること求められる。
        // このとき、printSuffixArrayを実行すると
        //   suffixArray[ 0]= 0:ABC
        //   suffixArray[ 1]= 1:BC
        //   suffixArray[ 2]= 2:C
        // のようになるべきである。
        // もし、mySpace が"CBA"ならば
        // suffixArray = { 2, 1, 0} となることが求めらる。
        // このとき、printSuffixArrayを実行すると
        //   suffixArray[ 0]= 2:A
        //   suffixArray[ 1]= 1:BA
        //   suffixArray[ 2]= 0:CBA
        // のようになるべきである。

        // クイックソート
        // 参考 Web : https://talavax.com/quicksort.html#gsc.tab=0
        // 参考 Web : https://qiita.com/gigegige/items/4817c27314a2393eb02d
        quick_sort( 0, mySpace.length-1 );

    }

    private void quick_sort(int left, int right){
        if( left >= right ) return;

        int pivot;
        pivot = (left+right)/2;

        int l = left;
        int r = right;
        while( l <= r ){
            while( suffixCompare( suffixArray[pivot], suffixArray[l] ) == 1) l++;
            while( suffixCompare( suffixArray[pivot], suffixArray[r] ) == -1) r--;
            if( l <= r ){
                int tmp;
                if( l == pivot ) pivot = r;
                if( r == pivot ) pivot = l;
                tmp = suffixArray[l];
                suffixArray[l] = suffixArray[r];
                suffixArray[r] = tmp;
                l++;
                r--;
            }
        }

        quick_sort(left, r );
        quick_sort(l, right );
    }


    public void buble_setSpace(byte []space) {
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.
        }
        //
        // ここに、int suffixArrayをソートするコードを書け。
        // もし、mySpace が"ABC"ならば、
        // suffixArray = { 0, 1, 2} となること求められる。
        // このとき、printSuffixArrayを実行すると
        //   suffixArray[ 0]= 0:ABC
        //   suffixArray[ 1]= 1:BC
        //   suffixArray[ 2]= 2:C
        // のようになるべきである。
        // もし、mySpace が"CBA"ならば
        // suffixArray = { 2, 1, 0} となることが求めらる。
        // このとき、printSuffixArrayを実行すると
        //   suffixArray[ 0]= 2:A
        //   suffixArray[ 1]= 1:BA
        //   suffixArray[ 2]= 0:CBA
        // のようになるべきである。

        // バブルソート
        int che = 0;
        for(int i=0; i < mySpace.length - 1; i++){
            for(int j=i+1; j < mySpace.length; j++){
                if( suffixCompare( suffixArray[i], suffixArray[j] ) == 1){
                    int tmp;
                    tmp = suffixArray[i];
                    suffixArray[i] = suffixArray[j];
                    suffixArray[j] = tmp;
                }
            }
        }

    }

    // ここから始まり、指定する範囲までは変更してはならないコードである。

    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }

    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }

    public int subByteFrequency(int start, int end) {
        // start, and end specify a string to search in myTarget,
        // if myTarget is "ABCD",
        //     start=0, and end=1 means string "A".
        //     start=1, and end=3 means string "BC".
        // This method returns how many the string appears in my Space.
        //
        /* This method should be work as follows, but much more efficient.
           int spaceLength = mySpace.length;
           int count = 0;
           for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false;
            for(int i = 0; i< (end - start); i++) {
             if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
           }
        */
        // The following the counting method using suffix array.
        // 演習の内容は、適切なsubByteStartIndexとsubByteEndIndexを定義することである。
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);

        //int first = slow_subByteStartIndex(start, end);
        //int last1 = slow_subByteEndIndex(start, end);
        return last1 - first;
    }
    // 変更してはいけないコードはここまで。

    public int slowsubByteFrequency(int start, int end){
        int spaceLength = mySpace.length;
        int count = 0;
        for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false;
            for(int i = 0; i< (end - start); i++) {
                if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
        }
        return count;
    }

    private int targetCompare(int i, int j, int k) {
        // subByteStartIndexとsubByteEndIndexを定義するときに使う比較関数。
        // 次のように定義せよ。
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // When mySpace is "ABCD", suffix_0 is "ABCD", suffix_1 is "BCD",
        // suffix_2 is "CD", and sufffix_3 is "D".
        // target_j_k is a string in myTarget start at j-th postion ending k-th position.
        // if myTarget is "ABCD",
        //     j=0, and k=1 means that target_j_k is "A".
        //     j=1, and k=3 means that target_j_k is "BC".
        // This method compares suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_j_k, it return 0.
        // if suffix_i > target_j_k it return 1;
        // if suffix_i < target_j_k it return -1;
        // if first part of suffix_i is equal to target_j_k, it returns 0;
        //
        // Example of search
        // suffix          target
        // "o"       >     "i"
        // "o"       <     "z"
        // "o"       =     "o"
        // "o"       <     "oo"
        // "Ho"      >     "Hi"
        // "Ho"      <     "Hz"
        // "Ho"      =     "Ho"
        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        // "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
        // The behavior is different from suffixCompare on this case.
        // For example,
        //    if suffix_i is "Ho Hi Ho", and target_j_k is "Ho",
        //            targetCompare should return 0;
        //    if suffix_i is "Ho Hi Ho", and suffix_j is "Ho",
        //            suffixCompare should return 1. (It was written -1 before 2021/12/21)
        //
        // ここに比較のコードを書け
        int suffix_i = i;
        int target_j_k = j;
        byte s_i,t_j_k;
        int comp_pos;
        for(comp_pos = 0; comp_pos < (k-j); comp_pos++){
            if( (suffix_i + comp_pos) >= mySpace.length ) return -1;// targetの方が長い
            s_i = mySpace[ suffix_i + comp_pos ];
            t_j_k = myTarget[ target_j_k + comp_pos ];
            if( s_i > t_j_k ) return 1; // suffix_iの方が辞書順で遅い
            else if( s_i < t_j_k ) return -1; // target_j_kの方が辞書順で遅い
        }
        // suffix_iとtarget_j_kが辞書順で等しい
        return 0;
    }


    private int subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
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

        // It returns the index of the first suffix
        // which is equal or greater than target_start_end.
    // Suppose target is set "Ho Ho Ho Ho"
        // if start = 0, and end = 2, target_start_end is "Ho".
        // if start = 0, and end = 3, target_start_end is "Ho ".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 5.
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho ", it will return 6.
        //
        // ここにコードを記述せよ。
        //
        int suffix_i;
        int comp_result;
        int s,e;
        int pivot;

        s = 0;
        e = mySpace.length;
        pivot = 0;
        while( s < e ){
            pivot = s + (e - s)/2;
            suffix_i = suffixArray[ pivot ];
            comp_result = targetCompare( suffix_i, start, end );
            if( comp_result == 1 ) e = pivot;
            else if( comp_result == -1 ) s = pivot+1;
            else e = pivot;
        }
        //System.out.printf("s %d, pivot %d, e %d\n", s, pivot, e);
        return s;
    }

    private int slow_subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
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

        // It returns the index of the first suffix
        // which is equal or greater than target_start_end.
    // Suppose target is set "Ho Ho Ho Ho"
        // if start = 0, and end = 2, target_start_end is "Ho".
        // if start = 0, and end = 3, target_start_end is "Ho ".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 5.
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho ", it will return 6.
        //
        // ここにコードを記述せよ。
        //
        int suffix_pos;
        int suffix_i;
        int comp_result;

        for( suffix_pos = 0; suffix_pos < mySpace.length; suffix_pos++){
            suffix_i = suffixArray[ suffix_pos ];
            comp_result = targetCompare( suffix_i, start, end );
            //System.out.printf("comp_result : %d\n", comp_result);

            if( comp_result == 0 ) break;
        }
        return suffix_pos;
    }


    private int subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
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
        // It returns the index of the first suffix
        // which is greater than target_start_end; (and not equal to target_start_end)
    // Suppose target is set "High_and_Low",
        // if start = 0, and end = 2, target_start_end is "Hi".
        // if start = 1, and end = 2, target_start_end is "i".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".
        // ** suffixArrayの添字+1を返している?
        //
        //　ここにコードを記述せよ
        int suffix_i;
        int comp_result;
        int s,e;
        int pivot;
        int ct;

        s = 0;
        e = mySpace.length;
        pivot = 0;
        while( s < e ){
            pivot = s + (e - s)/2;
            suffix_i = suffixArray[ pivot ];
            comp_result = targetCompare( suffix_i, start, end );
            if( comp_result == 1 ) e = pivot;
            else if( comp_result == -1 ) s = pivot+1;
            else s = pivot+1;
        }
        //System.out.printf("s %d, pivot %d, e %d\n", s, pivot, e);
        return e;
    }

    private int slow_subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        // The meaning of start and end is the same as subByteFrequency.
        /* Example of suffix created from "Hi Ho Hi Ho"
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
        // It returns the index of the first suffix
        // which is greater than target_start_end; (and not equal to target_start_end)
    // Suppose target is set "High_and_Low",
        // if start = 0, and end = 2, target_start_end is "Hi".
        // if start = 1, and end = 2, target_start_end is "i".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".
        // Assuming the suffix array is created from "Hi Ho Hi Ho",
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".
        // ** suffixArrayの添字+1を返している?
        //
        //　ここにコードを記述せよ
        //
        int suffix_pos;
        int suffix_i;
        int comp_result;

        for( suffix_pos = mySpace.length-1; suffix_pos >= 0; suffix_pos--){
            suffix_i = suffixArray[ suffix_pos ];
            comp_result = targetCompare( suffix_i, start, end );
            //System.out.printf("comp_result : %d\n", comp_result);

            if( comp_result == 0 ) break;
        }
        if( suffix_pos < 0 ) return suffixArray.length;
        return suffix_pos+1;
    }


    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try { // テストに使うのに推奨するmySpaceの文字は、"ABC", "CBA", "HHH", "Hi Ho Hi Ho".
            // Test ABC
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("ABC".getBytes());
            frequencerObject.printSuffixArray();

            // Test CBA
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("CBA".getBytes());
            frequencerObject.printSuffixArray();

            // Test HHH
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("HHH".getBytes());
            frequencerObject.printSuffixArray();

            // Test : mySpace … Ho Hi Ho, myTarget … Ho
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Ho Hi Ho".getBytes());
            frequencerObject.setTarget("Ho".getBytes());
            frequencerObject.printSuffixArray();

            // Test Hi Ho Hi Ho
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


//          // targetCompare Test
//          int comp_result;
//          /// o,i
//            frequencerObject = new Frequencer();
//            frequencerObject.setSpace("o".getBytes());
//            frequencerObject.setTarget("i".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length) ;
//            if(1 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// o,z
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("o".getBytes());
//            frequencerObject.setTarget("z".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length);
//            if(-1 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// o,o
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("o".getBytes());
//            frequencerObject.setTarget("o".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length);
//            if(0 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// o,oo
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("o".getBytes());
//            frequencerObject.setTarget("oo".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length);
//            if(-1 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// Ho,Hi
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("Ho".getBytes());
//            frequencerObject.setTarget("Hi".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length);
//            if(1 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// Ho,Hz
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("Ho".getBytes());
//            frequencerObject.setTarget("Hz".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length);
//            if(-1 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// Ho,Ho
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("Ho".getBytes());
//            frequencerObject.setTarget("Ho".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length);
//            if(0 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// Ho,"Ho "
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("Ho".getBytes());
//            frequencerObject.setTarget("Ho ".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length);
//            if(-1 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// Ho,H
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("Ho".getBytes());
//            frequencerObject.setTarget("H".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length);
//            if(0 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// Ho Hi Ho,Ho
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("Ho Hi Ho".getBytes());
//            frequencerObject.setTarget("Ho".getBytes());
//          comp_result = frequencerObject.targetCompare(0,0,frequencerObject.myTarget.length);
//            if(0 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
//
//          /// Ho [Hi] Ho,Hi
//          frequencerObject = new Frequencer();
//            frequencerObject.setSpace("Ho Hi Ho".getBytes());
//            frequencerObject.setTarget("Hi".getBytes());
//          comp_result = frequencerObject.targetCompare(3,0,frequencerObject.myTarget.length);
//            if(0 == comp_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }


            //
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex

            //subByteStartIndex, Test
            int start_result;
            /// mySpace 「Hi Ho Hi Ho」, myTarget 「Ho Ho Ho Ho」, search 0~2
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();
            frequencerObject.setTarget("Ho Ho Ho Ho".getBytes());
            start_result = frequencerObject.subByteStartIndex(0,2);
            if(5 == start_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            /// mySpace 「Hi Ho Hi Ho」, myTarget 「Ho Ho Ho Ho」, search 0~3
            start_result = frequencerObject.subByteStartIndex(0,3);
            if(6 == start_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            /// mySpace 「Hi Ho Hi Ho」, myTarget 「Ho Ho Ho Ho」, search 2~5
            start_result = frequencerObject.subByteStartIndex(2,5);
            if(1 == start_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            /// mySpace 「Hi Ho Hi Ho」, myTarget 「Ho Ho Ho Ho」, search 1~3
            start_result = frequencerObject.subByteStartIndex(1,3);
            if(10 == start_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }


            //subByteEndIndex, Test
            int end_result;
            /// mySpace 「Hi Ho Hi Ho」, myTarget 「High_and_Low」, search 0~2
            frequencerObject.setTarget("High_and_Low".getBytes());
            end_result = frequencerObject.subByteEndIndex(0,2);
            if(5 == end_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            /// mySpace 「Hi Ho Hi Ho」, myTarget 「High_and_Low」, search 0~2
            end_result = frequencerObject.subByteEndIndex(1,2);
            if(9 == end_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            /// mySpace 「Hi Ho Hi Ho」, myTarget 「High_and_Low」, search 11~12
            end_result = frequencerObject.subByteEndIndex(10,11);
            if(11 == end_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            /// mySpace 「Hi Ho Hi Ho」, myTarget 「Ho Ho Ho Ho」, search 11~12
            frequencerObject.setTarget("Ho Ho Ho Ho".getBytes());
            end_result = frequencerObject.subByteEndIndex(0,2);
            if(7 == end_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }


            // Frequency, Test
            // mySpace … Hi Ho Hi Ho, myTarget … H
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());

            frequencerObject.setTarget("H".getBytes());

            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }


            // Frequency vs slowsubByteFrequency
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray();


            /// Test mySpace … Hi Ho Hi Ho, myTarget … H
            frequencerObject.setTarget("H".getBytes());

            int Freq_result = frequencerObject.frequency();
            System.out.print("Freq = "+ Freq_result+" ");
            if(4 == Freq_result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

            int slow_result = frequencerObject.slowsubByteFrequency(0, frequencerObject.myTarget.length);
            System.out.print("Freq = "+ slow_result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }

        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("STOP");
        }
    }
}

