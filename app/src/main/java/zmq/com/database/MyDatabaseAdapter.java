package zmq.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zmq.com.model.PatientInfo;
import zmq.com.model.UploadStatus;

public class MyDatabaseAdapter {

	public static String DATABASE_NAME 		= 	"gacDB";
	public static int DATABASE_VERSION 		= 	1;
	
	MyHelper myHelper;
	SQLiteDatabase sqLiteDatabase;
	Context context;
	
	public MyDatabaseAdapter(Context c) {
		context = c;
	}
	
	public MyDatabaseAdapter openToRead() {
		
		myHelper = new MyHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		sqLiteDatabase = myHelper.getReadableDatabase();	
		return this;	
	}
	public MyDatabaseAdapter openToWrite() {
		myHelper = new MyHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		sqLiteDatabase = myHelper.getWritableDatabase();
		return this;		
	}
	
	public void close() {
		
		if(myHelper != null){
			myHelper.close();
		}
	}
	

	
	//////////////////////COMMAN_FUNCTION//////////////////////////////
	
	public long insertData(String tableName, ContentValues contentValues) {
		// TODO Auto-generated method stub
		long index = sqLiteDatabase.insert(tableName, null, contentValues);
		System.out.println("Record Saved..." + index + " "+contentValues.get(MyDatabaseAdapter.PATIENT_ID));
		return index;
	}
	
	public Cursor selectData(String tableName, String[] columns, String whereClause, String[] matchValue, String orderBy, String limit){
		
		Cursor c = sqLiteDatabase.query(tableName, columns, whereClause, matchValue, null, orderBy, limit);
		return c;
		
	}

	public ArrayList<PatientInfo> getAllContacts(String tableName) {
		ArrayList<PatientInfo> contactList = new ArrayList<PatientInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + PATIENT_TABLE;

		Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PatientInfo contact = new PatientInfo();

				contact.setRowID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ROW_ID))));
				contact.setServerDate(cursor.getString(cursor.getColumnIndex(SERVER_DATE)));
				contact.setpId(cursor.getString(cursor.getColumnIndex(PATIENT_ID)));
				contact.setFileName(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
				contact.setSaveStatus(cursor.getString(cursor.getColumnIndex(SAVE_STATUS)));
				contact.setUploadStatus(cursor.getString(cursor.getColumnIndex(UPLOADSTATUS)));
				contact.setpName(cursor.getString(cursor.getColumnIndex(PATIENT_NAME)));
				contact.setpAsha(cursor.getString(cursor.getColumnIndex(ASHA_NAME)));
				contact.setpRegime(cursor.getString(cursor.getColumnIndex(PATIENT_REGIME)));
				contact.setpDoseMode(cursor.getString(cursor.getColumnIndex(NEXTCOMPLIANCEMODE)));
				contact.setDoseDate(cursor.getString(cursor.getColumnIndex(PATIENTDOSETAKENDATE)));


				contact.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
				contact.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
				System.out.println("From Database Contact  " + contact);
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		System.out.println("From Database getAllContacts  "+contactList.size() +" "+contactList);
     return  contactList;
	}


	public HashMap<String,PatientInfo> getAllContactsInMap(String tableName) {
		HashMap<String,PatientInfo> contactList = new HashMap<String,PatientInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + PATIENT_TABLE;

		Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				PatientInfo contact = new PatientInfo();

				contact.setRowID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ROW_ID))));
				contact.setServerDate(cursor.getString(cursor.getColumnIndex(SERVER_DATE)));
				contact.setpId(cursor.getString(cursor.getColumnIndex(PATIENT_ID)));
				contact.setFileName(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
				contact.setSaveStatus(cursor.getString(cursor.getColumnIndex(SAVE_STATUS)));
				contact.setUploadStatus(cursor.getString(cursor.getColumnIndex(UPLOADSTATUS)));
				contact.setpName(cursor.getString(cursor.getColumnIndex(PATIENT_NAME)));
				contact.setpAsha(cursor.getString(cursor.getColumnIndex(ASHA_NAME)));
				contact.setpRegime(cursor.getString(cursor.getColumnIndex(PATIENT_REGIME)));
				contact.setpDoseMode(cursor.getString(cursor.getColumnIndex(NEXTCOMPLIANCEMODE)));
				contact.setDoseDate(cursor.getString(cursor.getColumnIndex(PATIENTDOSETAKENDATE)));


				contact.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
				contact.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
				System.out.println("From Database Contact  " + contact);
				// Adding contact to list
				contactList.put(contact.getpId(),contact);
			} while (cursor.moveToNext());
		}
		System.out.println("From Database getAllContactsInMap  "+contactList.size() +" "+contactList);
		return  contactList;
	}

	public int deleteData(String tableName, String whereClouse,String []whereargs){
		int rowAffected = sqLiteDatabase.delete(tableName, whereClouse, whereargs);
		System.out.println("All Deleted Successfully "+whereClouse);
		return rowAffected;		
	}
	
	public int updateData(String tableName, String whereClause, ContentValues values, String[] strings){
		int rowAffected = sqLiteDatabase.update(tableName, values, whereClause, strings);//(tableName, whereClouse, null);		
		System.out.println("Updated Successfully "+rowAffected + " "+ whereClause +" "+strings[0]);
		getCount(PATIENT_TABLE);
		return rowAffected;		
	}
	
	public PatientInfo selectSpecificData(String tableName, String[] columns, String whereClause){
		PatientInfo contact=new PatientInfo();

		Cursor cursor = sqLiteDatabase.query(tableName, columns, whereClause, null, null, null, null);

		if (cursor.moveToFirst()) {


			contact.setRowID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ROW_ID))));
			contact.setServerDate(cursor.getString(cursor.getColumnIndex(SERVER_DATE)));
			contact.setpId(cursor.getString(cursor.getColumnIndex(PATIENT_ID)));
			contact.setFileName(cursor.getString(cursor.getColumnIndex(FILE_NAME)));
			contact.setSaveStatus(cursor.getString(cursor.getColumnIndex(SAVE_STATUS)));
			contact.setUploadStatus(cursor.getString(cursor.getColumnIndex(UPLOADSTATUS)));
			contact.setpName(cursor.getString(cursor.getColumnIndex(PATIENT_NAME)));
			contact.setpAsha(cursor.getString(cursor.getColumnIndex(ASHA_NAME)));
			contact.setpRegime(cursor.getString(cursor.getColumnIndex(PATIENT_REGIME)));
			contact.setpDoseMode(cursor.getString(cursor.getColumnIndex(NEXTCOMPLIANCEMODE)));
			contact.setDoseDate(cursor.getString(cursor.getColumnIndex(PATIENTDOSETAKENDATE)));


			contact.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
			contact.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
				System.out.println("From Database selectSpecificData  " + contact);

		}
		cursor.close();
		return contact;
		
	}

   public int getCount(String tableName){

	   Cursor cursor= sqLiteDatabase.rawQuery("SELECT * FROM " + tableName ,null);
	   int count = cursor.getCount();
	   cursor.close();
       System.out.println("Table Count "+count);
	   return count;
   }
	public boolean dropTable(String tableName){

		String drop= "DROP TABLE IF EXISTS "+tableName;
		try {
			sqLiteDatabase.execSQL(drop);
			sqLiteDatabase.execSQL(MyDatabaseAdapter.SCRIPT_CREATE_TABLE_1);
			System.out.println("dropTable Successfully...........");
			return true;
		}catch (Exception e){
			return false;
		}

	}

	public Cursor getLastRecord(String tableName){

		String selectQuery= "SELECT * FROM "+tableName+" ORDER BY _id DESC LIMIT 1";

		Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
		return cursor;
	}
	
	//////////////////// TABLE => LEVEL_1 ///////////////////////////
		
//	public static final String UPLOAD_TABLE = "UploadTable";
	public static final String PATIENT_TABLE = "PatientTable";

    public static final String ROW_ID = "_id";

	public static final String SERVER_DATE = "date";
	public static final String PATIENT_ID = "Patien_Id";
	public static final String PATIENT_NAME = "Patien_Name";
	public static final String ASHA_NAME = "Asha_Name";
	public static final String PATIENT_REGIME = "patient_Regime";
	public static final String NEXTCOMPLIANCEMODE = "nextComplianceMode";
	public static final String PATIENTDOSETAKENDATE = "patientDoseTakenDate";

	public static final String FILE_NAME = "NA";
	public static final String SAVE_STATUS = "savestatus";
	public static final String UPLOADSTATUS = "Uploadstatus";

	public static final String USER_ID = "UserId";
	public static final String PASSWORD = "Password";

	public static final String SCRIPT_CREATE_TABLE_1 =
			
			"CREATE TABLE IF NOT EXISTS " + PATIENT_TABLE + "("
					+ ROW_ID + " integer primary key autoincrement, "
					+ SERVER_DATE + " TEXT NOT NULL, "
					+ PATIENT_ID + " TEXT NOT NULL, "
					+ PATIENT_NAME+ " TEXT NOT NULL, "
					+ ASHA_NAME+ " TEXT NOT NULL, "
					+ PATIENT_REGIME+ " TEXT NOT NULL, "
					+ NEXTCOMPLIANCEMODE+ " TEXT NOT NULL, "
					+ PATIENTDOSETAKENDATE+ " TEXT NOT NULL, "

					+ FILE_NAME + " TEXT NOT NULL, "
					+ SAVE_STATUS + " TEXT NOT NULL, "
					+ UPLOADSTATUS + " TEXT NOT NULL, "
					+ USER_ID + " TEXT NOT NULL, "
					+ PASSWORD + " TEXT NOT NULL "
					+")";

}
