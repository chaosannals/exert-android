package com.example.j2demo.ui.pages;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FallbackStrategy;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.PendingRecording;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.Recorder;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.j2demo.databinding.FragmentCameraBinding;
import com.google.common.util.concurrent.ListenableFuture;

public class CameraFragment extends Fragment implements LifecycleObserver {
    public final static String[] REQUIRED_PERMISSIONS = requiredPermissions();
    private final static String TAG = "CameraXApp";
    private final static String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    public final static int REQUEST_CODE_PERMISSIONS = 10;

    private FragmentCameraBinding binding;
    private CameraViewModel mViewModel;

    private ImageCapture imageCapture;
    private VideoCapture<Recorder> videoCapture;
    private Recording recording;
    private ExecutorService cameraExecutor;

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
//        return inflater.inflate(R.layout.fragment_camera, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.takePhotoButton.setOnClickListener(v -> {
            takePhoto();
        });
        binding.recodingButton.setOnClickListener(v -> {
            captureVideo();
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        if (allPermissionGranted(getContext())) {
            startCamera(getContext());
        } else {
            ActivityResultLauncher launcher = registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    permissions -> {
                        for (String key: permissions.keySet()) {
                            boolean v = permissions.get(key);
                            Toast.makeText(getContext(), key + " granted:" + v, Toast.LENGTH_SHORT).show();
                        }
                        startCamera(getContext());
                    }
            );
            launcher.launch(REQUIRED_PERMISSIONS);

            // 这种比较适合在 Activity 使用，Fragment 还是使用 registerForActivityResult
            // activity 获取 Fragment 在有导航的情况很难获得。
            // 没有权限发起请求，Activity 的 onRequestPermissionsResult 里面得到结果。
//            ActivityCompat.requestPermissions(
//                    getActivity(),
//                    REQUIRED_PERMISSIONS,
//                    REQUEST_CODE_PERMISSIONS
//            );
        }

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private void takePhoto() {
        if (imageCapture == null) return;

        String name = new SimpleDateFormat(FILENAME_FORMAT)
                .format(System.currentTimeMillis());
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        cv.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image");
        }
        ContentResolver contentResolver = getContext().getContentResolver();
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                cv
        ).build();
        imageCapture.takePicture(
                outputFileOptions,
                ContextCompat.getMainExecutor(getContext()), new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(getContext(), "saved photo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(getContext(), "saved photo error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void captureVideo() {
        if (videoCapture == null) return;

        binding.recodingButton.setEnabled(false);

        if (recording != null) {
            recording.stop();
            recording = null;
            return;
        }

        String name = new SimpleDateFormat(FILENAME_FORMAT)
                .format(System.currentTimeMillis());
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        cv.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, "Movies/CameraX-Video");
        }
        ContentResolver contentResolver = getContext().getContentResolver();
        MediaStoreOutputOptions mediaStoreOutputOptions = new MediaStoreOutputOptions.Builder(
                contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(cv)
                .build();
        PendingRecording pr = videoCapture.getOutput()
                .prepareRecording(getContext(), mediaStoreOutputOptions);
        if (PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PermissionChecker.PERMISSION_GRANTED) {
            pr.withAudioEnabled();
        }
        recording = pr.start(ContextCompat.getMainExecutor(getContext()), re -> {
            if (re instanceof VideoRecordEvent.Start) {
                binding.recodingButton.setText("STOP");
                binding.recodingButton.setEnabled(true);
            } else if (re instanceof VideoRecordEvent.Finalize) {
                if (!((VideoRecordEvent.Finalize) re).hasError()) {
                    Toast.makeText(getContext(), "successed", Toast.LENGTH_SHORT).show();
                } else {
                    if (recording != null) {
                        recording.close();
                        recording = null;
                    }
                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                }
                binding.recodingButton.setText("START");
                binding.recodingButton.setEnabled(true);
            }
        });
    }


    public void startCamera(Context context) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =  ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder().build();

                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.HIGHEST, FallbackStrategy.higherQualityOrLowerThan(Quality.SD)))
                        .build();
                videoCapture = VideoCapture.withOutput(recorder);

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, videoCapture);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(context));
    }

    public static boolean allPermissionGranted(Context context) {
        for(String it : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static String[] requiredPermissions() {
        ArrayList<String> result =  new ArrayList<>();
        result.add(Manifest.permission.CAMERA);
        result.add(Manifest.permission.RECORD_AUDIO);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            result.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        String[] r = new String[result.size()];
        return result.toArray(r);
    }
}