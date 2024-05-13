package algo.algo_lab;

import java.io.Serializable;

public class ObjectLogin implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int value1;
    private final String value2;
    private final String value3;
    private final String value4;


    public ObjectLogin(int value1, String value2, String value3, String value4) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public int getValue1() { return value1; }

    public String getValue2() {
        return value2;
    }

    public String getValue3() { return value3; }
    public String getValue4() { return value4; }

    @Override
    public String toString() {
        return "ObjectLogin{" +
                "value1='" + value1 + '\'' +
                ", value2=" + value2 + '\'' +
                ", value2=" + value3 + '\'' +
                ", value2=" + value4 +
                '}';
    }
}
