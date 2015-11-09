package com.tutecentral.BUNK;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class CustomAdaptor extends ArrayAdapter<String> {

    MyDatabaseAdapter myhelper;
    SharedPreferences prefA;
    CustomAdaptor(Context context, String[] items)
    {
        super(context,R.layout.custom_row,items);
        prefA = context.getSharedPreferences("PERCENTAGE", Context.MODE_PRIVATE);
        myhelper = new MyDatabaseAdapter(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater  = LayoutInflater.from(getContext());
        View custom_view = inflater.inflate(R.layout.custom_row, parent, false);
        String single_item = getItem(position);

        int total_classes=0,total_attended=0;


        String[] arr = single_item.split(" ");
        String itms = myhelper.getSubjectData(arr[0]);
        String[] items = itms.split(" ");
        for(int k=1;k<items.length;k=k+2)
        {
            if(!items[k].equals("0")) {
                    if (items[k].equals("-1")) {
                        total_classes++;
                    }
                    else {
                        total_attended++;
                        total_classes++;
                    }
                }
        }

        String precnt = prefA.getString("percent","");
        int cent;
        if(precnt.equals(""))
        {
            cent=75;
        }
        else
        cent = Integer.parseInt(precnt);
        TextView my_text = (TextView) custom_view.findViewById(R.id.textView4);
        my_text.setText(arr[0]);
        TextView my_text2 = (TextView) custom_view.findViewById(R.id.textView5);
        my_text2.setText("Total Classes      :   " + (Integer.parseInt(arr[1]) + total_classes) );
        TextView my_text3 = (TextView) custom_view.findViewById(R.id.textView6);
        my_text3.setText("Attended              :   " + (Integer.parseInt(arr[2])+total_attended) );
        float percent = (Float.parseFloat(arr[2])+total_attended)/(Float.parseFloat(arr[1])+total_classes);
        percent*=100;
        String s = String.format("%.2f", percent);
        TextView my_text4 = (TextView) custom_view.findViewById(R.id.textView7);
        my_text4.setText("Percentage          :   " + s);
        int sfive = ((Integer.parseInt(arr[2])+total_attended)*100)/cent;
        int att = Integer.parseInt(arr[1])+total_classes;
        TextView my_text5 = (TextView) custom_view.findViewById(R.id.textView8);
        my_text5.setText("Safe Bunks          :   " + (sfive-att));
        if(sfive -att < 0)
            my_text5.append(" (Danger) ");

        return custom_view;
    }
}
