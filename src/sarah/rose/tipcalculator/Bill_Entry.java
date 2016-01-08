package sarah.rose.tipcalculator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Bill_Entry extends Activity implements OnSeekBarChangeListener, TextWatcher {

    private SeekBar bar; // declare seekbar object variable

    private EditText bill_amount_value_local, round_to, split_between;
    
    private TextView tip_amount_local, tip_percentage_value_local, total_value_local,total_value_rounded_down, total_value_rounded_up;
    
    private float tip_percent=0;
    
    private double round_to_double=0;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bill_entry);
		
		bar = (SeekBar)findViewById(R.id.tip_percentage_slider); // make seekbar object
        bar.setOnSeekBarChangeListener(this); // set seekbar listener.
        // since we are using this class as the listener the class is "this"
        
        tip_percentage_value_local = (TextView)findViewById(R.id.tip_percentage_value);
        bill_amount_value_local = (EditText)findViewById(R.id.bill_amount_value);
        total_value_local = (TextView)findViewById(R.id.total_value);
        total_value_rounded_down = (TextView)findViewById(R.id.rounded_down);
        total_value_rounded_up = (TextView)findViewById(R.id.rounded_up);
        round_to = (EditText)findViewById(R.id.round_to);
        split_between = (EditText)findViewById(R.id.split_value);
    	tip_amount_local = (TextView)findViewById(R.id.tip_amount);

        round_to.addTextChangedListener(this);
        split_between.addTextChangedListener(this);
    	bill_amount_value_local.addTextChangedListener(this);
    	SharedPreferences prefs = this.getSharedPreferences("org.sarah.rose.tipcalculator", Context.MODE_PRIVATE);

    	bar.setProgress(prefs.getInt("org.sarah.rose.tipcalculator.percentage", 0));
	}
	
	@Override
	 public void afterTextChanged(Editable arg0) 
	 {
	     // TODO Auto-generated method stub
	      total_update();
	 }
	  
	@Override
	 public void beforeTextChanged(CharSequence s, int start, int count,int after) 
	 {
	     // TODO Auto-generated method stub  
	 }
	  
	 @Override
	 public void onTextChanged(CharSequence s, int start, int before, int count) 
	 {
	     // TODO Auto-generated method stub
	              
	 }

	private void total_update() {

		double added_amount=0;
		
		

		if (bill_amount_value_local.getText().length() != 0){
			double bill_value=Double.parseDouble(bill_amount_value_local.getText().toString());
			//prevent div by 0 error

			if (split_between.getText().length()!=0){
				bill_value/=Integer.parseInt(split_between.getText().toString());				
			}
			
			
			if (tip_percent==0){
				added_amount=0;
				//also set the rounded up/down values to a dash
				total_value_rounded_down.setText("-");
				total_value_rounded_up.setText("-");
			} else {
				added_amount = bill_value * tip_percent;
			}
			tip_amount_local.setText(String.format("£%.2f",added_amount));
			total_value_local.setText(String.format("£%.2f", added_amount+bill_value));
			
			if (round_to.getText().length()!=0){
				round_to_double=Double.parseDouble(round_to.getText().toString());
				double rounded_down_percent,rounded_up_percent;
				
				double rounded_down_value = Math.floor((added_amount+bill_value)/round_to_double)*round_to_double;
				double rounded_up_value = Math.ceil((added_amount+bill_value)/round_to_double)*round_to_double;
				
				rounded_down_percent =  (((double) rounded_down_value/bill_value)-1)*100;
				rounded_up_percent =  (((double) rounded_up_value/bill_value)-1)*100;
			

				
				total_value_rounded_down.setText(String.format("£%.2f : %.0f%%",rounded_down_value, rounded_down_percent));
				total_value_rounded_up.setText(String.format("£%.2f : %.0f%%", rounded_up_value, rounded_up_percent));
			}
		} else {
			tip_amount_local.setText("£0.00");
			total_value_local.setText("£0.00");
			total_value_rounded_down.setText("£0 - 0%");
			total_value_rounded_up.setText("£0 - 0%");
		}
		
		
	}


	
	
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bill__entry, menu);
		return true;
	}
	
	@Override
	public void onProgressChanged(SeekBar tip_percentage_slider, int progress, boolean from) {
		// TODO Auto-generated method stub
		if (progress!=0){
		tip_percent=(float)progress/100;
		} else {
			tip_percent=0;
		}
		tip_percentage_value_local.setText(Integer.toString(progress)+"%");
		total_update();
		SharedPreferences prefs = this.getSharedPreferences("org.sarah.rose.tipcalculator", Context.MODE_PRIVATE);

		prefs.edit().putInt("org.sarah.rose.tipcalculator.percentage", progress).apply();


		
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		
	}
	
	

}
