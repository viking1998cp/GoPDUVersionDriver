package gopdu.pdu.gopduversiondriver.view;

import java.util.Map;

public interface ViewMainActivityListener {
    void nullPhoneNumber();
    void notNullPhoneNumber(Map<String, String> param);
    void notAvalidAccount(String message);
    void avalidAccount();
    void logged();
    void unLogged();

}
