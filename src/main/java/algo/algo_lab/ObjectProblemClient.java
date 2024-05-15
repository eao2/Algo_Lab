package algo.algo_lab;

import java.io.Serializable;

public class ObjectProblemClient implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String value1;
    private final String value2;
    private final String value3;
//    private final int value4;
//    private final int value5;
//    private final int value6;
//    private final int value7;
//    private final int value8;

    public ObjectProblemClient(String value1, String value2, String value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
//        this.value4 = value4;
//        this.value5 = value5;
//        this.value6 = value6;
//        this.value7 = value7;
//        this.value8 = value8;
    }

    public String getValue1() {
        return value1;
    }
    public String getValue2() {
        return value2;
    }
    public String getValue3() {
        return value3;
    }

    @Override
    public String toString() {
        return "ObjectLogin{" +
                "value1='" + value1 + '\'' +
                ", value2=" + value2 +'\'' +
                ", value3=" + value3 +'\'' +
                '}';
    }
}
