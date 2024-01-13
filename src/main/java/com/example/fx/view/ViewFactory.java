package com.example.fx.view;

import java.util.HashMap;
import java.util.Map;

public class ViewFactory {
    private static final Map<ViewType, BasicView> viewMap = new HashMap<>();

    public static BasicView createView(ViewType type) {
        switch (type) {
            case MAIN:
                return viewMap.computeIfAbsent(type, k -> new MainView());
            case LOGIN: {
                LoginView loginView = (LoginView) viewMap.computeIfAbsent(type, k -> new LoginView());
                loginView.refreshCaptureImage();
                return loginView;
            }
            case SCHEDULE:
                return viewMap.computeIfAbsent(type, k -> new ScheduleView(false));
            case ADMIN:
                AdminLoginView adminLoginView = (AdminLoginView) viewMap.computeIfAbsent(type, k -> new AdminLoginView());
                adminLoginView.refreshCaptureImage();
                return adminLoginView;
            case ADMIN_SCHEDULE:
                return viewMap.computeIfAbsent(type, k -> new ScheduleView(true));
            case TEACHER:
                return viewMap.computeIfAbsent(type, k -> new TeacherView());
            default:
                throw new IllegalArgumentException("Invalid UI Type");
        }
    }

    public static void show(ViewType type) {
        createView(type).show();
    }

    public static void close(ViewType type) {
        createView(type).close();
    }
}
