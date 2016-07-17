package org.mmaug.permissionsample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.yelinaung.permissionsample.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by yelinaung on 17/07/16.
 */
public class MainFragment extends Fragment {

  private String[] requiredPermissions =
      new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE };
  private static final int REQUEST_CODE_CAMERA_AND_EXTERNAL_STORAGE = 200;

  public static MainFragment newInstance() {
    Bundle args = new Bundle();
    MainFragment fragment = new MainFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View fragmentView = inflater.inflate(R.layout.fragment_main, null, false);

    Button btnRequestCameraAndLocation =
        (Button) fragmentView.findViewById(R.id.btnRequestCameraAndLocation);

    btnRequestCameraAndLocation.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        enableCameraAndLocation();
      }
    });

    return fragmentView;
  }

  @AfterPermissionGranted(REQUEST_CODE_CAMERA_AND_EXTERNAL_STORAGE)
  private void enableCameraAndLocation() {
    if (EasyPermissions.hasPermissions(getContext(), requiredPermissions)) {
      takePhotoAndGetLocation();
    } else {
      EasyPermissions.requestPermissions(this,
          getString(R.string.rationale_camera_and_external_storage),
          REQUEST_CODE_CAMERA_AND_EXTERNAL_STORAGE, requiredPermissions);
    }
  }

  private void takePhotoAndGetLocation() {
    Toast.makeText(getContext(), "Taking photo and writing to external storage", Toast.LENGTH_SHORT)
        .show();
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
      Toast.makeText(getContext(), "Back from Settings", Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }
}
