package test;

import me.errordev.mslogin.MSLogin;

public class Main {

    public static void main(String[] args) {
        MSLogin.INSTANCE.setHandler((uuid, username, accessToken) -> {
            System.out.println(uuid + ", " + username + ", " + accessToken);
        });
        MSLogin.INSTANCE.start();
    }
}