package com.example.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity2 extends AppCompatActivity {
    ArrayAdapter<String> province_adapter = null;  // 省级适配器
    ArrayAdapter<String> city_adapter = null;    // 地级适配器
    ArrayAdapter<String> county_adapter = null;    // 县级适配器
    int province_i = 0;
    private final String[] province = new String[] {"北京","上海","天津","广东"};
    private final String[][] city = new String[][]
            {
                    { "东城区", "西城区", "崇文区", "宣武区", "朝阳区", "海淀区", "丰台区", "石景山区", "门头沟区",
                            "房山区", "通州区", "顺义区", "大兴区", "昌平区", "平谷区", "怀柔区", "密云县",
                            "延庆县" },
                    { "长宁区", "静安区", "普陀区", "闸北区", "虹口区" },
                    { "和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "塘沽区", "汉沽区", "大港区",
                            "东丽区" },
                    { "广州", "深圳", "韶关"
                    }
            };
    private final String[][][] county = new String[][][]
            {
                    {   // 北京
                            {"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},
                            {"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"}
                    },
                    {    // 上海
                            {"无"},{"无"},{"无"},{"无"},{"无"}
                    },
                    {    // 天津
                            {"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"},{"无"}
                    },
                    {    // 广东
                            // 广州
                            {"海珠区","荔湾区","越秀区","白云区","萝岗区","天河区","黄埔区","花都区","从化市","增城市","番禺区","南沙区"},
                            // 深圳
                            {"宝安区","福田区","龙岗区","罗湖区","南山区","盐田区"},
                            // 韶关
                            {"武江区","浈江区","曲江区","乐昌市","南雄市","始兴县","仁化县","翁源县","新丰县","乳源县"}
                    }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /* get elements */
        // Button * 2
        Button lastPage_button = findViewById(R.id.lastPage_Button);
        ToggleButton light_toggleButton = findViewById(R.id.light_ToggleButton);

        // ImageView * 1
        ImageView light_imageView = findViewById(R.id.light_ImageView);

        // ListView * 1
        ListView items_listview = findViewById(R.id.items_ListView);

        // Spinner *3
        Spinner province_spinner = findViewById(R.id.province_Spinner);
        Spinner city_spinner = findViewById(R.id.city_Spinner);
        Spinner county_spinner = findViewById(R.id.county_Spinner);

        /* Spinner: province, city, county */
        // 绑定适配器和值
        province_adapter = new ArrayAdapter<String>(MainActivity2.this,
                android.R.layout.simple_spinner_item, province);
        province_spinner.setAdapter(province_adapter);
        province_spinner.setSelection(3,true);  // 设置默认选中项，此处为默认选中第4个值

        city_adapter = new ArrayAdapter<String>(MainActivity2.this,
                android.R.layout.simple_spinner_item, city[3]);
        city_spinner.setAdapter(city_adapter);
        city_spinner.setSelection(0,true);  // 设置默认选中项，此处为默认选中第0个值

        county_adapter = new ArrayAdapter<String>(MainActivity2.this,
                android.R.layout.simple_spinner_item, county[3][0]);
        county_spinner.setAdapter(county_adapter);
        county_spinner.setSelection(0,true);  // 设置默认选中项，此处为默认选中第0个值


        // 省级下拉列表监听
        province_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // i为当前的省级序号
                // 改变适配器city_adapter的值为第i个市
                city_adapter = new ArrayAdapter<String>(MainActivity2.this,
                        android.R.layout.simple_spinner_item, city[i]);
                // 更新新的城市下拉列表的值
                city_spinner.setAdapter(city_adapter);
                // 记录下当前选中的省份序号，方便选择区块
                province_i = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ;
            }
        });

        // 市级下拉列表监听
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 改变适配器count_spinner的值为第i个区
                county_adapter = new ArrayAdapter<String>(MainActivity2.this,
                        android.R.layout.simple_spinner_item, county[province_i][i]);
                // 更新新的市区下拉列表的值
                county_spinner.setAdapter(county_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { ; }
        });



        /* toggle button: light on & off */
        light_toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                light_toggleButton.setChecked(isChecked);
                light_imageView.setImageResource(isChecked?R.drawable.on:R.drawable.off);
            }
        });

        /* listview: response which item chosen */
        items_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] item = getResources().getStringArray(R.array.items);
                Toast.makeText(MainActivity2.this, item[i], Toast.LENGTH_LONG).show();
            }
        });

        /* last page button: go back to MainActivity1 */
        lastPage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}