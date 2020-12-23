package com.tibin.christmasfriend;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tibin.christmasfriend.javaClass.DBHelper;

public class FriendsViewTable extends AppCompatActivity {

    DBHelper myDb = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_view_table);

        TextView result = findViewById(R.id.friendsTable);
        result.setText(getFriendsList());
    }

    private StringBuffer getFriendsList() {
        StringBuffer buffer = new StringBuffer();
        try {
            Cursor res = myDb.getAllFriends();
            res.moveToFirst();
            while (!res.isAfterLast()) {
                buffer.append(res.getString(res.getColumnIndex("name")) + "   :   " + res.getString(res.getColumnIndex("friend")) + "\n\n");
                res.moveToNext();
            }
            return buffer;
        } catch (Exception e) {
            buffer.append("No Records Found");
            return buffer;
        }
    }
}