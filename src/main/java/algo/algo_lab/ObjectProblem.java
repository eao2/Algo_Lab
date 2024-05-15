package algo.algo_lab;

import java.io.Serializable;

public class ObjectProblem implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String value1;
    private final String value2;
    private final int value3;
    private final int value4;
    private final int value5;
    private final int value6;
    private final int value7;
    private final int value8;

    public ObjectProblem(String value1, String value2, int value3, int value4, int value5, int value6, int value7, int value8) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
    }

    public String getValue1() {
        return value1;
    }
    public String getValue2() {
        return value2;
    }
    public int getValue3() {
        return value3;
    }
    public int getValue4() {
        return value4;
    }
    public int getValue5() {
        return value5;
    }
    public int getValue6() {
        return value6;
    }
    public int getValue7() {
        return value7;
    }
    public int getValue8() {
        return value8;
    }

    @Override
    public String toString() {
        return "ObjectLogin{" +
                "value1='" + value1 + '\'' +
                ", value2=" + value2 +'\'' +
                ", value3=" + value3 +'\'' +
                ", value4=" + value4 +'\'' +
                ", value5=" + value5 +'\'' +
                ", value6=" + value6 +'\'' +
                ", value7=" + value7 +'\'' +
                ", value8=" + value8 +
                '}';
    }
}
