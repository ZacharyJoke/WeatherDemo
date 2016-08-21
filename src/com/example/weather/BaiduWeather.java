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
							System.out.println("��������");
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
	
	



	// ��ȡ������Ϣ
	public  String GetWeater(String city) {
		String buffstr = null;
		try {
			String xml = this.GetXmlCode(URLEncoder.encode(city, "utf-8")); // ����������еı��룬������ٶ�����api��Ҫ
			buffstr = this.readStringXml(xml, city);// ����xml��������
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
        	return buffer.toString(); // ���ػ�ȡ��xml�ַ���
        	
    	}
//				URL url =new URL(requestUrl);
//				connection =(HttpsURLConnection)url.openConnection();
//				connection.setRequestMethod("GET");
//				connection.setConnectTimeout(8000);
//				connection.setReadTimeout(8000);
//				// ��ȡ������
//				InputStream in = connection.getInputStream();
//				BufferedReader reader =new BufferedReader(
//						new InputStreamReader(in));
//				// ��ȡ���ؽ��
////				StringBuilder response =new StringBuilder();
//				String line;
//				buffer = new StringBuffer(); 
//				while ((line=reader.readLine())!=null) {
//					buffer.append(line);
//				}
			// �ͷ���Դ
//			connection.disconnect();
		
	

	public String readStringXml( String xml, String ifcity) {
		StringBuffer buff = new StringBuffer(); // ����ƴ��������Ϣ��
		List listdate = null; // �����������

		List listday = null; // ������Ű���ͼƬ·����Ϣ

		List listweather = null;

		List listwind = null;

		List listtem = null;
		
	

			try {
				// ��ȡ������XML�ĵ�
				// �������ͨ������xml�ַ�����
				Document doc;
				doc = DocumentHelper.parseText(xml);
				// ���ַ���תΪXML
				Element rootElt = doc.getRootElement(); // ��ȡ���ڵ�
				Iterator iter = rootElt.elementIterator("results"); // ��ȡ���ڵ��µ��ӽڵ�results
				String status = rootElt.elementText("status"); // ��ȡ״̬���������success,��ʾ������
				if (!status.equals("success"))
					return "��������"; // ������������ݣ�ֱ�ӷ���
				String date = rootElt.elementText("date"); // ��ȡ���ڵ��µģ���������
				buff.append(date + "\n");
				// ����results�ڵ�
				while (iter.hasNext()) {
					Element recordEle = (Element) iter.next();
					Iterator iters = recordEle.elementIterator("weather_data"); //
					// ����results�ڵ��µ�weather_data�ڵ�
					while (iters.hasNext()) {
						Element itemEle = (Element) iters.next();

						listdate = itemEle.elements("date");
						// ��date���Ϸŵ�listdate��
						listday = itemEle.elements("dayPictureUrl");
						listweather = itemEle.elements("weather");
						listwind = itemEle.elements("wind");
						listtem = itemEle.elements("temperature");
					}

					for (int i = 0; i < listdate.size(); i++) { // ����ÿһ��list.size����ȣ�����ͳһ����

						Element eledate = (Element) listdate.get(i); // ����ȡ��date
						//					Element eleday = (Element) listday.get(i);// ..
						Element eleweather = (Element) listweather.get(i);
						Element elewind = (Element) listwind.get(i);
						Element eletem = (Element) listtem.get(i);

						buff.append(eledate.getText() + "==="
								+ eleweather.getText() + "==="
								+ elewind.getText() + "===" + eletem.getText()
								+ "\n"); // ƴ����Ϣ
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
//		// ����
//		System.out.println(GetWeater("֣��").toString());
//
//	}
}