package com.dimas519.chargingprotection.Widget.Thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import com.dimas519.chargingprotection.Widget.WidgetInterface;

public class OwnHandler extends Handler {
    private Context context;
    private WidgetInterface widgetInterface;

    public OwnHandler(Context context, WidgetInterface widgetInterface){
        this.context=context;
        this.widgetInterface=widgetInterface;

    }


    @Override
    public void handleMessage(@NonNull Message msg) {
        int codeSwitch=msg.arg1;
        this.widgetInterface.responseFromThread(this.context,codeSwitch);
    }

    public void messageProcess(int code){
        Message message=new Message();
        message.arg1=code;



        this.sendMessage(message);
    }



}
