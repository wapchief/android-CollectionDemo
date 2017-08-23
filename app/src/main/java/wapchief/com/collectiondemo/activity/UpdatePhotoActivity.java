package wapchief.com.collectiondemo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
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
    private Uri contentUri;
    private boolean isfalse;
    private Bitmap photo1;
    private File file;
    private static final int PHOTO_TK = 0;
    private static final int PHOTO_PZ = 1;
    private static final int PHOTO_CLIP = 2;

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
                showHeadDialog();
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
        //图库
        update_dialog_TK=(TextView)view.findViewById(R.id.update_dialog_TK);
        update_dialog_TK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO_TK);
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
        //相机拍照
        update_dialog_PZ = (TextView) view.findViewById(R.id.update_dialog_PZ);
        update_dialog_PZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri mImageCaptureUri;
                // 判断7.0android系统
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    contentUri = FileProvider.getUriForFile(UpdatePhotoActivity.this,
                            "wapchief.com.collectiondemo.fileProvider",
                            new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                } else {
                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                }
                startActivityForResult(intent, PHOTO_PZ);
                dialog.dismiss();
            }
        });
    }

    /**
     * 裁剪图片的方法.
     * 用于拍照完成或者选择本地图片之后
     */
    private Uri uritempFile;

    public void startPhotoZoom(Uri uri) {
        Log.e("uri=====", "" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 60);
        intent.putExtra("outputY", 60);
        //uritempFile为Uri类变量，实例化uritempFile
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //开启临时权限
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //重点:针对7.0以上的操作
            intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
            uritempFile = uri;
        } else {
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_CLIP);
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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_PZ:
                    Uri pictur;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
                        pictur = contentUri;
                    } else {
                        pictur = Uri.fromFile(new File(
                                Environment.getExternalStorageDirectory() + "/temp.jpg"));
                    }
                    startPhotoZoom(pictur);
                    break;
                case PHOTO_TK:
                    startPhotoZoom(data.getData());
                    break;
                case PHOTO_CLIP:
                    //裁剪后的图像转成BitMap
//                        photo1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
//                        //创建路径
//                        String path = Environment.getExternalStorageDirectory()
//                                .getPath() + "/Pic";
//                        //获取外部储存目录
//                        file = new File(path);
//                        Log.e("file", file.getPath());
//                        //创建新目录
//                        file.mkdirs();
//                        //以当前时间重新命名文件
//                        long i = System.currentTimeMillis();
//                        //生成新的文件
//                        file = new File(file.toString() + "/" + i + ".png");
//                        Log.e("fileNew", file.getPath());
//                        //创建输出流
//                        OutputStream out = new FileOutputStream(file.getPath());
//                        //压缩文件
//                        boolean flag = photo1.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                        if (file.getName() != null || !file.getName().equals("")) {
                    Picasso.with(this)
                            .load(uritempFile)
                            .into(cardviewImg);
//                        }

                    break;
            }
        }


    }

}
