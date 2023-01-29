package s4.B223323; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;

import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    private final FrequencerInterface myFrequencer = new Frequencer();  // Object for counting frequency
    private boolean mySpaceNotReady = true;
    private boolean myTargetNotReady = true;

    //定数群
    private static final double C0 = 1/Math.log10(2d);
    private static final int INT_MAX = Integer.MAX_VALUE;
    private static final double DOUBLE_MAX = Double.MAX_VALUE;

    public final int INITIAL_MAX_TARGET_LEN = 10;                  //memoの初期サイズがこの値で決まる
    private double C1 = 0;                                         //log10(mySpace.length)
    private int len;                                               //target.length
    private int max_target_len = INITIAL_MAX_TARGET_LEN;           //memoのサイズを管理

    /* ある長さまでで最小のfreqの総積とその時の長さを保持する。
     * targetを"abc"とする 0~1 の各要素は以下のようになる。
     ------------------------------------------------------
     |     0     |                   1                    |
     | freq("a") | min(freq("ab"), freq("a") * freq("b")) |
     ------------------------------------------------------
     * 要素2(len-1)は定数、3,4(len ~ 2*len-1)の各要素は要素1(1~len-2)を求める際にfreqを計算した回数+1である。
     ------------------------------------------------
     |  2 | 3 |          4                          |
     |  1 | 2 | 要素1が"ab"ならば0, "a" * "b"ならば1 |
     ------------------------------------------------*/
    private int[] memo = new int[(INITIAL_MAX_TARGET_LEN << 1) - 1];

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    private final double iq(int freq) {
        return (-Math.log10((double) freq) + C1) / Math.log10(2d);
    }

    public final void setTarget(byte [] target) { 
        len = target.length; //このクラスで直接必要なtargetの情報はこれだけ
        if (len == 0) {
            myTargetNotReady = true; 
        } else {
            myTargetNotReady = false;
            myFrequencer.setTarget(target); //targetの内容はmyFrequencerで処理する。
            if (max_target_len < len) { //targetの長さがmax_target_lenより長い場合はmemoサイズを拡張する。
                max_target_len = len;
                memo = new int[(len << 1) - 1];
            }
        }
    }
    public final void setSpace(byte []space) {
        mySpaceNotReady = space.length == 0;
        if (mySpaceNotReady) return;
        myFrequencer.setSpace(space);
        C1 = Math.log10((double) space.length);
    }

    /*
     * iqの計算式を変更している
     * 注:Σは総和,Πは総積を表す
     * Σ(-log(freq/mySpace.len) / log(2d))
     *  ↓
     * Σ((log(mySpace.len) - log(freq)) / log(2d))
     *  ↓
     * (n * log(mySpace.len) - log(Π(freq))) / log(2d)
     * nはfreqを掛ける回数。
     */
    public final double estimation() {
        if (myTargetNotReady) return 0d; // returns 0.0 when the target is not set or Target's length is zero;
        if (mySpaceNotReady) return DOUBLE_MAX; // It returns Double.MAX_VALUE, when the true value is infinite, or the space is not set.

        int min = myFrequencer.subByteFrequency(0, 1);
        if (len == 1) return (min == 0) ? DOUBLE_MAX : (C1 - Math.log10(min)) * C0; //targetの長さが1の場合はここで判定。

        int end = 2, start = 1, mp = 1;
        
        //memoの初期化(他の値は使用される前に上書きされる)
        memo[len - 1] = 1;
        memo[len] = 2;
        memo[0] = (min == 0) ? INT_MAX: min;

        for (;;) { // while(true)
            //final int mp = end - 1;
            //int start = mp;
            
            min = myFrequencer.subByteFrequency(0, end); //メモから足す必要がないためループ外で処理
            int p = -1;
            if (min == 0) min = INT_MAX;
            
            do {
                //targetをendまでとしたときのfreqの総積が最も小さくなるものをもとめる。
                //minはmin(freq("abc"), freq("a") * freq("bc"), freq("ab") * freq("c"))
                //pはその時の使ったmemoのアドレス(memo[p+len]がfreqを掛けた回数を示す)
                int freq = myFrequencer.subByteFrequency(start, end);
                if (freq == 0) break;
                int iq = freq * memo[--start];
                if (iq < min) { min = iq; p = start; }
            } while (start > 0);

            if (end == len)
                /*
                 * iq = (n * log(mySpace.len) - log(Π(freq))) / log(2d)
                 * C0 = 1/log(2d)
                 * C1 = log(mySpace.len)
                 * memo[p + len] = n
                 * min = Π(freq)
                 */
                return min == INT_MAX ? DOUBLE_MAX: (C1 * memo[p + len] - Math.log10((double) min)) * C0;

            //メモの更新
            memo[mp + len] = memo[p + len] + 1;
            memo[mp] = min;

            mp = start = end++;
        }
    }

    private final double slowEstimation(){
        if (myTargetNotReady) return 0d;
        if (mySpaceNotReady) return DOUBLE_MAX;

        boolean [] partition = new boolean[len+1];
        int np;
        np = 1<<(len-1);
        // System.out.println("np="+np+" length="+len);
        double value = Double.MAX_VALUE; // value = mininimum of each "value1".

        for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
            // binary representation of p forms partition.
            // for partition {"ab" "cde" "fg"}
            // a b c d e f g   : myTarget
            // T F T F F T F T : partition:
            partition[0] = true; // I know that this is not needed, but..
            for(int i=0; i<len -1;i++) {
            partition[i+1] = (0 !=((1<<i) & p));
            }
            partition[len] = true;

            // Compute Information Quantity for the partition, in "value1"
            // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
            double value1 = (double) 0.0;
            int end = 0;
            int start = end;
            while(start<len) {
                // System.out.write(myTarget[end]);
                end++;
                while(partition[end] == false) { 
                    // System.out.write(myTarget[end]);
                    end++;
                }
                // System.out.print("("+start+","+end+")");
                int freq = myFrequencer.subByteFrequency(start, end);
                value1 = freq == 0 ? Double.MAX_VALUE : value1 + iq(freq);
                
                start = end;
            }
            // System.out.println(" "+ value1);

            // Get the minimal value in "value"
            if(value1 < value) value = value1;
        }
        return value;
    }

    private final double check() {
        double fast = estimation();
        double slow = slowEstimation();
        if (Math.abs(fast - slow) > 1e-15) System.out.println("ERROR: WRONG\nfast:" + fast + "\nslow:" + slow);
        return fast;
    }

    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.check();
        System.out.println(">0 "+value);
        myObject.setTarget("01".getBytes());
        value = myObject.check();
        System.out.println(">01 "+value);
        myObject.setTarget("0123".getBytes());
        value = myObject.check();
        System.out.println(">0123 "+value);
        myObject.setTarget("00".getBytes());
        value = myObject.check();
        System.out.println(">00 "+value);
        myObject.setTarget("4".getBytes());
        value = myObject.check();
        System.out.println(">4 "+value);
        myObject.setTarget("321".getBytes());
        value = myObject.check();
        System.out.println(">321 "+value);
        myObject.setTarget("3210".getBytes());
        value = myObject.check();
        System.out.println(">3210 "+value);
        myObject.setTarget("32121".getBytes());
        value = myObject.check();
        System.out.println(">32121 "+value);
        myObject.setTarget("31313131".getBytes());
        value = myObject.check();
        System.out.println(">31313131 "+value);
    }
}