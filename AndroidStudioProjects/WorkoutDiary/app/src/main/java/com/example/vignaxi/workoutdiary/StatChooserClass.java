package com.example.vignaxi.workoutdiary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StatChooserClass {

    static int identify;
    int y;
    int width;
    int height;

    static int[] actualHeight;
    static int initialHeight;
    static StatChooserClass.Ani a;

    View view;

    LinearLayout container;
    static LinearLayout rowsInsideScroll;

    static ArrayList<LinearLayout> rowList;
    static ArrayList<Workout> workoutList;

    static LinearLayout.LayoutParams bottomPara;
    static LinearLayout.LayoutParams buttonParams;

    public static void removeRow(int index) {
        rowList.remove(index);
    }

    public static LinearLayout createLayout(View v, LinearLayout containers) {
        make(containers, v);
        return containers;
    }

    public static void make(LinearLayout containers, View v) {

        workoutList = new ArrayList<Workout>();
        buildit(v);
        containers.setBackgroundColor(BottomNaviClass.grey);
        containers.setOrientation(LinearLayout.VERTICAL);
        rowsInsideScroll = new LinearLayout(v.getContext());
        LinearLayout scrollContainer = new LinearLayout(v.getContext());
        LinearLayout allViews = new LinearLayout(v.getContext());
        allViews.setOrientation(LinearLayout.VERTICAL);

        LinearLayout divider = new LinearLayout(v.getContext());

        Button addEntryButton = new Button(v.getContext());
        MyButton.makeButton(addEntryButton, "green");
        addEntryButton.setMinimumWidth(BottomNaviClass.width);
        addEntryButton.setMinimumHeight(100);
        addEntryButton.setText("Day view");


        buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        makeButtonAndTextRowParams(buttonParams);

        bottomPara = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, Gravity.BOTTOM);

        rowList = new ArrayList<LinearLayout>();


        HorizontalScrollView rowScroller = new HorizontalScrollView(v.getContext());
        rowScroller.setLayoutParams(new RelativeLayout.LayoutParams(
                ScrollView.LayoutParams.FILL_PARENT, BottomNaviClass.height));

        makeRows(v, rowList);
        setRowHeights(v, rowsInsideScroll);


        rowsInsideScroll.setOrientation(LinearLayout.HORIZONTAL);

        allViews.addView(addEntryButton);
        rowScroller.addView(rowsInsideScroll);
        allViews.addView(rowScroller, bottomPara);
        allViews.addView(divider);
        containers.addView(allViews, bottomPara);


    }

    public static void makeButtonAndTextRowParams(LinearLayout.LayoutParams params) {
        params.leftMargin = 10;
        params.rightMargin = 10;
        params.topMargin = 20;
        params.bottomMargin = 20;
        params.gravity = Gravity.BOTTOM;
    }


    public static void makeRows(View v, ArrayList<LinearLayout> rowList) {
        for (int i = 0; i < workoutList.size(); i++) {
            rowList.add(new LinearLayout(v.getContext()));
            LinearLayout rowCreated = rowList.get(i);
            String theId = Integer.toString(i);
            rowCreated.setOnClickListener(new RowClickedOnClickListener(theId));
        }
    }

    public static void setUpRows(View v, LinearLayout rowsInsideScroll, ArrayList<LinearLayout> rowList, LinearLayout.LayoutParams buttonP) {

        int counter = workoutList.size();

        while (counter != 0) {
            System.out.println("Row counter is at " + counter);

            LinearLayout rowCreated = rowList.get(counter - 1);
            rowCreated.setBackgroundColor(BottomNaviClass.green);
            rowCreated.setMinimumWidth(70);
            rowCreated.setMinimumHeight(300);

            rowsInsideScroll.addView(rowCreated, buttonP);

            counter = counter -1;
        }
    }

    public static void setRowHeights(View v, LinearLayout rowsInsideScroll) {

        int counter = workoutList.size();

        int[] s = new int[counter];
        actualHeight = new int[counter];
        while (counter != 0) {

            Double num = getMax();
            int x = (int) ((SaveWorkout.volumes[counter-1]) / num);

            System.out.println("Row counter is at " + counter);
            Workout work = workoutList.get(counter - 1);
            LinearLayout rowCreated = rowList.get(counter - 1);
            LinearLayout rowForRow = new LinearLayout(v.getContext());
            rowCreated.setBackgroundColor(BottomNaviClass.green);
            rowCreated.setMinimumWidth(60);

            System.out.println("This is the height: " + x);
            s[counter-1] = (SaveWorkout.volumes[counter-1]).intValue();

            rowCreated.setMinimumHeight(x);
            rowForRow.addView(rowCreated);

            //a = new Ani();


            actualHeight[counter-1] = x;
            a = new Ani(rowCreated, counter-1);
            System.out.println("Hi: " + 1);
            a.setDuration(800);
            rowCreated.startAnimation(a);



            rowsInsideScroll.addView(rowForRow, buttonParams);

            counter = counter -1;
        }
    }

    static int g;
    static class RowClickedOnClickListener implements View.OnClickListener {
        String id;

        public RowClickedOnClickListener(String name) {
            id = name;
        }

        public RowClickedOnClickListener() {
        }

        @Override
        public void onClick(View view) {

            System.out.println("Row " + id + " clicked");
            Context context = view.getContext();
            System.out.println("add");
            identify = Integer.parseInt(id);
            System.out.println("two");
            System.out.println("five");

            int row = Integer.parseInt(id);
            LinearLayout rowCreated = rowList.get(row);
            rowCreated.setBackgroundColor(Color.rgb(0,80,34));

            if (g != row) {
                System.out.println("Number: " + g);
                LinearLayout rowz = rowList.get(g);
                rowz.setBackgroundColor(BottomNaviClass.green);
                g = row;
            } else {
                System.out.println("Number: " + g);
                g = row;
            }
        }



    }






    public static void buildit(View v) {
        SaveWorkout opener = new SaveWorkout();

        for (int i = 0; i < SaveWorkout.n; i++) {
            workoutList.add(new Workout());
            Workout workout = workoutList.get(i);
            try {
                workout = opener.openWorkout(i, v.getContext());
                System.out.println("Workout built: " + i);
            } catch (Exception e) {
                System.out.println("No workout found");
                System.out.println(workoutList.size());
            }
        }
    }

    static class Ani extends Animation {
        LinearLayout row;
        int id;
        public Ani(LinearLayout rows, int identity) {
            row = rows;
            id = identity;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight;

            newHeight = (int)(actualHeight[id] * interpolatedTime);
            row.removeAllViews();
            row.getLayoutParams().height = newHeight;
            row.requestLayout();

        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            initialHeight = actualHeight[id];
            System.out.println("Heightish: " + initialHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public static Double getMax() {
        Double maxHeight = 0.0;
        Double returnMultiplicationNumber = 0.0;
        for (int i = 0; i < SaveWorkout.volumes.length; i++ ) {
            if (maxHeight < SaveWorkout.volumes[i]) {
                maxHeight = SaveWorkout.volumes[i];
            }
        }
        if (maxHeight > BottomNaviClass.height - 500) {
            while (maxHeight / returnMultiplicationNumber > BottomNaviClass.height - 500) {
                returnMultiplicationNumber += 10;
            }
        }
        return returnMultiplicationNumber;
    }


}






/*
class thisclass {

    int x;
    HorizontalScrollView rowScroller;
    ArrayList<LinearLayout> rows = new ArrayList<LinearLayout>();
    int y;
    private int rowClickedId;
    int clicked;
    Button barButton;

    int initialHeight;
    int actualHeight[];
    Runnable run1;
    Runnable run2;
    Handler handler;
    StatsActivity.Ani a;
    LinearLayout row;
    StatsActivity.Ani b;
    LinearLayout row2;
    int heighty;


    ArrayList<Workout> workoutList;

    ArrayList<StatsActivity.Ani> animatorList = new ArrayList<StatsActivity.Ani>();

    LinearLayout rowsInsideScroll;
    ArrayList<LinearLayout> rowsLister = new ArrayList<LinearLayout>();


    public void setUp() {

        SaveWorkout.checkExistingFiles(view.getContext());

        buildit();

        LinearLayout layout = new LinearLayout(this);
        setContentView(layout);
        layout.setBackgroundColor(BottomNaviClass.grey);
        rowsInsideScroll = new LinearLayout(this);
        rowsInsideScroll.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        makeButtonAndTextRowParams(buttonParams);

        rowScroller = new HorizontalScrollView(this);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 900);

        rowScroller.setLayoutParams(new RelativeLayout.LayoutParams(
                ScrollView.LayoutParams.FILL_PARENT, heighty));


        int counter = workoutList.size();
        System.out.println("Size "+ counter);
        System.out.println("Workout list size " + workoutList.size());

        for (int i = 0; i < counter; i++) {
            rows.add(new LinearLayout(getBaseContext()));
            LinearLayout rowCreated = rows.get(i);
            String theId = Integer.toString(i);
            rowCreated.setOnClickListener(new BarClickedOnClickListener(theId));
        }











        int[] s = new int[counter];
        actualHeight = new int[counter];
        while (counter != 0) {

            Double num = getMax();
            int x = (int) ((SaveWorkout.volumes[counter-1]) / num);

            System.out.println("Row counter is at " + counter);
            Workout work = workoutList.get(counter - 1);
            LinearLayout rowCreated = rows.get(counter - 1);
            LinearLayout rowForRow = new LinearLayout(this);
            rowCreated.setBackgroundColor(BottomNaviClass.green);
            rowCreated.setMinimumWidth(50);

            System.out.println("This is the height: " + x);
            s[counter-1] = (SaveWorkout.volumes[counter-1]).intValue();

            rowCreated.setMinimumHeight(x);
            rowForRow.addView(rowCreated);

            //a = new Ani();


            actualHeight[counter-1] = x;
            a = new StatsActivity.Ani(rowCreated, counter-1);
            System.out.println("Hi: " + 1);
            a.setDuration(800);
            rowCreated.startAnimation(a);



            rowsInsideScroll.addView(rowForRow, buttonParams);

            counter = counter -1;
        }



        LinearLayout bottomBar = new LinearLayout(this);
        bottomBar.setBackgroundColor(Color.BLACK);
        bottomBar.setMinimumHeight(150);
        bottomBar.setMinimumWidth(width);

        LinearLayout containerAllViews = new LinearLayout(this);
        containerAllViews.setMinimumWidth(width);
        containerAllViews.setMinimumHeight(heighty);
        containerAllViews.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams bottomPara = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, Gravity.BOTTOM);

        LinearLayout bar = new LinearLayout(this);
        BottomNav.makeBottomnavBar(bar, width, view.getContext(), 1);

        LinearLayout buttonBar = new LinearLayout(this);
        buttonBar.setMinimumWidth(width);
        buttonBar.setMinimumHeight(150);


        barButton = new Button(this);
        barButton.setBackgroundColor(Color.rgb(30,30,30));
        barButton.setWidth(width);
        barButton.setHeight(120);
        barButton.setText("Day View");
        barButton.setTextSize(20);
        barButton.setTextColor(BottomNaviClass.green);
        barButton.setOnClickListener(new StatsActivity.ChangeBarOnClickListener());



        buttonBar.addView(barButton);
        rowScroller.addView(rowsInsideScroll);
        containerAllViews.addView(buttonBar);
        containerAllViews.addView(rowScroller, bottomPara);




        layout.addView(containerAllViews, bottomPara);



    }
    List<Integer> barHeights;
class ChangeBarOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {


        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        makeButtonAndTextRowParams(buttonParams);
        System.out.println("Clicked " + clicked);
        rowsInsideScroll.removeAllViews();

        if (clicked == 0) {
            System.out.println("We are in");
            clicked++;
            System.out.println("clicked2 " + clicked);
            setRows(7);
            barButton.setText("Week View");

            int counter2 = barHeights.size();
            actualHeight = new int[counter2];

            while (counter2 != 0) {
                System.out.println("Row counter is at " + counter2);
                Workout work = workoutList.get(counter2 -1);
                LinearLayout rowCreated = rows.get(counter2 - 1);
                LinearLayout rowForRow = new LinearLayout(getBaseContext());
                rowCreated.setBackgroundColor(BottomNaviClass.green);
                rowCreated.setMinimumWidth(50);

                Double num = getMax();
                int x = (int) ((barHeights.get(counter2-1)) / num);

                System.out.println("This is the height: " + x);

                rowCreated.setMinimumHeight(x);
                rowForRow.addView(rowCreated);

                actualHeight[counter2-1] = x;
                a = new StatsActivity.Ani(rowCreated, counter2-1);
                System.out.println("Hi: " + 1);
                a.setDuration(800);
                rowCreated.startAnimation(a);


                rowsInsideScroll.addView(rowForRow, buttonParams);

                counter2 = counter2 -1;
            }

        } else if (clicked == 1) {
            barButton.setText("Month View");
            clicked++;
            System.out.println(clicked);
            setRows(30);
            int counter2 = barHeights.size();
            actualHeight = new int[counter2];

            while (counter2 != 0) {
                System.out.println("Row counter is at " + counter2);
                Workout work = workoutList.get(counter2 -1);
                LinearLayout rowCreated = rows.get(counter2 - 1);
                LinearLayout rowForRow = new LinearLayout(getBaseContext());
                rowCreated.setBackgroundColor(BottomNaviClass.green);
                rowCreated.setMinimumWidth(50);

                System.out.println("This is the height: " + work.getVolume());

                Double num = getMax();
                int x = (int) ((barHeights.get(counter2-1)) / num);

                rowCreated.setMinimumHeight(x);
                rowForRow.addView(rowCreated);

                actualHeight[counter2-1] = x;
                a = new StatsActivity.Ani(rowCreated, counter2-1);
                System.out.println("Hi: " + 1);
                a.setDuration(800);
                rowCreated.startAnimation(a);

                rowsInsideScroll.addView(rowForRow, buttonParams);

                counter2 = counter2 -1;
            }

        } else if (clicked == 2) {

            barButton.setText("Day View");
            clicked = 0;
            int counter = workoutList.size();
            actualHeight = new int[counter];
            System.out.println();
            for (int i = 0; i < counter; i++) {
                rows.add(new LinearLayout(getBaseContext()));
                LinearLayout rowCreated = rows.get(i);
                String theId = Integer.toString(i);
                rowCreated.setOnClickListener(new StatsActivity.BarClickedOnClickListener(theId));
            }
            while (counter != 0) {
                System.out.println("Row counter is at " + counter);
                LinearLayout rowCreated = rows.get(counter - 1);
                LinearLayout rowForRow = new LinearLayout(getBaseContext());
                rowCreated.setBackgroundColor(BottomNaviClass.green);
                rowCreated.setMinimumWidth(50);

                Double num = getMax();
                int x = (int) ((SaveWorkout.volumes[counter-1]) / num);

                System.out.println("This is the height: " + x);


                rowForRow.addView(rowCreated);

                actualHeight[counter-1] = x;
                a = new StatsActivity.Ani(rowCreated, counter-1);
                System.out.println("Hi: " + 1);
                a.setDuration(800);
                rowCreated.startAnimation(a);
                rowCreated.setMinimumHeight(x);


                rowsInsideScroll.addView(rowForRow, buttonParams);

                counter = counter -1;
            }
        }
    }
}
    int weekDays = 7;


    public void setRows(int weekOrMonth) {
        int vol = 0;
        int rowCount;
        rows.clear();
        if (weekOrMonth == 7) {
            rowCount = 0;
            System.out.println("building");
            // try to sum up each seven days of workoutvolumes and set each bar to be that tall
            System.out.println("building workouts " + SaveWorkout.n);
            barHeights = new ArrayList<>();
            int a = 0;
            for (int i = 0; i < SaveWorkout.n; i++) {
                if (a == 6) {
                    rows.add(new LinearLayout(getBaseContext()));
                    LinearLayout rowCreated = rows.get(rowCount);
                    String theId = Integer.toString(rowCount);
                    rowCreated.setOnClickListener(new StatsActivity.BarClickedOnClickListener(theId));
                    barHeights.add(vol);
                    System.out.println(vol);
                    vol = 0;
                    a = 0;
                    rowCount++;
                } else {
                    vol = vol + SaveWorkout.volumes[i].intValue();
                    a++;
                }
            }
        }
        if (weekOrMonth == 30) {
            rowCount = 0;
            // try to sum up every 30 workouts worth of volumes and set each bar to be that tall
            barHeights = new ArrayList<>();
            int a = 0;
            if (workoutList.size() >= 29) {
                for (int i = 0; i < x; i++) {
                    if (a == 29) {
                        rows.add(new LinearLayout(getBaseContext()));
                        LinearLayout rowCreated = rows.get(rowCount);
                        String theId = Integer.toString(rowCount);
                        rowCreated.setOnClickListener(new StatsActivity.BarClickedOnClickListener(theId));
                        barHeights.add(vol);
                        vol = 0;
                        a = 0;
                    } else {
                        vol = vol + SaveWorkout.volumes[i].intValue();
                        a++;
                    }
                }
            }

        } else {
            // Print a messege to the user "Nothing to show here yet"
            System.out.println("Not enough workouts yet: " + workoutList.size());
        }
    }

    private int height;

    public void setHeight(int volume) {
        height = volume;
    }

    public int getHeight() {
        return height;
    }

    public void createHeight() {

    }

    int g;

class BarClickedOnClickListener implements View.OnClickListener {
    String id;


    public BarClickedOnClickListener(String name) {
        id = name;
    }

    public BarClickedOnClickListener() {
    }

    @Override
    public void onClick(View view) {
        System.out.println("Row " + id + " clicked");
        //CopyOfWorkoutActivity builder = new CopyOfWorkoutActivity();
        //builder.setWorkout(Integer.parseInt(id));
        int row = Integer.parseInt(id);
        LinearLayout rowCreated = rows.get(row);
        rowCreated.setBackgroundColor(Color.rgb(0,80,34));

        if (g != row) {
            System.out.println("Number: " + g);
            LinearLayout rowz = rows.get(g);
            rowz.setBackgroundColor(BottomNaviClass.green);
            g = row;
        } else {
            System.out.println("Number: " + g);
            g = row;
        }

    }
}

    public void makeButtonAndTextRowParams(LinearLayout.LayoutParams params) {
        params.leftMargin = 10;
        params.rightMargin = 10;
        params.topMargin = 20;
        params.bottomMargin = 20;
        params.gravity = Gravity.BOTTOM;
    }

    public ArrayList<Workout> buildit() {
        View view = new View(getBaseContext());
        workoutList = new ArrayList<Workout>();
        SaveWorkout opener = new SaveWorkout();

        for (int i = 0; i < SaveWorkout.n; i++) {
            workoutList.add(new Workout());
            Workout workout = workoutList.get(i);
            try {
                workout = opener.openWorkout(i, view.getContext());
                System.out.println("Workout built: " + i);
            } catch (Exception e) {
                System.out.println("No workout found");
                System.out.println(workoutList.size());
            }
        }
        return workoutList;
    }

class Ani extends Animation {
    LinearLayout row;
    int id;
    public Ani(LinearLayout rows, int identity) {
        row = rows;
        id = identity;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;

        //LinearLayout row = rows.get(i);
        newHeight = (int)(actualHeight[id] * interpolatedTime);
        row.removeAllViews();
        row.getLayoutParams().height = newHeight;
        row.requestLayout();

    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        initialHeight = actualHeight[id];
        System.out.println("Heightish: " + initialHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}

    public Double getMax() {
        Double maxHeight = 0.0;
        Double returnMultiplicationNumber = 0.0;
        for (int i = 0; i < SaveWorkout.volumes.length; i++ ) {
            if (maxHeight < SaveWorkout.volumes[i]) {
                maxHeight = SaveWorkout.volumes[i];
            }
        }
        if (maxHeight > heighty) {
            while (maxHeight / returnMultiplicationNumber > heighty) {
                returnMultiplicationNumber += 10;
            }
        }
        return returnMultiplicationNumber;
    }


}

*/