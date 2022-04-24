package de.miao.jaymod.util;

import net.minecraft.client.util.Session;

import java.util.Optional;

public class SessionUtil {

    private static Session newSession;
    public static boolean defaultSession = true;

    public static Session getNewSession() {
        return newSession;
    }


    public static void setNewSession(Session session) {
        newSession = session;
    }

    public static void changeCrackedName(String newName) {
        Session session = new Session(newName, "", "", Optional.empty(),
                Optional.empty(), Session.AccountType.MOJANG);

        newSession = session;
    }
}
