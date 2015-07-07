/**
 * Copyright OCTAL INFO SOLUTIONS PVT LTD.
 */
package com.hinj.app.utils;


import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hinj.app.utils.AField.FieldType;


public class Database {
	public boolean isDatabase = false;
	public static SQLiteDatabase _db = null;
	private String _dbPath;
	private String _dbKey;
	private Context _cont;
	private String nl = "\n"; 
	private ProgressBar myProgressBar;
	private int myProgress = 0;
	private TextView progressTitle;
	private TextView progressTable;
	private String progressTitleText = "";
	private String progressTableText = "";
	private Dialog pd;
	private Handler theHandle;	
	private boolean logging = false;
	
	//TODO add Context to all call to be able to show error messages generated here 
	/**
	 * Open a existing database at the given path
	 * @param dbPath Path to the database
	 */
	public Database(String dbPath, String dbKey, Context cont) {
		_dbPath = dbPath;
		_dbKey = dbKey;
	//	logging = Prefs.getLogging(cont);
		try {
			if (true) {
				// Here we know it is a SQLite 3 file
				Utility.logD("Trying to open (RW): " + dbPath, logging);
				//_db = DBViewer._db;
				_db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
				String sqlString = "PRAGMA key=\"" + _dbKey + "\";";
				_db.execSQL(sqlString);
				//_cont = cont;
				isDatabase = true;
			}
		} catch (Exception e) {
			//Utils.logE("Trying to open Exception: " + e.getMessage(), logging);
			//Utils.printStackTrace(e, logging);
			// It is not a database
			isDatabase = false;
		}
	}

	/**
	 * Close the database
	 */
	public void close() {
		//This sometimes throws SQLiteException: unable to close due to unfinalised statements
		try {
			Utility.logD("Closing database", logging);
			_db.close();
		} catch (Exception e) {
			Utility.logE("onClose", logging);
			Utility.printStackTrace(e, logging);
		}
	}

	/**
	 * Test the database if not open open it
	 */
	
	/**
	 * Retrieve all the table names of the database
	 * @return
	 */
	public String[] getTables() {
		testDB();
		String sql ="select name from sqlite_master where type = 'table' order by name";
		Cursor res = _db.rawQuery(sql, null);
		int recs = res.getCount();
		String[] tables = new String[recs + 1];
		int i = 1;
		tables[0] = "sqlite_master";
		//Utils.logD("Tables: " + recs);
		while(res.moveToNext()) {
			tables[i] = res.getString(0);
			i++;
		}
		res.close();
		return tables;
	}
	/**
	 * Enable foreign keys checking
	 */
	public void FKOn() {
		int res = 0;
		Utility.logD("Turning on foreign keys checkin", logging);
		_db.execSQL("PRAGMA foreign_keys = on");
		try {
			Cursor curs = _db.rawQuery("Pragma foreign_keys", null);
			while(curs.moveToNext()) {
				res = curs.getInt(0);
			}
			curs.close();
		} catch (Exception e) {
			//Utils.showMessage("Error", e.getLocalizedMessage(), _cont);
			Utility.logE("FKOn", logging);
			Utility.printStackTrace(e, logging);
		}
		Utility.logD("Foreign key on? " + res, logging);
		if (res == 0) {
			//Utils.showMessage("Error", "Could not turn on foreign keys - too old Android?", _cont);
		}
	}
	/**
	 * Retrieve all views from a database
	 * @return
	 */
	public String[] getIndex() {
		testDB();
		String sql ="select name from sqlite_master where type = 'index'";
		Cursor res = _db.rawQuery(sql, null);
		int recs = res.getCount();
		String[] index = new String[recs];
		int i = 0;
		//Utils.logD("Index: " + recs);
		while(res.moveToNext()) {
			index[i] = res.getString(0);
			i++;
		}
		res.close();
		return index;
	}
	
	/**
	 * Retrieve row Count from message table
	 * 
	 */
	
	public int getRowCount() {
		int rowsCount=0;
		String sql1 = "select * from message";	
		try {
			Cursor cursor = _db.rawQuery(sql1, null);
			int columns = cursor.getColumnCount();
			 rowsCount = cursor.getCount();
			Log.i("rows****************",""+rowsCount);
			cursor.close();
		} catch (Exception e) {
		
		}
		return rowsCount;

	}

	

	/**
	 * Retrieve data for table viewer
	 * @param cont Context on which to display errors
	 * @param table Name of table
	 * @param where Where clause for filter
	 * @param order Order if data sorted (by clicking on title)
	 * @param offset Offset if paging
	 * @param limit Page size
	 * @param view indication of view (with out on update trigger) 
	 * @return a list of Records
	 */
	/*public Record[] getTableDataWithWhere(Context cont, String table, String where, String order, int offset, int limit,int remaningSMS, boolean view) {
		testDB();
		String sql = "";
		if (view)
			sql = "select ";
		else
			sql = "select typeof(rowid), rowid as rowid, ";
		if (where.trim().equals(""))
			where = "";
		else
			where = " where " + where + " ";
		
		String[] fieldNames = getFieldsNames(table);
		
		for (int i = 0; i < fieldNames.length; i++) {
			sql += "typeof([" + fieldNames[i] +"]), [" + fieldNames[i] + "]";
			if (i < fieldNames.length - 1)
				 sql += ", ";
		}
		//sql="select typeof(rowid), rowid as rowid, typeof([isSend]), typeof([content]), [createTime], [talker]";
		
		//sql += " from [" + table + "] " + where + order + " limit " + limit + " offset " + offset;
		String xx=limit + ", "  +remaningSMS;
		sql += " from [" + table + "] " + where + order + " limit " + xx limit +","+ remaningSMS + " offset " + offset;
		Record[] recs = null;
		Utils.logD(sql, logging);
		try {
			Cursor cursor = _db.rawQuery(sql, null);
			int columns = cursor.getColumnCount() / 2;
			//Utils.logD("Columns: " + columns, logging);
			int rows = cursor.getCount();
			//Utils.logD("Rows = " + rows, logging);
			recs = new Record[rows];
			int i = 0;
			while(cursor.moveToNext()) {
				recs[i] = new Record();
				AField[] fields = new AField[columns];
				for(int j = 0; j < columns; j++) {
					AField fld = new AField();
					//Get the field type due to SQLites flexible handling of field types the type from 
					//the table definition can't be used
					try {
						String fldType = cursor.getString(j*2);   //TODO still problems here with BLOB fields!?!?!?!
						fld.setFieldType(getFieldType(fldType));
					} catch(Exception e) {
						fld.setFieldType(AField.FieldType.UNRESOLVED);
					}
					if (fld.getFieldType() == AField.FieldType.NULL) {
						fld.setFieldData("");
					} else if (fld.getFieldType() == AField.FieldType.BLOB) {
						fld.setFieldData("BLOB (size: " + cursor.getBlob(j*2 + 1).length + ")");
					} else if (fld.getFieldType() == AField.FieldType.UNRESOLVED) {
						fld.setFieldData("Unknown field");
					} else {
						fld.setFieldData(cursor.getString(j*2 + 1));
						
										
					}
					fields[j] = fld;
				}
				recs[i++].setFields(fields);
			}
			
			cursor.close();
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		return recs;
	}
	*/

	public Record[] getTableDataWithWhere1(Context cont, String table, String where, String order, int offset, int limit, boolean view) {
		testDB();
		String sql = "";
		if (view)
			sql = "select ";
		else
			sql = "select typeof(rowid), rowid as rowid, ";
		if (where.trim().equals(""))
			where = "";
		else
			where = " where " + where + " ";
		
		String sql1=null;
		String table1="rcontact";
		String sql_main=sql+","+sql1;
		
		sql="select  typeof([msgId]), [msgId], typeof([msgSvrId]), [msgSvrId], typeof([status]), [status], typeof([isSend]), [isSend], typeof([isShowTimer]), [isShowTimer], typeof([createTime]), [createTime], typeof([talker]), [talker], typeof([content]), [content], typeof([imgPath]), [imgPath], typeof([reserved]), [reserved], typeof([lvbuffer]), [lvbuffer],typeof([username]), [username], typeof([nickname]), [nickname] ";
		sql += " from [" + table + "] as A INNER JOIN [" + table1 + "]  as B on A.talker=B.username  " + where /*+"ORDER BY A.talker"*/ + " limit " + limit + " offset " + offset;
		
		Record[] recs = null;
		//Utils.logD(sql, logging);
		try {
			Cursor cursor = _db.rawQuery(sql, null);
			int columns = cursor.getColumnCount() / 2;			
			int rows = cursor.getCount();			
			recs = new Record[rows];
			int i = 0;
			while(cursor.moveToNext()) {
				recs[i] = new Record();
				AField[] fields = new AField[columns];
				for(int j = 0; j < columns; j++) {
					AField fld = new AField();
					
					try {
						String fldType = cursor.getString(j*2);   //TODO still problems here with BLOB fields!?!?!?!
						fld.setFieldType(getFieldType(fldType));
					} catch(Exception e) {
						fld.setFieldType(AField.FieldType.UNRESOLVED);
					}
					if (fld.getFieldType() == AField.FieldType.NULL) {
						fld.setFieldData("");
					} else if (fld.getFieldType() == AField.FieldType.BLOB) {
						fld.setFieldData("BLOB (size: " + cursor.getBlob(j*2 + 1).length + ")");
					} else if (fld.getFieldType() == AField.FieldType.UNRESOLVED) {
						fld.setFieldData("Unknown field");
					} else {
						fld.setFieldData(cursor.getString(j*2 + 1));
						
										
					}
					fields[j] = fld;
				}
				recs[i++].setFields(fields);
			}
			
			cursor.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return recs;
	}
	public Record[] getTableDataWithWhereOld1(Context cont, String table, String where, String order, int offset, int limit, boolean view) {
		testDB();
		String sql = "";
		if (view)
			sql = "select ";
		else
			sql = "select typeof(rowid), rowid as rowid, ";
		if (where.trim().equals(""))
			where = "";
		else
			where = " where " + where + " ";
		
		String[] fieldNames = getFieldsNames(table);
		
		for (int i = 0; i < fieldNames.length; i++) {
			sql += "typeof([" + fieldNames[i] +"]), [" + fieldNames[i] + "]";
			if (i < fieldNames.length - 1)
				 sql += ", ";
		}
		
	    /* String sql1="select typeof(rowid), rowid as rowid, typeof([username]), typeof([alias]), typeof([conRemark]), typeof([domainList]), typeof([nickname]), typeof([pylnitial]), typeof([quanPin]), typeof([showHead]), typeof([type]), typeof([weiboFlag]), typeof([weiboNickname]), typeof([conRemarkPYFull]), typeof([comRemarkPYShort]), typeof([lvbuff]), typeof([verifyFlag]), typeof([encryptUsername])";
		 sql="select typeof(rowid), rowid as rowid, typeof([msgId]), [msgId], typeof([msgSvrId]), [msgSvrId], typeof([type]), [type], typeof([status]), [status], typeof([isSend]), [isSend], typeof([isShowTimer]), [isShowTimer], typeof([createTime]), [createTime], typeof([talker]), [talker], typeof([content]), [content], typeof([imgPath]), [imgPath], typeof([reserved]), [reserved], typeof([lvbuffer]), [lvbuffer]";
		*/
		
		//sql += " from [" + table + "] " + where + order + " limit " + limit + " offset " + offset;
		//String xx=limit + ", "  +remaningSMS;
		sql += " from [" + table + "] " + where + order + " limit " + limit + " offset " + offset;
		Record[] recs = null;
		Utility.logD(sql, logging);
		try {
			Cursor cursor = _db.rawQuery(sql, null);
			int columns = cursor.getColumnCount() / 2;
			//Utils.logD("Columns: " + columns, logging);, typeof([])
			int rows = cursor.getCount();
			//Utils.logD("Rows = " + rows, logging);
			recs = new Record[rows];
			int i = 0;
			while(cursor.moveToNext()) {
				recs[i] = new Record();
				AField[] fields = new AField[columns];
				for(int j = 0; j < columns; j++) {
					AField fld = new AField();
					//Get the field type due to SQLites flexible handling of field types the type from 
					//the table definition can't be used
					try {
						String fldType = cursor.getString(j*2);   //TODO still problems here with BLOB fields!?!?!?!
						fld.setFieldType(getFieldType(fldType));
					} catch(Exception e) {
						fld.setFieldType(AField.FieldType.UNRESOLVED);
					}
					if (fld.getFieldType() == AField.FieldType.NULL) {
						fld.setFieldData("");
					} else if (fld.getFieldType() == AField.FieldType.BLOB) {
						fld.setFieldData("BLOB (size: " + cursor.getBlob(j*2 + 1).length + ")");
					} else if (fld.getFieldType() == AField.FieldType.UNRESOLVED) {
						fld.setFieldData("Unknown field");
					} else {
						fld.setFieldData(cursor.getString(j*2 + 1));
						
										
					}
					fields[j] = fld;
				}
				recs[i++].setFields(fields);
			}
			
			cursor.close();
		} catch (Exception e) {
			/*Utils.showMessage(_cont.getText(R.string.Error).toString(), e.getLocalizedMessage(), cont);
			Utils.logE("getTableDataWithWhere", logging);
			Utils.printStackTrace(e, logging);*/
			e.printStackTrace();
		}
		return recs;
	}

	/**
	 * Test the database if not open open it
	 */
	private void testDB() {
		if (_db == null) {
			//Utils.logD("TestDB database is null", logging);
			if (_dbPath != null) {  //then Content probably also null
				try {
					_db = SQLiteDatabase.openDatabase(_dbPath, null, SQLiteDatabase.OPEN_READWRITE);
					String sqlString = "PRAGMA key=\"" + _dbKey + "\";";
					_db.execSQL(sqlString);
				} catch (Exception e) {
					//Utils.logE("testDB " + e.getLocalizedMessage().toString(), logging);
					//Utils.printStackTrace(e, logging);
				/*	Utils.showMessage(_cont.getText(R.string.Error).toString(), //TODO 3.0 null pointer exception here _cont?
							e.getLocalizedMessage().toString() + "\n" +
							_cont.getText(R.string.StrangeErr).toString(), _cont);*/
				}
			} else {
			/*	Utils.showMessage(_cont.getText(R.string.Error).toString(),
						_cont.getText(R.string.StrangeErr).toString(), _cont);*/
			}
		} else if (!_db.isOpen()) {
			//Utils.logD("TestDB database not open", logging);
			if (_dbPath == null) {
			/*	Utils.showMessage(_cont.getText(R.string.Error).toString(),
						_cont.getText(R.string.StrangeErr).toString() + " dbPath = null", _cont);*/
			} else {
				try {
					_db = SQLiteDatabase.openDatabase(_dbPath, null, SQLiteDatabase.OPEN_READWRITE);
					String sqlString = "PRAGMA key=\"" + _dbKey + "\";";
					_db.execSQL(sqlString);
				} catch (Exception e) {
					//Utils.logE("testDB " + e.getLocalizedMessage().toString(), logging);
					//Utils.printStackTrace(e, logging);
					/*Utils.showMessage(_cont.getText(R.string.Error).toString(),
							e.getLocalizedMessage().toString() + "\n" +
							_cont.getText(R.string.StrangeErr).toString(), _cont);*/
				}
			}
		} else {

		}
	}

	/** 
	 * Return a String list with all field names of the table
	 * @param table
	 * @return
	 */
	public String[] getFieldsNames(String table) {
	//	testDB();
		String sql = "pragma table_info([" + table + "])";
		Cursor res = _db.rawQuery(sql, null); //TODO 3.3 NullPointerException here
		int cols = res.getCount();
		String[] fields = new String[cols];
		int i = 0;
		// getting field names
		while(res.moveToNext()) {
			fields[i] = res.getString(1);
			i++;
		}
		res.close();
		return fields;
	}
	

	/**
	 * This should replace getTableData and return both data and type
	 * @param table
	 * @param offset
	 * @param limit
	 * @param view
	 * @return
	 */
	public Record[] getTableData(String table, int offset, String order, int limit, boolean view) {
		Utility.logD("getTableData view " + view, logging);
		String sql = "";
		if (view)
			sql = "select ";
		else
			sql = "select typeof(rowid), rowid as rowid, ";
		String[] fieldNames = getFieldsNames(table);
		
		for (int i = 0; i < fieldNames.length; i++) {
			sql += "typeof([" + fieldNames[i] +"]), [" + fieldNames[i] + "]";
			if (i < fieldNames.length - 1)
				sql += ", ";
		}
		
		//sql="select typeof(rowid), rowid as rowid, typeof([msgId]), [msgId], typeof([msgSvrId]), [msgSvrId], typeof([type]), [type], typeof([status]), [status], typeof([isSend]), [isSend], typeof([isShowTimer]), [isShowTimer], typeof([createTime]), [createTime], typeof([talker]), [talker], typeof([content]), [content], typeof([imgPath]), [imgPath], typeof([reserved]), [reserved], typeof([lvbuffer]), [lvbuffer]";
		
		sql += " from [" + table + "] " + order + " " + " limit " + limit + " offset " + offset;
		Utility.logD(sql, logging);
		Cursor cursor = _db.rawQuery(sql, null);
		int columns = cursor.getColumnCount() / 2;
		Utility.logD("Columns: " + columns, logging);
		int rows = cursor.getCount();
		Utility.logD("Rows = " + rows, logging);
		Record[] recs = new Record[rows];
		int i = 0;
		while(cursor.moveToNext()) {
			recs[i] = new Record();
			AField[] fields = new AField[columns];
			for(int j = 0; j < columns; j++) {
				AField fld = new AField();
				//Get the field type due to SQLites flexible handling of field types the type from 
				//the table definition can't be used
				try {
					String fldType = cursor.getString(j*2);   //TODO still problems here with BLOB fields!?!?!?!
					fld.setFieldType(getFieldType(fldType));
				} catch(Exception e) {
					fld.setFieldType(AField.FieldType.UNRESOLVED);
				}
				if (fld.getFieldType() == AField.FieldType.NULL) {
					fld.setFieldData("");
				} else if (fld.getFieldType() == AField.FieldType.BLOB) {
					fld.setFieldData("BLOB (size: " + cursor.getBlob(j*2 + 1).length + ")");
				} else if (fld.getFieldType() == AField.FieldType.UNRESOLVED) {
					fld.setFieldData("Unknown field");
				} else {
					fld.setFieldData(cursor.getString(j*2 + 1));
				}
				fields[j] = fld;
			}
			recs[i++].setFields(fields);
		}
		cursor.close();
		return recs;
	}
	
	/**
	 * Retrieve the number of columns in a table
	 * @param table
	 * @return
	 */
	public int getNumCols(String table) {
		testDB();
		String sql = "select * from [" + table + "] limit 1";
		Cursor cursor = _db.rawQuery(sql, null);
		int cols = cursor.getColumnCount();
		cursor.close();
		return cols;
	}
	
	/**
	 * Translate a field type in text format to the field type as "enum"
	 * @param fldType
	 * @return
	 */
	private FieldType getFieldType(String fldType) {
		if (fldType.equalsIgnoreCase("TEXT"))
			return AField.FieldType.TEXT;
		else if (fldType.equalsIgnoreCase("INTEGER"))
			return AField.FieldType.INTEGER;
		else if (fldType.equalsIgnoreCase("REAL"))
			return AField.FieldType.REAL;
		else if (fldType.equalsIgnoreCase("BLOB"))
			return AField.FieldType.BLOB;
		else if (fldType.equalsIgnoreCase("NULL"))
			return AField.FieldType.NULL;
		return AField.FieldType.UNRESOLVED;
	}
	
	/**
	 * Return the headings for a tables structure
	 * @param table
	 * @return
	 */
	public String[] getTableStructureHeadings(String table) {
		String[] ret = {"id", "name","type","notnull","dflt_value","pk"};
		return ret;
	}
	/**
	 * Return table structure i a list of Record
	 * @param cont 
	 * @param table
	 * @return
	 */
	public Record[] getTableStructure(Context cont, String table) {
		testDB();
		String sql = "pragma table_info (["+table+"])";
		Record[] recs = null;
		Utility.logD(sql, logging);
		String[] fieldNames = getTableStructureHeadings(table);
		try {
			Cursor cursor = _db.rawQuery(sql, null);
			int columns = cursor.getColumnCount();
			int rows = cursor.getCount();
			//Utils.logD("Rows " + rows + " cols " + columns, logging);
			recs = new Record[rows];
			int i = 0;
			while(cursor.moveToNext()) {
				recs[i] = new Record();
				AField[] fields = new AField[columns];
				for(int j = 0; j < columns; j++) {
					AField fld = new AField();
					fld.setFieldData(cursor.getString(j));
					fld.setFieldType(FieldType.TEXT);
					fields[j] = fld;
					//Utils.logD("Field no " + j + " " + fld.toString(), logging);
				}
				recs[i].setFieldNames(fieldNames);
				recs[i].setFields(fields);
				i++;
			}
			cursor.close();
		} catch (Exception e) {
			//Utils.showMessage(cont.getText(R.string.Error).toString(), e.getLocalizedMessage(), cont);
			Utility.logE("getTableDataWithWhere", logging);
			Utility.printStackTrace(e, logging);
		}
		return recs;
	}
	
/*****************************
 * 
 */
	public Record[] getTableDataWithWhereGetCount(Context cont, String table, String where, String order, int offset, int limit, int limit2, boolean view) {
		testDB();
		String sql = "";
		if (view)
			sql = "select ";
		else
			sql = "select typeof(rowid), rowid as rowid, ";
		if (where.trim().equals(""))
			where = "";
		else
			where = " where " + where + " ";
		
		String[] fieldNames = getFieldsNames(table);
		
		for (int i = 0; i < fieldNames.length; i++) {
			sql += "typeof([" + fieldNames[i] +"]), [" + fieldNames[i] + "]";
			if (i < fieldNames.length - 1)
				 sql += ", ";
		}
		//sql="select typeof(rowid), rowid as rowid, typeof([isSend]), typeof([content]), [createTime], [talker]";
		
		//sql += " from [" + table + "] " + where + order + " limit " + limit + " offset " + offset;
		sql += " from [" + table + "] " + where + order + " limit " + limit +","+ limit2 + " offset " + offset;
		Record[] recs = null;
		Utility.logD(sql, logging);
		try {
			Cursor cursor = _db.rawQuery(sql, null);
			int columns = cursor.getColumnCount() / 2;
			//Utils.logD("Columns: " + columns, logging);
			int rows = cursor.getCount();
			//Utils.logD("Rows = " + rows, logging);
			recs = new Record[rows];
			int i = 0;
			while(cursor.moveToNext()) {
				recs[i] = new Record();
				AField[] fields = new AField[columns];
				for(int j = 0; j < columns; j++) {
					AField fld = new AField();
					//Get the field type due to SQLites flexible handling of field types the type from 
					//the table definition can't be used
					try {
						String fldType = cursor.getString(j*2);   //TODO still problems here with BLOB fields!?!?!?!
						fld.setFieldType(getFieldType(fldType));
					} catch(Exception e) {
						fld.setFieldType(AField.FieldType.UNRESOLVED);
					}
					if (fld.getFieldType() == AField.FieldType.NULL) {
						fld.setFieldData("");
					} else if (fld.getFieldType() == AField.FieldType.BLOB) {
						fld.setFieldData("BLOB (size: " + cursor.getBlob(j*2 + 1).length + ")");
					} else if (fld.getFieldType() == AField.FieldType.UNRESOLVED) {
						fld.setFieldData("Unknown field");
					} else {
						fld.setFieldData(cursor.getString(j*2 + 1));										
					}
					fields[j] = fld;
				}
				recs[i++].setFields(fields);
			}
			
			cursor.close();
		} catch (Exception e) {
			/*Utils.showMessage(_cont.getText(R.string.Error).toString(), e.getLocalizedMessage(), cont);
			Utils.logE("getTableDataWithWhere", logging);
			Utils.printStackTrace(e, logging);*/
		}
		return recs;
	}
}