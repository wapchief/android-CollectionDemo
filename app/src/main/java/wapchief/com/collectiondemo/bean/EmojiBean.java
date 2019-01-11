package wapchief.com.collectiondemo.bean;

/**
 * @author wapchief
 * @date 2019/1/10
 */


public class EmojiBean {
    String emoji;

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public EmojiBean(String emoji) {
        this.emoji = emoji;
    }

    @Override
    public String toString() {
        return "EmojiBean{" +
                "emoji='" + emoji + '\'' +
                '}';
    }
}