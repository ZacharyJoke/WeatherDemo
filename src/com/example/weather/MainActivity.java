package com.example.weather;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener{
	private EditText editText;
    private Button button;
    private TextView textView;
    

	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText =(EditText)findViewById(R.id.edt);
        button = (Button) findViewById(R.id.btn);
        textView = (TextView) findViewById(R.id.tv);
        button.setOnClickListener(this);
      
    }

  

    //解析XML数据
   

	@Override
	public void onClick(View v) {
		if(v.equals(button)){
		 Log.i("TAG", "点击了Button");
		 
		  String city = editText.getText().toString();
		  Log.i("TAG", city);
		  if(city==null)
		  {
			  city="北京";
		  }
		  BaiduWeather weather=new BaiduWeather(city,textView);
		  
//		  weather.myThread.start();
		  
//		  String text = weather.GetWeater(city);
//		  Log.i("TAG", text);
//		  textView.setText(text);
		  
	        
		}
		
	}
}