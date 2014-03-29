package com.tugu.ilkproje;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AttendanceListAdapter extends ArrayAdapter<Attendance> {

	 private final Context context;
     private final List<Attendance> values;

     public AttendanceListAdapter(Context context, int textViewResourceId, List<Attendance> objects) {
    	 	 super(context, textViewResourceId, objects);

             this.context = context;
             this.values = objects;
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {

             LayoutInflater inflater = (LayoutInflater) context
                             .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

             Attendance g = values.get(position);
             if ( g.course.equals("seperator")){
                     View seperatorView = inflater.inflate(R.layout.seperator, parent, false);
                     TextView course = (TextView) seperatorView.findViewById(R.id.seperatorText);
                     course.setText(g.attendance);
                     return seperatorView ;
             } else {
                     View rowView = inflater.inflate(R.layout.attendance_item, parent, false);
                     TextView desc = (TextView) rowView.findViewById(R.id.week);
                     TextView grade = (TextView) rowView.findViewById(R.id.attendance);                                

                     desc.setText(g.attendance);
                     grade.setText(g.date);                        
                     return rowView;
             }                                                
     }
}
