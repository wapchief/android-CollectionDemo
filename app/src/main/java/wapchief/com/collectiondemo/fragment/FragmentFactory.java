package wapchief.com.collectiondemo.fragment;

import androidx.fragment.app.Fragment;

import java.util.HashMap;

/**
 * Created by wapchief on 2017/9/6.
 */

public class FragmentFactory {
    private static HashMap<Integer, Fragment> fragments;

    public static Fragment createFragment(int position) {
        fragments = new HashMap<Integer, Fragment>();
        Fragment fragment = fragments.get(position);//从集合中取出Fragment
        if (fragment == null) {//没有在集合中取到再进入实例化过程
            switch (position) {
                case 0:
                    fragment = new GoodsListFragment();
                    break;
                case 1:
                    fragment = new GoodsListFragment();
                    break;
                case 2:
                    fragment = new GoodsListFragment();
                    break;
                default:
                    break;
            }
            fragments.put(position, fragment);//存入集合中
        }
        return fragment;
    }
}
