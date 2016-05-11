package com.hust.radiofeeler.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hust.radiofeeler.Mina.MainActivity;
import com.hust.radiofeeler.R;


public class LoginActivity extends Activity  {
	protected static final String TAG = "LoginActivity";

	private EditText mIdEditText; // Id编辑框
	private EditText mPwdEditText; // 密码编辑
	private ImageView mMoreUser; // 下拉图标
	private Animation mTranslate; // 位移动画
	private LinearLayout mLoginLinearLayout; // 登陆内容的容器
	private Button mLoginButton; // 登录


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
		mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate); // 初始化动画对象
		mLoginLinearLayout.startAnimation(mTranslate); // Y轴水平移动



		mLoginButton= (Button) findViewById(R.id.login_btnLogin);
		mLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, MainActivity.class);


				startActivity(i);
				finish();
			}
		});


	}



}
