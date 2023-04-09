package ru.m210projects.bafeditor;

public class UserContext {

    private UserContext() {
    }

    private static class SingletonHolder {
        public static final UserContext HOLDER_INSTANCE = new UserContext();
    }

    public static UserContext getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

}
