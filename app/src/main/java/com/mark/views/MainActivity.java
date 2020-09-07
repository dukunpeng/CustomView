package com.mark.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mark.views.model.HistoryBean;
import com.mark.views.views.LineGraph1;
import com.mark.views.views.base.ChartViewData;
import com.mark.views.views.base.HistoryChartView;
import com.mark.views.views.base.ILine;
import com.mark.views.views.base.IPoint;
import com.mark.views.views.base.IRootChartData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {


    private ChartViewData chartViewData;
    private LineGraph1 line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HistoryChartView chartView = findViewById(R.id.chart);
        chartView.setmDatas(makeDatas());

        line = findViewById(R.id.line);
        chartViewData = new ChartViewData();
        chartViewData.register(line);
        line.postDelayed(this::data, 100);

    }
    private void data() {
        chartViewData.initData(testData()).update();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (chartViewData != null) {
            chartViewData.unRegister(line);
        }
    }

    private IRootChartData testData() {
        final Random random = new Random();
        final List<IPoint> list1 = new ArrayList<>();
        final List<IPoint> list2 = new ArrayList<>();
        final  String colorString1 = "#d00000";
        final  String colorString2 = "#2F8AC6";
        int color1 = Color.parseColor(colorString1);
        int color2 = Color.parseColor(colorString2);
        for (int i = 0; i < 200; i++) {
            final int finalI = i;
            final float valueY = random.nextFloat() * 500;
            final float valueY2 = random.nextFloat() * 500;

            PointF pointF = new PointF();
            PointF pointF2 = new PointF();
            IPoint point = new IPoint() {
                @Override
                public String originalKey() {
                    return String.format("第%d个", finalI);
                }

                @Override
                public String key() {
                    return "" + finalI;
                }

                @Override
                public String valueYString() {
                    return "" + valueY();
                }

                @Override
                public float valueY() {
                    return valueY;
                }

                @Override
                public int state() {
                    return 0;
                }

                @Override
                public PointF pointF() {
                    //这里不可以写成new
                    return pointF;
                }
            };
            IPoint point2 = new IPoint() {
                @Override
                public String originalKey() {
                    return String.format("第%d个", finalI);
                }

                @Override
                public String key() {
                    return "" + finalI;
                }

                @Override
                public String valueYString() {
                    return "" + valueY();
                }

                @Override
                public float valueY() {
                    return valueY2;
                }

                @Override
                public int state() {
                    return 0;
                }

                @Override
                public PointF pointF() {
                    //这里不可以写成new
                    return pointF2;
                }
            };
            list1.add(point);
            list2.add(point2);
        }

        final ILine line = new ILine() {
            @Override
            public List<IPoint> points() {
                return list1;
            }

            @Override
            public int color() {
                return color1;
            }

            @Override
            public String colorHexString() {
                return colorString1;
            }

            @Override
            public String name() {
                return "第一个";
            }

            @Override
            public boolean isShowPoints() {
                return true;
            }

            @Override
            public int id() {
                return 1;
            }
        };
       final ILine line2 = new ILine() {
            @Override
            public List<IPoint> points() {
                return list2;
            }

            @Override
            public int color() {
                return color2;
            }

           @Override
           public String colorHexString() {
               return colorString2;
           }

           @Override
            public String name() {
                return "第一个";
            }

            @Override
            public boolean isShowPoints() {
                return true;
            }

            @Override
            public int id() {
                return 2;
            }
        };
        final List<ILine> lineList = new ArrayList<>();
        lineList.add(line);
        lineList.add(line2);
        IRootChartData data = () -> lineList;
        return data;
    }

    private List<HistoryBean> makeDatas() {
        List<HistoryBean> beans = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//24小时制
        try {
            HistoryBean bean = new HistoryBean();
            bean.setUpdTm(simpleDateFormat.parse("2020-06-22 08:40").getTime());
            bean.setClsngPrc(0.8090);
            bean.setTrdQnty(90.00);
            HistoryBean bean1 = new HistoryBean();
            bean1.setUpdTm(simpleDateFormat.parse("2020-06-23 09:05").getTime());
            bean1.setClsngPrc(1.0000);
            bean1.setTrdQnty(20.00);
            HistoryBean bean2 = new HistoryBean();
            bean2.setUpdTm(simpleDateFormat.parse("2020-06-24 10:30").getTime());
            bean2.setClsngPrc(0.7200);
            bean2.setTrdQnty(52.00);
            HistoryBean bean3 = new HistoryBean();
            bean3.setUpdTm(simpleDateFormat.parse("2020-06-25 10:58").getTime());
            bean3.setClsngPrc(1.7200);
            bean3.setTrdQnty(16.00);
            HistoryBean bean4 = new HistoryBean();
            bean4.setUpdTm(simpleDateFormat.parse("2020-06-26 12:30").getTime());
            bean4.setClsngPrc(1.5002);
            bean4.setTrdQnty(35.00);
            beans.add(bean);
            beans.add(bean1);
            beans.add(bean2);
            beans.add(bean3);
            beans.add(bean4);
            Random random = new Random();
            for (int i = 0; i < 40; i++) {
                HistoryBean b = new HistoryBean();
                b.setUpdTm(simpleDateFormat.parse("2020-07-0" + i + " 12:30").getTime());
                b.setClsngPrc(random.nextFloat() * 100);
                b.setTrdQnty(random.nextFloat() * 100);
                beans.add(b);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return beans;
    }


}
