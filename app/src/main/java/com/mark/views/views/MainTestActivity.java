package com.mark.views.views;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.views.R;
import com.mark.views.ScrollLinearLayoutManager;
import com.mark.views.views.base.ChartViewData;
import com.mark.views.views.base.CustomScrollView;
import com.mark.views.views.base.ILine;
import com.mark.views.views.base.IPoint;
import com.mark.views.views.base.IRootChartData;
import com.mark.views.views.base.IRootKChartData;
import com.mark.views.views.base.KChartViewData;
import com.mark.views.views.base.KPointModel;
import com.mark.views.views.base.LinePoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.mark.views.DateUtil.dateFormater2;


public class MainTestActivity extends AppCompatActivity {


    private ChartViewData chartViewData;
    private ChartViewData chartViewData2;
    private KChartViewData kChartViewData;
    private LineGraph1 line;
    private LineGraph2 line2;
    private KLine1 kLine1;
    private KLine2 kLine2;
    ScrollLinearLayoutManager manager;
    private RecyclerView recyclerView;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.main);
                kChartViewData = new KChartViewData();
      /*  kChartViewData.register(kLine1);
        kChartViewData.register(kLine2);*/
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.postDelayed(this::dataK,1000);
                 manager = new ScrollLinearLayoutManager(this);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(new RecyclerView.Adapter() {
                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(MainTestActivity.this).inflate(R.layout.item,null);
                        return new RecyclerView.ViewHolder(view) {};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        kChartViewData.register( holder.itemView.findViewById(R.id.line));
                        ((KLine1) holder.itemView.findViewById(R.id.line)).setManager(manager);

                    }

            @Override
            public int getItemCount() {
                return 10;
            }
        });
        /*line = findViewById(R.id.line);
        line2 = findViewById(R.id.line2);
        kLine1 = findViewById(R.id.kline1);
        kLine2 = findViewById(R.id.kline2);
        chartViewData = new ChartViewData();
        chartViewData2 = new ChartViewData();
        chartViewData.register(line);
        chartViewData2.register(line2);
        line.postDelayed(this::data, 100);
        line2.postDelayed(this::data2, 100);

        kChartViewData = new KChartViewData();
        kChartViewData.register(kLine1);
        kChartViewData.register(kLine2);
        kLine1.postDelayed(this::dataK,100);
        CustomScrollView customScrollView = findViewById(R.id.scrollView);
        customScrollView.addDisallowViews(kLine1);*/
      //  customScrollView.addDisallowViews(kLine2);

    }
    private void data() {
        chartViewData.initData(testData()).update();
    }
    private void data2() {
        chartViewData2.initData(testData2()).update();
    }
    private void dataK() {
        kChartViewData.initData(testKData()).update();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (chartViewData != null) {
            chartViewData.unRegister(line);
        }
        if (chartViewData2 != null) {
            chartViewData2.unRegister(line2);
        }
        if (kChartViewData!=null){
            kChartViewData.unRegister(kLine1);
            kChartViewData.unRegister(kLine2);
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
         long time  = calendar.getTimeInMillis();
        Log.i("time","time="+time);
        for (int i = 0; i < 200; i++) {
            final int finalI = i;
            final float valueY = random.nextFloat() * 500;
            final float valueY2 = random.nextFloat() * 500;

            PointF pointF = new PointF();
            PointF pointF2 = new PointF();
            final int t2 = random.nextInt(856000);
            time+=t2;

            LinePoint point = new LinePoint(""+finalI,""+time,valueY,0,pointF);
            LinePoint point2 = new LinePoint(""+finalI,""+time,valueY2,0,pointF2);

            /*IPoint point2 = new IPoint() {
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
                    return ""+time;
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
            };*/
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
                return false;
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
    private IRootChartData testData2() {
        final Random random = new Random();
        final List<IPoint> list1 = new ArrayList<>();
        final List<IPoint> list2 = new ArrayList<>();
        final List<IPoint> list3 = new ArrayList<>();
        final  String colorString1 = "#3897F1";
        final  String colorString2 = "#EA4949";
        final  String colorString3 = "#FFC200";
        int color1 = Color.parseColor(colorString1);
        int color2 = Color.parseColor(colorString2);
        int color3 = Color.parseColor(colorString3);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
         long time  = calendar.getTimeInMillis();
        Log.i("time","time="+time);
        float s = 200f;
        float valueY = s;
        float valueY2 = s;
        float valueY3 = s;
        for (int i = 0; i < 100; i++) {
            final int finalI = i;
              int v1 = random.nextInt() * 20;
            int v2 = random.nextInt() * 20;
            int v3 = random.nextInt() * 20;
            if (v1%2==0){
                valueY -= v1;
            }else {
                valueY += v1;
            }
            if (v2%2==0){
                valueY2 -= v2;
            }else {
                valueY2 += v2;
            }
            if (v3%2==0){
                valueY3 -= v3;
            }else {
                valueY3 += v3;
            }

            PointF pointF = new PointF();
            PointF pointF2 = new PointF();
            PointF pointF3 = new PointF();
            final int t2 = random.nextInt(856000);
            time+=t2;

            LinePoint point = new LinePoint(""+finalI,""+time,valueY,0,pointF);
            LinePoint point2 = new LinePoint(""+finalI,""+time,valueY2,0,pointF2);
            LinePoint point3 = new LinePoint(""+finalI,""+time,valueY3,0,pointF3);
            list1.add(point);
            list2.add(point2);
            list3.add(point3);
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
                return "Bid";
            }

            @Override
            public boolean isShowPoints() {
                return false;
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
                return "Ask";
            }

            @Override
            public boolean isShowPoints() {
                return false;
            }

            @Override
            public int id() {
                return 2;
            }
        };
       final ILine line3 = new ILine() {
            @Override
            public List<IPoint> points() {
                return list3;
            }

            @Override
            public int color() {
                return color3;
            }

           @Override
           public String colorHexString() {
               return colorString3;
           }

           @Override
            public String name() {
                return "Latest";
            }

            @Override
            public boolean isShowPoints() {
                return false;
            }

            @Override
            public int id() {
                return 3;
            }
        };
        final List<ILine> lineList = new ArrayList<>();
        lineList.add(line);
        lineList.add(line2);
        lineList.add(line3);
        IRootChartData data = () -> lineList;
        return data;
    }


    private IRootKChartData testKData(){
        final Random random = new Random();

        List<KPointModel> list = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());


        float lastEnd = 50f;
        for (int i = 0; i < 300; i++) {
            float start= 0,end = 0,lower = 0,higher = 0;
            int r = random.nextInt(100);
            if (r<50){
                start = lastEnd + random.nextFloat()*lastEnd/10;
            }else{
                start = lastEnd - random.nextFloat()*lastEnd/10;
            }
            if (r<50){
                end = lastEnd + random.nextFloat()*lastEnd/10;
            }else{
                end = lastEnd - random.nextFloat()*lastEnd/10;
            }
            lastEnd = end;
            float max = Math.max(start,end);
                higher = max + random.nextFloat()*max/10;

            float min = Math.min(start,end);
            lower = min - random.nextFloat()*min/10;
            calendar.set(Calendar.DAY_OF_YEAR,i);
            KPointModel model = new KPointModel(start,end,higher,lower, dateFormater2.get().format(calendar.getTimeInMillis()));
            list.add(model);
        }
        IRootKChartData data = () -> list;
        return  data;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
