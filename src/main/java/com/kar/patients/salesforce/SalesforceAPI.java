package com.kar.patients.salesforce;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kar.patients.entities.Patient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;


public class SalesforceAPI  {
    private static final long serialVersionUID = 1L;
    static final String PASS = "Colombia88";
    static final String USERNAME = "omarpolaniasf2@gmail.com";
    static final String PASSWORD = PASS;
    static final String LOGINURL = "https://person59-dev-ed.my.salesforce.com";
    static final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
    static final String CLIENTID = "3MVG9p1Q1BCe9GmB5MQukYhQ9j1j2WCnxkIH2uispwHO.jpSUdmjREwuR6Ymqf.uKHdQoHsuqTswaAE9bBWmm";
    static final String CLIENTSECRET = "862E363BA8FE039C12C2F92B231E9F047F227BC2F46922AE7FBF7882F564EC11";
    static final String CREATE_BULK_URL = "/services/data/v53.0/jobs/ingest";
    static final String UPLOAD_BULK_DATA = "/services/data/v53.0/jobs/ingest/";


    public  String getAuthToken() throws Exception {
        String loginAccessToken = "";
        try {

            CloseableHttpClient httpclient = HttpClientBuilder.create().build();
            String loginURL = LOGINURL + GRANTSERVICE + "&client_id=" + CLIENTID + "&client_secret=" + CLIENTSECRET + "&username=" + USERNAME + "&password=" + PASSWORD;
            HttpPost httpPost = new HttpPost(loginURL);
            HttpResponse resp = httpclient.execute(httpPost);
            final int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw  new Exception("Error authenticating to Force.com: " + statusCode);

            }
            String getResult  = EntityUtils.toString(resp.getEntity());
            JSONObject jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
            loginAccessToken = jsonObject.getString("access_token");
            String loginInstanceUrl = jsonObject.getString("instance_url");
            System.out.println(resp.getStatusLine());
            System.out.println("Successful login");
            System.out.println("  instance URL: " + loginInstanceUrl);
            System.out.println("  access token/session ID: " + loginAccessToken);
            httpPost.releaseConnection();


        } catch (ClientProtocolException cpException) {
            cpException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return loginAccessToken;

    }

    public String createBatch(String token,String SalesforceObject) throws Exception {

        String batchId = "";
        String body = "{\n" +
                "  \"operation\" : \"upsert\",\n" +
                "  \"object\" : \""+SalesforceObject+"\",\n" +
                "  \"contentType\" : \"CSV\",\n" +
                "\"externalIdFieldName\":\"patientsId__c\",\n" +
                "  \"lineEnding\" : \"LF\"\n" +
                "}";
        HttpPost httpPost = new HttpPost(LOGINURL+CREATE_BULK_URL);
        httpPost.setEntity(new StringEntity(body));
        httpPost.setHeader("Authorization", "Authorization: Bearer "+token);
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse resp = httpclient.execute(httpPost);
        final int statusCode = resp.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw  new Exception("Error creating batch on Force.com: " + statusCode);

        }
        if (statusCode == HttpStatus.SC_OK){
            String getResult  = EntityUtils.toString(resp.getEntity());
            JSONObject jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
            batchId = jsonObject.getString("id");
        }
        System.out.println("  BULK Created: "+ batchId);
        return batchId;
    }

    public void uploadBatchData(String token,String batchId,String data) throws Exception {
        HttpPut httpput = new HttpPut(LOGINURL+UPLOAD_BULK_DATA+batchId+"/batches");
        httpput.setEntity(new StringEntity(data));
        httpput.setHeader("Authorization", "Authorization: Bearer "+token);
        httpput.setHeader("Content-type", "text/csv");
        httpput.setHeader("Accept", "application/json");
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse resp = httpclient.execute(httpput);
        final int statusCode = resp.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_CREATED) {
            throw  new Exception("Error creating batch on Force.com: " + statusCode);

        }
        if (statusCode == HttpStatus.SC_CREATED){
            System.out.println("  BULK data UPLOADED: " );
        }

    }

    public void completeBatch(String token,String batchId) throws Exception {
        String state = "";
        HttpPatch httpPatch = new HttpPatch(LOGINURL+UPLOAD_BULK_DATA+batchId);
        httpPatch.setEntity(new StringEntity("""
                {
                   "state" : "UploadComplete"
                }"""));
        httpPatch.setHeader("Authorization", "Authorization: Bearer "+token);
        httpPatch.setHeader("Content-type", "application/json");
        //httpPatch.setHeader("Accept", "application/json");
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpResponse resp = httpclient.execute(httpPatch);
        final int statusCode = resp.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw  new Exception("Error completing batch on Force.com: " + statusCode);

        }
        if (statusCode == HttpStatus.SC_OK){
            String getResult  = EntityUtils.toString(resp.getEntity());
            JSONObject jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
            state = jsonObject.getString("state");
            System.out.println("  BULK state: " +state);

        }

    }


    public static void main(String[] args) throws Exception {
        SalesforceAPI salesforceAPI = new SalesforceAPI();
       String accessToken = salesforceAPI.getAuthToken();
        salesforceAPI.createBatch(accessToken,"Contact");
        salesforceAPI.uploadBatchData(accessToken,"7505f000003Iv0CAAS","");




    }
}