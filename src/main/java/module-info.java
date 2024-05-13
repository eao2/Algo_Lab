module algo.algo_lab {
    requires java.desktop;
    requires com.formdev.flatlaf;
    requires com.formdev.flatlaf.intellijthemes;
    requires java.sql;
    requires mysql.connector.j;


    opens algo.algo_lab to javafx.fxml;
    exports algo.algo_lab;
}