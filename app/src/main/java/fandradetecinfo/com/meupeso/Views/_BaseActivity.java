package fandradetecinfo.com.meupeso.Views;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import fandradetecinfo.com.meupeso.R;

public abstract class _BaseActivity extends AppCompatActivity {

    protected Spinner mySpinner;
    protected EditText myEdit;

    protected void tratarSpinner(List<String> spinnerArray)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerArray
        );

        mySpinner.setAdapter(adapter);
    }

    protected DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay)
        {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            month1 = month1.length() == 1 ? "0" + month1 : month1;
            day1 = day1.length() == 1 ? "0" + day1 : day1;
            myEdit.setText(day1 + "/" + month1 + "/" + year1);
        }
    };

    protected void tratarData(boolean hasFocus)
    {
        if (hasFocus)
        {
            Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

            DatePickerDialog datePicker = new DatePickerDialog(this,
                    R.style.FullScreenDialog, datePickerListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));

            datePicker.setCancelable(false);
            datePicker.setTitle("Selecione a data");
//            datePicker.getWindow().setLayout(600, 1000);
            datePicker.show();
        }
    }



}
