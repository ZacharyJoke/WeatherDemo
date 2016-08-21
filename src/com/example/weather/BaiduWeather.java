package com.example.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import android.net.IpPrefix;
import android.util.Log;
import android.widget.TextView;



public class BaiduWeather {
	public String result;
	public Thread myThread;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = this.result+result;
	}

	public BaiduWeather(final String city,final TextView textView) {
		super();
		try {
			 myThread= new Thread(){
				public void run() {
					if(GetWeater(city)!=null)
					{
						try {
							textView.setText(getResult());
							System.out.println("到达了吗");
							System.out.println(getResult());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			 };
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 myThread.start();
	}
	
	



	// 获取天气信息
	public  String GetWeater(String city) {
		String buffstr = null;
		try {
			String xml = this.GetXmlCode(URLEncoder.encode(city, "utf-8")); // 设置输入城市的编码，以满足百度天气api需要
			buffstr = this.readStringXml(xml, city);// 调用xml解析函数
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffstr;
	}

	public String GetXmlCode(String city) throws UnsupportedEncodingException {
		
		String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location="
				+ city + "&output=xml&ak=FK9mkfdQsloEngodbFl4FeY3";

		HttpURLConnection connection = null;
		 StringBuffer buffer = null;  
        try {     
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true); 
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            InputStreamReader inputStreamReader =new InputStreamReader(in,"utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            
            buffer=new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                
                buffer.append(line);
            }
            
            reader.close();  
            in.close();  
            inputStreamReader.close();
            connection.disconnect(); 
            
        	}catch (Exception e) {
				// TODO: handle exception
			}
            Log.i("TAG", buffer.toString());
        	return buffer.toString(); // 返回获取的xml字符串
        	
    	}
//				URL url =new URL(requestUrl);
//				connection =(HttpsURLConnection)url.openConnection();
//				connection.setRequestMethod("GET");
//				connection.setConnectTimeout(8000);
//				connection.setReadTimeout(8000);
//				// 获取输入流
//				InputStream in = connection.getInputStream();
//				BufferedReader reader =new BufferedReader(
//						new InputStreamReader(in));
//				// 读取返回结果
////				StringBuilder response =new StringBuilder();
//				String line;
//				buffer = new StringBuffer(); 
//				while ((line=reader.readLine())!=null) {
//					buffer.append(line);
//				}
			// 释放资源
//			connection.disconnect();
		
	

	public String readStringXml( String xml, String ifcity) {
		StringBuffer buff = new StringBuffer(); // 用来拼接天气信息的
		List listdate = null; // 用来存放日期

		List listday = null; // 用来存放白天图片路径信息

		List listweather = null;

		List listwind = null;

		List listtem = null;
		
	

			try {
				// 读取并解析XML文档
				// 下面的是通过解析xml字符串的
				Document doc;
				doc = DocumentHelper.parseText(xml);
				// 将字符串转为XML
				Element rootElt = doc.getRootElement(); // 获取根节点
				Iterator iter = rootElt.elementIterator("results"); // 获取根节点下的子节点results
				String status = rootElt.elementText("status"); // 获取状态，如果等于success,表示有数据
				if (!status.equals("success"))
					return "暂无数据"; // 如果不存在数据，直接返回
				String date = rootElt.elementText("date"); // 获取根节点下的，当天日期
				buff.append(date + "\n");
				// 遍历results节点
				while (iter.hasNext()) {
					Element recordEle = (Element) iter.next();
					Iterator iters = recordEle.elementIterator("weather_data"); //
					// 遍历results节点下的weather_data节点
					while (iters.hasNext()) {
						Element itemEle = (Element) iters.next();

						listdate = itemEle.elements("date");
						// 将date集合放到listdate中
						listday = itemEle.elements("dayPictureUrl");
						listweather = itemEle.elements("weather");
						listwind = itemEle.elements("wind");
						listtem = itemEle.elements("temperature");
					}

					for (int i = 0; i < listdate.size(); i++) { // 由于每一个list.size都相等，这里统一处理

						Element eledate = (Element) listdate.get(i); // 依次取出date
						//					Element eleday = (Element) listday.get(i);// ..
						Element eleweather = (Element) listweather.get(i);
						Element elewind = (Element) listwind.get(i);
						Element eletem = (Element) listtem.get(i);

						buff.append(eledate.getText() + "==="
								+ eleweather.getText() + "==="
								+ elewind.getText() + "===" + eletem.getText()
								+ "\n"); // 拼接信息
						setResult(buff.toString());
						System.out.println(buff.toString());
						
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		return buff.toString();

	}


//	public static void main(String[] args) {
//
//		// 测试
//		System.out.println(GetWeater("郑州").toString());
//
//	}
}