package com.example.schedulemeeting;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.schedulemeeting.Adapters.RecyclerViewAddedUserAdapter;
import com.example.schedulemeeting.Adapters.RecyclerViewUserAdapter;
import com.example.schedulemeeting.Model.UserModel;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUser,recyclerViewNewAddedUser;
    private RecyclerView.LayoutManager layoutManager;
    private TextView datePickerTextView,timePickerTextView;
    private EditText userNameEditText;
    private Button searchButton,createMeetingButton;
    private CheckBox checkBoxNotify;
    private String userNameStr,TAG="MainActivity",dateStr,timeStr;
    private TextInputEditText textInputEditText;
    private RecyclerViewUserAdapter recyclerViewUserAdapter;
    private RecyclerViewAddedUserAdapter recyclerViewAddedUserAdapter;
    private ArrayList<UserModel> userList,userSearchArrayList, addedUserArrayList,finalUserArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //This method will be used to initialize all the attributes with XML
        initialize();
        //Hiding the visibility for new added user as gone
        visibilityGone(recyclerViewNewAddedUser);
        //This method will be used to get or add data into our arrayLists
        setData();
        //Setting the click listener for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //In this part we need to do certain things
                //1.First search the item through our previous list || 2.If we can find any match then add those items with their                       corresponding icon into new list || 3.Else do nothing
                userNameStr=userNameEditText.getText().toString();
                //Checking the value for user name
                if (userNameStr.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Insert name",Toast.LENGTH_LONG).show();
                }
                else{
                    for(int i=0;i<userList.size();i++){
                        if (userList.get(i).getName().toLowerCase().contains(userNameStr)
                        || userList.get(i).getName().toUpperCase().contains(userNameStr)){
                            userSearchArrayList.add(new UserModel(userList.get(i).getIcon(),userList.get(i).getName()));
                        }
                    }
                    layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                    recyclerViewUser.setLayoutManager(layoutManager);
                    recyclerViewUserAdapter =new RecyclerViewUserAdapter(getApplicationContext(),userSearchArrayList);
                    recyclerViewUser.setAdapter(recyclerViewUserAdapter);
                }
            }
        });
        //This part will be used to add an user by means of searching
        recyclerViewUser.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerViewUser, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserModel userModel = userSearchArrayList.get(position);
                if(addedUserArrayList.size()>0){
                    for (UserModel user : addedUserArrayList) {
                        if(user.getName().equals(userModel.getName())) {
                            Toast.makeText(getApplicationContext(),"Already available",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                //Adding the selected user into added list user
                addedUserArrayList.add(new UserModel(userSearchArrayList.get(position).getIcon(),userSearchArrayList.get(position)                      .getName()));
                //Setting the visibility for new added user as visible
                visibilityVisible(recyclerViewNewAddedUser);
                recyclerViewAddedUserAdapter=new RecyclerViewAddedUserAdapter(getApplicationContext(),addedUserArrayList);
                layoutManager=new LinearLayoutManager(getApplicationContext());
                recyclerViewNewAddedUser.setLayoutManager(layoutManager);
                recyclerViewNewAddedUser.setAdapter(recyclerViewAddedUserAdapter);
            }
        }));
        //Setting the change click listener for user name editText
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If username is empty then hide the view otherwise retrieve it
                userNameStr=s.toString();
                if (userNameStr.isEmpty()){
                    visibilityGone(recyclerViewUser);
                }
                else{
                    userSearchArrayList.clear();
                    visibilityVisible(recyclerViewUser);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //Setting the on click listener for date picker,Which will be useful to pick a date and set the picked date to the textView
        datePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int calYear = calendar.get(Calendar.YEAR);
                int calMonth = calendar.get(Calendar.MONTH);
                final int calDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateStr=getDay(year,month,dayOfMonth) + " - "+dayOfMonth+" "+getMonthName(year,month,dayOfMonth);
                        datePickerTextView.setText(dateStr);
                    }
                },calYear,calMonth,calDay);
                datePickerDialog.show();
            }
        });
        //Setting the on click listener for time picker,Which will be useful to pick time and set the picked time to the textView
        timePickerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog  mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeStr=getTime(selectedHour,selectedMinute);
                        timePickerTextView.setText(timeStr);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }
    //This method returns the time into our desired format
    private String getTime(int hr,int min) {
        Time tme = new Time(hr,min,0);
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }
    //This method returns only the user selected day
    public String getDay(int selectedYear,int selectedMonth,int selectedDay){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(selectedYear, selectedMonth, selectedDay-1);
        Log.v(TAG,"Day : "+simpledateformat.format(date));
        return simpledateformat.format(date);
    }
    //This method returns only the user selected month
    public String getMonthName(int selectedYear,int selectedMonth,int selectedDay){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMMM");
        Date date=new Date(selectedYear,selectedMonth,selectedDay);
        Log.v(TAG,"Month : "+simpledateformat.format(date));
        return simpledateformat.format(date);
    }

    public void initialize(){
        recyclerViewUser=findViewById(R.id.recyclerViewUsersXML);
        recyclerViewNewAddedUser=findViewById(R.id.recyclerViewNewAddedUsersXML);
        userNameEditText=findViewById(R.id.userNameSearchXML);
        datePickerTextView=findViewById(R.id.textViewForDateAsSpinnerXML);
        timePickerTextView=findViewById(R.id.textViewForTimeAsSpinnerXML);
        searchButton=findViewById(R.id.searchButtonXML);
        createMeetingButton=findViewById(R.id.createMeetingButtonXML);
        textInputEditText=findViewById(R.id.meetingNameTextInputXML);
        checkBoxNotify=findViewById(R.id.notifyCheckBoxXML);
        userList=new ArrayList<>();
        userSearchArrayList=new ArrayList<>();
        addedUserArrayList =new ArrayList<>();
        finalUserArrayList=new ArrayList<>();
    }
    public void setData(){
        //Adding userNames and their corresponding icons
        userList.add(new UserModel(R.drawable.image2,"Alexa"));
        userList.add(new UserModel(R.drawable.image1,"David"));
        userList.add(new UserModel(R.drawable.image3,"Alex"));
        userList.add(new UserModel(R.drawable.image4,"John"));
    }
    public void visibilityGone(RecyclerView recyclerView){
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
    }
    public void visibilityVisible(RecyclerView recyclerView){
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
