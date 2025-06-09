package vcmsa.projects.progproject2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //SQLiteOpenHelper usage and structure:
    //Android Developer Docs on SQLiteOpenHelper:
    //https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper

    public static final String DATABASE_NAME = "BudgetData.db";
    public static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //SQL Table creation and management:
    //General SQL schema creation with SQLite in Android referenced from:
    //https://developer.android.com/training/data-storage/sqlite

    //User authentication logic (register and validate):
    //Inspired by examples on user credential validation using SQLite from:
    //Stack Overflow:
    //https://stackoverflow.com/questions/49891292/android-studio-login-using-sqlite-database

    //Exception handling with try-catch around SQL commands:
    //Best practices inferred from Android SQLite handling guidelines:
    //https://developer.android.com/training/data-storage/sqlite#java

    @Override
    public void onCreate(SQLiteDatabase DB) {
        try {
            DB.execSQL("CREATE TABLE IF NOT EXISTS ExpenseDetails (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "date TEXT, " +
                    "time TEXT, " +
                    "amount TEXT, " +
                    "category TEXT, " +
                    "imageUri TEXT)");

            DB.execSQL("CREATE TABLE IF NOT EXISTS BudgetDetails (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "date TEXT, " +
                    "time TEXT, " +
                    "amount TEXT, " +
                    "category TEXT)");

            DB.execSQL("CREATE TABLE IF NOT EXISTS MonthlyGoals (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "month TEXT, " +
                    "goal TEXT)");

            DB.execSQL("CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT, " +
                    "password TEXT)");

            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "admin");
            contentValues.put("password", "1234");
            DB.insert("Users", null, contentValues);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        try {
            DB.execSQL("DROP TABLE IF EXISTS ExpenseDetails");
            DB.execSQL("DROP TABLE IF EXISTS BudgetDetails");
            DB.execSQL("DROP TABLE IF EXISTS MonthlyGoals");
            DB.execSQL("DROP TABLE IF EXISTS Users");
            onCreate(DB);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert Expense
    public boolean insertExpense(String date, String time, String amount, String category, String imageUri) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("amount", amount);
        contentValues.put("category", category);
        contentValues.put("imageUri", imageUri);

        long result = DB.insert("ExpenseDetails", null, contentValues);
        DB.close();
        return result != -1;
    }

    // Insert Budget
    public boolean insertBudgetData(String date, String time, String amount, String category) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("amount", amount);
        contentValues.put("category", category);

        long result = DB.insert("BudgetDetails", null, contentValues);
        DB.close();
        return result != -1;
    }

    // Insert Monthly Goal
    public boolean insertMonthlyGoal(String month, String goal) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month", month);
        contentValues.put("goal", goal);

        long result = DB.insert("MonthlyGoals", null, contentValues);
        DB.close();
        return result != -1;
    }

    // Register User
    public boolean registerUser(String username, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);

        long result = DB.insert("Users", null, contentValues);
        DB.close();
        return result != -1;
    }

//Cursor usage and rawQuery for fetching/filtering records:
//Referenced from official Android SQLite Cursor documentation:
//https://developer.android.com/reference/android/database/Cursor

    // Validate User
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Fetch all expenses
    public Cursor getAllExpenses() {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT * FROM ExpenseDetails", null);
    }

    // Fetch all budgets
    public Cursor getAllBudgets() {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT * FROM BudgetDetails", null);
    }

    // Fetch all monthly goals
    public Cursor getAllMonthlyGoals() {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery("SELECT * FROM MonthlyGoals", null);
    }

    // ✅ Get expenses in a date range
    public Cursor getExpensesBetweenDates(String startDate, String endDate) {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery(
                "SELECT * FROM ExpenseDetails WHERE date BETWEEN ? AND ?",
                new String[]{startDate, endDate}
        );
    }

    // ✅ Get budgets in a date range
    public Cursor getBudgetsBetweenDates(String startDate, String endDate) {
        SQLiteDatabase DB = this.getReadableDatabase();
        return DB.rawQuery(
                "SELECT * FROM BudgetDetails WHERE date BETWEEN ? AND ?",
                new String[]{startDate, endDate}
        );
    }
}

