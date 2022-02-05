package test;

import me.errordev.mslogin.MSLogin;

public class Main {

    public static void main(String[] args) {
        MSLogin.INSTANCE.setHandler((uuid, username, accessToken, clientToken) -> {
            System.out.println(uuid + ", " + username + ", " + accessToken + ", " + clientToken);
        });
        MSLogin.INSTANCE.start();
    }
}