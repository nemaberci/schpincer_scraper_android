package hu.vercibar.SchpincerNotifier

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MySimpleArrayAdapter(context: Context, private val values: List<String>):
    ArrayAdapter<String>(context, R.layout.row_layout, values) {

        private val myContext = context

        override fun notifyDataSetChanged() {
            // TODO Auto-generated method stub
            super.notifyDataSetChanged();
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var rowView: View? = null;
            val inflater = myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
            rowView = inflater.inflate(R.layout.row_layout, parent, false);

            // Displaying a textview
            val textView = rowView.findViewById<TextView>(R.id.label);
            textView.text = values[position];

            return rowView;
        }
}