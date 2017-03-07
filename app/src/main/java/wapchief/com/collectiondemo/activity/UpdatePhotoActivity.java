package wapchief.com.collectiondemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.MainActivity;
import wapchief.com.collectiondemo.R;

/**
 * Created by Wu on 2017/2/28 0028 上午 10:14.
 * 描述：
 */
public class UpdatePhotoActivity extends AppCompatActivity {

    @BindView(R.id.cardview_img)
    ImageView cardviewImg;
    @BindView(R.id.ll_update)
    LinearLayout llUpdate;
    Dialog dialog;
    Context mContext;
    //图片对象
    private Uri photoUri;
    //相机
    private final int PIC_FROM_CAMERA = 1;
    //本地
    private final int PIC_FROM＿LOCALPHOTO = 2;
    private final int SELECT_PIC=3;
    private boolean isfalse;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_photo);
        ButterKnife.bind(this);

        mContext = UpdatePhotoActivity.this;

    }

    @OnClick({R.id.cardview_img, R.id.ll_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardview_img:
                break;
            case R.id.ll_update:
                showHeadDialog();
                break;
        }
    }

    private void showHeadDialog() {
         TextView update_dialog_TK,update_dialog_PZ,update_dialog_cancel;
        View view = getLayoutInflater().inflate(R.layout.show_head_dialog, null);
        dialog = new Dialog(this, R.style.dialog_photo);

        Display display = getWindowManager().getDefaultDisplay();
        dialog.setContentView(view, new ViewGroup.LayoutParams(
                display.getWidth()
                , display.getHeight()
        ));
        dialog.getWindow().setWindowAnimations(R.style.Dialog_Anim_Style);
//        dialog.setCanceledOnTouchOutside(true);
        update_dialog_TK=(TextView)view.findViewById(R.id.update_dialog_TK);

        update_dialog_TK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHandlerPhoto(PIC_FROM＿LOCALPHOTO);
                dialog.dismiss();
            }
        });
        //取消
        update_dialog_cancel= (TextView)view.findViewById(R.id.update_dialog_cancel);
        update_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /**
     * 裁剪图片的方法.
     * 用于拍照完成或者选择本地图片之后
     */
    private void cropImageUriByTakePhoto(){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");//启动裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scale", true); //  是否保存比例
        intent.putExtra("return-data", false);//返回数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);//储存位置
        startActivityForResult(intent,PIC_FROM_CAMERA);//存放本地

    }

    /**
     * 回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                //图库
                case PIC_FROM_CAMERA:
                    if (resultCode == RESULT_OK) {
                        if (!isfalse) {
//此处启动裁剪程序
                            Intent intent = new Intent("com.android.camera.action.CROP");
                            //此处注释掉的部分是针对android 4.4路径修改的一个测试
                            //有兴趣的读者可以自己调试看看
                            String text=data.getData().toString();
                            Log.e("--------data",text);
                            intent.setDataAndType(data.getData(), "image/*");
                            intent.putExtra("scale", true);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, PIC_FROM＿LOCALPHOTO);
                        }else{
                            try {
                                cropImageUriByTakePhoto();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
//                case :
//                    if (photoUri!=null){
//
//                    }
//                    break;
            }

    }
    private void doHandlerPhoto(int type) {
        //创建File对象，用于存储拍照后的图片
        //将此图片存储于SD卡的根目录下
        File outputImage = new File(Environment.getExternalStorageDirectory(),
                "image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将File对象转换成Uri对象
        //Uri表标识着图片的地址
        photoUri = Uri.fromFile(outputImage);

        if (type == PIC_FROM＿LOCALPHOTO) {
            isfalse=false;
            Intent intent = new Intent(Intent.ACTION_PICK,null);
            //此处调用了图片选择器
            //如果直接写intent.setDataAndType("image/*");
            //调用的是系统图库
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PIC_FROM_CAMERA);
        } else {
            isfalse=true;
            Intent cameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(cameraIntent, PIC_FROM_CAMERA);

        }

    }
    /**
     * 保存图片
     * @param data
     */
//    private void saveBitmap(Intent data) {
//        Bundle bundle = data.getExtras();
//        if (bundle != null) {
//            Bitmap bitmap = bundle.getParcelable("data");
//            File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
//            try {
//                file.createNewFile();
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//                fileOutputStream.flush();
//                fileOutputStream.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
}
