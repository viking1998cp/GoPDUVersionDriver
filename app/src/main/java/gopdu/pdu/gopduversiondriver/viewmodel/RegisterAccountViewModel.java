package gopdu.pdu.gopduversiondriver.viewmodel;

import gopdu.pdu.gopduversiondriver.modelresponse.RegisterAccountResponse;

public class RegisterAccountViewModel{
    RegisterAccountResponse callback;

    public RegisterAccountViewModel(RegisterAccountResponse callback) {
        this.callback = callback;
    }


    public void checkExits(boolean exits){
       if(!exits){
           callback.onNotExits();
       }else {
           callback.onExits();
       }
    }
    public void checkEmpty(String name, String phone, String birthdate, String gender, String licenseplates) {
        boolean check = true;
        if (name.equals("")) {
            check = false;
        }
        if (birthdate.equals("")) {
            check = false;
        }
        if (phone.equals("")) {
            check = false;
        }
        if (gender.equals("")) {
            check = false;
        }
        if (licenseplates.equals("")) {
            check = false;
        }
        if(!check){
            callback.onEmpty();
        }else {
            callback.onNotEmpty();
        }
    }


}
