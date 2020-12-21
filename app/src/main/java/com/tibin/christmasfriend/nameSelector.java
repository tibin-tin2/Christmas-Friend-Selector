package com.tibin.christmasfriend;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.tibin.christmasfriend.javaClass.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class nameSelector extends AppCompatActivity {

    List<Button> buttons;
    List<Integer> buttonId;
    DBHelper myDb = new DBHelper(this);

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_name_selector);
//
//        LinearLayout linearLayout = findViewById(R.id.linearLayout);
//
//        generateButtons();
//        for (int i = 0; i < buttons.size(); i++) {
//            Button button = buttons.get(i);
//            linearLayout.addView(button);
//            buttonOnClick(i);
//        }
//    }

    @Override
    public void onResume() {

        super.onResume();
        setContentView(R.layout.activity_name_selector);

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        generateButtons();
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            linearLayout.addView(button);
            buttonOnClick(i);
        }
    }

    private List<String> getNameList() {
        return myDb.getAllNames();
    }

    private List<Map<String, Boolean>> getNameListWithisSelected() {
        return myDb.getAllNamesWithIsSelected();
    }

    private void generateButtons() {
        List<String> names = getNameList();
        List<Map<String, Boolean>> namesWithIsSelected = getNameListWithisSelected();
        buttons = new ArrayList<>();
        buttonId = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            Map<String, Boolean> myMap = namesWithIsSelected.get(i);
            Button button = new Button(this);
            button.setText(names.get(i));
            button.setId(i);
            button.setTag(names.get(i));
            button.setEnabled(!myMap.get((String) names.get(i)));
            buttons.add(button);
            buttonId.add(button.getId());
        }
    }

    private Button getButtonById(int id) {
        Button button = findViewById(id);
        return button;
    }

    private void getSpinActivity(String name) {
        Intent intent = new Intent(this, SpinActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    private void buttonOnClick(int id) {
        Button button = getButtonById(id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpinActivity((String) button.getText());
            }
        });
    }
}