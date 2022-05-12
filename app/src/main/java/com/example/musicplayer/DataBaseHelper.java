package com.example.musicplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String MUSIC_DB = "Music_DB";
    public static final String COLUMN_TITLE = "COLUMN_TITLE";
    public static final String UPDATED_DATE = "UPDATE_DATE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_ALBUM = "ALBUM";
    public static final String COLUMN_ARTIST = "ARTIST";
    public static final String COLUMN_PATH = "SONG_PATH";

    public static final String FAVORITE_TABLE = "FAVORITE_DB";
    public static final String FAV_ID = "FAV_ID";
    public static final String FAV_PATH = "FAV_PATH";
    public static final String FAV_SONG_NAME = "FAV_NAME";
    public static final String FAV_DATE = "DATE";
    private static final String FAV_ARTIST = "FAV_ARTIST";
    private static final String FAV_ALBUM = "FAV_ALBUM";

    public static final String PLAY_LIST_TABLE = "playList";
    public static final String PLAYLIST_ID = "pl_id";
    public static final String PLAY_SONG_NAME = "SONGName";
    public static final String CommonID = "list_id";
    private static final String PLAY_PATH = "songPath";


    public static final String STORAGE_PLAYLISTS_TABLE = "myPlayLists";
    public static final String PLAY_LIST_NAME = "listName";
    public static final String LIST_ID = "list_id";


    Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, MUSIC_DB, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = " CREATE TABLE " + MUSIC_DB + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_ARTIST + " TEXT, "
                + UPDATED_DATE + " TEXT,"
                + COLUMN_ALBUM + " TEXT,"
                + COLUMN_PATH + " TEXT )";

        // CREATE TABLE name (<your column definitions>, UNIQUE (procode, proname, user) ON CONFLICT REPLACE);
        String createFavTable = " CREATE TABLE " + FAVORITE_TABLE + " ( "
                + FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FAV_SONG_NAME + " TEXT, "
                + FAV_DATE + " TEXT,"
                + FAV_ARTIST + " TEXT, "
                + FAV_ALBUM + " TEXT,"
                + FAV_PATH + " TEXT )";


        String createPlayListStorageTable = " create table " + STORAGE_PLAYLISTS_TABLE + "( "
                + LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLAY_LIST_NAME + " TEXT )";

        String createPlayListTable = " create table " + PLAY_LIST_TABLE + "( "
                + PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLAY_SONG_NAME + " TEXT ,"
                + CommonID + " INTEGER NOT NULL ,"
                + PLAY_PATH + " TExt )";


        sqLiteDatabase.execSQL(createTable);
        sqLiteDatabase.execSQL(createFavTable);
        sqLiteDatabase.execSQL(createPlayListStorageTable);
        sqLiteDatabase.execSQL(createPlayListTable);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MUSIC_DB);
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STORAGE_PLAYLISTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLAY_LIST_TABLE);
        onCreate(db);
    }

    public void addPlaylist(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLAY_LIST_NAME, name);
        db.insert(STORAGE_PLAYLISTS_TABLE, null, cv);
    }

    public List<String> getPlayListName() {
        List<String> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + STORAGE_PLAYLISTS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                                String title = cursor.getString(1);
                returnList.add(title);

            } while (cursor.moveToNext());
            cursor.close();
        }

        cursor.close();

        return returnList;
    }

    //    public List<AudioData> getPlayListSongs() {
//        List<AudioData> returnList = new ArrayList<>();
//        String query = "SELECT * FROM " + PLAY_LIST_TABLE ;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            do {
//                int id = cursor.getInt(0);
//                String title = cursor.getString(1);
//                String path=cursor.getString(3);
//
//                AudioData audioData = new AudioData(id, title, path);
//
//                 returnList.add(audioData);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//
//        return returnList;
//    }
    public List<AudioData> getPerticularListSongs(int ids) {
        List<AudioData> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + PLAY_LIST_TABLE + " where list_id = " + ids;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String path = cursor.getString(3);

                AudioData audioData = new AudioData(id, title, path);

                returnList.add(audioData);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return returnList;
    }


    public void addOne(String name, String artist, String path, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = 0;
        ContentValues cv = new ContentValues();
        String p_query = "select * from " + MUSIC_DB + " where song_path = ?";
        Cursor c = db.rawQuery(p_query, new String[]{path});

        if (c.moveToFirst()) {
            count++;
        } else {

            cv.put(COLUMN_TITLE, name);
            cv.put(COLUMN_ARTIST, artist);
            cv.put(UPDATED_DATE, date);
            cv.put(COLUMN_PATH, path);

            db.insertWithOnConflict(MUSIC_DB, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
            c.close();
        }

    }

    public void insertSongsPlaylist(String name, int list_id, String path) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PLAY_SONG_NAME, name);
        cv.put(LIST_ID, list_id);
        cv.put(PLAY_PATH, path);

        db.insertWithOnConflict(PLAY_LIST_TABLE, null, cv, SQLiteDatabase.CONFLICT_IGNORE);


    }


    public void addFavOne(String name, String artist, String path, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(FAV_SONG_NAME, name);
        cv.put(FAV_ARTIST, artist);
        cv.put(FAV_PATH, path);
        cv.put(FAV_DATE, date);
        db.insertWithOnConflict(FAVORITE_TABLE, null, cv, SQLiteDatabase.CONFLICT_IGNORE);


    }

    public AudioData getPaths(int ids) {
        String path , songName ;
        AudioData audioData = null;
        String query = "SELECT * FROM " + MUSIC_DB + " where id =" + ids;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                path = cursor.getString(5);
                songName = cursor.getString(1);
                audioData = new AudioData(songName, path);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return audioData;
    }

    public AudioData getFavPaths(int ids) {
        String path, songName ;
        AudioData audioData = null;
        String query = "SELECT * FROM " + FAVORITE_TABLE + " where FAV_ID =" + ids;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                path = cursor.getString(5);
                songName = cursor.getString(1);
                audioData = new AudioData(id, songName, path);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return audioData;
    }

    public AudioData getlistPaths(int id) {
        String path, songName;
        AudioData audioData = null;
        String query = "SELECT * FROM " + PLAY_LIST_TABLE + " where pl_id =" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int ids = cursor.getInt(0);
                path = cursor.getString(3);
                songName = cursor.getString(1);
                audioData = new AudioData(ids, songName, path);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return audioData;
    }


    public String listName(int ids) {
        String listName1 = "";
        String query = "SELECT * FROM " + STORAGE_PLAYLISTS_TABLE + " where list_id =" + ids;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                listName1 = cursor.getString(1);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return listName1;
    }

    public List<AudioData> getFavTitles() {
        List<AudioData> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + FAVORITE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                //String mainStory=cursor.getString(2);
                String date = cursor.getString(3);

                String pathCol = cursor.getString(5);

                AudioData audioData = new AudioData(id, title, date, pathCol);

                returnList.add(audioData);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return returnList;
    }

    public List<AudioData> getSongTitles() {
        List<AudioData> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + MUSIC_DB + " order by column_title asc ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                //String mainStory=cursor.getString(2);
                String date = cursor.getString(3);
                String artist = cursor.getString(2);
                String pathCol = cursor.getString(5);

                AudioData audioData = new AudioData(id, title, date, artist, pathCol);

                returnList.add(audioData);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return returnList;
    }

    public AudioData getAllFromId(int ids, String songFrom) {
        AudioData returnList = new AudioData();


        if (songFrom.equalsIgnoreCase("FromSong") || songFrom.isEmpty()) {

            String query = "SELECT * FROM " + MUSIC_DB + " where id =" + ids;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);

                    String date = cursor.getString(3);
                    String artist = cursor.getString(2);
                    String pathCol = cursor.getString(5);

                    returnList = new AudioData(id, title, date, artist, pathCol);

                    // returnList.add(audioData);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } else if (songFrom.equalsIgnoreCase("FromFav")) {
            String query = "SELECT * FROM " + FAVORITE_TABLE + " where FAv_id =" + ids;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);

                    String date = cursor.getString(3);
                    String artist = cursor.getString(2);
                    String pathCol = cursor.getString(5);

                    returnList = new AudioData(id, title, date, artist, pathCol);

                    // returnList.add(audioData);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } else if (songFrom.equalsIgnoreCase("FromplayList")) {
            String query = "SELECT * FROM " + PLAY_LIST_TABLE + " where pl_id =" + ids;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);


                    String pathCol = cursor.getString(3);

                    returnList = new AudioData(id, title, pathCol);

                    // returnList.add(audioData);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        return returnList;
    }

    public AudioData getNext(int ids) {
        {
            AudioData returnList = new AudioData();

            String query = "SELECT * FROM " + MUSIC_DB + " WHERE id >" + ids + "  order by id asc LIMIT 1";

            String query2 = "Select * from " + MUSIC_DB + " Order by id Limit 1";

            SQLiteDatabase db = this.getReadableDatabase();


            if (ids < getMaxId()) {

                Cursor cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);

                        String date = cursor.getString(3);
                        String artist = cursor.getString(2);
                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, date, artist, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            } else {
                Cursor cursor = db.rawQuery(query2, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);

                        String date = cursor.getString(3);
                        String artist = cursor.getString(2);
                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, date, artist, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            }
            return returnList;

        }
    }

    public AudioData getNextSong(int ids, String songFrom) {
        AudioData returnList = new AudioData();

        if (songFrom.equalsIgnoreCase("FromSong")) {


            String query = "SELECT * FROM " + MUSIC_DB + " WHERE id >" + ids + "  order by id asc LIMIT 1";

            String query2 = "Select * from " + MUSIC_DB + " Order by id Limit 1";

            SQLiteDatabase db = this.getReadableDatabase();


            if (ids < getMaxId()) {

                Cursor cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);

                        String date = cursor.getString(3);
                        String artist = cursor.getString(2);
                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, date, artist, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            } else {
                Cursor cursor = db.rawQuery(query2, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);

                        String date = cursor.getString(3);
                        String artist = cursor.getString(2);
                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, date, artist, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            }

        } else if (songFrom.equalsIgnoreCase("FromFav")) {
            String query = "SELECT * FROM " + FAVORITE_TABLE + " WHERE FAV_ID >" + ids + "  order by FAV_ID asc LIMIT 1";

            String query2 = "Select * from " + FAVORITE_TABLE + " Order by FAV_ID Limit 1";

            SQLiteDatabase db = this.getReadableDatabase();


            if (ids < getFavMaxId()) {

                Cursor cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);


                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            } else {
                Cursor cursor = db.rawQuery(query2, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);


                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            }

        } else if (songFrom.equalsIgnoreCase("FromplayList")) {


            String query = "SELECT * FROM " + PLAY_LIST_TABLE + " WHERE pl_id >" + ids + "  order by pl_id asc LIMIT 1";

            String query2 = "Select * from " + PLAY_LIST_TABLE + " Order by pl_id Limit 1";

            SQLiteDatabase db = this.getReadableDatabase();


            if (ids < getplayMaxId()) {

                Cursor cursor = db.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);

                        String pathCol = cursor.getString(3);


                        returnList = new AudioData(id, title, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            } else {
                Cursor cursor = db.rawQuery(query2, null);
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);


                        String pathCol = cursor.getString(3);

                        returnList = new AudioData(id, title, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            }

        }

        return returnList;
    }

    public AudioData getPrevious(int ids) {
        AudioData returnList = new AudioData();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + MUSIC_DB + " WHERE id <" + ids + " order by id DESC  LIMIT 1";

        String query1 = "select * from " + MUSIC_DB + " where id <" + ids + " order by id desc limit 1";

        if (ids > getMinId()) {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    //String mainStory=cursor.getString(2);
                    String date = cursor.getString(3);
                    String artist = cursor.getString(2);
                    String pathCol = cursor.getString(5);

                    returnList = new AudioData(id, title, date, artist, pathCol);


                } while (cursor.moveToNext());
            }

            cursor.close();
        } else {
            Cursor cursor = db.rawQuery(query1, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    //String mainStory=cursor.getString(2);
                    String date = cursor.getString(3);
                    String artist = cursor.getString(2);
                    String pathCol = cursor.getString(5);

                    returnList = new AudioData(id, title, date, artist, pathCol);


                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return returnList;
    }

    public AudioData getPreviousSong(int ids, String songFrom) {
        AudioData returnList = new AudioData();

        SQLiteDatabase db = this.getReadableDatabase();
        if (songFrom.equalsIgnoreCase("FromSong")) {
            String query = "SELECT * FROM " + MUSIC_DB + " WHERE id <" + ids + " order by id DESC  LIMIT 1";

            String query1 = "select * from " + MUSIC_DB + " where id <" + ids + " order by id desc limit 1";

            if (ids > getMinId()) {
                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);
                        //String mainStory=cursor.getString(2);
                        String date = cursor.getString(3);
                        String artist = cursor.getString(2);
                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, date, artist, pathCol);


                    } while (cursor.moveToNext());

                }

                cursor.close();
            } else {
                Cursor cursor = db.rawQuery(query1, null);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);
                        //String mainStory=cursor.getString(2);
                        String date = cursor.getString(3);
                        String artist = cursor.getString(2);
                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, date, artist, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            }
        } else if (songFrom.equalsIgnoreCase("FromFav")) {
            String query = "SELECT * FROM " + FAVORITE_TABLE + " WHERE FAV_ID <" + ids + " order by fav_id DESC  LIMIT 1";

            String query1 = "select * from " + FAVORITE_TABLE + "where FAV_ID <" + ids + " order by fav_id desc limit 1";

            if (ids > getFavMinId()) {
                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);
                        //String mainStory=cursor.getString(2);

                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            } else {
                Cursor cursor = db.rawQuery(query1, null);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);

                        String pathCol = cursor.getString(5);

                        returnList = new AudioData(id, title, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            }
        } else if (songFrom.equalsIgnoreCase("FromplayList")) {
            String query = "SELECT * FROM " + PLAY_LIST_TABLE + " WHERE pl_id <" + ids + " order by pl_id DESC  LIMIT 1";

            String query1 = "select * from " + PLAY_LIST_TABLE + "where pl_id <" + ids + " order by pl_id desc limit 1";

            if (ids > getMinPlayId()) {
                Cursor cursor = db.rawQuery(query, null);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);
                        //String mainStory=cursor.getString(2);

                        String pathCol = cursor.getString(3);

                        returnList = new AudioData(id, title, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            } else {
                Cursor cursor = db.rawQuery(query1, null);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);

                        String pathCol = cursor.getString(3);

                        returnList = new AudioData(id, title, pathCol);


                    } while (cursor.moveToNext());
                }

                cursor.close();
            }
        }
        return returnList;
    }


    public void deleteOne(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "DELETE FROM " + MUSIC_DB + " WHERE " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        cursor.close();
    }


    public long getMaxId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select max(id) from " + MUSIC_DB;
        Cursor cursor = db.rawQuery(query, null);
        long count = 0;
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);

            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return count;
    }

    public long getMinId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select min(id) from " + MUSIC_DB;
        Cursor cursor = db.rawQuery(query, null);
        long count = 0;
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);

            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return count;
    }

    public long getMinPlayId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select min(pl_id) from " + PLAY_LIST_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        long count = 0;
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);

            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return count;
    }

    public long getFavMinId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select min(Fav_id) from " + FAVORITE_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        long count = 0;
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);

            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return count;
    }

    public long getFavMaxId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select max(Fav_id) from " + FAVORITE_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        long count = 0;
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);

            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return count;
    }

    public long getplayMaxId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select max(pl_id) from " + PLAY_LIST_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        long count = 0;
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);

            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return count;
    }


}
