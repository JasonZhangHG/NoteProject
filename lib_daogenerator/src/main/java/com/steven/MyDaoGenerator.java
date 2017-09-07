package com.steven;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1 , "com.steven.greendao");

        // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
        createEntity(schema);

        // 使用 DAOGenerator 类的 generateAll() 方法自动生成代码，
        // 此处你需要根据自己的情况更改输出目录（即之前在app模块中创建的 java-gen)。
        // 输出目录的路径也可以在 build.gradle 中设置
        new DaoGenerator().generateAll(schema,
                "./../Android40_NoteLogGreenDao/app/src/main/java-gen");
    }

    private static void createEntity(Schema schema) {
        Entity entity = schema.addEntity("NoteEntity");
        entity.setTableName("tb_note");
        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
        entity.addIdProperty().autoincrement().primaryKey();
        entity.addStringProperty("title").notNull();
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
        // For example, a property called “creationDate” will become a database column
        // “CREATION_DATE”.
        entity.addStringProperty("date").notNull();
        entity.addStringProperty("content").notNull();
    }
}
