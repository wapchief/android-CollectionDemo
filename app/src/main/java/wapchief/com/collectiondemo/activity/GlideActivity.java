package wapchief.com.collectiondemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import wapchief.com.collectiondemo.R;

/**
 * Created by Wu on 2017/2/20 0020 下午 3:06.
 * 描述：图片缓存
 */
public class GlideActivity extends AppCompatActivity {
    @BindView(R.id.img_glide)
    ImageView imgGlide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glide);
        ButterKnife.bind(this);
        Glide.with(this)
                .load("http://upload.jianshu.io/users/upload_avatars/2858691/4db2d471c01c?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240")
                .asBitmap()
                .thumbnail(0.2f)
                .into(imgGlide);

    }
}
