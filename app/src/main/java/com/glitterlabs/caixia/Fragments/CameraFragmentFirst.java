package com.glitterlabs.caixia.Fragments;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.glitterlabs.caixia.Adapters.TimerAdapter;
import com.glitterlabs.caixia.Models.MessageData;
import com.glitterlabs.caixia.MyFriendsActivity;
import com.glitterlabs.caixia.R;
import com.glitterlabs.caixia.util.GPSTracker;
import com.glitterlabs.caixia.util.GeoPointParse;
import com.glitterlabs.caixia.util.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
    Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
    All rights reserved. Patents pending.

    Responsible: Abhay Bhusari
 */
public class CameraFragmentFirst extends Fragment {
    // Native camera.
    private Camera mCamera;
    // View to display the camera output.
    private CameraPreview mPreview;
    // Reference to the containing view.
    private View mCameraView;
    boolean opened = false;
    public static final String ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER";
    int camId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private ImageView ivCapture, ivCancelPhoto, ivSwitchCamera, ivTextToPhoto, ivTimePicker, ivDatePiker, ivSendToFrend, ivGPS,ivSavePhoto;
    private EditText etTextToPhoto;
    private RelativeLayout rlCameraBottomMenu;
    //create object of MessageData class to set all meassage data
    MessageData messageData = new MessageData();
    private byte[] mData;

    //Default empty constructor.
    public CameraFragmentFirst() {
        super();
    }

    /**
     * Static factory method
     *
     * @param sectionNumber
     * @return
     */
    public static CameraFragmentFirst newInstance(int sectionNumber) {
        CameraFragmentFirst fragment = new CameraFragmentFirst();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * OnCreateView fragment override
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_native_camera, container, false);

        // Create our Preview view and set it as the content of our activity.
        opened = safeCameraOpenInView(view, camId);
        ivCancelPhoto = (ImageView) view.findViewById(R.id.ivCancelPhoto);
        rlCameraBottomMenu = (RelativeLayout) view.findViewById(R.id.rlPhotoSetting);
        ivCapture = (ImageView) view.findViewById(R.id.ivCapture);
        ivSwitchCamera = (ImageView) view.findViewById(R.id.ivSwitchCamera);
        ivTextToPhoto = (ImageView) view.findViewById(R.id.ivText);
        ivTimePicker = (ImageView) view.findViewById(R.id.ivTime);
        ivDatePiker = (ImageView) view.findViewById(R.id.ivDatePicker);
        etTextToPhoto = (EditText) view.findViewById(R.id.etTextToPhoto);
        ivGPS = (ImageView) view.findViewById(R.id.ivGPS);
        ivSendToFrend = (ImageView) view.findViewById(R.id.ivSend);
        ivSavePhoto= (ImageView) view.findViewById(R.id.ivSave);
        onclickEvent();


        if (opened == false) {
            Log.d("CameraGuide", "Error, Camera failed to open");
            return view;
        }

        return view;
    }


    private void onclickEvent() {
        ivCancelPhoto.setVisibility(View.GONE);
        rlCameraBottomMenu.setVisibility(View.GONE);
        etTextToPhoto.setVisibility(View.GONE);
        ivTextToPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTextToPhoto.setVisibility(View.VISIBLE);

            }
        });

        ivTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        ivDatePiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        ivCapture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);



                    }
                });

        ivCancelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.startPreview();
                ivCancelPhoto.setVisibility(View.GONE);
                ivSwitchCamera.setVisibility(View.VISIBLE);
                ivCapture.setVisibility(View.VISIBLE);
                rlCameraBottomMenu.setVisibility(View.GONE);
                etTextToPhoto.setVisibility(View.GONE);
            }
        });

        ivSwitchCamera.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.stopPreview();
                        //  }
                        //NB: if you don't release the current camera before switching, you app will crash
                        releaseCameraAndPreview();
                        //swap the id of the camera to be used
                        if (camId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            camId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        } else {
                            camId = Camera.CameraInfo.CAMERA_FACING_BACK;
                        }
                        // opened = safeCameraOpenInView(view, camId);
                        mCamera = Camera.open(camId);
                        //Code snippet for this method from somewhere on android developers, i forget where
                        try {
                            //this step is critical or preview on new camera will no know where to render to
                            mCamera.setPreviewDisplay(mPreview.mHolder);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mCamera.startPreview();
                    }
                });
        ivSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoto();
            }
        });
         // get geo point
        ivGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPointParse point = null;
                // create class object
                GPSTracker gpsTracker = new GPSTracker(getActivity());

                // check if GPS enabled
                if (gpsTracker.canGetLocation()) {

                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    point = new GeoPointParse(latitude, longitude);
                    messageData.setGeoPoint(point);
                    // \n is for new line
                    Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gpsTracker.showSettingsAlert();
                }


            }
        });


        ivSendToFrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageData.setTextMessage(etTextToPhoto.getText().toString());
                Intent i = new Intent(getActivity(), MyFriendsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("messageData", messageData);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

    }

    private void showTimePicker() {
        String times[] = {"1 second", "2 seconds", "3 seconds", "4 seconds", "5 seconds", "6 seconds", "7 seconds", "8 seconds", " 9 seconds", "10 seconds"};
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Select Time");
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        dialog.setContentView(R.layout.timer_list);
        ListView list = (ListView) dialog.findViewById(R.id.lvTimer);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int i = 0;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //set time
                messageData.setTimeForDisplay(position + 1);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        TimerAdapter adapter = new TimerAdapter(getActivity(), times);
        list.setAdapter(adapter);

        dialog.show();


    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    OnDateSetListener ondate = new OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String scheduldDate = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(year);



            messageData.setScheduldDate(scheduldDate);
            Toast.makeText(getActivity(), String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1)
                    + "-" + String.valueOf(year), Toast.LENGTH_LONG).show();
            /*edittext.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/

        }
    };

    /**
     * Recommended "safe" way to open the camera.
     *
     * @param view
     * @return
     */
    private boolean safeCameraOpenInView(View view, int id) {
        boolean qOpened = false;
        releaseCameraAndPreview();
        mCamera = getCameraInstance(id);

        mCameraView = view;
        qOpened = (mCamera != null);

        if (qOpened == true) {
            mPreview = new CameraPreview(getActivity().getBaseContext(), mCamera, view);
            FrameLayout preview = (FrameLayout) view.findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            mPreview.startCameraPreview();
        }
        return qOpened;
    }

    /**
     * Safe method for getting a camera instance.
     *
     * @return
     */
    public static Camera getCameraInstance(int id) {
        Camera c = null;
        try {
            //  c = Camera.open(); // attempt to get a Camera instance
            c = Camera.open(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCameraAndPreview();
    }

    /**
     * Clear any existing preview / camera.
     */
    private void releaseCameraAndPreview() {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if (mPreview != null) {
            mPreview.destroyDrawingCache();
            mPreview.mCamera = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ivCancelPhoto.setVisibility(View.GONE);
        ivSwitchCamera.setVisibility(View.VISIBLE);
        ivCapture.setVisibility(View.VISIBLE);
        rlCameraBottomMenu.setVisibility(View.GONE);
        etTextToPhoto.setVisibility(View.GONE);
    }

    /**
     * Picture Callback for handling a picture capture and saving it out to a file.
     */
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            rlCameraBottomMenu.setVisibility(View.VISIBLE);
            ivCapture.setVisibility(View.GONE);
            ivSwitchCamera.setVisibility(View.GONE);
            ivCancelPhoto.setVisibility(View.VISIBLE);
            mData=data;
            messageData.setByteImage(data);
        }
    };

    /**
     * Used to return the camera File output.
     *
     * @return
     */

    private void savePhoto(){
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Toast.makeText(getActivity(), "Image retrieval failed.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(mData);
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CiaxiaApp");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Camera Guide", "Required media storage does not exist");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        Helper.showDialog("Success!", "Your picture has been saved!", getActivity());
        return mediaFile;
    }



    /**
     * Surface on which the camera projects it's capture results. This is derived both from Google's docs and the
     * excellent StackOverflow answer provided below.
     * <p/>
     * Reference / Credit: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
     */
    class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

        // SurfaceHolder
        private SurfaceHolder mHolder;

        // Our Camera.
        private Camera mCamera;

        // Parent Context.
        private Context mContext;

        // Camera Sizing (For rotation, orientation changes)
        private Camera.Size mPreviewSize;

        // List of supported preview sizes
        private List<Camera.Size> mSupportedPreviewSizes;

        // Flash modes supported by this camera
        private List<String> mSupportedFlashModes;

        // View holding this camera.
        private View mCameraView;

        public CameraPreview(Context context, Camera camera, View cameraView) {
            super(context);

            // Capture the context
            mCameraView = cameraView;
            mContext = context;
            setCamera(camera);

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setKeepScreenOn(true);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        /**
         * Begin the preview of the camera input.
         */
        public void startCameraPreview() {
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        /**
         * Extract supported preview and flash modes from the camera.
         *
         * @param camera
         */
        private void setCamera(Camera camera) {
            // Source: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
            mCamera = camera;
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedFlashModes = mCamera.getParameters().getSupportedFlashModes();

            // Set the camera to Auto Flash mode.
            if (mSupportedFlashModes != null && mSupportedFlashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                mCamera.setParameters(parameters);
            }

            requestLayout();
        }

        /**
         * The Surface has been created, now tell the camera where to draw the preview.
         *
         * @param holder
         */
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Dispose of the camera preview.
         *
         * @param holder
         */
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mCamera != null) {
                mCamera.stopPreview();
            }
        }

        /**
         * React to surface changed events
         *
         * @param holder
         * @param format
         * @param w
         * @param h
         */
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null) {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                Camera.Parameters parameters = mCamera.getParameters();

                // Set the auto-focus mode to "continuous"
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

                // Preview size must exist.
                if (mPreviewSize != null) {
                    Camera.Size previewSize = mPreviewSize;
                    parameters.setPreviewSize(previewSize.width, previewSize.height);
                }

                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Calculate the measurements of the layout
         *
         * @param widthMeasureSpec
         * @param heightMeasureSpec
         */
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // Source: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            setMeasuredDimension(width, height);

            if (mSupportedPreviewSizes != null) {
                mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
            }
        }

        /**
         * Update the layout based on rotation and orientation changes.
         *
         * @param changed
         * @param left
         * @param top
         * @param right
         * @param bottom
         */
        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            // Source: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
            if (changed) {
                final int width = right - left;
                final int height = bottom - top;

                int previewWidth = width;
                int previewHeight = height;

                if (mPreviewSize != null) {
                    Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

                    switch (display.getRotation()) {
                        case Surface.ROTATION_0:
                            previewWidth = mPreviewSize.height;
                            previewHeight = mPreviewSize.width;
                            mCamera.setDisplayOrientation(90);
                            break;
                        case Surface.ROTATION_90:
                            previewWidth = mPreviewSize.width;
                            previewHeight = mPreviewSize.height;
                            break;
                        case Surface.ROTATION_180:
                            previewWidth = mPreviewSize.height;
                            previewHeight = mPreviewSize.width;
                            break;
                        case Surface.ROTATION_270:
                            previewWidth = mPreviewSize.width;
                            previewHeight = mPreviewSize.height;
                            mCamera.setDisplayOrientation(180);
                            break;
                    }
                }

                final int scaledChildHeight = previewHeight * width / previewWidth;
                mCameraView.layout(0, height - scaledChildHeight, width, height);
            }
        }

        /**
         * @param sizes
         * @param width
         * @param height
         * @return
         */
        private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
            // Source: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
            Camera.Size optimalSize = null;

            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) height / width;

            // Try to find a size match which suits the whole screen minus the menu on the left.
            for (Camera.Size size : sizes) {

                if (size.height != width) continue;
                double ratio = (double) size.width / size.height;
                if (ratio <= targetRatio + ASPECT_TOLERANCE && ratio >= targetRatio - ASPECT_TOLERANCE) {
                    optimalSize = size;
                }
            }

            // If we cannot find the one that matches the aspect ratio, ignore the requirement.
            if (optimalSize == null) {
                // TODO : Backup in case we don't get a size.
            }

            return optimalSize;
        }
    }




}
