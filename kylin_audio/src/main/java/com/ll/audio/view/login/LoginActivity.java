package com.ll.audio.view.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ll.audio.R;
import com.ll.audio.api.ApiCenter;
import com.ll.audio.events.LoginEvent;
import com.ll.audio.model.user.UserManager;
import com.ll.audio.model.user.UserProtocol;
import com.ll.audio.view.home.HomeActivity;
import com.ll.lib_common_ui.base.BaseFragmentActivity;
import com.ll.lib_network.okhttps.response.listener.DisposeDataListener;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录面板
 *
 * @author kylin
 * @date 2020/4/16
 */
public class LoginActivity extends BaseFragmentActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener ,DisposeDataListener{
    public static final String TAG = "KYLIN";

    private TextView mLoginPhone, mUserProtocol, mPrivacyPolicy, mChildrenPrivacyPolicy;
    private CheckBox mProtocolAgree;
    private ImageView mThirdLoginWechat, mThirdLoginQQ, mThirdLoginWeibo, mThirdLoginEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initData();
    }

    private void initViews() {
        mLoginPhone = findViewById(R.id.home_login_phone);
        mUserProtocol = findViewById(R.id.home_protocol_user_protocol);
        mPrivacyPolicy = findViewById(R.id.home_protocol_privacy_policy);
        mChildrenPrivacyPolicy = findViewById(R.id.home_protocol_children_privacy_policy);
        mProtocolAgree = findViewById(R.id.home_protocol_agree_check_box);
        mThirdLoginWechat = findViewById(R.id.home_third_type_login_wechat);
        mThirdLoginQQ = findViewById(R.id.home_third_type_login_qq);
        mThirdLoginWeibo = findViewById(R.id.home_third_type_login_weibo);
        mThirdLoginEmail = findViewById(R.id.home_third_type_login_email);

        mLoginPhone.setOnClickListener(this);
        mUserProtocol.setOnClickListener(this);
        mPrivacyPolicy.setOnClickListener(this);
        mChildrenPrivacyPolicy.setOnClickListener(this);
        mThirdLoginWechat.setOnClickListener(this);
        mThirdLoginQQ.setOnClickListener(this);
        mThirdLoginWeibo.setOnClickListener(this);
        mThirdLoginEmail.setOnClickListener(this);
        mProtocolAgree.setOnCheckedChangeListener(this);
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        switch (id) {
            case R.id.home_login_phone:
                onClickLogin();
                break;
            case R.id.home_protocol_user_protocol:
                break;
            case R.id.home_protocol_privacy_policy:
                break;
            case R.id.home_protocol_children_privacy_policy:
                break;
            case R.id.home_third_type_login_wechat:
                break;
            case R.id.home_third_type_login_qq:
                break;
            case R.id.home_third_type_login_weibo:
                break;
            case R.id.home_third_type_login_email:
                break;
        }
    }

    private void onClickLogin() {
        ApiCenter.login(LoginActivity.this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onSuccess(Object responseObj) {
        try {
            UserProtocol userProtocol = (UserProtocol) responseObj;
            Log.d(TAG, "onSuccess: " + userProtocol);
            UserManager.getInstance().setUser(userProtocol);
            if (userProtocol.ecode == 0){
                EventBus.getDefault().post(new LoginEvent(LoginEvent.SUCCESS, "loginSuccess", responseObj));
                LoginActivity.this.finish();
            } else {
                EventBus.getDefault().post(new LoginEvent(LoginEvent.FAILED, "failed", responseObj));
            }
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().post(new LoginEvent(LoginEvent.FAILED, "failed"));
        }
    }

    @Override
    public void onFailed(Object responseObj) {
        EventBus.getDefault().post(new LoginEvent(LoginEvent.FAILED, "failed", responseObj));
    }
}
