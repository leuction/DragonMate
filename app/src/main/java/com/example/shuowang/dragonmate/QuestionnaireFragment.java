package com.example.shuowang.dragonmate;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.greenrobot.event.EventBus;

/**
 * Created by shuowang on 7/31/15.
 */
public class QuestionnaireFragment extends Fragment{
    EditText et_Name;
    EditText et_Age;
    EditText et_Height;
    EditText et_Weight;
    EditText et_Hometown;
    EditText et_Liveplace;
    EditText et_Hobby;
    EditText et_Specialty;
    EditText et_Requirement;

    RadioGroup rg_Education;
    RadioButton rb_Bachelor,rb_Master,rb_Doctor,rb_Returnee,rb_Others;
    int education;

    RadioGroup rg_Salary;
    RadioButton rb_LowSalary,rb_MiddleSalary,rb_HighSalary;
    int salary;

    RadioGroup rg_House;
    RadioButton rb_HaveHouse,rb_DontHaveHouse;
    int house;

    RadioGroup rg_Car;
    RadioButton rb_HaveCar,rb_DontHaveCar;
    int car;

    RadioGroup rg_Marriage;
    RadioButton rb_Unmarried,rb_Dissociaton;
    int marriage;

    Button btn_complete;







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_questionnaire,container,false);

        et_Name = (EditText) root.findViewById(R.id.Q_NameText);
        et_Age = (EditText) root.findViewById(R.id.Q_AgeText);
        et_Height = (EditText) root.findViewById(R.id.Q_HeightText);
        et_Weight = (EditText) root.findViewById(R.id.Q_WeightText);
        et_Hometown = (EditText) root.findViewById(R.id.Q_HometownText);
        et_Liveplace = (EditText) root.findViewById(R.id.Q_LiveplaceText);
        et_Hobby = (EditText) root.findViewById(R.id.Q_HobbyText);
        et_Specialty = (EditText) root.findViewById(R.id.Q_SpecialtyText);
        et_Requirement = (EditText) root.findViewById(R.id.Q_RequirementText);

        rg_Education = (RadioGroup)root.findViewById(R.id.Q_EducationChoiceRadioGroup);
        rb_Bachelor = (RadioButton)root.findViewById(R.id.Q_BachelorChoice);
        rb_Master = (RadioButton)root.findViewById(R.id.Q_MasterChoice);
        rb_Doctor = (RadioButton)root.findViewById(R.id.Q_DoctorChoice);
        rb_Returnee = (RadioButton)root.findViewById(R.id.Q_ReturneeChoice);
        rb_Others = (RadioButton)root.findViewById(R.id.Q_OthersChoice);

        rg_Salary = (RadioGroup)root.findViewById(R.id.Q_SalaryChoiceRadioGroup);
        rb_LowSalary = (RadioButton)root.findViewById(R.id.Q_LowSalaryChoice);
        rb_MiddleSalary = (RadioButton)root.findViewById(R.id.Q_MiddleSalaryChoice);
        rb_HighSalary = (RadioButton)root.findViewById(R.id.Q_HighSalaryChoice);

        rg_House = (RadioGroup)root.findViewById(R.id.Q_HouseChoiceRadioGroup);
        rb_HaveHouse = (RadioButton)root.findViewById(R.id.Q_HaveHouseChoice);
        rb_DontHaveHouse = (RadioButton)root.findViewById(R.id.Q_DontHaveHouseChoice);

        rg_Car = (RadioGroup)root.findViewById(R.id.Q_CarChoiceRadioGroup);
        rb_HaveCar = (RadioButton)root.findViewById(R.id.Q_HaveCarChoice);
        rb_DontHaveCar = (RadioButton)root.findViewById(R.id.Q_DontHaveCarChoice);

        rg_Marriage = (RadioGroup)root.findViewById(R.id.Q_MarriageChoiceRadioGroup);
        rb_Unmarried = (RadioButton)root.findViewById(R.id.Q_UnmarriedChoice);
        rb_Dissociaton = (RadioButton)root.findViewById(R.id.Q_DissociatonChoice);


        final MyUser currentUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.getObject(getActivity(), currentUser.getObjectId(), new GetListener<MyUser>() {
            @Override
            public void onSuccess(MyUser myUser) {
                et_Name.setHint("姓名："+myUser.getName());
                et_Age.setHint("年龄："+myUser.getAge());
                et_Height.setHint("身高："+myUser.getHeight());
                et_Weight.setHint("体重："+myUser.getWeight());
                et_Hometown.setHint("籍贯："+myUser.getHometown());
                et_Liveplace.setHint("现居住地："+myUser.getLiveplace());
                et_Hobby.setHint("爱好："+myUser.getHobby());
                et_Specialty.setHint("特长："+myUser.getSpecialty());
                et_Requirement.setHint("特殊择偶要求"+myUser.getRequirement());

                if (myUser.itsEducation() == 1) {
                    rg_Education.check(R.id.Q_BachelorChoice);
                } else if (myUser.itsEducation() == 2) {
                    rg_Education.check(R.id.Q_MasterChoice);
                } else if (myUser.itsEducation() == 3) {
                    rg_Education.check(R.id.Q_DoctorChoice);
                } else if (myUser.itsEducation() == 4) {
                    rg_Education.check(R.id.Q_ReturneeChoice);
                } else if (myUser.itsEducation() == 5) {
                    rg_Education.check(R.id.Q_OthersChoice);
                }

                if (myUser.itsSalary() == 1) {
                    rg_Salary.check(R.id.Q_LowSalaryChoice);
                } else if (myUser.itsSalary() == 2) {
                    rg_Salary.check(R.id.Q_MiddleSalaryChoice);
                } else if (myUser.itsSalary() == 3) {
                    rg_Salary.check(R.id.Q_HighSalaryChoice);
                }

                if (myUser.itsHouse()==1) {
                    rg_House.check(R.id.Q_HaveHouseChoice);
                } else if(myUser.itsHouse()==2) {
                    rg_House.check(R.id.Q_DontHaveHouseChoice);
                }

                if (myUser.itsCar()==1) {
                    rg_Car.check(R.id.Q_HaveCarChoice);
                } else if(myUser.itsCar()==2){
                    rg_Car.check(R.id.Q_DontHaveCarChoice);
                }

                if (myUser.itsMarriage()==1) {
                    rg_Marriage.check(R.id.Q_UnmarriedChoice);
                } else if (myUser.itsMarriage()==2){
                    rg_Marriage.check(R.id.Q_DissociatonChoice);
                }
                Toast.makeText(getActivity(), "修改完成.", Toast.LENGTH_SHORT).show();
            }

            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(), "修改失败.", Toast.LENGTH_SHORT).show();
            }
        });

        rg_Education.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_Bachelor.getId()) {
                    education = 1;
                } else if (checkedId == rb_Master.getId()) {
                    education = 2;
                } else if (checkedId == rb_Doctor.getId()) {
                    education = 3;
                } else if (checkedId == rb_Returnee.getId()) {
                    education = 4;
                } else if (checkedId == rb_Others.getId()) {
                    education = 5;
                }
            }
        });

        rg_Salary.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_LowSalary.getId()) {
                    salary = 1;
                } else if (checkedId == rb_MiddleSalary.getId()) {
                    salary = 2;
                } else if (checkedId == rb_HighSalary.getId()) {
                    salary = 3;
                }
            }
        });


        rg_House.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_HaveHouse.getId()) {
                    house = 1;
                } else if (checkedId == rb_DontHaveHouse.getId()) {
                    house = 2;
                }
            }
        });

        rg_Car.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_HaveCar.getId()) {
                    car = 1;
                } else if (checkedId == rb_DontHaveCar.getId()) {
                    car = 2;
                }
            }
        });

        rg_Marriage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_Unmarried.getId()) {
                    marriage = 1;
                } else if (checkedId == rb_Dissociaton.getId()) {
                    marriage = 2;
                }
            }
        });

        root.findViewById(R.id.Q_CompleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyUser newUser = new MyUser();
                if (!"".equals(et_Name.getText().toString().trim()) && et_Name.getText() != null) {
                    newUser.setName(et_Name.getText().toString());
                }
                if (!"".equals(et_Age.getText().toString().trim()) && et_Age.getText() != null) {
                    newUser.setAge(et_Age.getText().toString());
                }
                if (!"".equals(et_Height.getText().toString().trim()) && et_Height.getText() != null) {
                    newUser.setHeight(et_Height.getText().toString());
                }
                if (!"".equals(et_Weight.getText().toString().trim()) && et_Weight.getText() != null) {
                    newUser.setWeight(et_Weight.getText().toString());
                }
                if (!"".equals(et_Hometown.getText().toString().trim()) && et_Hometown.getText() != null) {
                    newUser.setHometown(et_Hometown.getText().toString());
                }
                if (!"".equals(et_Liveplace.getText().toString().trim()) && et_Liveplace.getText() != null) {
                    newUser.setLiveplace(et_Liveplace.getText().toString());
                }
                if (!"".equals(et_Hobby.getText().toString().trim()) && et_Hobby.getText() != null) {
                    newUser.setHobby(et_Hobby.getText().toString());
                }
                if (!"".equals(et_Specialty.getText().toString().trim()) && et_Specialty.getText() != null) {
                    newUser.setSpecialty(et_Specialty.getText().toString());
                }
                if (!"".equals(et_Requirement.getText().toString().trim()) && et_Requirement.getText() != null) {
                    newUser.setRequirement(et_Requirement.getText().toString());
                }
                newUser.setEducation(education);
                newUser.setSalary(salary);
                newUser.setHouse(house);
                newUser.setCar(car);
                newUser.setMarriage(marriage);
                newUser.update(getActivity(), currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), "updatesucceed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getActivity(), "updatefailed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return root;
    }
}
