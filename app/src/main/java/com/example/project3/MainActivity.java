package com.example.project3;

import static java.lang.Math.pow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String sex = "";
    String height = "";
    String weight = "";
    String name = "";
    String pwd = "";
    private TextView mTxt;
    private ArrayList<JsonBean> options1Items = new ArrayList<>(); // 省
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();// 市
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();// 区

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

        /* get elements */
        // TextView * 4
        TextView name_ET = findViewById(R.id.name_EditText);
        TextView pwd_ET = findViewById(R.id.pwd_EditText);
        TextView height_ET = findViewById(R.id.height_EditText);
        TextView weight_ET = findViewById(R.id.weight_EditText);

        // Button * 3
        Button compute_button = findViewById(R.id.compute_Button);
        Button reset_button = findViewById(R.id.reset_Button);
        Button nextPage_button = findViewById(R.id.nextPage_Button);

        // RadioGroup * 1
        RadioGroup sex_RadioGroup = findViewById(R.id.sex_RadioGroup);

        /* sex radio group: deter sex */
        sex_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // which button chosen
                RadioButton sex_RadioButton = (RadioButton) findViewById(i);
                sex = sex_RadioButton.getText().toString();
            }
        });

        /* compute button: compute BMI */
        compute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                height = height_ET.getText().toString();
                weight = weight_ET.getText().toString();

                System.out.println(height);
                System.out.println(weight);

                if(weight.equals("") || height.equals("")) {
                    // input error
                    Toast.makeText(MainActivity.this, "身高或体重为空", Toast.LENGTH_LONG).show();
                } else {
                    // kg / m ^ 2
                    double BMI = Double.parseDouble(weight) / pow(Double.parseDouble(height), 2);

                    if(sex.equals("男")) {
                        if(BMI<19) {
                            Toast.makeText(MainActivity.this, "体重偏低", Toast.LENGTH_LONG).show();
                        } else if(BMI<=25) {
                            Toast.makeText(MainActivity.this, "体重健康", Toast.LENGTH_LONG).show();
                        } else if(BMI<=30) {
                            Toast.makeText(MainActivity.this, "超重", Toast.LENGTH_LONG).show();
                        } else if(BMI<=39) {
                            Toast.makeText(MainActivity.this, "严重超重", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "极度超重", Toast.LENGTH_LONG).show();
                        }
                    } else if(sex.equals("女")) {
                        if(BMI<18) {
                            Toast.makeText(MainActivity.this, "体重偏低", Toast.LENGTH_LONG).show();
                        } else if(BMI<=24) {
                            Toast.makeText(MainActivity.this, "体重健康", Toast.LENGTH_LONG).show();
                        } else if(BMI<=29) {
                            Toast.makeText(MainActivity.this, "超重", Toast.LENGTH_LONG).show();
                        } else if(BMI<=38) {
                            Toast.makeText(MainActivity.this, "严重超重", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "极度超重", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });

        /* reset button: reset EditText and radio group */
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_ET.setText("");
                pwd_ET.setText("");
                height_ET.setText("");
                weight_ET.setText("");
                // reset radio group
                sex_RadioGroup.setOnCheckedChangeListener(null);
                sex_RadioGroup.clearCheck();
                sex_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        ;
                    }
                });
            }
        });

        /* next page buuton: go to MainActivity2 */
        nextPage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三级）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            //添加城市数据
            options2Items.add(CityList);
            //添加地区数据
            options3Items.add(Province_AreaList);
        }
    }


    private void initView() {
        mTxt = (TextView) findViewById(R.id.txt);

        mTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPickerView();
            }
        });
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                mTxt.setText(options1Items.get(options1).getPickerViewText() + "  "
                        + options2Items.get(options1).get(options2) + "  "
                        + options3Items.get(options1).get(options2).get(options3));

            }
        })
                .setTitleText("城市选择")
                .setTitleBgColor(Color.WHITE)//设置标题的背景颜色
                .setDividerColor(Color.BLACK)//设置分割线的颜色
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

}