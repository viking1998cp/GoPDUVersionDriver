package gopdu.pdu.gopduversiondriver.presenter;

import gopdu.pdu.gopduversiondriver.modelresponse.MotorcyclepaperImageResponse;
import gopdu.pdu.gopduversiondriver.object.Driver;
import gopdu.pdu.gopduversiondriver.view.ViewMotorcyclepaperImageListener;
import gopdu.pdu.gopduversiondriver.viewmodel.MotocyclepaperImageViewModel;

public class PresenterMotocyclePaperImage implements MotorcyclepaperImageResponse
{
    private MotocyclepaperImageViewModel motocyclepaperViewModel;
    private ViewMotorcyclepaperImageListener callback;

    public PresenterMotocyclePaperImage(ViewMotorcyclepaperImageListener callback) {
        motocyclepaperViewModel = new MotocyclepaperImageViewModel(this);
        this.callback = callback;
    }

    public void getData(int requestCode, int resultCode) {
        motocyclepaperViewModel.getData(requestCode, resultCode);
    }

    public void checkNullImage(Driver driver) {
        motocyclepaperViewModel.checkNull(driver);
    }

    @Override
    public void onMotorcyclepaperImageFront() {
        callback.onMotorcyclepaperImageFront();
    }

    @Override
    public void onMotorcyclepaperImageBackside() {
        callback.onMotorcyclepaperImageBackside();
    }

    @Override
    public void nullImage() {
        callback.nullImage();
    }

    @Override
    public void notNullImage() {
        callback.notNullImage();
    }
}
