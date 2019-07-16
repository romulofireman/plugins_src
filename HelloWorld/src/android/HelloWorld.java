package com.caobaohe.cordova.helloworld;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

import com.urbanodata.bets1.R;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class HelloWorld extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {

            String data      = args.getString(0);
            this.coolMethod( data, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod( String data , CallbackContext callbackContext) {

        try {

            JSONObject dados = new JSONObject(data);

            //Create a custom dialog with the dialog_date.xml file
            Dialog dialogCots = new Dialog( this.cordova.getActivity() ); 
            dialogCots.setCanceledOnTouchOutside(false);

            dialogCots.setContentView(R.layout.dialog_date);
            //dialogCots.setTitle(  dados.getString("_sTitulo") ); 

            ArrayList<String> cots_text  = new ArrayList( Arrays.asList( dados.getString("_sCotText").split("---") ) );  
            ArrayList<String> cots_desc  = new ArrayList( Arrays.asList( dados.getString("_sCotDesc").split("---") ) );


            WheelView selectCot = dialogCots.findViewById(R.id.day);
            selectCot.setViewAdapter( new DayWheelAdapter(this.cordova.getActivity(), cots_text) );
            selectCot.setCurrentItem( dados.getInt("_sItemSelected") ); 


            dialogCots.show();

            TextView titleTextView = dialogCots.findViewById(R.id.titleView);
            titleTextView.setText( dados.getString("_sTitulo") );


            TextView descTextView = dialogCots.findViewById(R.id.descTextView);
            descTextView.setText( cots_desc.get( selectCot.getCurrentItem() ) );

            selectCot.addScrollingListener( new OnWheelScrollListener() {
                public void onScrollingStarted(WheelView wheel) {

                }
                public void onScrollingFinished(WheelView wheel) {
                    Log.d("Twat", selectCot.getCurrentItem() + "");
                    Log.d("Twat", cots_desc.get( selectCot.getCurrentItem() ));

                    descTextView.setText( cots_desc.get( selectCot.getCurrentItem() ) ); 
                }
            });

            Button setCot = dialogCots.findViewById(R.id.set);
            setCot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Cotação", cots_desc.get( selectCot.getCurrentItem() ));
                    dialogCots.dismiss();
                    callbackContext.success( selectCot.getCurrentItem() );
                }
            });

            Button cancelCot = (Button) dialogCots.findViewById(R.id.cancel);
            cancelCot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogCots.dismiss();
                }
            });





        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error("Expected one non-empty string argument."); 
        }
        
    }   
}


class DayWheelAdapter extends AbstractWheelTextAdapter {

    ArrayList<String> dates;

    protected DayWheelAdapter(Context context, ArrayList<String> dates) {

        super(context, R.layout.wheel_item_time);
        this.dates = dates;

    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {

        View view = super.getItem(index, cachedView, parent);

        TextView weekday = (TextView) view.findViewById(R.id.time_item);

        weekday.setText(  Html.fromHtml( dates.get(index) )  );

        //Assign the text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           weekday.setText(  Html.fromHtml(dates.get(index) ,Html.FROM_HTML_MODE_LEGACY)  );
        }

        if (index == 0) {
           //  weekday.setText("Today");
           //  weekday.setTextColor(0xFF0000F0);
        }
        else{
            weekday.setTextColor(0xFF111111);
        }

        return view;
    }

    @Override
    public int getItemsCount() {
        return dates.size();
    }

    @Override
    protected CharSequence getItemText(int index) {
        return "";
    }

}
