package org.mmaug.permissionsample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

  public static final int REQUEST_CODE_CAMERA = 100;
  public static final int REQUEST_CODE_LOCATION = 101;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button btnRequestCamera = (Button) findViewById(R.id.btnRequestCamera);

    btnRequestCamera.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        enableCamera();
      }
    });

    Button btnRequestLocation = (Button) findViewById(R.id.btnRequestLocation);

    btnRequestLocation.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        enableLocation();
      }
    });

    Button btnRequestInFragment = (Button) findViewById(R.id.btnRequestInFragment);

    btnRequestInFragment.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        MainFragment fragment = MainFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.flContainer, fragment, "tag").commit();
      }
    });
  }

  @AfterPermissionGranted(REQUEST_CODE_CAMERA) public void enableCamera() {
    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
      // have camera permission, open camera
      openCamera();
    } else {
      // Don't have permission. Ask for one.
      EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera),
          REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
    }
  }

  @AfterPermissionGranted(REQUEST_CODE_LOCATION) public void enableLocation() {
    if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
      // have camera permission, open camera
      openGPS();
    } else {
      // Don't have permission. Ask for one.
      EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera),
          REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    }
  }

  public void openCamera() {
    Toast.makeText(this, "Opening camera", Toast.LENGTH_SHORT).show();
  }

  public void openGPS() {
    Toast.makeText(this, "Opening GPS for location", Toast.LENGTH_SHORT).show();
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
  }

  @Override public void onPermissionsGranted(int requestCode, List<String> perms) {
    if (requestCode == REQUEST_CODE_CAMERA) {
      openCamera();
    } else if (requestCode == REQUEST_CODE_LOCATION) {
      openGPS();
    }
  }

  @Override public void onPermissionsDenied(int requestCode, List<String> perms) {
    if (requestCode == REQUEST_CODE_CAMERA) {

      // Handle negative button on click listener. Pass null if you don't want to handle it.
      DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          // Let's show a toast
          Toast.makeText(MainActivity.this, "Setting dialog is cancelled", Toast.LENGTH_SHORT)
              .show();
        }
      };

      // (Optional) Check whether the user denied permissions and checked NEVER ASK AGAIN.
      // This will display a dialog directing them to enable the permission in app settings.
      EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
          getString(R.string.rationale_ask_again), R.string.settings, android.R.string.cancel,
          cancelButtonListener, perms);
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Do something after user returned from app settings screen. User may be
    // changed/updated the permissions. Let's check whether the user has some permissions or not
    // after returned from settings screen
    if (requestCode == EasyPermissions.SETTINGS_REQ_CODE) {
      boolean hasSomePermissions = EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA);

      // Do something with the updated permissions

      if (hasSomePermissions) {
        enableCamera();
      }
    }
  }
}
