package com.pweschmidt.healthapps;


import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

public class SelectEffects extends Activity implements View.OnClickListener
{
	private String selectedEffect;
    private ListView effectsTextListView;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selecteffects);
    	ImageButton save = (ImageButton)findViewById(R.id.SaveButton);
    	save.setOnClickListener(this);
    	
    	ImageButton back = (ImageButton)findViewById(R.id.CancelButton);
    	back.setOnClickListener(this);
    	
    	ImageButton delete = (ImageButton)findViewById(R.id.TrashButton);
    	delete.setVisibility(View.GONE);
		String[] effects = getResources().getStringArray(R.array.AllSideEffects);
    	effectsTextListView = (ListView)findViewById(R.id.effectsTextListView);        
    	effectsTextListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	SideEffectsAdapter adapter = new SideEffectsAdapter(this, R.layout.effects_item, effects);
        effectsTextListView.setAdapter(adapter);
	}
	
	public void onClick(View view)
	{
		int resID = view.getId();
		switch(resID)
		{
		case R.id.SaveButton:
			if(null != selectedEffect)
			{
				Intent effectsIntent = new Intent();
				effectsIntent.putExtra("selectedEffect", selectedEffect);
	    		setResult(RESULT_OK, effectsIntent);
			}
			else
			{
	    		setResult(RESULT_CANCELED, null);
	    		finish();            					
			}
    		finish();            	
			break;
		case R.id.CancelButton:
    		setResult(RESULT_CANCELED, null);
    		finish();            	
			break;
		
		}
		
	}
	
	
	private class SideEffectsAdapter extends ArrayAdapter<String>
	{
	    Context context; 
	    int layoutResourceId;    
	    String data[] = null;
	    boolean[] boxesChecked = null;
	    
	    private class ViewHolder
	    {
	    	TextView effectsTextView;
	    	CheckBox effectsCheckBox;
	    }
	    
		public SideEffectsAdapter(Context context, int layoutResourceId, String[] data)
		{
	        super(context, layoutResourceId, data);			
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	        boxesChecked = new boolean[data.length];
	        for(int i = 0 ; i < boxesChecked.length ; i++)
	        {
	        	boxesChecked[i] = false;
	        }
		}
		
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	    	View row = convertView;
	    	final ViewHolder holder;
	    	if(null == row)
	    	{
	    		holder = new ViewHolder();
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            TextView effectsText = (TextView)row.findViewById(R.id.effectsTextView);
	            CheckBox box = (CheckBox)row.findViewById(R.id.effectsCheckButton);
		    	holder.effectsTextView = effectsText;
		    	holder.effectsCheckBox = box;	    		
	    	}
	    	else
	    	{
	    		holder = (ViewHolder)row.getTag();
	    	}
	    	
	    	row.setTag(holder);
	    	holder.effectsTextView.setText(data[position]);
	    	holder.effectsCheckBox.setId(position);
            holder.effectsCheckBox.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					for(int i = 0 ; i < data.length ; i++)
					{
						if(v.getId() == i)
						{
							boxesChecked[i] = true;
						}
						else
						{
							boxesChecked[i] = false;
						}
					}
					notifyDataSetChanged();
				}
			});
            
            if(boxesChecked[position])
            {
            	holder.effectsCheckBox.setChecked(true);
            	selectedEffect = (String) holder.effectsTextView.getText();
            }
            else
            	holder.effectsCheckBox.setChecked(false);
            
            
	    	return row;
	    }
		
	}
	
}
