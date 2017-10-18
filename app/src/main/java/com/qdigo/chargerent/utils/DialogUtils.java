package com.qdigo.chargerent.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qdigo.chargerent.MyApplication;
import com.qdigo.chargerent.R;
import com.qdigo.chargerent.entity.data.ChragDetail;
import com.qdigo.chargerent.interfaces.DialogCallback;


/**
 * Created by jpj on 2017-03-07.
 */

public class DialogUtils {

    /**
     * 创建加载中的dialog
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消

        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.width = MyApplication.getInstance().widthPixels;
        lp.height = MyApplication.getInstance().heightPixels;

        loadingDialog.setContentView(v);// 设置布局
        return loadingDialog;

    }


    /**
     * 显示用车提示
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createUseBikeTipDialog(Context context, String msg, final DialogCallback callback) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_use_bike_tip, null);// 得到加载view
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tv_title);// 提示文字
        if(!TextUtils.isEmpty(msg)){
            tipTextView.setText(msg);// 设置加载信息
        }
        ImageView tipImageView = (ImageView) v.findViewById(R.id.iv_tip);// 提示文字
        Button confirm = (Button)v.findViewById(R.id.confirm);

        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);//点击dialog 之外可以取消
//        Window window = loadingDialog.getWindow();
//        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
//		lp.width = MyApplication.getInstance().widthPixels;
//		lp.height = MyApplication.getInstance().heightPixels;
//		window.setAttributes(lp);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setContentView(v);// 设置布局

        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                if(callback!= null){
                    callback.confirm();
                }
            }
        });
        return loadingDialog;

    }


    public static Dialog create30sDialog(Context context, String msg, final DialogCallback callback) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_30s, null);// 得到加载view
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tv_title);// 提示文字
        if(!TextUtils.isEmpty(msg)){
            tipTextView.setText(msg);// 设置加载信息
        }

        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);//点击dialog 之外可以取消
        Button confirm = (Button)v.findViewById(R.id.confirm);
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                if(callback!= null){
                    callback.confirm();
                }
            }
        });
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.width = MyApplication.getInstance().widthPixels;
        lp.height = MyApplication.getInstance().heightPixels;

        loadingDialog.setContentView(v);// 设置布局
        return loadingDialog;

    }
    public static Dialog createChargeDetail(Context context, ChragDetail detail, final DialogCallback callback) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.window_charge_detail, null);// 得到加载view
        FrameLayout windowInfo = (FrameLayout) v.findViewById(R.id.windowInfo);
//        TextView chragCountTv = (TextView) v.findViewById(R.id.chragCountTv);
        TextView CarCountTv = (TextView) v.findViewById(R.id.CarCountTv);
        TextView disTv = (TextView) v.findViewById(R.id.disTv);
        ImageView chragImg = (ImageView) v.findViewById(R.id.chragImg);
        TextView addressTv = (TextView) v.findViewById(R.id.addressTv);
        TextView nameTv = (TextView) v.findViewById(R.id.nameTv);

        nameTv.setText(detail.stationName);
        addressTv.setText(detail.address);
        Glide.with(context)
                .load(detail.picUrl)
                .into(chragImg);
//        ImageLoader.getInstance().displayImage(detail.picUrl, chragImg,ImageLoadOptions.getOptions());
        disTv.setText("说明："+detail.note);
        CarCountTv.setText("可用车辆："+detail.bikeCount+"辆");
//        chragCountTv.setText("充电桩数："+detail.chargeStationsCount+"个");

        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(true);//点击dialog 之外不可以取消
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.width = MyApplication.getInstance().widthPixels;
        lp.height = MyApplication.getInstance().heightPixels;

        loadingDialog.setContentView(v);// 设置布局
        return loadingDialog;

    }


    public static void showSelectPicture(Context ctx, final DialogCallback callback) {
        final Dialog dialog = createDialog(ctx, R.layout.dialog_select_picture_layout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.findViewById(R.id.cameraBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null) {
                    callback.camareClick();
                }
            }
        });

        dialog.findViewById(R.id.picBtn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callback != null) {
                    callback.picClick();
                }
            }
        });

        dialog.show();

    }
    public static Dialog createDialog(Context ctx, int resLayout) {
        Dialog dialog = new Dialog(ctx, R.style.custom_dialog);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = MyApplication.getInstance().widthPixels;
        lp.height = MyApplication.getInstance().heightPixels;
        dialog.setContentView(resLayout);
        return dialog;
    }
    public static Dialog createOnkeyDialog(Context context, String num, String address, String type, String detail, final DialogCallback callback) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_onkey_save, null);// 得到加载view
        Button cancel = (Button) v.findViewById(R.id.cancel);
        TextView time = (TextView) v.findViewById(R.id.tv_time);
        TextView vol = (TextView) v.findViewById(R.id.tv_vol);
        TextView dot = (TextView) v.findViewById(R.id.tv_dot);
        TextView cost = (TextView) v.findViewById(R.id.tv_cost);

        if (!TextUtils.isEmpty(num)) {
            time.setText(num);// 设置加载信息
        }
        if (!TextUtils.isEmpty(address)) {
            vol.setText(address);// 设置加载信息
        }
        if (!TextUtils.isEmpty(type)) {
            dot.setText(type);// 设置加载信息
        }
        if (!TextUtils.isEmpty(detail)) {
            cost.setText(detail);// 设置加载信息
        }

        final Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);//点击dialog 之外不可以取消
        Button confirm = (Button) v.findViewById(R.id.confirm);
        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                if (callback != null) {
                    callback.confirm();
                }
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.width = MyApplication.getInstance().widthPixels;
        lp.height = MyApplication.getInstance().heightPixels;

        loadingDialog.setContentView(v);// 设置布局
        return loadingDialog;

    }

}
