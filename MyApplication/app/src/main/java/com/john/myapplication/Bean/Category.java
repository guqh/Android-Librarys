package com.john.myapplication.Bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * @author John Gu
 * @date 2017/s11/1.
 */

@Table(name="categories")
public class Category extends Model {
    @Column(name = "name")
    public String name;
    }

@Table(name = "items")
class Item extends Model{
    @Column(name = "remote_id",unique = true,onUniqueConflict = Column.ConflictAction.REPLACE)
    public int remoteId;
    @Column(name = "name")
    public String name;
    @Column(name = "category")
    public Category category;
    public Item(){
        super();
    }
    public Item(String name,Category category){
        super();
        this.name=name;
        this.category=category;
    }

//  ==============================增删改查=========================================================================
//    1 单条插入
//    Category restaurants = new Category();
//    restaurants.name = "Restaurants";
//    restaurants.save();

//    2批量插入
//    ActiveAndroid.beginTransaction();
//    try {
//        for (int i = 0; i < 100; i++) {
//            Item item = new Item();
//            item.name = "Example " + i;
//            item.save();
//        }
//        ActiveAndroid.setTransactionSuccessful();
//    }finally {
//        ActiveAndroid.endTransaction();
//    }

//    3.更新
//    new Update(Person.class).set("age=?," + "name=?", age, name).execute();
//    new Update(User.class).set("addr = ?","上海").where("userName=?",userName).execute();

//    4.删除
//    Item item = Item.load(Item.class, 1);
//    item.delete();//通过id加载一个Item对象，并且删除他

//    Item.delete(Item.class, 1);//通过静态方法删除

//    new Delete().from(Item.class).where("Id = ?", 1).execute();//创建调用Delete对象删除

//    5.查询
//      List<Model> execute = new Select().from(Item.class).where("Category = ?", 1).orderBy("name desc").execute();
//                                                          条件                     排序规则
//    public static Item getRandom(Category category) {
//        return new Select()
//              .from(Item.class)
//              .where("Category = ?", category.getId())
//              .orderBy("RANDOM()")
//              .executeSingle(); //查询一条
//    }

//    public static List<Item> getAll(Category category) {
//        return new Select()
//                .from(Item.class)
//                .where("Category = ?", category.getId())
//                .orderBy("Name ASC")
//                .execute(); //查询所有
//    }

//   =======================增删改查=========================================================================


}

