package com.ems358.tfaudiomanager;

import android.content.Context;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jenuce on 2016/11/26.
 */

public class TFAudioManager implements TFPlaymanagerInterface, TFRecordManagerInterface {

    private static final TFAudioManager s_instance_ = new TFAudioManager();
    private int delegate_;
    private Map config_;
    //private List voice_list_ = new ArrayList<Map<String, String>>();
    private List voice_list_ = new ArrayList<Map<String,TFVoiceItem>>();
    private List send_file_list_ = new ArrayList<TFSendTask>();
    private List down_file_list_ = new ArrayList<String>();
    //private List white_voice_list_ = new ArrayList<Map<String, String>>();
    private List white_voice_list_ = new ArrayList<Map<String,TFVoiceItem>>();
    private ExecutorService queue_ = Executors.newSingleThreadExecutor();

    private TFPlaymanager player_ = new TFPlaymanager();
    private TFRecordManager recorder_ = new TFRecordManager();
    private boolean bStopRecord_ = true;

    private OkHttpClient http_manager_up_ = new OkHttpClient();
    private OkHttpClient http_manager_down_ = new OkHttpClient();

    private TelephonyManager telephonyManager;
    ///////////////////////////////////////////////////////////////
    public static TFAudioManager sharedManager() {
        return s_instance_;
    }

    public static void setDelegate(int delegate) {
        if (sharedManager().delegate_ != 0) {
//            Cocos2dxLuaJavaBridge.releaseLuaFunction(sharedManager().delegate_);
        }
    }

    public static void startRecord(final String config) {
        final TFAudioManager manager = TFAudioManager.sharedManager();
        manager.queue_.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    manager.bStopRecord_ = false;
                    JSONObject jsonObject = new JSONObject(config);
                    manager.config_ = jsonToMap(jsonObject);
                    manager.player_.stop();
                    manager.recorder_.start(TFAudioManager.defaultFilePath()+TFAudioManager.defaultFileName());
                    Log.e("TFAudioMenager","startRecord");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void stopRecord() {
        TFAudioManager.sharedManager().cancelRecord_(false);
    }
    private void cancelRecord_(final boolean cancel){
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                if (cancel){
                    recorder_.cancel();
                }else{
                    recorder_.stop();
                }
                Log.e("TFAudioMenager","stopRecord");
            }
        });
    }
    public static void cancelRecord() {
        TFAudioManager.sharedManager().cancelRecord_(true);
    }

    public static void addPlay(String file) {
        try {
            JSONObject jsonObject = new JSONObject(file);
            final TFAudioManager manager = TFAudioManager.sharedManager();
            synchronized (manager.down_file_list_) {
                manager.down_file_list_.add(jsonObject.getString("url"));
                if (manager.down_file_list_.size() == 1) {
                    manager.doDownLoadFile(jsonObject.getString("url"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void passPlay() {
        final TFAudioManager manager = TFAudioManager.sharedManager();
        manager.queue_.execute(new Runnable() {
            @Override
            public void run() {
                if (manager.player_.isPlaying()) {
                    manager.player_.pass();
                } else {
                    synchronized (manager.voice_list_) {
                        List array = new ArrayList<String>();
                        for (int i = 0; i < manager.voice_list_.size(); i++) {
                            Map<String, TFVoiceItem> m = (Map<String, TFVoiceItem>) manager.voice_list_.get(i);
                            array.add(m.keySet().toArray()[0]);
                        }
                        manager.player_.addPlayFiles(array);
                    }
                }
            }
        });
    }

    public static void stopPlay() {
        final TFAudioManager manager = TFAudioManager.sharedManager();
        manager.cleanPlayResouce();
        manager.stopPlay_();
    }
    private void stopPlay_(){
        queue_.execute(new Runnable() {
            @Override
            public void run() {
                player_.stop();
            }
        });
    }
    public static void cleanPlaysCache() {
        TFAudioManager manager = TFAudioManager.sharedManager();
        manager.cleanPlayCache();
    }

    public static void playOneLast(String json) {
        String uid = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            uid = jsonObject.getString("userid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (uid==null) return;
        TFAudioManager manager = TFAudioManager.sharedManager();
        synchronized (manager.white_voice_list_) {
            for (int i = 0; i < manager.white_voice_list_.size(); i++) {
                TFVoiceItem value = null;
                String key = null;
                Map m = (Map) manager.white_voice_list_.get(i);
                Iterator iterator = m.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    value = (TFVoiceItem) entry.getValue();
                    key = (String) entry.getKey();
                    if (value!=null && value.recorder_id.equals(uid)){
//                        synchronized (manager.voice_list_) {
//                            manager.voice_list_.add(m);
//
//                        }
                        if (manager.bStopRecord_) {
                            manager.player_.addToFirst(key);
                        }
                    }
                    break;
                }
            }
        }
    }

    ///////////////////////////////////////////////
    private void doDownLoadFile(final String url) {
        Uri uri = Uri.parse(url);
        String tempPath = uri.getPath();
        int index = tempPath.lastIndexOf("/");
        final String downFile = TFAudioManager.defaultFilePath()+tempPath.substring(index);
        Request request = new Request.Builder().url(url).build();
        http_manager_down_.newBuilder().writeTimeout(30, TimeUnit.SECONDS).build().newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call audioCallBack, IOException e) {
                        synchronized (down_file_list_) {
                            if (down_file_list_.size() > 0) {
                                down_file_list_.remove(0);
                            }
                            if (down_file_list_.size()>0){
                                doDownLoadFile((String) down_file_list_.get(0));
                            }
                        }
                    }

                    @Override
                    public void onResponse(Call audioCallBack, Response response) throws IOException {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        try {
                            long total = response.body().contentLength();
                            long current = 0;
                            is = response.body().byteStream();
                            fos = new FileOutputStream(downFile);
                            while ((len = is.read(buf)) != -1) {
                                current += len;
                                fos.write(buf, 0, len);
                            }
                            fos.flush();
                            synchronized (voice_list_){
                                Map m = new HashMap<String,TFVoiceItem>();
                                TFVoiceItem item = new TFVoiceItem();
                                item.localPath = downFile;
                                item.url = url;
                                final UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(url);
                                item.recorder_id = sanitizer.getValue("recorder_id");
                                item.appid = sanitizer.getValue("appid");
                                item.roomid = sanitizer.getValue("roomid");
                                item.record_len = sanitizer.getValue("record_len");
                                m.put(downFile,item);
                                voice_list_.add(m);
                                if (bStopRecord_ && telephonyManager.getCallState()==TelephonyManager.CALL_STATE_IDLE){
                                    player_.addPlayFile(downFile);
                                }
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }finally {
                            if (is!=null){
                                is.close();
                            }
                            if (fos!=null){
                                fos.close();
                            }
                            synchronized (down_file_list_){
                                if (down_file_list_.size()>0){
                                    down_file_list_.remove(0);
                                }
                                if (down_file_list_.size()>0){
                                    doDownLoadFile((String) down_file_list_.get(0));
                                }
                            }
                        }
                    }
                }
        );

    }

    //////////////////////////////////////////////////////////////////
    @Override
    public void OnWillPlay(final String filename) {
        TFVoiceItem value = null;
        int count =0;
        synchronized (voice_list_){
            for (int i = 0;i<voice_list_.size();i++){
                Map m = (Map) voice_list_.get(i);
                value = (TFVoiceItem) m.get(filename);
                if (value!=null){
                    replaceWhiteVoice(m);
                    break;
                }
            }
            if (value!=null && voice_list_.size()>0){
                count = voice_list_.size()-1;
            }else{
                count = voice_list_.size();
            }
        }
        if (value==null){
            synchronized (white_voice_list_){
                for (int i = 0;i<white_voice_list_.size();i++){
                    Map m = (Map) white_voice_list_.get(i);
                    value = (TFVoiceItem) m.get(filename);
                    if (value!=null){
                        break;
                    }
                }
            }
        }
        if (value==null){
            TFAudioManager.passPlay();
            return;
        }
        final int finalCount = count;
        final TFVoiceItem finalValue = value;
        ContextUtility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> dic = new HashMap(){{
                    put("userid",finalValue.recorder_id);
                    put("appid",finalValue.appid);
                    put("roomid",finalValue.roomid);
                    put("record_len",finalValue. record_len);
                    put("remain_count",String.format("%d", finalCount));
                }};

                JSONObject obj = new JSONObject();
                try {
                    obj.put("Event", "OnWillPlay");
                    JSONObject p = mapToJson(dic);
                    obj.put("Param", p);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                ContextUtility.getAudioCallBack().onWillPlay(AudioCallBack.WILL_PLAY, obj.toString());


//                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(delegate_, obj.toString());
            }
        });
    }

    @Override
    public void OnPlayOver(String filename) {
        Map old = null;
        synchronized (voice_list_){
            for (int i = 0;i<voice_list_.size();i++){
                Map m = (Map) voice_list_.get(i);
                if (m.get(filename)!=null){
                    old = m;
                    voice_list_.remove(i);
                    break;
                }
            }
        }
        if (old==null){
            synchronized (white_voice_list_){
                for (int i = 0;i<white_voice_list_.size();i++){
                    Map m = (Map) white_voice_list_.get(i);
                    if (m.get(filename)!=null){
                        old = m;
                        break;
                    }
                }
            }
        }
        if (old!=null){
            //replaceWhiteVoice(old);
        }else{
            FileUtility.deleteFile(filename);
        }
    }

    @Override
    public void OnWillStop() {
        ContextUtility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("Event", "OnPlayStop");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ContextUtility.getAudioCallBack().onWillStop(AudioCallBack.WILL_STOP, obj.toString());
//                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(delegate_, obj.toString());
            }
        });
    }

    ////////////////////////////////////////////////////////
    @Override
    public void onRecordStart() {
        ContextUtility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("Event", "OnRecordStart");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ContextUtility.getAudioCallBack().onRecordStart(AudioCallBack.RECORD_START, obj.toString());
//                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(delegate_, obj.toString());
            }
        });
    }

    @Override
    public void onRecordStop(final String filepath, final Date startDate, final Double soundTime, boolean bCancel) {
        if (soundTime <= 0.55) {
            bCancel = true;
        }
        if (bCancel) {
            synchronized (voice_list_){
                bStopRecord_ = true;
                List array = new ArrayList<String>();
                for (int i = 0; i < voice_list_.size(); i++) {
                    Map<String, TFVoiceItem> m = (Map<String,TFVoiceItem >) voice_list_.get(i);
                    array.add(m.keySet().toArray()[0]);
                }
                player_.addPlayFiles(array);
            }
            FileUtility.deleteFile(filepath);
            doDelegateOnRecordStop(null, soundTime, true);
        } else {

            synchronized (voice_list_) {
                bStopRecord_ = true;
                Map<String,TFVoiceItem> temp = new HashMap<String, TFVoiceItem>();
                TFVoiceItem item = new TFVoiceItem();
                item.localPath = filepath;
                item.recorder_id = (String)config_.get("userid");
                item.url = String.format("%s?recorder_id=%s&appid=%s&roomid=%s&record_time=%f&record_len=%.3f",
                        (String)config_.get("url"), (String)config_.get("userid"), (String)config_.get("appid"), (String)config_.get("roomid"), (double)startDate.getTime() / 1000, soundTime
                );
                item.appid = (String)config_.get("appid");
                item.roomid = (String)config_.get("roomid");
                item.record_len = String.format("%.3f",soundTime);
                temp.put(filepath, item);
                voice_list_.add(0,temp);
                List array = new ArrayList<String>();
                for (int i = 0; i < voice_list_.size(); i++) {
                    Map<String, TFVoiceItem> m = (Map<String, TFVoiceItem>) voice_list_.get(i);
                    array.add(m.keySet().toArray()[0]);
                }
                player_.addPlayFiles(array);
            }

            synchronized (send_file_list_) {
                TFSendTask task = new TFSendTask();
                task.filePath = filepath;
                task.interval = soundTime;
                task.date = startDate;
                send_file_list_.add(task);
                if (send_file_list_.size() == 1) {
                    sendFile(task);
                }
            }
        }
    }

    @Override
    public void onRecordTimeout() {
        TFAudioManager.stopRecord();
        ContextUtility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("Event", "OnRecordTimeOut");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ContextUtility.getAudioCallBack().onRecordTimeout(AudioCallBack.RECORD_TIMEOUT, obj.toString());
//                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(delegate_, obj.toString());
            }
        });
    }

    @Override
    public void onRecordFailed(String failureInfoString) {
        ContextUtility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("Event", "OnRecordFailed");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ContextUtility.getAudioCallBack().onRecordFailed(AudioCallBack.RECORD_FAILED, obj.toString());
//                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(delegate_, obj.toString());
            }
        });
    }

    private void doDelegateOnRecordStop(final String param, final Double interval, final boolean bCancel) {
        ContextUtility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("Event", "OnRecordStop");
                    JSONObject p = new JSONObject();
                    p.put("BCancel", bCancel ? "1" : "0");
                    p.put("SoundLen", String.format("%f", interval));
                    if (param != null) {
                        p.put("Url", param);
                    }
                    obj.put("Param", p);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                ContextUtility.getAudioCallBack().onRecordStop(AudioCallBack.RECORD_STOP, obj.toString());
//                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(delegate_, obj.toString());
            }
        });
    }

    private void sendFile(final TFSendTask task) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(task.filePath));
            int len = inputStream.available();
            byte[] buffer = new byte[len];
            inputStream.read(buffer);
            inputStream.close();
            RequestBody file_body = RequestBody.create(MediaType.parse("application/octet-stream"), buffer);
            RequestBody request_body = new MultipartBody.Builder().
                    setType(MultipartBody.FORM)
                    .addFormDataPart("userid", (String) config_.get("userid"))
                    .addFormDataPart("token", (String) config_.get("token"))
                    .addFormDataPart("appid", (String) config_.get("appid"))
                    .addFormDataPart("roomid", (String) config_.get("roomid"))
                    .addFormDataPart("record_time", String.format("%f", (double)task.date.getTime() / 1000))
                    .addFormDataPart("record_len", String.format("%.3f", task.interval))
                    .addFormDataPart("upfile", "upfile.spx", file_body)
                    .build();
            Request request = new Request.Builder()
                    .url((String) config_.get("url"))
                    .post(request_body)
                    .build();
            http_manager_up_.newBuilder().writeTimeout(30, TimeUnit.SECONDS).build().newCall(request).enqueue(
                    new Callback() {
                        @Override
                        public void onFailure(Call audioCallBack, IOException e) {
                            doDelegateOnRecordStop(null, task.interval, false);
                            synchronized (send_file_list_) {
                                send_file_list_.remove(0);
                                if (send_file_list_.size() > 0) {
                                    sendFile((TFSendTask) send_file_list_.get(0));
                                }
                            }
                        }

                        @Override
                        public void onResponse(Call audioCallBack, Response response) throws IOException {
                            String jsondata = response.body().string();
                            try {
                                JSONObject jobject = new JSONObject(jsondata);
                                String rsp_url = String.format("%s?recorder_id=%s&appid=%s&roomid=%s&record_time=%f&record_len=%.3f",
                                        jobject.get("url"), (String)config_.get("userid"), (String)config_.get("appid"), (String)config_.get("roomid"), (double)task.date.getTime() / 1000, task.interval);
                                doDelegateOnRecordStop(rsp_url, task.interval, false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                doDelegateOnRecordStop(null, task.interval, false);
                            }finally {
                                synchronized (send_file_list_) {
                                    send_file_list_.remove(0);
                                    if (send_file_list_.size() > 0) {
                                        sendFile((TFSendTask) send_file_list_.get(0));
                                    }
                                }
                            }
                        }
                    }
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            doDelegateOnRecordStop(null, task.interval, false);
            synchronized (send_file_list_) {
                doDelegateOnRecordStop(null, task.interval, false);
                send_file_list_.remove(0);
                if (send_file_list_.size() > 0) {
                    sendFile((TFSendTask) send_file_list_.get(0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            doDelegateOnRecordStop(null, task.interval, false);
            doDelegateOnRecordStop(null, task.interval, false);
            synchronized (send_file_list_) {
                send_file_list_.remove(0);
                if (send_file_list_.size() > 0) {
                    sendFile((TFSendTask) send_file_list_.get(0));
                }
            }
        }

    }


    /////////////////////
    private void initTFAudioManager() {
        player_.setDelegate(this);
        recorder_.setDelegate(this);
        telephonyManager = (TelephonyManager) ContextUtility.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener listener = new PhoneStateListener(){

            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state){
                    case TelephonyManager.CALL_STATE_RINGING:
                        cancelRecord_(true);
                        stopPlay_();
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        break;
                }
                super.onCallStateChanged(state,incomingNumber);
            }
        };
        telephonyManager.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void cleanPlayCache() {
        synchronized (white_voice_list_) {
            for (int i = 0; i < white_voice_list_.size(); i++) {
                Map<String, TFVoiceItem> m = (Map<String, TFVoiceItem>) white_voice_list_.get(i);
                FileUtility.deleteFile((String) m.keySet().toArray()[0]);
            }
            white_voice_list_.clear();
        }
    }

    private void cleanPlayResouce() {
        synchronized (down_file_list_) {
            down_file_list_.clear();
        }
        synchronized (voice_list_) {
            for (int i = 0; i < voice_list_.size(); i++) {
                Map<String, TFVoiceItem> m = (Map<String, TFVoiceItem>) voice_list_.get(i);
                FileUtility.deleteFile((String) m.keySet().toArray()[0]);
            }
            voice_list_.clear();
        }
    }
    private void replaceWhiteVoice(Map new_dic){
        TFVoiceItem value = (TFVoiceItem) new_dic.values().toArray()[0];

        synchronized (white_voice_list_){
            Boolean bHave = false;
            for (int i = 0; i < white_voice_list_.size(); i++) {
                TFVoiceItem v = null;
                Map m = (Map) white_voice_list_.get(i);
                v = (TFVoiceItem) m.values().toArray()[0];
                if (v!=null && v.recorder_id.equals(value.recorder_id)){
                    bHave = true;
                    if (!value.localPath.equals(v.localPath)){
                        FileUtility.deleteFile((String) m.keySet().toArray()[0]);
                        white_voice_list_.remove(i);
                        white_voice_list_.add(new_dic);
                    }
                    break;
                }
            }
            if (!bHave){
                white_voice_list_.add(new_dic);
            }
        }
    }

    private static String defaultFileName() {
        return "up_"+String.format("%d",new Timestamp(System.currentTimeMillis()).getTime())+".spx";
    }

    private static String defaultFilePath() {
        //return "/mnt/sdcard/";
        String ret = null;
        try {
            ret = FileUtility.getLogCachePath(ContextUtility.getApplication().getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
            ret = FileUtility.getAppDataDir()+"/voice";
        }
        FileUtility.createFolders(ret);
        return ret;
    }


    private static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }
    private static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static JSONObject mapToJson(Map<?, ?> data) {
        JSONObject object = new JSONObject();

        for (Map.Entry<?, ?> entry : data.entrySet()) {
            /*
             * Deviate from the original by checking that keys are non-null and
             * of the proper type. (We still defer validating the values).
             */
            String key = (String) entry.getKey();
            if (key == null) {
                throw new NullPointerException("key == null");
            }
            try {
                object.put(key, wrap(entry.getValue()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return object;
    }

    public static JSONArray collectionToJson(Collection data) {
        JSONArray jsonArray = new JSONArray();
        if (data != null) {
            for (Object aData : data) {
                jsonArray.put(wrap(aData));
            }
        }
        return jsonArray;
    }

    public static JSONArray arrayToJson(Object data) throws JSONException {
        if (!data.getClass().isArray()) {
            throw new JSONException("Not a primitive data: " + data.getClass());
        }
        final int length = Array.getLength(data);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < length; ++i) {
            jsonArray.put(wrap(Array.get(data, i)));
        }

        return jsonArray;
    }

    private static Object wrap(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return collectionToJson((Collection) o);
            } else if (o.getClass().isArray()) {
                return arrayToJson(o);
            }
            if (o instanceof Map) {
                return mapToJson((Map) o);
            }
            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private TFAudioManager() {
        FileUtility.deleteFolder(TFAudioManager.defaultFilePath());
        initTFAudioManager();
    }




    class TFSendTask {
        public String filePath;
        public Date date;
        public Double interval;
    }

    class TFVoiceItem{
        public String localPath;
        public String url;
        public String recorder_id;
        public String appid;
        public String roomid;
        public String record_len;
    }
}
