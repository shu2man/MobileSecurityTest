package com.example.hxs15.mobilesecuritytest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by hxs15 on 2018-6-23.
 */

public class SignInActivity extends AppCompatActivity{
    private SharedPreferences sharedPreferences;
    private int SignMode=1;
    private String defaultString="官方默认字段";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initLogin();
    }

    public void initLogin(){
        sharedPreferences=getApplicationContext().getSharedPreferences("MyPreference",MODE_PRIVATE);
        String lastUser=sharedPreferences.getString("lastUser",null);
        String lastPswd=sharedPreferences.getString("lastPswd",null);
        //lastPswd非空表示已登录，直接跳转到主页，否则重新登录
        if(!(TextUtils.isEmpty(lastUser) || TextUtils.isEmpty(lastPswd))){
            goToMain(lastUser,lastPswd);
        }
        else if(!TextUtils.isEmpty(lastUser)){
            EditText name=findViewById(R.id.login_name_edit);
            name.setText(lastUser);
        }
    }

    public void registerIn(View view){
        login();
    }

    public void login(){
        EditText name=findViewById(R.id.login_name_edit);
        EditText pswd=findViewById(R.id.login_password_edit);
        EditText cpsd=findViewById(R.id.confirm_password_edit);
        if(SignMode==1){
            if(!(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(pswd.getText()))){
                boolean isPsdRight=confirmUser(name.getText().toString(),pswd.getText().toString());
                if(isPsdRight){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("lastUser",name.getText().toString());
                    editor.putString("lastPswd",MD5Utils.myMD5Encrypt(pswd.getText().toString()));
                    editor.apply();
                    goToMain(name.getText().toString(),pswd.getText().toString());
                }
                else{
                    Toast.makeText(this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
            else if(TextUtils.isEmpty(name.getText()) && TextUtils.isEmpty(pswd.getText())){
                Toast.makeText(this,"输入为空",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(name.getText())){
                name.setError("用户名为空");
            }
            else if(TextUtils.isEmpty(pswd.getText())){
                pswd.setError("密码为空");
            }
        }
        else if(SignMode==2){
            if(!(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(pswd.getText()) || TextUtils.isEmpty(cpsd.getText()))){
                if(pswd.getText().toString().equals(cpsd.getText().toString())){
                    String spname=sharedPreferences.getString(name.getText().toString(),null);
                    if (!TextUtils.isEmpty(spname)) {
                        Toast.makeText(this,"用户名已经被使用啦！",Toast.LENGTH_SHORT).show();
                    }
                    else if(pswd.getText().toString().length()<8){
                        Toast.makeText(this,"密码长度不能小于8哦",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("lastUser",name.getText().toString());
                        editor.putString("lastPswd",MD5Utils.myMD5Encrypt(pswd.getText().toString()));
                        //存储为SharedPreference
                        editor.putString(name.getText().toString(),MD5Utils.myMD5Encrypt(pswd.getText().toString()));
                        //存储为File
                        saveToFile(name.getText().toString(),pswd.getText().toString());
                        //存储为SQL DB
                        saveToDatabase(name.getText().toString(),pswd.getText().toString());
                        editor.apply();
                        goToMain(name.getText().toString(),pswd.getText().toString());
                    }
                }
                else{
                    Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
            else if(TextUtils.isEmpty(name.getText()) && TextUtils.isEmpty(pswd.getText()) && TextUtils.isEmpty(cpsd.getText())){
                Toast.makeText(this,"输入为空",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(name.getText())){
                name.setError("用户名为空");
            }
            else if(TextUtils.isEmpty(pswd.getText())){
                pswd.setError("密码为空");
            }
            else if(TextUtils.isEmpty(cpsd.getText())){
                cpsd.setError("请确认密码");
            }
        }
    }

    public void goToMain(String name,String pswd){
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("user",name);
        intent.putExtra("pswd",pswd);
        startActivity(intent);
        this.finish();
    }

    public boolean confirmUser(String name,String psd){
        String spswd=sharedPreferences.getString(name,null);
        if(spswd==null) return false;
        else if(spswd.equals(MD5Utils.myMD5Encrypt(psd))) return true;
        else return false;
    }

    public void goToRegister(View view){
        SignMode=2;
        registerVisibility(2);
    }

    public void registerVisibility(int mode){//1-exist user login,  2-new user register
        ConstraintLayout CL1=(ConstraintLayout)findViewById(R.id.login_alternative_container);
        TextView tv=(TextView)findViewById(R.id.confirm_password_text);
        TextView ltv=findViewById(R.id.login_has_count);
        EditText et=(EditText)findViewById(R.id.confirm_password_edit);
        EditText etname=(EditText)findViewById(R.id.login_name_edit);
        Button btn=(Button)findViewById(R.id.login_btn);
        if(mode==1){
            CL1.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
            ltv.setVisibility(View.GONE);
            et.setVisibility(View.GONE);
            btn.setText("注册");
        }
        else if(mode==2){
            CL1.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            ltv.setVisibility(View.VISIBLE);
            et.setVisibility(View.VISIBLE);
            btn.setText("完成");
        }
    }

    public void registerByWechat(View view){
        Toast.makeText(this,"暂不支持，敬请期待！",Toast.LENGTH_SHORT).show();
    }

    public void fromRegister2Login(View view){
        SignMode=1;
        registerVisibility(1);
    }

    public void saveToFile(String name,String pswd){
        //密码加密，用户名明文
    }

    public void saveToDatabase(String name,String pswd){
        //密码加密，用户名明文
    }
}
