package com.example.eventmanager;

public class ActivityHolder {
    private static WelcomeActivity activityA;

    public static void setActivityA(WelcomeActivity activity) {
        activityA = activity;
    }

    public static WelcomeActivity getActivityA() {
        return activityA;
    }
}

