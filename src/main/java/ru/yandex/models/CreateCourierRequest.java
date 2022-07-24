package ru.yandex.models;

import org.apache.commons.lang3.RandomStringUtils;

public class CreateCourierRequest {

    private String login;
    private String password;
    private String firstName;

    public CreateCourierRequest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public static CreateCourierRequest create() {
        String login = RandomStringUtils.randomAlphanumeric(8);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphanumeric(8);
        return new CreateCourierRequest(login, password, firstName);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}