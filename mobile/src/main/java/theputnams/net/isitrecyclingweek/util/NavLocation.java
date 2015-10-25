package theputnams.net.isitrecyclingweek.util;

import java.lang.reflect.Field;

public enum NavLocation {
    ABOUT("About"),
    SETTINGS("Settings"),
    RECYCLING_INFO("Recycling info");

    NavLocation(String name) {
        try {
            Field fieldName = getClass().getSuperclass().getDeclaredField("name");
            fieldName.setAccessible(true);
            fieldName.set(this, name);
            fieldName.setAccessible(false);
        } catch (Exception e) {}
    }
}
