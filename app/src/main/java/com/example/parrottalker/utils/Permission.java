package com.example.parrottalker.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class Permission {

    public interface Listener {
        void onPermission(boolean agree);
    }

    public static Permission getInstance;

    public static Permission getInstance() {
        return getInstance == null ? getInstance = new Permission() : getInstance;
    }


    public void requestWriteExternalPermission(final Listener mListener) {
        if (Dexter.isRequestOngoing()) {
            return;
        }
        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                mListener.onPermission(true);
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                mListener.onPermission(false);
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void requestReadExternalPermission(final Listener mListener) {
        if (Dexter.isRequestOngoing()) {
            return;
        }
        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                mListener.onPermission(true);
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                mListener.onPermission(false);
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }




    public void requestRecordPermission(final Listener mListener) {
        if (Dexter.isRequestOngoing()) {
            return;
        }
        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                mListener.onPermission(true);
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                mListener.onPermission(false);
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.RECORD_AUDIO);
    }


    public boolean checkExternalPermmision(){
        int result = PackageManager.PERMISSION_DENIED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             result = App.get().getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return result == PackageManager.PERMISSION_GRANTED;
    }



    public void requestRecordAndWriteStorage(final Listener mListener){
        requestRecordPermission(new Listener() {
            @Override
            public void onPermission(boolean agree) {
                if(agree){
                    requestWriteExternalPermission(new Listener() {
                        @Override
                        public void onPermission(boolean agree) {
                            mListener.onPermission(agree);
                        }
                    });
                }
            }
        });
    }



}
