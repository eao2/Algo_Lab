package algo.algo_lab;

import java.io.Serializable;

public class ObjectProblem implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String value1;
    private final String value2;
    private final String value3;

    public ObjectProblem(String value1, String value2, String value3) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
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
                ", value2=" + value3 +
                '}';
    }
}
