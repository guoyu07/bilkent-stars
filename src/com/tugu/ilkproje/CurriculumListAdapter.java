package com.tugu.ilkproje;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CurriculumListAdapter extends ArrayAdapter<Curriculum> {

	 private final Context context;
     private final List<Curriculum> values;

     public CurriculumListAdapter(Context context, int textViewResourceId, List<Curriculum> objects) {
    	 	 super(context, textViewResourceId, objects);

             this.context = context;
             this.values = objects;
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {

             LayoutInflater inflater = (LayoutInflater) context
                             .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

             Curriculum g = values.get(position);
             if ( g.code.equals("semester")){
                     View seperatorView = inflater.inflate(R.layout.seperator, parent, false);
                     TextView course = (TextView) seperatorView.findViewById(R.id.seperatorText);
                     course.setText(g.name);
                     return seperatorView ;
             } else {
                     View rowView = inflater.inflate(R.layout.grade_item, parent, false);
                     TextView desc = (TextView) rowView.findViewById(R.id.grade_title);
                     TextView grade = (TextView) rowView.findViewById(R.id.grade_grade);                                

                     if("".equals(g.code))
                    	 desc.setText(g.name);
                     else 
                    	 desc.setText(g.code);
                     grade.setText(g.grade);                        
                     return rowView;
             }                                                
     }
}
