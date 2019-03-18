package hackfest_test;

import java.net.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.json.*;

import com.fazecast.jSerialComm.SerialPort;

import java.io.*;

public class WalmartCart {
    public static JSONObject items;
    static String portName = "COM3";
    public static void main(String[] args) throws Exception{
       //TODO
        WalmartCart ob = new WalmartCart();
        items = ob.getItems();
        String cartId = "AA:BB:CC:DD";
        Set<String> tags = new HashSet<String>();
        SerialPort ports[] = SerialPort.getCommPorts();
        for(SerialPort port : ports) {
            if(port.getSystemPortName().toString().equals(portName)) {
                if(port.openPort()) {
                    System.out.println("port Open");
                    port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
                    Scanner data = new Scanner(port.getInputStream());
                    while(data.hasNextLine()) {
                        
                            String number = null;
                            try{
                                number = data.nextLine().toString();
                                System.out.println(number);
                                 tags.add(number);
                                 }
                            catch(Exception e){
                                 
                            }
                            if(tags.size()>=5)
                                break;

                    }
                    data.close();
                    port.closePort();
                    System.out.println(tags.size());
                }else {
                    System.out.println("cannot open port");
                    return;
                }
            }
            
        }
        JSONObject cart = new JSONObject();
        Map<String,JSONObject> map = new HashMap<String,JSONObject>();
        for(String i:tags) {
            JSONObject item = ob.getItemByTag(i);
            map.put(i,item);
        }
        cart.put("id", cartId);
        cart.put("checkout", map);
        System.out.println(cart.toString());
        System.out.println(ob.postRequest(cart));
   }

    private String postRequest(JSONObject cart)throws Exception {
        // TODO Auto-generated method stub
        URL url = new URL("https://devsena-hf-19.firebaseio.com/carts.json");
       HttpURLConnection con = (HttpURLConnection) url.openConnection();
       con.setRequestMethod("POST");

       //String encodedData = URLEncoder.encode( cart.toString(), "UTF-8" );
       con.setDoOutput(true);
       con.setRequestProperty("Content-Type", "application/json");
       OutputStream os = con.getOutputStream();
       os.write(cart.toString().getBytes());

       int status = con.getResponseCode();
       if(status!=200)
           return null;
       BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
       String inputLine;
       StringBuffer content = new StringBuffer();
       while ((inputLine = in.readLine()) != null) {
           content.append(inputLine);
       }
       in.close();

       return content.toString();
    }

    String getRequest(String dir) throws Exception{
        URL url = new URL("https://devsena-hf-19.firebaseio.com/"+dir);
       HttpURLConnection con = (HttpURLConnection) url.openConnection();
       con.setRequestMethod("GET");

       int status = con.getResponseCode();
       if(status!=200)
           return null;
       BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
       String inputLine;
       StringBuffer content = new StringBuffer();
       while ((inputLine = in.readLine()) != null) {
           content.append(inputLine);
       }
       in.close();

       return content.toString();
    }
    
   JSONObject getItems()throws Exception{
       //?orderBy=\"tag\"&equalTo=\""+tag+"\"
       JSONObject jsonObject = new JSONObject(getRequest("items.json"));
       return jsonObject;
   }

   JSONObject getCarts() throws Exception{
       return new JSONObject(getRequest("carts.json"));
   }

   JSONObject getItemByTag(String tag) throws JSONException {
       if(items.has(tag))
           return (JSONObject) items.get(tag);
       return null;
   }
}
