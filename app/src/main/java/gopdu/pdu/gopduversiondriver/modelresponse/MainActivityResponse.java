package gopdu.pdu.gopduversiondriver.modelresponse;

import java.util.Map;

public interface MainActivityResponse {
    void nullPhoneNumber();

    void notNullPhoneNumber(Map<String, String> param);

    void avalidAccount();

    void notAvalidAccount(String message);

    void logged();

    void unLogged();
}
