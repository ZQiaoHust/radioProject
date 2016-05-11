package com.hust.radiofeeler.tab4;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hust.radiofeeler.R;

/**
 * Created by Administrator on 2015/7/21.
 */
public class Me_fragment extends Fragment {


//    EditText etUserName, etUserPass;
//    CheckBox chk;
//    SharedPreferences pref;
//    SharedPreferences.Editor edtior;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        etUserName = (EditText) getActivity().findViewById(R.id.etuserName);
//        etUserPass = (EditText) getActivity().findViewById(R.id.etuserpass);
//        chk = (CheckBox) getActivity().findViewById(R.id.chkSaveName);
//        SharedPreferences pref = getActivity().getSharedPreferences("UserInfo", 0);
//        edtior = pref.edit();
//        String name = pref.getString("userName", "");
//        if (name == null) {
//            chk.setChecked(false);
//        } else {
//            chk.setChecked(true);
//            etUserName.setText(name);
//        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.me_fragment, container, false);
    }
}
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//
//            case R.id.btnLogin:
//                Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.btnCancel:
//                Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_LONG).show();
//
//
//
//
//                String name = etUserName.getText().toString().trim();
//                String pass = etUserPass.getText().toString().trim();
//                if ("admin".equals(name)&&"123456".equals(pass)) {
//                    if (chk.isChecked()) {
//                        edtior.putString("userName", name);
//                        edtior.commit();
//
//                    }else {
//                        edtior.remove("userName");
//                        edtior.commit();
//                    }
//                    Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_LONG).show();
//                }else {
//                    Toast.makeText(getActivity(), "禁止登录", Toast.LENGTH_LONG).show();
//                }
//                break;
//
//            default:
//                break;
//
//        }
//    }
//}

//final View view = inflater.inflate(R.layout.me_fragment, container, false);
//Button button = (Button) view.findViewById(R.id.btnLogin);
//button.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        String name = etUserName.getText().toString().trim();
//        String pass = etUserPass.getText().toString().trim();
//        if ("admin".equals(name) && "123456".equals(pass)) {
//        if (chk.isChecked()) {
//        edtior.putString("userName", name);
//        edtior.commit();
//
//        } else {
//        edtior.remove("userName");
//        edtior.commit();
//        }
//        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_LONG).show();
//        }
//        }
//        }






