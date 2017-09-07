package com.steven.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.steven.greendao.NoteEntity;

import com.steven.greendao.NoteEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteEntityDaoConfig;

    private final NoteEntityDao noteEntityDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteEntityDaoConfig = daoConfigMap.get(NoteEntityDao.class).clone();
        noteEntityDaoConfig.initIdentityScope(type);

        noteEntityDao = new NoteEntityDao(noteEntityDaoConfig, this);

        registerDao(NoteEntity.class, noteEntityDao);
    }
    
    public void clear() {
        noteEntityDaoConfig.getIdentityScope().clear();
    }

    public NoteEntityDao getNoteEntityDao() {
        return noteEntityDao;
    }

}