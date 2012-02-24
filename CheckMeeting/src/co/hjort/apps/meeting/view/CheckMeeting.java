package co.hjort.apps.meeting.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import co.hjort.apps.meeting.R;
import co.hjort.apps.meeting.domain.Meeting;

public class CheckMeeting extends ListActivity {

	private List<Meeting> meetings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		meetings = new ArrayList<Meeting>();
		meetings.add(new Meeting(1l, new Date(), "Reunião CONSEGI 2012",
				"ESAF - Sala 3"));
		meetings.add(new Meeting(2l, new Date(), "Reunião SICONV",
				"Regional - Sala 1"));
		meetings.add(new Meeting(2l, new Date(), "Apresentação IBM SONAS",
				"Sede - Sala CETEC"));

		setListAdapter(new MyListAdapter(this));
	}

	private class MyListAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyListAdapter(Context context) {
			// cache the LayoutInflate to avoid asking for a new one each time
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return meetings.size();
		}

		@Override
		public Object getItem(int position) {
			return meetings.get(position);
		}

		@Override
		public long getItemId(int position) {
			return meetings.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_meeting, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.datetime = (TextView) convertView.findViewById(R.id.datetime);
				holder.place = (TextView) convertView.findViewById(R.id.place);
                
//                holder.text = (TextView) convertView.findViewById(R.id.text);
//                holder.icon = (ImageView) convertView.findViewById(R.id.icon);

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // bind the data efficiently with the holder
            Meeting meeting = meetings.get(position);
            holder.title.setText(meeting.getTitle());
            holder.datetime.setText(meeting.getDateTime());
            holder.place.setText(meeting.getPlace());
            
			// holder.text.setText(DATA[position]);
			// holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);

            return convertView;
		}

		private class ViewHolder {
			TextView title;
			TextView datetime;
			TextView place;
			// TextView text;
			// ImageView icon;
		}
	}

}