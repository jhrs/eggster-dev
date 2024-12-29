package com.bell_inter.eggster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

public class CIntSelector extends DialogFragment
{
    private NumberPicker.OnValueChangeListener valueChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final NumberPicker numberPicker = new NumberPicker(getActivity());

        numberPicker.setMinValue(20);
        numberPicker.setMaxValue(60);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Value");
        builder.setMessage("Choose a number :");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());

            }

        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                valueChangeListener.onValueChange(numberPicker,
                        numberPicker.getValue(), numberPicker.getValue());

            }

        });

        builder.setView(numberPicker);
        return builder.create();

    }

    public NumberPicker.OnValueChangeListener getValueChangeListener()
    {
        return valueChangeListener;

    }

    public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener)
    {
        this.valueChangeListener = valueChangeListener;

    }

}
