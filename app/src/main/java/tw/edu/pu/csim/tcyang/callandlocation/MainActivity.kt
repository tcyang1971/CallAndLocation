package tw.edu.pu.csim.tcyang.callandlocation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class MainActivity : AppCompatActivity(), MultiplePermissionsListener {

    lateinit var callButton: Button
    lateinit var locationButton:Button
    lateinit var settingButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callButton = findViewById(R.id.callButton)
        locationButton = findViewById(R.id.locationButton)
        settingButton = findViewById(R.id.settingButton)

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this)
                .check()
    }

    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
        if (p0!!.areAllPermissionsGranted()) {
            Toast.makeText(this, "您已允許所有權限", Toast.LENGTH_SHORT).show()
            settingButton.isEnabled = false  //關閉設定按鈕
        }
        else{
            //權限未全部允許時，關閉對應之按鈕
            for (i in 0.. p0.getDeniedPermissionResponses().size-1) {
                var Denied:String = p0.getDeniedPermissionResponses().get(i).getPermissionName()
                if (Denied.equals("android.permission.CALL_PHONE") ){
                    callButton.isEnabled = false
                }
                else if (Denied.equals("android.permission.ACCESS_FINE_LOCATION") ) {
                    locationButton.isEnabled = false
                }
            }
        }
    }

    override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
        p1?.continuePermissionRequest()
    }

    fun ClickBtn(view : View) {
        when (view.id){
            R.id.callButton -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    val calleIntent: Intent = Intent(Intent.ACTION_CALL)
                    val u: Uri = Uri.parse("tel:0426328001")
                    calleIntent.setData(u)
                    startActivity(calleIntent)
                }
            }

            R.id.locationButton -> {
            }

            R.id.settingButton -> {
                var it:Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                var uri:Uri = Uri.fromParts("package", getPackageName(), null)
                it.setData(uri)
                startActivity(it)
            }
        }
    }


}