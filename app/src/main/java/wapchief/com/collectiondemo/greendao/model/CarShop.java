package wapchief.com.collectiondemo.greendao.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wapchief on 2017/9/6.
 * 购物车
 */

@Entity
public class CarShop {
    @Id(autoincrement = true)
    private Long id;
    //数量
    public int num;
    //名称
    public String name;
    //金额
    public double price;
    //店铺id
    public String shopId;
    public String entityId;
    //位置
    public int position;
    //商品图
    public String headpic;
    //规格
    public String spec;
    //库存
    public String stockCount;
    @Generated(hash = 2006230642)
    public CarShop(Long id, int num, String name, double price, String shopId,
            String entityId, int position, String headpic, String spec,
            String stockCount) {
        this.id = id;
        this.num = num;
        this.name = name;
        this.price = price;
        this.shopId = shopId;
        this.entityId = entityId;
        this.position = position;
        this.headpic = headpic;
        this.spec = spec;
        this.stockCount = stockCount;
    }
    @Generated(hash = 1479430411)
    public CarShop() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getShopId() {
        return this.shopId;
    }
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public String getEntityId() {
        return this.entityId;
    }
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    public int getPosition() {
        return this.position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public String getHeadpic() {
        return this.headpic;
    }
    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }
    public String getSpec() {
        return this.spec;
    }
    public void setSpec(String spec) {
        this.spec = spec;
    }
    public String getStockCount() {
        return this.stockCount;
    }
    public void setStockCount(String stockCount) {
        this.stockCount = stockCount;
    }

}
