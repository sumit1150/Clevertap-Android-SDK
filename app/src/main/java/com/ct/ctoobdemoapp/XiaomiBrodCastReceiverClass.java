package com.ct.ctoobdemoapp;

import android.content.Context;
import android.os.Bundle;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.Utils;
import com.google.android.exoplayer2.util.Log;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONException;

import java.util.List;

public class XiaomiBrodCastReceiverClass extends PushMessageReceiver {

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {

        super.onReceivePassThroughMessage(context, miPushMessage);

        try {
            String ctData = miPushMessage.getContent();
            Bundle extras = Utils.stringToBundle(ctData);
            CleverTapAPI.createNotification(context,extras);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage);
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String mRegId = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String xiaomiRegion = MiPushClient.getAppRegion(context);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                System.out.println("XToken"+mRegId);
                Log.d("MiReceiver", "Xiaomi token - " + mRegId);
                //CleverTapAPI.getDefaultInstance(context).pushXiaomiRegistrationId(mRegId, xiaomiRegion, true);
            }
        }
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String mRegId = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String xiaomiRegion = MiPushClient.getAppRegion(context);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                Log.d("MiReceiver", "Xiaomi token - " + mRegId);
               // CleverTapAPI.getDefaultInstance(context).pushXiaomiRegistrationId(mRegId, xiaomiRegion, true);
            }
        }
    }
}
