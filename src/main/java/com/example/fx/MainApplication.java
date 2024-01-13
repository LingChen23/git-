package com.example.fx;

import com.example.fx.view.ViewFactory;
import com.example.fx.view.ViewType;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        ViewFactory.show(ViewType.MAIN);
    }
}
