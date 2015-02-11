package com.fairytale;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccessModel {
	
	public final int INSERT_SUCCESS = 1;
	public final int INSERT_FAIL = 2;
	
	private DatabaseHelper helper;
	private SQLiteDatabase db;
	private Context context;
	
	public DatabaseAccessModel(Context context){
		this.context = context;
		helper = new DatabaseHelper(context, "com.fairytale.db", null, 1);
	}
	
	//인자
	//	table		: insert를 수행할 테이블
	//	cols		: 값이 해당하는 속성들의 집합
	//	col_values	: 속성에 들어갈 값의 집합
	//	cols와 values는 동일한 인덱스끼리 대응한다.
	//테이블에 각 속성들의 값에 해당하는 레코드를 생성한다.
	public int insert(String table, String[] cols, String[] col_values){
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		if(cols.length != col_values.length){
			mLog.e("Database: Columns size is diffrent with values size");
			return INSERT_FAIL;
		}
		for(int c_loop = 0; c_loop < cols.length; c_loop++)
			values.put(cols[c_loop], col_values[c_loop]);
		db.insert(table, null, values);
		
		mLog.i("Database: Insert record in "+table);
		mLog.i("        : Data size is "+cols.length);
		return INSERT_SUCCESS;
	}
	//인자 설명
	//	table			: update를 수행할 테이블
	//	selection		: where절 (형태 예제: "ID=? AND AGE<?")
	//	selection_values: selection의 각 요소의 값
	//	update_key		: 수정될 속성의 이름
	//	update_value	: 수정될 속성의 값
	//테이블에 특정 레코드를 찾아 속성 값을 수정한다.
	public void update(String table, String selection, String[] selection_values, 
						String update_key, String update_value){
		db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(update_key, update_value);
		db.update(table, values, selection, selection_values);
	}
	//인자 설명
	//	table			: delete를 수행할 테이블
	//	selection		: where절 (형태 예제: "ID=? AND AGE<?")
	//	selection_values: selection의 각 요소의 값
	//테이블에 특정 레코드를 제거한다.
	public void delete(String table, String selection, String[] selection_values){
		db = helper.getWritableDatabase();
		db.delete(table, selection, selection_values);
		mLog.i("Database: Delete Record from "+table);
		mLog.i("        : "+selection+", "+selection_values.toString());
	}
	//인자 설명
	//	table			: select를 수행할 테이블
	//	columns			: select로 찾을 속성
	//	selection		: where절 (형태 예제: "ID=? AND AGE<?")
	//	selectionArgs	: selection의 각 요소의 값
	//	groupBy			: groupBy절
	//	having			: having절
	//	orderBy			: orderBy절
	//테이블에 특정 레코드를 찾아 속성 값을 수정한다.
	//
	public ArrayList<HashMap<String, String>> selectQuery(String table, String[] columns, String selection, 
						String[] selectionArgs, String groupBy, String having, String orderBy){
		db = helper.getReadableDatabase();
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		
		ArrayList<HashMap<String, String>> list = new ArrayList<>();
		int elem_length = columns.length;
		while(c.moveToNext()){
			HashMap<String, String> map = new HashMap<>();
			for(int c_loop = 0; c_loop < elem_length; c_loop++)
				map.put(columns[c_loop], c.getString(c.getColumnIndex(columns[c_loop])));
			list.add(map);
		}
		mLog.i("Database: Select executed");
		mLog.i("        : "+list.size()+" Maps in List");
		mLog.i("        : "+elem_length+" keys in Map");
		return list;
	}
	
	private class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String query = null;
			try{
				int index;
				AssetManager asset_mgr = context.getAssets();
				InputStream is = asset_mgr.open("local_db_create");
				StringBuffer init_buf = new StringBuffer();
				byte[] buffer = new byte[2048];
				
				while ((index = is.read(buffer)) != -1){
					init_buf.append(new String(buffer, 0, index));
				}
				query = init_buf.toString();
			}catch (IOException e){
				e.printStackTrace();
			}
			if(query != null){
				db.execSQL(query);
			}else
				mLog.e("Database: Database Create Error");
				mLog.e("        : Fail to read initializing file");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}
		
	}
}
