package com.acmenxd.mvp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.acmenxd.mvp.db.core.DBManager;
import com.acmenxd.mvp.db.dao.DaoSession;
import com.acmenxd.mvp.db.dao.StudentDao;
import com.acmenxd.mvp.model.db.Student;

import org.greenrobot.greendao.query.Query;

import java.util.Date;
import java.util.List;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/28 10:00
 * @detail 数据库操作工具类
 */
public final class StudentDB {
    private static StudentDB instance;
    private SQLiteDatabase db;
    private DaoSession ds;
    private StudentDao mStudentDao;

    private StudentDB() {
        db = DBManager.getInstance().getDatabase();
        ds = DBManager.getInstance().getDaoSession();
        mStudentDao = ds.getStudentDao();
    }

    public static StudentDB getInstance() {
        if (instance == null) {
            instance = new StudentDB();
        }
        return instance;
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public Cursor getStudentCursor() {
        //查询，得到cursor
        String orderBy = StudentDao.Properties.Id.columnName + " DESC";//根据Id降序排序
        Cursor cursor = db.query(mStudentDao.getTablename(), mStudentDao.getAllColumns(), null, null, null, null, orderBy);
        return cursor;
    }

    /**
     * 添加学生
     */
    public void addStudent(@NonNull String name, @IntRange(from = 0) int age, @FloatRange(from = 0) double score) {
        Student entity = new Student(null, name, age, score, new Date());
        //面向对象添加表数据
        mStudentDao.insert(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void deleteStudent(@IntRange(from = 0) long id) {
        mStudentDao.deleteByKey(id);
    }

    /**
     * 更新
     */
    public void updateStudent(@NonNull Long id, @NonNull String name, @IntRange(from = 0) int age, @FloatRange(from = 0) double score) {
        Student entity = new Student(id, name, age, score, new Date());
        mStudentDao.update(entity);
    }

    /**
     * 根据name查询
     *
     * @param name
     */
    public List queryStudent(@NonNull String name) {
        // Query 类代表了一个可以被重复执行的查询
        Query query = mStudentDao.queryBuilder()
                .where(StudentDao.Properties.Name.eq(name))
                .orderAsc(StudentDao.Properties.Id)
                .build();
        // 查询结果以 List 返回
        return query.list();
    }
}
