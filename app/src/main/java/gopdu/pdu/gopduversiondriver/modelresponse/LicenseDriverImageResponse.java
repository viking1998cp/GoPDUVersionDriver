package gopdu.pdu.gopduversiondriver.modelresponse;

public interface LicenseDriverImageResponse {
    void onLicenseDriverImageFront();
    void onLicenseDriverImageBackside();
    void nullImage();
    void notNullImage();
}
