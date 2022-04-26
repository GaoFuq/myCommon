package com.gfq.common.system.sp.simple;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gfq.common.system.sp.SPData;
import com.gfq.common.system.sp.SPTableDelegate;

/**
 * 2022/4/26 9:34
 *
 * @auth gaofuq
 * @description
 */
class JavaUseSimple {
    //1.定义表名 和 要存的数据对应的key
    static class UserTable {
        static final String SP_TABLE_NAME = "UserTable";
        static final String DATA_KEY_NAME = "selfUserInfo";
    }

    static class AppConfigTable {
        static final String SP_TABLE_NAME = "AppConfigTable";
        static final String DATA_KEY_NAME = "appConfig";
    }

    //2.定义数据类
    static class UserBeanSimple implements SPData {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @NonNull
        @Override
        public String getSpTableName() {
            return UserTable.SP_TABLE_NAME;
        }

        @NonNull
        @Override
        public String getDataKeyName() {
            return UserTable.DATA_KEY_NAME;
        }

        @Override
        public void save() {
            SPData.DefaultImpls.save(this);
        }
    }

    //3.定义代理类
    static class UserSp extends SPTableDelegate<UserBeanSimple> {

        public UserSp(UserBeanSimple userBeanSimple, @NonNull String spTableName) {
            super(userBeanSimple, spTableName);
        }

        public UserSp() {
            super(new UserBeanSimple(), UserTable.SP_TABLE_NAME);
        }
    }

    //4.存取自定义数据类
    void saveAndGetData() {
        //4.1 通过代理类存取
        UserSp userSp = new UserSp();

        //4.2 取
        UserBeanSimple bean = userSp.getOrDefault();
        String name = bean.getName();
        int age = bean.getAge();

        //4.3 保存/更新
        bean = userSp.getOrDefault();
        bean.setName("name");
        bean.setAge(18);
        bean.save();


        //4.4 保存/更新
        bean = getUserFromNet();
        if (bean != null) {
            bean.save();
        }

    }


    //5.存取基本类型数据
    void saveData1() {
        //直接使用 SPTableDelegate ，spTableName = SPTableDelegate.defaultSPTableName
        //5.1 定义代理
        SPTableDelegate<Boolean> booleanSp = new SPTableDelegate<>(false, SPTableDelegate.defaultSPTableName);
        boolean isLogin = booleanSp.getOrDefault("isLogin");
        //5.3 取
        if (isLogin) {
            //do something
        }
        //5.2 存
        isLogin = true;

        SPTableDelegate<String> stringSp = new SPTableDelegate<>("", SPTableDelegate.defaultSPTableName);
        String name = stringSp.getOrDefault("");
        //5.3 取
        Log.e("TAG", name);
        //5.2 存
        name = "name";

        //也可以专门指定一张表来存相关数据
        //数据类型设置为 T ，可以存任意类型。
        //数据类型设置为指定类型 ，就只能存取指定的类型。
        class MySpTable<T> extends SPTableDelegate<T> {
            public MySpTable(T def, @NonNull String spTableName) {
                super(def, spTableName);
            }

            public MySpTable(T def) {
                super(def,"MySpTable");
            }
        }
    }


    private UserBeanSimple getUserFromNet() {
        return null;
    }
}
