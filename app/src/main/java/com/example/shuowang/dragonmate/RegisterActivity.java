package com.example.shuowang.dragonmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends BaseActivity {
    EditText et_username,et_email,et_phone,et_password,et_passwordcheck;
    RadioGroup rg_sex;
    RadioButton rb_male,rb_female;
    Button btn_register;
    boolean sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_username = (EditText)findViewById(R.id.R_UserNameText);
        et_email = (EditText)findViewById(R.id.R_EmailText);
        et_phone = (EditText)findViewById(R.id.R_PhoneText);
        et_password = (EditText)findViewById(R.id.R_PasswordText);
        et_passwordcheck = (EditText)findViewById(R.id.R_PasswordCheckText);
        rg_sex = (RadioGroup)findViewById(R.id.R_SexChoiceRadioGroup);
        rb_male = (RadioButton)findViewById(R.id.R_MaleChoice);
        rb_female = (RadioButton)findViewById(R.id.R_FemaleChoice);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==rb_male.getId()){
                    sex = true;
                }
                else if(checkedId==rb_female.getId()){
                    sex = false;
                }
            }
        });
        btn_register = (Button)findViewById(R.id.R_RegisterButton);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String email = et_email.getText().toString();
                String phone = et_phone.getText().toString();
                String password = et_password.getText().toString();
                String passwordcheck = et_passwordcheck.getText().toString();
                MyUser user = new MyUser();
                user.setUsername(username);
                user.setEmail(email);
                user.setMobilePhoneNumber(phone);
                user.setPassword(password);
                user.setSex(sex);
                if(password.equals(passwordcheck)&&!email.equals("")&&!phone.equals("")){
                    user.signUp(RegisterActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            toast("成功登陆");
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            toast("登录失败，请检查您的网络设置");
                        }
                    });
                }
                else{
                    toast("填写错误！");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
