package gopdu.pdu.gopduversiondriver.modelresponse;

public interface IdentityCardImageResponse {
    void onIdentityCardImageFront();
    void onIdentityCardImageBackside();
    void nullImage();
    void notNullImage();
}
