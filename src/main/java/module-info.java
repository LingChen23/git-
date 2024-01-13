module com.example.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires java.sql.rowset;
    requires java.datatransfer;
    requires org.apache.commons.codec;
    requires kaptcha;
    requires java.desktop;

    opens com.example.fx to javafx.fxml;
    exports com.example.fx;
    exports com.example.fx.view;
    opens com.example.fx.view to javafx.fxml;
    exports com.example.fx.util;
    opens com.example.fx.util to javafx.fxml;
}
