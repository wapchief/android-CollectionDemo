package wapchief.com.collectiondemo.activity;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;

/**
 * Created by Wu on 2017/2/20 0020 下午 3:06.
 * 描述：图片缓存
 */
public class GlidePicassoActivity extends AppCompatActivity {
    @BindView(R.id.img_glide)
    ImageView imgGlide;
    @BindView(R.id.glide_bt)
    Button glideBt;
    @BindView(R.id.picasso_bt)
    Button picassoBt;

    String url = "http://upload.jianshu.io/users/upload_avatars/2858691/4db2d471c01c?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240";
    String url2 = "http://upload.jianshu.io/users/upload_avatars/1767483/6321b54d19be.jpeg?imageMogr2/auto-orient/strip|imageView2/1/w/144/h/144";
    Uri ur = Uri.parse(url);
    Uri ur2 = Uri.parse(url2);
    @BindView(R.id.clean_bt)
    Button cleanBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glide);
        ButterKnife.bind(this);
        initglide();


    }

    private void initglide() {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .thumbnail(0.2f)
                .into(imgGlide);
    }

    private void initpicasso() {
        Picasso.with(this)
                .load(url2)
                .into(imgGlide);
    }

    private void cleanImg() {
//        Picasso.with(this)
//                .invalidate(ur);
//        Picasso.with(this)
//                .invalidate(ur2);
        Glide.get(this).clearMemory();
        Glide.get(this).clearDiskCache();
    }


    @OnClick({R.id.glide_bt, R.id.picasso_bt,R.id.clean_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.glide_bt:
                initglide();
                break;
            case R.id.picasso_bt:
                initpicasso();
                break;
            case R.id.clean_bt:
                cleanImg();
                break;
        }
    }

}
